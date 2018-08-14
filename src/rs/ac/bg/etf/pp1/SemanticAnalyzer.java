package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.TabSym;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	public enum Scope {GLOBAL, LOCAL, CLASS, METHOD};
	Scope state;

	boolean global_error = false;
	boolean error = false;

	Struct currType;
	Obj currClass;
	Obj currMethod;

	Struct methodType;
	Struct retType;

	List<FormParsPart> formParamList = new ArrayList<FormParsPart>();;
	List<Struct> actParamList = new ArrayList<Struct>();

	boolean doWhile = false;
	int doWhileLevel = 0;

	public void print_error(int line, String name, String msg) {
		global_error = true;
		error = true;
		System.err.println("Semantic error '" + name + "' at line:" + line + " - " + msg);
	}

	public void print_info(String msg) {
		System.out.println(msg);
		System.out.println("");
	}
// ----------------------------------------------- Program ----------------------------------------------------------- //

	public void visit(Program Program)
	{
		String name = ((ProgramId)Program.getProgId()).getName();

		TabSym.chainLocalSymbols(Program.getProgId().obj);
		TabSym.closeScope();

		state = Scope.GLOBAL;

		print_info("Program '" + name + "' " + (global_error ? "" : "successfully ") + "processed.");
	}

	public void visit(ProgramId ProgramId)
	{
		String name = ProgramId.getName();
		int line = ProgramId.getLine();

		ProgramId.obj = TabSym.insert(Obj.Prog, name, TabSym.noType);
		TabSym.openScope();
		
		error = false;
		state = Scope.GLOBAL;

		print_info("Program '" + name + "' declared at line:" + line);
	}

	public void visit(Type Type)
	{
		String name = Type.getName();
		int line = Type.getLine();
		
		currType = TabSym.noType;
		Obj typeObj = TabSym.find(name);

		if (typeObj == TabSym.noObj || typeObj.getKind() != Obj.Type)
			print_error(line, name, "Symbol '" + name + "' is not a type!");
		else
			currType = typeObj.getType();
	}

// ----------------------------------------------- ConstDecl ----------------------------------------------------------- //

	public void visit(ConstDeclarationNum ConstDeclarationNum)
	{
		String name = ConstDeclarationNum.getName();
		int line = ConstDeclarationNum.getLine();
		int value = ConstDeclarationNum.getValue().intValue();

		if (!currType.equals(TabSym.intType)) {
			print_error(line, name, "Incompatible types, expected int!");
			return;
		}

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
			return;
		}

		Obj constant = TabSym.insert(Obj.Con, name, currType);
		constant.setAdr(value);

		print_info("Symbolic num constant '" + name + " = " + value + "' declared at line:" + line);
	}

	public void visit(ConstDeclarationChar ConstDeclarationChar)
	{
		String name = ConstDeclarationChar.getName();
		int line = ConstDeclarationChar.getLine();
		char value = ConstDeclarationChar.getValue().charValue();

		if (!currType.equals(TabSym.charType)) {
			print_error(line, name, "Incompatible types, expected char!");
			return;
		}

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
			return;
		}

		Obj constant = TabSym.insert(Obj.Con, name, currType);
		constant.setAdr(value);

		print_info("Symbolic char constant '" + name + " = " + value + "' declared at line:" + line);
	}

	public void visit(ConstDeclarationBool ConstDeclarationBool)
	{
		String name = ConstDeclarationBool.getName();
		int line = ConstDeclarationBool.getLine();
		String value = ConstDeclarationBool.getValue();

		if (!currType.equals(TabSym.boolType)) {
			print_error(line, name, "Incompatible types, expected bool!");
			return;
		}

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
			return;
		}

		Obj constant = TabSym.insert(Obj.Con, name, currType);
		constant.setAdr((value.equals("true")) ? 1 : 0);

		print_info("Symbolic bool constant '" + name + " = " + value + "' declared at line:" + line);
	}

// ----------------------------------------------- VarDecl ----------------------------------------------------------- //

	public void visit(VarDeclarationPart VarDeclarationPart)
	{
		String name = VarDeclarationPart.getName();
		int line = VarDeclarationPart.getLine();

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
			return;
		}

		if (!currType.equals(TabSym.noType)) {
			int kind = (state.equals(Scope.CLASS)) ? Obj.Fld : Obj.Var;
			TabSym.insert(kind, name, currType);

			switch (state) {
			case GLOBAL: print_info("Global variable '" + name + "' declared at line:" + line); break;
			case LOCAL: print_info("Local variable '" + name + "' declared in function '" + currMethod.getName() + "' at line:" + line); break;
			case CLASS: print_info("Field '" + name + "' declared in class '" + currClass.getName() + "' at line:" + line); break;
			case METHOD: print_info("Local variable '" + name + "' declared in class '" + currClass.getName() + "' method '" + currMethod.getName() + "' at line:" + line); break;
			}
		}
	}

	public void visit(VarDeclarationPartArray VarDeclarationPartArray)
	{
		String name = VarDeclarationPartArray.getName();
		int line = VarDeclarationPartArray.getLine();

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
			return;
		}

		if (!currType.equals(TabSym.noType)) {
			int kind = (state.equals(Scope.CLASS)) ? Obj.Fld : Obj.Var;
			TabSym.insert(kind, name, new Struct(Struct.Array, currType));

			switch (state) {
			case GLOBAL: print_info("Global variable '" + name + "[]' declared at line:" + line); break;
			case LOCAL: print_info("Local variable '" + name + "[]' declared in function '" + currMethod.getName() +"' at line:" + line); break;
			case CLASS: print_info(""); break;
			case METHOD: print_info(""); break;
			}
		}
	}

// ----------------------------------------------- ClassDecl ----------------------------------------------------------- //

	public void visit(ClassDeclaration ClassDeclaration)
	{
		String name = ClassDeclaration.getClassId().getName();

		TabSym.chainLocalSymbols(currClass.getType());
		TabSym.closeScope();

		state = Scope.GLOBAL;
		currClass = null;

		print_info("Class '" + name + "' " + (error ? "" : "successfully ") + "processed.");
	}

	public void visit(ClassId ClassId)
	{
		String name = ClassId.getName();
		int line = ClassId.getLine();

		if (TabSym.currentScope().findSymbol(name) != null) {
			currClass = new Obj(Obj.Type, name, new Struct(Struct.Class));
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
		}
		else {
			currClass = TabSym.insert(Obj.Type, name, new Struct(Struct.Class));
			print_info("Class '" + name + "' declared at line:" + line);
		}

		TabSym.openScope();

		error = false;
		state = Scope.CLASS;
	}

	public void visit(ExtendsDeclaration ExtendsDeclaration)
	{
		String name = ExtendsDeclaration.getType().getName();
		int line = ExtendsDeclaration.getLine();

		Obj parent = TabSym.find(name);

		if (parent == TabSym.noObj || parent.getType().getKind() != Struct.Class) {
			print_error(line, name, "Symbol '" + name + "' is not a declared class!");
			TabSym.currentScope.getOuter().getLocals().deleteKey(currClass.getName());

			return;
		}

		for (Obj field : parent.getType().getMembers())
			if (!field.getName().equals("this")) {
				TabSym.insert(Obj.Fld, field.getName(), field.getType());
			}

		print_info("Class '" + currClass.getName() + "' extends class '" + name + "'");
	}

// ----------------------------------------------- MethodDecl ----------------------------------------------------------- //

	public String methodName(MethodDeclaration MethodDeclaration) {
		StringBuilder name = new StringBuilder();

		String methodName = currMethod.getName();
		String returnName = methodType.equals(TabSym.noType) ? "void" : ((ReturnType)MethodDeclaration.getRetType()).getType().getName();

		name.append(returnName + " " + methodName + "(");

		if (!formParamList.isEmpty()) {
			for (FormParsPart formParam : formParamList) {		// Append formal parameter names
				if (formParam instanceof FormalParamPart) {
					String paramType = ((FormalParamPart)formParam).getType().getName();
					String paramName = ((FormalParamPart) formParam).getName();
					name.append(paramType + " " + paramName + ", ");
				}
				else {
					String paramType = ((FormalParamPartArray)formParam).getType().getName();
					String paramName = ((FormalParamPartArray) formParam).getName();
					name.append(paramType + " " + paramName + "[], ");
				}
			}
			name.setLength(name.length() - 2);;
		}
		name.append(")");	

		return name.toString();
	}

	public void returnTypeCheck(MethodDeclaration MethodDeclaration) {
		String methodName = currMethod.getName();
		String returnName = methodType.equals(TabSym.noType) ? "void" : ((ReturnType)MethodDeclaration.getRetType()).getType().getName();
		int line = MethodDeclaration.getLine();

		if (retType == null || methodType.equals(TabSym.nullType)) {
			print_error(line, methodName, "Invalid return type!");
			TabSym.currentScope.getLocals().deleteKey(currMethod.getName());
			return;
		}
		if (methodType.equals(TabSym.noType))		// void method
			if (!retType.equals(TabSym.noType)) {
				print_error(line, methodName, "Void function must have empty or no return statements!");
				TabSym.currentScope.getLocals().deleteKey(currMethod.getName());
				return;
			}

		if (!methodType.equals(TabSym.noType)) {	// regular method
			if (retType.equals(TabSym.noType)) {
				print_error(line, methodName, "Return statement missing!");
				TabSym.currentScope.getLocals().deleteKey(currMethod.getName());
				return;
			}
			if (!methodType.compatibleWith(retType)) {
				print_error(line, methodName, "Incompatible return type, expected " + returnName + "!");
				TabSym.currentScope.getLocals().deleteKey(currMethod.getName());
				return;
			}
		}
	}

	public void visit(MethodDeclaration MethodDeclaration)
	{
		String name = methodName(MethodDeclaration);

		TabSym.chainLocalSymbols(currMethod);
		TabSym.closeScope();

		returnTypeCheck(MethodDeclaration);

		switch (state) {
		case METHOD:
			print_info("Method '" + name + "' " + (error ? "" : "successfully ") + "processed.");
			state = Scope.CLASS;
			currMethod.setLevel(formParamList.size() + 1);	// increment for implicit parameter this
			break;
		case LOCAL:
			print_info("Function '" + name + "' " + (error ? "" : "successfully ") + "processed.");
			state = Scope.GLOBAL;
			currMethod.setLevel(formParamList.size());	// set number of formal parameters
			break;
		default :
			break;
		}

		formParamList.clear();
		currMethod = null;
	}

	public void visit(MethodId MethodId)
	{
		String name = MethodId.getName();
		int line = MethodId.getLine();

		if (TabSym.currentScope().findSymbol(name) != null || methodType.equals(TabSym.nullType)) {
			currMethod = new Obj(Obj.Meth, name, methodType);
			if (TabSym.currentScope().findSymbol(name) != null)
				print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
		}
		else {
			currMethod = TabSym.insert(Obj.Meth, name, methodType);
			retType = TabSym.noType;

			switch (state) {
			case GLOBAL:
				print_info("Function '" + name + "' declared at line:" + line);
				break;
			case CLASS: 
				print_info("Method '" + name + "' declared in class '" + currClass.getName() + "' at line:" + line);
				break;
			default :
				break;
			}
		}

		TabSym.openScope();
		error = false;

		switch (state) {
		case GLOBAL:
			state = Scope.LOCAL;
			break;
		case CLASS: 
			Obj paramThis = TabSym.insert(Obj.Var, "this", currClass.getType());	// implicit class parameter this
			paramThis.setFpPos(0);
			state = Scope.METHOD;
			break;
		default :
			break;
		}
	}

	public void visit(ReturnType ReturnType) 
	{
		if (currType.equals(Tab.noType))
			methodType = Tab.nullType;
		else
			methodType = currType;
	}

	public void visit(ReturnTypeVoid ReturnTypeVoid)
	{
		methodType = TabSym.noType;
	}

	public void visit(FormalParamPart FormalParamPart)
	{
		String name = FormalParamPart.getName();
		String typeName = FormalParamPart.getType().getName();
		int line = FormalParamPart.getLine();

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Formal parameter '" + name + "' already defined!");
			return;
		}

		if (!currType.equals(TabSym.nullType)) {
			Obj formParam = TabSym.insert(Obj.Var, name, currType);
			formParam.setFpPos(currMethod.getLevel());	// formal param position
			formParamList.add(FormalParamPart);

			print_info("Formal parameter '" + typeName + " " + name + "' of method '" + currMethod.getName() + "' declared at line:" + line);
		}
	}

	public void visit(FormalParamPartArray FormalParamPartArray)
	{
		String name = FormalParamPartArray.getName();
		String typeName = FormalParamPartArray.getType().getName();
		int line = FormalParamPartArray.getLine();

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Formal parameter '" + name + "' already defined!");
			return;
		}

		if (!currType.equals(TabSym.nullType)) {
			Obj formParam = TabSym.insert(Obj.Var, name, new Struct(Struct.Array, currType));
			formParam.setFpPos(currMethod.getLevel());	// formal param position
			formParamList.add(FormalParamPartArray);

			print_info("Formal parameter '" + typeName + " " + name + "[]' of method '" + currMethod.getName() + "' declared at line:" + line);
		}
	
	}

// ----------------------------------------------- Statement ----------------------------------------------------------- //

		public void visit(BreakStatement BreakStatement)
		{
			int line = BreakStatement.getLine();
			if (doWhile == false)
				print_error(line, "break", "Break statement must be inside do-while loop!");
		}

		public void visit(ContinueStatement ContinueStatement)
		{
			int line = ContinueStatement.getLine();
			if (doWhile == false)
				print_error(line, "continue", "Continue statement must be inside do-while loop!");
		}

		public void visit(ReturnOption ReturnOption)
		{
			int line = ReturnOption.getLine();

			if (currMethod == null) {
				print_error(line, "return", "Return statement must be inside method or global function!");
				return;
			}

			retType = ReturnOption.getExpr().struct;
		}

		public void visit(EmptyReturnOption EmptyReturnOption)
		{
			int line = EmptyReturnOption.getLine();

			if (currMethod == null) {
				print_error(line, "return", "Return statement must be inside method or global function!");
				return;
			}

			retType = TabSym.noType;
		}

		public void visit(ReadStatement ReadStatement)
		{

		}

		public void visit(PrintStatement PrintStatement)
		{
			
		}

// ----------------------------------------------- Expression Term Factor ----------------------------------------------------------- //

		public void visit(Expressions Expressions)
		{
			Expressions.struct = Expressions.getExprList().struct;
		}

		public void visit(NegExpresions NegExpresions)
		{
			int line = NegExpresions.getLine();
			NegExpresions.struct = TabSym.noType;

			if (NegExpresions.getExprList().struct.equals(TabSym.intType))
				NegExpresions.struct = NegExpresions.getExprList().struct;
			else
				print_error(line, "neg expr", "Incompatible type in negative expression, expected int!");
		}

		public void visit(ExpressionList ExpressionList)
		{
			int line = ExpressionList.getLine();
			Struct ExprList = ExpressionList.getExprList().struct;
			Struct Term = ExpressionList.getTerm().struct;
			ExpressionList.struct = TabSym.noType;

			if (ExprList == null || Term == null)
				return;

			if (ExprList.equals(Term) && ExprList.equals(TabSym.intType) && Term.equals(TabSym.intType))
				ExpressionList.struct = TabSym.intType;
			else
				print_error(line, "addop", "Incompatible types in arithmetic expression, expected int!");
		}

		public void visit(SingleExpression SingleExpression)
		{
			SingleExpression.struct = SingleExpression.getTerm().struct;
		}

		public void visit(TermList TermList)
		{
			int line = TermList.getLine();
			Struct Term = TermList.getTerm().struct;
			Struct Factor = TermList.getFactor().struct;
			TermList.struct = TabSym.noType;

			if (Term == null || Factor == null)
				return;

			if (Term.equals(Factor) && Term.equals(TabSym.intType) && Factor.equals(TabSym.intType))
				TermList.struct = TabSym.intType;
			else
				print_error(line, "mulop", "Incompatible types in arithmetic expression, expected int!");
		}

		public void visit(SingleTerm SingleTerm)
		{
			SingleTerm.struct = SingleTerm.getFactor().struct;
		}

		public void visit(FactorDesignator FactorDesignator)
		{
			FactorDesignator.struct = FactorDesignator.getDesignator().obj.getType();
		}

		public void actualParamCheck(int line, Obj func) {
			List<Obj> formParamList = new ArrayList<Obj>();
			for (Obj param : func.getLocalSymbols())
				if (param.getFpPos() > 0)
					formParamList.add(param);

			if (actParamList.size() != formParamList.size()) {
				print_error(line, func.getName(), "Different number of formal and actual parameters!");
				actParamList.clear();
				return;
			}

			for (int i = 0; i < actParamList.size(); i++)
				if (!actParamList.get(i).assignableTo(formParamList.get(i).getType())) {
					print_error(line, func.getName(), "Actual parameter " + i + " does not match formal parameter!");
				}

			actParamList.clear();
		}

		public void visit(FactorProcCall FactorProcCall)
		{
			Obj procCall = FactorProcCall.getDesignator().obj;
			String name = FactorProcCall.getDesignator().obj.getName();
			int line = FactorProcCall.getLine();

			if (procCall.getKind() != Obj.Meth) {
				print_error(line, name, "Designator '" + name + "' is not a declared function!");
				FactorProcCall.struct = null;
				return;
			}

			actualParamCheck(line, procCall);

			FactorProcCall.struct = procCall.getType();
		}

		public void visit(FactorNum FactorNum)
		{
			FactorNum.struct = TabSym.intType;
		}

		public void visit(FactorChar FactorChar)
		{
			FactorChar.struct = TabSym.charType;
		}

		public void visit(FactorBool FactorBool)
		{
			FactorBool.struct = TabSym.boolType;
		}

		public void visit(FactorNew FactorNew)
		{
			String name = FactorNew.getType().getName();
			int line = FactorNew.getLine();

			Obj classType = TabSym.find(name);

			if (currType.equals(TabSym.noType)) {
				FactorNew.struct = null;
				return;
			}
			if (classType.getType().getKind() != Struct.Class) {
				print_error(line, "new", "Type '" + name + "' is not a declared class!");
				return;
			}

			FactorNew.struct = classType.getType();
			print_info("Creation of class '" + classType.getName() + "' object detected at line:" + line);
		}

		public void visit(FactorNewArray FactorNewArray)
		{
			String name = FactorNewArray.getType().getName();
			int line = FactorNewArray.getLine();

			Obj type = TabSym.find(name);

			if (currType.equals(TabSym.noType)) {
				FactorNewArray.struct = null;
				return;
			}
			if (!FactorNewArray.getExpr().struct.equals(TabSym.intType)) {
				print_error(line, "new", "Array size must be type int!");
				return;
			}

			FactorNewArray.struct = type.getType();
			print_info("Creation of array of '" + type.getName() + "' detected at line:" + line);
		}

		public void visit(FactorExpression FactorExpression)
		{
			FactorExpression.struct = FactorExpression.getExpr().struct;
		}
}
