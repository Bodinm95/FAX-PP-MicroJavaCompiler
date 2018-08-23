package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.util.TabSym;
import rs.etf.pp1.mj.runtime.Code;
import rs.ac.bg.etf.pp1.ast.*;

public class CodeGenerator extends VisitorAdaptor {

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
		// TO DO
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
