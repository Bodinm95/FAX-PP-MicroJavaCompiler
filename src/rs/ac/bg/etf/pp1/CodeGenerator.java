package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.util.TabSym;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;

public class CodeGenerator extends VisitorAdaptor {

	int Relop;
	List<Integer> AndQueue;
	List<Integer> OrQueue = new ArrayList<Integer>();

	boolean IfCondition = false;
	boolean WhileCondition = false;

	Stack<List<Integer>> IfStack = new Stack<>();
	Stack<Integer> WhileStack = new Stack<>();

	Stack<List<Integer>> BreakStack = new Stack<>();
	Stack<List<Integer>> ContinueStack = new Stack<>();

// ----------------------------------------------------------- Program ----------------------------------------------------------- //

	public void visit(Program Program)
	{
		Code.dataSize = Program.getLine();	// Get number of globals written to line field of Program class
	}

	public void visit(ProgramId ProgramId)
	{
		codeChr();
		codeOrd();
		codeLen();
	}

	// Generate code for chr function - converts int to char
	public void codeChr() {
		TabSym.chrObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n + 0);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	// Generate code for ord function - converts char to int
	public void codeOrd() {
		TabSym.ordObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n + 0);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	// Generate code for len function - returns array length
	public void codeLen() {
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

// ----------------------------------------------------------- MethodDecl ----------------------------------------------------------- //

	public void visit(MethodDeclaration MethodDeclaration)
	{
		if (Code.buf[Code.pc - 1] != Code.return_) {	// Check if return instruction is already there
		Code.put(Code.exit);
		Code.put(Code.return_);
		}
	}

	public void visit(MethodId MethodId)
	{
		Obj currMethod = MethodId.obj;

		if (currMethod.getName().equalsIgnoreCase("main"))
			Code.mainPc = Code.pc;

		currMethod.setAdr(Code.pc);

		Code.put(Code.enter);
		Code.put(currMethod.getLevel());
		Code.put(currMethod.getLocalSymbols().size());
	}

// ----------------------------------------------------------- Statement ----------------------------------------------------------- //

	public void visit(IfStatement IfStatement)
	{
		if (!AndQueue.isEmpty())	// Fix previous jumps to if end
			for (int addr : AndQueue)
				Code.fixup(addr);
		AndQueue.clear();
		AndQueue = null;

		if (!IfStack.isEmpty())
			AndQueue = IfStack.pop();   // Restore previous if statement AndQueue
	}

	public void visit(If If)
	{
		if (AndQueue != null)
			IfStack.push(AndQueue);  // Save previous AndQueue

		AndQueue = new ArrayList<Integer>();  // Make new AndQueue for next if statement

		IfCondition = true;
	}

	public void visit(Else Else)
	{
		Code.putJump(0);   // Put empty unconditional jump to skip else branch

		if (!AndQueue.isEmpty())	// Fix previous jumps to else
			for (int addr : AndQueue)
				Code.fixup(addr);
		AndQueue.clear();

		AndQueue.add(Code.pc - 2);   // Save address of empty skip jump
	}

	public void visit(WhileStatement WhileStatement)
	{
		if (!AndQueue.isEmpty())	// Fix previous jumps to while end
			for (int addr : AndQueue)
				Code.fixup(addr);
		AndQueue.clear();
		AndQueue = null;

		if (!BreakStack.peek().isEmpty())	// Fix previous break jumps
			for (int addr : BreakStack.peek())
				Code.fixup(addr);
		BreakStack.pop();

		if (!IfStack.isEmpty())
			AndQueue = IfStack.pop();   // Restore eventual previous if statement AndQueue
	}

	public void visit(While While)
	{
		if (AndQueue != null)
			IfStack.push(AndQueue);  // Save eventual previous if statement AndQueue

		AndQueue = new ArrayList<Integer>();  // Make new AndQueue for while condition statement

		if (!ContinueStack.peek().isEmpty())	// Fix previous continue jumps
			for (int addr : ContinueStack.peek())
				Code.fixup(addr);
		ContinueStack.pop();

		WhileCondition = true;
	}

	public void visit(DoStatement DoStatement)
	{
		WhileStack.add(Code.pc);  // Save do-while loop start address

		ContinueStack.push(new ArrayList<Integer>());  // Make new continue jump list
		BreakStack.push(new ArrayList<Integer>());     // Make new break jump list
	}

	public void visit(BreakStatement BreakStatement)
	{
		Code.putJump(0);
		BreakStack.peek().add(Code.pc - 2);
	}

	public void visit(ContinueStatement ContinueStatement)
	{
		Code.putJump(0);
		ContinueStack.peek().add(Code.pc - 2);
	}

	public void visit(ReturnStatement ReturnStatement)
	{
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(ReadStatement ReadStatement)
	{
		if (ReadStatement.getDesignator().obj.getType().equals(TabSym.charType))
			Code.put(Code.bread);
		else
			Code.put(Code.read);

		Code.store(ReadStatement.getDesignator().obj);
	}

	public void visit(PrintStatement PrintStatement)
	{
		int width = (PrintStatement.getPrintOpt() instanceof PrintOption) ? ((PrintOption)PrintStatement.getPrintOpt()).getValue() : 0;

		if (PrintStatement.getExpr().struct.equals(TabSym.intType)) {
			width = (width == 0) ? 5 : width;
			Code.loadConst(width);
			Code.put(Code.print);
		}
		if (PrintStatement.getExpr().struct.equals(TabSym.charType)) {
			width = (width == 0) ? 5 : width;
			Code.loadConst(width);
			Code.put(Code.bprint);
		}
		if (PrintStatement.getExpr().struct.equals(TabSym.boolType)) {
			width = (width == 0) ? 1 : width;
			Code.loadConst(width);
			Code.put(Code.print);
		}
	}

// ----------------------------------------------------------- DesignatorStatement ----------------------------------------------------------- //

	public void visit(Assignment Assignment)
	{
		Code.store(Assignment.getDesignator().obj);
	}

	public void visit(ProcCall ProcCall)
	{
		int destAdr = ProcCall.getDesignator().obj.getAdr() - Code.pc;

		Code.put(Code.call);
		Code.put2(destAdr);

		if (ProcCall.getDesignator().obj.getType() != Tab.noType)	// Remove eventual return value from stack
			Code.put(Code.pop);
	}

	public void visit(Increment Increment)
	{
		if (Increment.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);

		Code.load(Increment.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(Increment.getDesignator().obj);
	}

	public void visit(Decrement Decrement)
	{
		if (Decrement.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);

		Code.load(Decrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(Decrement.getDesignator().obj);
	}

// ----------------------------------------------------------- Designator ----------------------------------------------------------- //

	public void visit(DesignatorField DesignatorField)
	{
		Code.load(DesignatorField.getDesignator().obj);
	}

	public void visit(ArrayDesignator ArrayDesignator)
	{
		Code.load(ArrayDesignator.getDesignator().obj);
	}

// ----------------------------------------------------------- Condition ----------------------------------------------------------- //

	public void visit(Condition Condition)
	{
		if (IfCondition) {
			Code.putFalseJump(Relop, 0);  // Put empty inverse jump to else branch or if end
			AndQueue.add(Code.pc - 2);    // Save address for later fixup

			if (!OrQueue.isEmpty())   // Fix previous OR jumps
				for (int addr : OrQueue)
					Code.fixup(addr);
			OrQueue.clear();

			IfCondition = false;
		}

		if (WhileCondition) {
			int startAddr = WhileStack.pop();                   // Get do-while loop start address
			Code.putFalseJump(Code.inverse[Relop], startAddr);  // Put jump to do-while start

			WhileCondition = false;
		}
	}

	public void visit(Or Or)
	{
		if (IfCondition) {
			Code.putFalseJump(Code.inverse[Relop], 0);  // Put empty jump to then branch
			OrQueue.add(Code.pc - 2);                   // Save address for later fixup

			if (!AndQueue.isEmpty())   // Fix previous AND jumps
				for (int addr : AndQueue)
					Code.fixup(addr);
			AndQueue.clear();
		}

		if (WhileCondition) {
			int startAddr = WhileStack.peek();                  // Get do-while loop start address
			Code.putFalseJump(Code.inverse[Relop], startAddr);  // Put jump to do-while start

			if (!AndQueue.isEmpty())   // Fix previous AND jumps
				for (int addr : AndQueue)
					Code.fixup(addr);
			AndQueue.clear();
		}
	}

	public void visit(And And)
	{
		if (IfCondition) {
			Code.putFalseJump(Relop, 0);  // Put empty inverse jump to else branch or if end or next OR condition
			AndQueue.add(Code.pc - 2);    // Save address for later fixup
		}

		if (WhileCondition) {
			Code.putFalseJump(Relop, 0);  // Put empty inverse jump to while end or next OR condition
			AndQueue.add(Code.pc - 2);    // Save address for later fixup
		}
	}

	public void visit(ConditionFact ConditionFact)
	{
		Code.loadConst(1);
		Relop = Code.eq;
	}

	public void visit(ConditionFactRelop ConditionFactRelop)
	{
		if (ConditionFactRelop.getRelop() instanceof RelopEQ)
			Relop = Code.eq;
		if (ConditionFactRelop.getRelop() instanceof RelopNEQ)
			Relop = Code.ne;
		if (ConditionFactRelop.getRelop() instanceof RelopGR)
			Relop = Code.gt;
		if (ConditionFactRelop.getRelop() instanceof RelopGREQ)
			Relop = Code.ge;
		if (ConditionFactRelop.getRelop() instanceof RelopLS)
			Relop = Code.lt;
		if (ConditionFactRelop.getRelop() instanceof RelopLSEQ)
			Relop = Code.le;
	}

// ----------------------------------------------------------- Expression ----------------------------------------------------------- //

	public void visit(NegExpresions NegExpresions)
	{
		Code.put(Code.neg);
	}

	public void visit(ExpressionList ExpressionList)
	{
		// TO DO Vector Multiplication

		if (ExpressionList.getAddop() instanceof AddopAdd)
			Code.put(Code.add);
		if (ExpressionList.getAddop() instanceof AddopSub)
			Code.put(Code.sub);
	}

// ----------------------------------------------------------- Term ----------------------------------------------------------- //

	public void visit(TermList TermList)
	{
		// TO DO Vector Addition

		if (TermList.getMulop() instanceof MulopMul)
			Code.put(Code.mul);
		if (TermList.getMulop() instanceof MulopDiv)
			Code.put(Code.div);
		if (TermList.getMulop() instanceof MulopMod)
			Code.put(Code.rem);
	}

// ----------------------------------------------------------- Factor ----------------------------------------------------------- //

	public void visit(FactorDesignator FactorDesignator)
	{
		Code.load(FactorDesignator.getDesignator().obj);
	}

	public void visit(FactorProcCall FactorProcCall)
	{
		int destAdr = FactorProcCall.getDesignator().obj.getAdr() - Code.pc;

		Code.put(Code.call);
		Code.put2(destAdr);
	}

	public void visit(FactorNum FactorNum)
	{
		Code.loadConst(FactorNum.getValue());
	}

	public void visit(FactorChar FactorChar)
	{
		Code.loadConst(FactorChar.getValue());
	}

	public void visit(FactorBool FactorBool)
	{
		if(FactorBool.getValue().equals("true"))
			Code.loadConst(1);
		else
			Code.loadConst(0);
	}

	public void visit(FactorNew FactorNew)
	{
		Code.put(Code.new_);
		Code.put2(FactorNew.struct.getNumberOfFields() * 4);
	}

	public void visit(FactorNewArray FactorNewArray)
	{
		Code.put(Code.newarray);
		if (FactorNewArray.getType().struct.equals(TabSym.charType))
			Code.put(0);
		else
			Code.put(1);
	}
}
