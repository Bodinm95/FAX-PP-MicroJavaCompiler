package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.util.TabSym;
import rs.ac.bg.etf.pp1.util.MethodTable;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

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

	int vector_add;
	int vector_mul;
	int scalar_mul;

	MethodTable MethodTable = new MethodTable();

	boolean method = false;

	int invoke_call;

// ----------------------------------------------------------- Program ----------------------------------------------------------- //

	public void visit(Program Program)
	{
		Code.dataSize += Program.getLine();	// Get number of globals written to line field of Program class
	}

	public void visit(ProgramId ProgramId)
	{
		codeChr();
		codeOrd();
		codeLen();
		codeVector();
		codeInvokeCall();
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

	// Generate code for vector arithmetic functions
	public void codeVector() {
		vector_add = Code.pc;
		Code.put(Code.enter);            // int[] vector_add(int A[], int B[])
		Code.put(2);                     // int i;
		Code.put(4);                     // int result[];
		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);
		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.put(Code.jcc + Code.eq);    // if (len(A) != len(B)
		Code.put2(7);
		Code.put(Code.trap);
		Code.put(0);
		Code.put(Code.exit);
		Code.put(Code.return_);          // return 0;
		Code.put(Code.load_1);
		Code.put(Code.arraylength);      // i = len(B);
		Code.put(Code.store_2);
		Code.put(Code.load_2);
		Code.put(Code.newarray);         // result = new int[i];
		Code.put(1);
		Code.put(Code.store_3);
		Code.put(Code.load_2);           // do
		Code.put(Code.const_1);
		Code.put(Code.sub);              // i--;
		Code.put(Code.store_2);
		Code.put(Code.load_3);
		Code.put(Code.load_2);
		Code.put(Code.load_n + 0);
		Code.put(Code.load_2);
		Code.put(Code.aload);
		Code.put(Code.load_1);
		Code.put(Code.load_2);
		Code.put(Code.aload);
		Code.put(Code.add);
		Code.put(Code.astore);           // result[i] = A[i] + B[i]
		Code.put(Code.load_2);
		Code.put(Code.const_n + 0);
		Code.put(Code.jcc + Code.gt);    // while (i > 0);
		Code.put2(-16);
		Code.put(Code.load_3);
		Code.put(Code.exit);
		Code.put(Code.return_);          // return result;

		vector_mul = Code.pc;
		Code.put(Code.enter);            // int[] vector_mul(int A[], int B[])
		Code.put(2);                     // int i;
		Code.put(4);                     // int result;
		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);
		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.put(Code.jcc + Code.eq);    // if (len(A) != len(B)
		Code.put2(7);
		Code.put(Code.trap);
		Code.put(0);
		Code.put(Code.exit);
		Code.put(Code.return_);          // return 0;
		Code.put(Code.load_1);
		Code.put(Code.arraylength);      // i = len(B);
		Code.put(Code.store_2);
		Code.put(Code.load_2);           // do
		Code.put(Code.const_1);
		Code.put(Code.sub);              // i--;
		Code.put(Code.store_2);
		Code.put(Code.load_3);
		Code.put(Code.load_n + 0);
		Code.put(Code.load_2);
		Code.put(Code.aload);
		Code.put(Code.load_1);
		Code.put(Code.load_2);
		Code.put(Code.aload);
		Code.put(Code.mul);
		Code.put(Code.add);
		Code.put(Code.store_3);           // result += A[i] * B[i]
		Code.put(Code.load_2);
		Code.put(Code.const_n + 0);
		Code.put(Code.jcc + Code.gt);    // while (i > 0);
		Code.put2(-16);
		Code.put(Code.load_3);
		Code.put(Code.exit);
		Code.put(Code.return_);          // return result;

		scalar_mul = Code.pc;
		Code.put(Code.enter);            // int[] scalar_mul(int A[], int S)
		Code.put(2);                     // int i;
		Code.put(4);                     // int result[];
		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);      // i = len(A);
		Code.put(Code.store_2);
		Code.put(Code.load_2);
		Code.put(Code.newarray);         // result = new int[i];
		Code.put(1);
		Code.put(Code.store_3);
		Code.put(Code.load_2);           // do
		Code.put(Code.const_1);
		Code.put(Code.sub);              // i--;
		Code.put(Code.store_2);
		Code.put(Code.load_3);
		Code.put(Code.load_2);
		Code.put(Code.load_n + 0);
		Code.put(Code.load_2);
		Code.put(Code.aload);
		Code.put(Code.load_1);
		Code.put(Code.mul);
		Code.put(Code.astore);           // result[i] = A[i] * S
		Code.put(Code.load_2);
		Code.put(Code.const_n + 0);
		Code.put(Code.jcc + Code.gt);    // while (i > 0);
		Code.put2(-14);
		Code.put(Code.load_3);
		Code.put(Code.exit);
		Code.put(Code.return_);          // return result;
		
	}

	// Generate code for getting class object address for stack before invokevirtual
	public void codeInvokeCall() {
		invoke_call = Code.pc;
		Code.put(Code.enter);         // void invoke(int num)
		Code.put(1);                  // int adr; int len;
		Code.put(4);                  // int actPars[];
		Code.put(Code.load_n);
		Code.put(Code.store_2);       // len = num;
		Code.put(Code.load_2);
		Code.put(Code.newarray);
		Code.put(1);
		Code.put(Code.store_3);       // actPars = new int[len];
		Code.put(Code.load_2);        // do
		Code.put(Code.const_1);
		Code.put(Code.sub);           // len--;
		Code.put(Code.store_2);
		Code.put(Code.store_1);       // adr = pop();
		Code.put(Code.load_3);
		Code.put(Code.load_2);
		Code.put(Code.load_1);
		Code.put(Code.astore);        // actPars[len] = adr;
		Code.put(Code.load_2);
		Code.put(Code.const_n);
		Code.put(Code.jcc + Code.gt); // while(len >= 0);
		Code.put2(-11);
		Code.put(Code.store_1);       // adr = pop();
		Code.put(Code.load_1);        // push(adr);
		Code.put(Code.load_3);        // do
		Code.put(Code.load_2);
		Code.put(Code.aload);         // push(actPars[len]);
		Code.put(Code.load_2);
		Code.put(Code.const_1);
		Code.put(Code.add);           // len++;
		Code.put(Code.store_2);
		Code.put(Code.load_2);
		Code.put(Code.load_n);
		Code.put(Code.jcc + Code.lt); // while (len < num);
		Code.put2(-9);
		Code.put(Code.load_1);        // push(adr);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
// ----------------------------------------------------------- ClassDecl ----------------------------------------------------------- //
	
	public void visit(ClassDeclaration ClassDeclaration)
	{
		ClassDeclaration.obj.setAdr(Code.dataSize); // Set virtual function table address

		// Fix eventual not overridden inherited methods addresses
		if (ClassDeclaration.getExtendsDecl() instanceof ExtendsDeclaration) {
			Obj parent = ClassDeclaration.getExtendsDecl().obj;
			for (Obj method : ClassDeclaration.obj.getType().getMembers()) {
				if (method.getKind() == Obj.Meth && method.getAdr() == 0)
					method.setAdr(parent.getType().getMembersTable().searchKey(method.getName()).getAdr());
			}
		}

		for (Obj method : ClassDeclaration.obj.getType().getMembers())  // Generate class virtual function table
			if (method.getKind() == Obj.Meth)
				MethodTable.addFunctionEntry(method.getName(), method.getAdr());
		MethodTable.addTableTerminator();
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

		if (currMethod.getName().equalsIgnoreCase("main")) {
			Code.mainPc = Code.pc;

			// Initialize virtual function tables code
			for (Byte codeByte : MethodTable.table)
				Code.buf[Code.pc++] = codeByte.byteValue();
			MethodTable.table.clear();
		}

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
		Obj func = ProcCall.getDesignator().obj;
		if (method) {
			// Load class object for getting virtual function field
			if (func.getLevel() == 1) {
				Code.put(Code.dup);
			}
			else {
				Code.loadConst(func.getLevel() - 1);
				int destAddr = invoke_call - Code.pc;
				Code.put(Code.call);
				Code.put2(destAddr);
			}
			Code.put(Code.getfield);  // Load virtual function table address
			Code.put2(0);
			Code.put(Code.invokevirtual);  // Invoke virtual function
			for (int i = 0; i < func.getName().length(); i++)
				Code.put4(func.getName().charAt(i));
			Code.put4(-1);

			method = false;
		}
		else {
			int destAdr = func.getAdr() - Code.pc;

			Code.put(Code.call);
			Code.put2(destAdr);
		}
		if (func.getType() != Tab.noType)	// Remove eventual return value from stack
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

		if (DesignatorField.obj.getKind() == Obj.Meth)
			method = true;	// set method flag
	}

	public void visit(ArrayDesignator ArrayDesignator)
	{
		Code.load(ArrayDesignator.getDesignator().obj);
	}

	public void visit(DesignatorIdent DesignatorIdent)
	{
		if (DesignatorIdent.obj.getKind() == Obj.Fld)  // Load implicit class parameter this
			Code.put(Code.load_n + 0);
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
		if (ExpressionList.getExprList().struct.getKind() == Struct.Array && ExpressionList.getTerm().struct.getKind() == Struct.Array) {
			int destAddr = vector_add - Code.pc;
			Code.put(Code.call);
			Code.put2(destAddr);
			return;
		}

		if (ExpressionList.getAddop() instanceof AddopAdd)
			Code.put(Code.add);
		if (ExpressionList.getAddop() instanceof AddopSub)
			Code.put(Code.sub);
	}

// ----------------------------------------------------------- Term ----------------------------------------------------------- //

	public void visit(TermList TermList)
	{
		if (TermList.getTerm().struct.getKind() == Struct.Array && TermList.getFactor().struct.getKind() == Struct.Array) {
			int destAddr = vector_mul - Code.pc;
			Code.put(Code.call);
			Code.put2(destAddr);
			return;
		}

		if (TermList.getTerm().struct.getKind() == Struct.Array || TermList.getFactor().struct.getKind() == Struct.Array) {
			if (TermList.getFactor().struct.getKind() == Struct.Array) {
				// Switch formal arguments on stack: int array -> array int
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
			}
			int destAddr = scalar_mul - Code.pc;
			Code.put(Code.call);
			Code.put2(destAddr);
			return;
		}

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
		Obj func = FactorProcCall.getDesignator().obj;
		if (method) {
			// Load class object for getting virtual function field
			if (func.getLevel() == 1) {
				Code.put(Code.dup);
			}
			else {
				Code.loadConst(func.getLevel() - 1);
				int destAddr = invoke_call - Code.pc;
				Code.put(Code.call);
				Code.put2(destAddr);
			}
			Code.put(Code.getfield);  // Load virtual function table address
			Code.put2(0);
			Code.put(Code.invokevirtual);  // Invoke virtual function
			for (int i = 0; i < func.getName().length(); i++)
				Code.put4(func.getName().charAt(i));
			Code.put4(-1);

			method = false;
		}
		else {
			int destAdr = func.getAdr() - Code.pc;

			Code.put(Code.call);
			Code.put2(destAdr);
		}
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
		Code.put2((FactorNew.struct.getNumberOfFields() + 1) * 4);

		// initialize virtual function table pointer
		Obj classObj = TabSym.findTypeObject(FactorNew.struct);
		Code.put(Code.dup);
		Code.loadConst(classObj.getAdr());
		Code.put(Code.putfield);
		Code.put2(0);
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
