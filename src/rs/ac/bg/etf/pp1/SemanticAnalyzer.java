package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.TabSym;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	public enum Scope {GLOBAL, LOCAL, CLASS, METHOD, WHILE};
	Stack<Scope> state = new Stack<Scope>();	

	boolean global_error = false;
	boolean class_error = false;
	boolean method_error = false;

	int error = 0;

	Struct currType;
	Obj currProgram;
	Obj currClass;
	Obj currMethod;

	boolean returnFound;

	List<Obj> formParamList = new ArrayList<Obj>();;
	List<Struct> actParamList = new ArrayList<Struct>();

	String methodClass = "";

	public void print_error(int line, String name, String msg) {
		global_error = true;
		class_error = true;
		method_error = true;
		error++;

		name = (name.equals("") ? name : "'" + name + "' ");
		
		System.err.println("Semantic error " + name + "at line:" + line + " - " + msg);
	}

	public void print_info(String msg) {
		System.out.println(msg);
		System.out.println("");
	}
// ----------------------------------------------- Program ----------------------------------------------------------- //

	public void visit(Program Program)
	{
		String name = currProgram.getName();

		TabSym.chainLocalSymbols(currProgram);
		TabSym.closeScope();

		state.pop();
		print_info("Program '" + name + "' " + (global_error ? "" : "successfully ") + "processed.");
	}

	public void visit(ProgramId ProgramId)
	{
		int line = ProgramId.getLine();
		String name = ProgramId.getName();

		global_error = false;
		currProgram = TabSym.insert(Obj.Prog, name, TabSym.noType);
		TabSym.openScope();

		state.push(Scope.GLOBAL);
		print_info("Program '" + name + "' declared at line:" + line);
	}

	public void visit(Type Type)
	{
		int line = Type.getLine();
		String name = Type.getName();

		Obj typeObj = TabSym.find(name);
		currType = TabSym.noType;

		if (typeObj == TabSym.noObj || typeObj.getKind() != Obj.Type)
			print_error(line, name, "'" + name + "' is not a valid type!");
		else
			currType = typeObj.getType();
	}

// ----------------------------------------------- ConstDecl ----------------------------------------------------------- //

	public void visit(ConstDeclarationNum ConstDeclarationNum)
	{
		int line = ConstDeclarationNum.getLine();
		String name = ConstDeclarationNum.getName();
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
		int line = ConstDeclarationChar.getLine();
		String name = ConstDeclarationChar.getName();
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
		int line = ConstDeclarationBool.getLine();
		String name = ConstDeclarationBool.getName();
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
		int line = VarDeclarationPart.getLine();
		String name = VarDeclarationPart.getName();
		String type = TabSym.findTypeName(currType);

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
			return;
		}
		if (currType.equals(TabSym.noType))	// Type error
			return;

		int kind = (state.peek() == Scope.CLASS) ? Obj.Fld : Obj.Var;
		TabSym.insert(kind, name, currType);

		switch (state.peek()) {
		case GLOBAL: print_info("Global variable '" + type + " " + name + "' declared at line:" + line); break;
		case LOCAL: print_info("Local variable '" + type + " " + name + "' declared in function '" + methodName(currMethod, false) + "' at line:" + line); break;
		case CLASS: print_info("Field '" + type + " " + name + "' declared in class '" + currClass.getName() + "' at line:" + line); break;
		case METHOD: print_info("Local variable '" + type + " " + name + "' declared in class '" + currClass.getName() + "' method '" + methodName(currMethod, false) + "' at line:" + line); break;
		default: break;
			}
	}

	public void visit(VarDeclarationPartArray VarDeclarationPartArray)
	{
		int line = VarDeclarationPartArray.getLine();
		String name = VarDeclarationPartArray.getName();
		String type = TabSym.findTypeName(currType);

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
			return;
		}
		if (currType.equals(TabSym.noType))	// Type error
			return;

		int kind = (state.peek() == Scope.CLASS) ? Obj.Fld : Obj.Var;
		TabSym.insert(kind, name, new Struct(Struct.Array, currType));

		switch (state.peek()) {
		case GLOBAL: print_info("Global variable '" + type + " " + name + "[]' declared at line:" + line); break;
		case LOCAL: print_info("Local variable '" + type + " " + name + "[]' declared in function '" + methodName(currMethod, false) +"' at line:" + line); break;
		case CLASS: print_info("Field '" + type + " " + name + "[]' declared in class '" + currClass.getName() + "' at line:" + line); break;
		case METHOD: print_info("Local variable '" + type + " " + name + "[]' declared in class '" + currClass.getName() + "' method '" + methodName(currMethod, false) + "' at line:" + line); break;
		default: break;
		}
	}

// ----------------------------------------------- ClassDecl ----------------------------------------------------------- //

	public void visit(ClassDeclaration ClassDeclaration)
	{
		String name = ClassDeclaration.getClassId().getName();

		TabSym.chainLocalSymbols(currClass.getType());
		TabSym.closeScope();

		state.pop();
		print_info("Class '" + name + "' " + (class_error ? "" : "successfully ") + "processed.");

		currClass = null;
	}

	public void visit(ClassId ClassId)
	{
		int line = ClassId.getLine();
		String name = ClassId.getName();

		class_error = false;	// reset class error

		if (TabSym.currentScope().findSymbol(name) != null) {
			currClass = new Obj(Obj.Type, name, new Struct(Struct.Class));
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
		}
		else {
			currClass = TabSym.insert(Obj.Type, name, new Struct(Struct.Class));
		}

		TabSym.openScope();

		state.push(Scope.CLASS);
		print_info("Class '" + name + "' declared at line:" + line);
	}

	public void visit(ExtendsDeclaration ExtendsDeclaration)
	{
		int line = ExtendsDeclaration.getLine();
		String name = ExtendsDeclaration.getType().getName();

		Obj parent = TabSym.find(name);

		if (parent == TabSym.noObj || parent.getType().getKind() != Struct.Class) {
			print_error(line, name, "'" + name + "' is not a declared class!");

			if (TabSym.currentScope.getOuter().findSymbol(currClass.getName()) != null)	// remove invalid class object form symbol table
				TabSym.currentScope.getOuter().getLocals().deleteKey(currClass.getName());
			return;
		}

		for (Obj field : parent.getType().getMembers()) {
			if (field.getKind() == Obj.Fld) {	// add inherited fields
				TabSym.insert(Obj.Fld, field.getName(), field.getType());
				}

			if (field.getKind() == Obj.Meth) {	// add inherited methods
				Obj method = TabSym.insert(field.getKind(), field.getName(), field.getType());
				method.setLevel(field.getLevel());
				method.setFpPos(1);	// inherited flag

				TabSym.openScope();	// add method local symbols
				for (Obj symbol : field.getLocalSymbols()) {
					Obj param;
					if (symbol.getName().equals("this"))
						param = TabSym.insert(symbol.getKind(), symbol.getName(), currClass.getType());
					else
						param = TabSym.insert(symbol.getKind(), symbol.getName(), symbol.getType());
					param.setFpPos(symbol.getFpPos());
				}
				TabSym.chainLocalSymbols(method);
				TabSym.closeScope();
			}
		}

		print_info("Class '" + currClass.getName() + "' extends class '" + name + "'");
	}

// ----------------------------------------------------------- MethodDecl ----------------------------------------------------------- //

	public void visit(MethodDeclaration MethodDeclaration)
	{
		int line = MethodDeclaration.getLine();
		String name = methodName(currMethod, true);

		TabSym.chainLocalSymbols(currMethod);
		TabSym.closeScope();

		if (!currMethod.getType().equals(TabSym.noType) && returnFound == false)
			print_error(line, name, "Return statement missing!");

		switch (state.peek()) {
		case LOCAL:
			state.pop();
			print_info("Function '" + name + "' " + (method_error ? "" : "successfully ") + "processed.");
			break;
		case METHOD:
			state.pop();
			print_info("Method '" + name + "' " + (method_error ? "" : "successfully ") + (currMethod.getFpPos() != 0 && !method_error ? "overridden." : "processed."));
			break;
		default :
			break;
		}

		currMethod = null;
	}

	public void visit(MethodSignature MethodSignature)
	{
		int line = MethodSignature.getLine();

		boolean override = checkOverride(line);

		if (!method_error) {
			// Save formal params from TabSym and close method scope used for inserting formal params
			SymbolDataStructure formPars = TabSym.currentScope().getLocals();
			TabSym.closeScope();

			// Insert method object in TabSym or get inherited method and set number of formal params to 0
			if (override)
				currMethod = TabSym.currentScope.findSymbol(currMethod.getName());
			else
				currMethod = TabSym.insert(currMethod.getKind(), currMethod.getName(), currMethod.getType());
			currMethod.setLocals(null);
			currMethod.setLevel(0);

			TabSym.openScope();  // Open method scope

			// Insert saved formal params to new method scope
			if (formPars != null) {
				for(Obj param : formPars.symbols()) {
					Obj formParam = TabSym.insert(param.getKind(), param.getName(), param.getType());
					formParam.setFpPos(param.getFpPos());   // Add saved formal param position to inserted param
				}
				currMethod.setLevel(formPars.numSymbols()); // Set number of formal params
			}
			formPars = null;
		}
	}

	public void visit(MethodId MethodId)
	{
		int line = MethodId.getLine();
		String name = MethodId.getName();
		String type = TabSym.findTypeName(currType);

		method_error = false;	// Reset method error

		if (currType.equals(TabSym.errorType))	// Type error
			method_error = true;

		currMethod = TabSym.currentScope().findSymbol(name);
		if (currMethod != null && currMethod.getFpPos() == 0)
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");

		currMethod = new Obj(Obj.Meth, name, currType);
		currMethod.setLevel(0);
		returnFound = false;

		TabSym.openScope();

		switch (state.peek()) {
		case GLOBAL:
			state.push(Scope.LOCAL);
			print_info("Function '" + type + " " + name + "(...)' declared at line:" + line);
			break;
		case CLASS: 
			state.push(Scope.METHOD);
			print_info("Method '" + type + " " + name + "(...)' declared in class '" + currClass.getName() + "' at line:" + line);

			Obj paramThis = TabSym.insert(Obj.Var, "this", currClass.getType());	// implicit class parameter this
			paramThis.setFpPos(0);
			currMethod.setLevel(1);
			break;
		default :
			break;
		}
	}

	public void visit(ReturnType ReturnType) 
	{
		if (currType.equals(TabSym.noType))	// Type error
			currType = TabSym.errorType;
	}

	public void visit(ReturnTypeVoid ReturnTypeVoid)
	{
		currType = TabSym.noType;
	}

	public void visit(FormalParamPart FormalParamPart)
	{
		int line = FormalParamPart.getLine();
		String name = FormalParamPart.getName();
		String typeName = FormalParamPart.getType().getName();

		String methodType = TabSym.findTypeName(currMethod.getType());
		String methodName = currMethod.getName() + "(...)";
		String methodKind = (state.peek() == Scope.METHOD) ? "method" : "function";

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Formal parameter '" + name + "' already defined!");
			return;
		}
		if (currType.equals(TabSym.noType))	// Type error
			return;

		Obj formParam = TabSym.insert(Obj.Var, name, currType);
		formParam.setFpPos(currMethod.getLevel());      // Formal param position
		currMethod.setLevel(currMethod.getLevel() + 1); // Formal params number++

		print_info("Formal parameter '" + typeName + " " + name + "' of " + methodKind + " '" + methodType + " " + methodName + "' declared at line:" + line);
	}

	public void visit(FormalParamPartArray FormalParamPartArray)
	{
		int line = FormalParamPartArray.getLine();
		String name = FormalParamPartArray.getName();
		String typeName = FormalParamPartArray.getType().getName();

		Obj formParam = TabSym.insert(Obj.Var, name, new Struct(Struct.Array, currType));
		formParam.setFpPos(currMethod.getLevel());      // Formal param position
		currMethod.setLevel(currMethod.getLevel() + 1); // Formal params number++

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Formal parameter '" + name + "' already defined!");
			return;
		}
		if (currType.equals(TabSym.noType))	// Type error
			return;

		String methodType = TabSym.findTypeName(currMethod.getType());
		String methodName = currMethod.getName() + "(...)";
		String methodKind = (state.peek() == Scope.METHOD) ? "method" : "function";

		print_info("Formal parameter '" + typeName + " " + name + "[]' of " + methodKind + " '" + methodType + " " + methodName + "' declared at line:" + line);
	}

	public boolean checkOverride(int line) {
		String name = methodName(currMethod, false);

		Obj inherited = TabSym.currentScope.getOuter().findSymbol(currMethod.getName());

		if (inherited == null || inherited.getFpPos() == 0)	// Method is not overridden
			return false;

		if (!currMethod.getType().assignableTo(inherited.getType()))
			print_error(line, name, "Invalid override, incompatible return types!");

		// Get formal parameters of both methods
		List<Obj> overridePars = new ArrayList<Obj>();
		for (Obj param : TabSym.currentScope().values())
			if (param.getFpPos() > 0)
				overridePars.add(param);
		for (Obj param : inherited.getLocalSymbols())
			if (param.getFpPos() > 0)
				formParamList.add(param);

		if (overridePars.size() != formParamList.size()) {
			print_error(line, name, "Invalid override, different number of formal parameters!");
			formParamList.clear();
			return false;
		}

		for (int i = 0; i < formParamList.size(); i++)
			if (!overridePars.get(i).getType().assignableTo(formParamList.get(i).getType())) {
				print_error(line, name, "Invalid override, formal parameter types do not match!");
				formParamList.clear();
				return false;
			}

		formParamList.clear();
		return true;
	}

	public String methodName(Obj func, boolean full) {
		StringBuilder name = new StringBuilder();

		String methodName = func.getName();
		String returnName = TabSym.findTypeName(func.getType());

		for (Obj param : TabSym.currentScope.values())
			if (param.getFpPos() > 0)
				formParamList.add(param);

		name.append(returnName + " " + methodName + "(");

		if (!formParamList.isEmpty()) {
			for (Obj formParam : formParamList) {	// Append formal parameter names
				String paramType = TabSym.findTypeName(formParam.getType());
				String paramName = formParam.getName();
				if (full)
					if (formParam.getType().getKind() != Struct.Array)
						name.append(paramType + " " + paramName + ", ");
					else
						name.append(paramType + " " + paramName + "[], ");
				else
					name.append(paramType + ", ");
			}
			name.setLength(name.length() - 2);;
		}
		name.append(")");

		formParamList.clear();
		return name.toString();
	}

// ----------------------------------------------------------- Statement ----------------------------------------------------------- //

	public void visit(DoStatement DoStatement)
	{
		state.push(Scope.WHILE);
	}

	public void visit(WhileStatement WhileStatement)
	{
		state.pop();
	}

	public void visit(BreakStatement BreakStatement)
	{
		int line = BreakStatement.getLine();

		if (state.peek() != Scope.WHILE)
			print_error(line, "break", "Break statement must be inside do-while loop!");
	}

	public void visit(ContinueStatement ContinueStatement)
	{
		int line = ContinueStatement.getLine();

		if (state.peek() != Scope.WHILE)
			print_error(line, "continue", "Continue statement must be inside do-while loop!");
	}

	public void visit(ReturnStatement ReturnStatement)
	{
		int line = ReturnStatement.getLine();

		if (currMethod == null) {
			print_error(line, "return", "Return statement must be inside method or global function!");
			return;
		}

		String name = methodName(currMethod, false);
		String dataType = TabSym.findTypeName(currMethod.getType());

		Struct methodType = currMethod.getType();
		Struct retType = (ReturnStatement.getRetOpt() instanceof ReturnOption) ?
		                    ((ReturnOption)ReturnStatement.getRetOpt()).getExpr().struct : null;
		returnFound = true;

		if (methodType.equals(TabSym.noType))    // Void method
			if (retType != null)
				print_error(line, name, "Void functions must have empty or no return statements!");

		if (!methodType.equals(TabSym.noType))   // Regular method
			if (retType == null)
				print_error(line, name, "Function must return a result of type " + dataType + "!");
			else if (retType.equals(TabSym.noType))
				print_error(line, name, "Invalid return expression!");
			else if (!methodType.compatibleWith(retType))
				print_error(line, name, "Incompatible return type, expected " + dataType + "!");
	}

	public void visit(ReadStatement ReadStatement)
	{
		int line = ReadStatement.getLine();
		String name = ReadStatement.getDesignator().obj.getName();

		int kind = ReadStatement.getDesignator().obj.getKind();
		Struct input = ReadStatement.getDesignator().obj.getType();

		if (ReadStatement.getDesignator().obj.equals(TabSym.noObj))	// Designator error pass up
			return;

		if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld || input.getKind() == Struct.Array) {
			print_error(line, "read", "Designator '" + name + "' must denote a variable, an array element or an object field!");
			return;
		}
		if (!input.equals(TabSym.intType) && !input.equals(TabSym.charType) && !input.equals(TabSym.boolType))
			print_error(line, "read", "Invalid read input type, expected int, char or bool!");
	}

	public void visit(PrintStatement PrintStatement)
	{
		int line = PrintStatement.getLine();

		Struct Expr = PrintStatement.getExpr().struct;

		if (Expr.equals(TabSym.noType))	// Expr error pass up
			return;

		if (!Expr.equals(TabSym.intType) && !Expr.equals(TabSym.charType) && !Expr.equals(TabSym.boolType))
			print_error(line, "print", "Invalid print type, expected int, char or bool!");
	}
// ----------------------------------------------------------- ActPars ----------------------------------------------------------- //

	public void visit(ActualParamsList ActualParamsList)
	{
		actParamList.add(ActualParamsList.getExpr().struct);
	}

	public void visit(SingleActualParam SingleActualParam)
	{
		actParamList.add(SingleActualParam.getExpr().struct);
	}

	public void actParsCheck(int line, Obj func) {
		String name = callName(func, 0);

		for (Obj param : func.getLocalSymbols())
			if (param.getFpPos() > 0)
				formParamList.add(param);

		if (actParamList.size() != formParamList.size()) {
			print_error(line, name, "Different number of formal and actual parameters!");
			formParamList.clear();
			actParamList.clear();
			return;
		}

		for (int i = 0; i < actParamList.size(); i++)
			if (!actParamList.get(i).assignableTo(formParamList.get(i).getType())) {
				name = callName(func, i + 1);
				String actual = TabSym.findTypeName(actParamList.get(i));
				String formal = TabSym.findTypeName(formParamList.get(i).getType());
				print_error(line, name, "Actual '" + actual + "' parameter does not match formal '" + formal + "' parameter!");
			}

		formParamList.clear();
		actParamList.clear();
	}

	public String callName(Obj func, int error) {
		StringBuilder name = new StringBuilder();

		String methodName = func.getName();
		name.append(methodName + "(");

		if (!actParamList.isEmpty()) {
			for (int i = 0; i < actParamList.size(); i++) {	// Append actual parameter types
				Struct actParam = actParamList.get(i);
				String paramType = TabSym.findTypeName(actParam);
				if (i != error - 1)
					name.append(paramType + ", ");
				else
					name.append("X, ");
			}
			name.setLength(name.length() - 2);;
		}
		name.append(")");

		return name.toString();
	}

// ----------------------------------------------------------- DesignatorStatement ----------------------------------------------------------- //

	public void visit(Assignment Assignment)
	{
		int line = Assignment.getLine();	

		Obj left = Assignment.getDesignator().obj;
		Struct right = Assignment.getExpr().struct;
		String name = left.getName();
		int kind = left.getKind();

		if (left.equals(TabSym.noObj) || right == TabSym.noType)	// Designator error pass up
			return;

		if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld) {
			print_error(line, name, "Designator '" + name + "' must denote a variable, an array element or an object field!");
			return;
		}
		if (!right.assignableTo(left.getType()))
			print_error(line, name, "Invalid assignment, incompatible types!");
	}

	public void visit(ProcCall ProcCall)
	{
		int line = ProcCall.getLine();
		String name = callName(ProcCall.getDesignator().obj, 0);
		String type = (methodClass.equals("") ? "Global function" : "Method");

		Obj procCall = ProcCall.getDesignator().obj;

		if (procCall.equals(TabSym.noObj))	// Designator error pass up
			return;

		if (procCall.getKind() != Obj.Meth) {
			print_error(line, name, "Designator '" + name + "' is not a declared function!");
			return;
		}

		actParsCheck(line, procCall);

		name = (methodClass.equals("") ? name : methodClass + "." + name);
		methodClass = "";

		print_info(type + " call '" + name + "' detected at line:" + line);
	}

	public void visit(Increment Increment)
	{
		int line = Increment.getLine();
		String name = Increment.getDesignator().obj.getName();

		int kind = Increment.getDesignator().obj.getKind();
		Struct type = Increment.getDesignator().obj.getType();

		if (Increment.getDesignator().obj.equals(TabSym.noObj))	// Designator error pass up
			return;

		if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld || type.getKind() == Struct.Array) {
			print_error(line, name + "++", "Designator '" + name + "' must denote a variable, an array element or an object field!");
			return;
		}
		if (!type.equals(TabSym.intType))
			print_error(line, name + "++", "Designator '" + name + "' must be type int!");
	}

	public void visit(Decrement Decrement)
	{
		int line = Decrement.getLine();
		String name = Decrement.getDesignator().obj.getName();

		int kind = Decrement.getDesignator().obj.getKind();
		Struct type = Decrement.getDesignator().obj.getType();

		if (Decrement.getDesignator().obj.equals(TabSym.noObj))	// Designator error pass up
			return;

		if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld || type.getKind() == Struct.Array) {
			print_error(line, name + "--", "Designator '" + name + "' must denote a variable, an array element or an object field!");
			return;
		}
		if (!type.equals(TabSym.intType))
			print_error(line, name + "--", "Designator '" + name + "' must be type int!");
	}

// ----------------------------------------------------------- Designator ----------------------------------------------------------- //

	public void visit(DesignatorField DesignatorField)
	{
		int line = DesignatorField.getLine();
		String name = DesignatorField.getName();

		Obj classObj = DesignatorField.getDesignator().obj;
		DesignatorField.obj = TabSym.noObj;

		if (classObj.equals(TabSym.noObj))	// Designator error pass up
			return;

		if (classObj.getType().getKind() != Struct.Class) {
			print_error(line, classObj.getName(), "Designator '" + classObj.getName() + "' is not a class type!");
			return;
		}

		String classType = TabSym.findTypeName(classObj.getType());
		Obj field = classObj.getType().getMembersTable().searchKey(name);

		if (field == null) {
			print_error(line, name, "Designator '" + name + "' is not a " + classType + " class field!");
			return;
		}

		if (field.getKind() == Obj.Fld) {
			name = (field.getType().getKind() == Struct.Array ? name + "[]" : name);
			String fieldName = classType + "." + name;
			print_info("Class field '" + fieldName + "' access detected at line:" + line);
		}
		else {
			methodClass = classType;	
		}

		DesignatorField.obj = field;
	}

	public void visit(DesignatorArray DesignatorArray)
	{
		int line = DesignatorArray.getLine();

		Obj array = DesignatorArray.getDesignator().obj;
		Struct Expr = DesignatorArray.getExpr().struct;
		DesignatorArray.obj = TabSym.noObj;

		if (array.equals(TabSym.noObj))	// Designator error pass up
			return;

		String dataType = TabSym.findTypeName(array.getType().getElemType());

		if (array.getType().getKind() != Struct.Array) {
			print_error(line, array.getName() + "[]", "Designator '" + array.getName() + "' is not an array type!");
			return;
		}
		if (!Expr.equals(TabSym.intType))
			print_error(line, array.getName() + "[]", "Index must be type int!");
		else
			print_info("Array '" + dataType + " " + array.getName() + "[]' element access detected at line:" + line);

		DesignatorArray.obj = new Obj(Obj.Elem, "", array.getType().getElemType());
	}

	public void visit(DesignatorIdent DesignatorIdent)
	{
		int line = DesignatorIdent.getLine();
		String name = DesignatorIdent.getName();

		Obj symbol = TabSym.find(name);
		DesignatorIdent.obj = symbol;

		if (symbol.equals(TabSym.noObj)) {
			print_error(line, name, "Symbol '" + name + "' undeclared!");
			return;
		}

		String dataType = TabSym.findTypeName(symbol.getType());

		switch (symbol.getKind()) {
			case Obj.Con:
				print_info("Symbolic constant '" + dataType + " " + name + "' detected at line:" + line);
				break;

			case Obj.Var:
				if (symbol.getType().getKind() == Struct.Array)
					name = name + "[]";
				String type = (symbol.getFpPos() > 0) ? "Formal argument" :
				              (symbol.getLevel() == 0) ? "Global variable" : "Local variable";
				print_info(type + " '" + dataType + " " + name + "' detected at line:" + line);
				break;

			case Obj.Fld:
				name = (symbol.getType().getKind() == Struct.Array ? name + "[]" : name);
				String fieldName = currClass.getName() + "." + name;
				String methName = methodName(currMethod, false);
				methName = currClass.getName() + "." + methName.substring(methName.indexOf(" ") + 1);
				print_info("Field '" + fieldName + "' inside method '" + methName + "' detected at line:" + line);
				break;
		}
	}

// ----------------------------------------------------------- Condition ----------------------------------------------------------- //

	public void visit(ConditionFact ConditionFact)
	{
		int line = ConditionFact.getLine();

		Struct Expr = ConditionFact.getExpr().struct;

		if (Expr == TabSym.noType)	// Expr error pass up
			return;

		if (!Expr.equals(TabSym.boolType))
			print_error(line, "", "Condition expression must have bool type!");
	}

	public void visit(ConditionFactRelop ConditionFactRelop)
	{
		int line = ConditionFactRelop.getLine();

		String relop = "";
		if (ConditionFactRelop.getRelop() instanceof RelopEQ) relop = "==";
		else if (ConditionFactRelop.getRelop() instanceof RelopNEQ) relop = "!=";
		else if (ConditionFactRelop.getRelop() instanceof RelopGR) relop = ">";
		else if (ConditionFactRelop.getRelop() instanceof RelopGREQ) relop = ">=";
		else if (ConditionFactRelop.getRelop() instanceof RelopLS) relop = "<";
		else if (ConditionFactRelop.getRelop() instanceof RelopLSEQ) relop = "<=";

		Struct ExprFirst = ConditionFactRelop.getExpr().struct;
		Struct ExprSecond = ConditionFactRelop.getExpr1().struct;

		if (ExprFirst == TabSym.noType || ExprSecond == TabSym.noType)	// Expr error pass up
			return;

		if (!ExprFirst.compatibleWith(ExprSecond)) {
			print_error(line, relop, "Incompatible comparison types!");
			return;
		}
		if (ExprFirst.isRefType() && !(relop.equals("==") || relop.equals("!=")))
			print_error(line, relop, "Invalid relational operator, expected '==' or '!=' for reference types!");
	}

// ----------------------------------------------------------- Expression ----------------------------------------------------------- //

	public void visit(Expressions Expressions)
	{
		Expressions.struct = Expressions.getExprList().struct;
	}

	public void visit(NegExpresions NegExpresions)
	{
		int line = NegExpresions.getLine();

		Struct ExprList = NegExpresions.getExprList().struct;
		NegExpresions.struct = TabSym.noType;

		if (ExprList == TabSym.noType)	// ExprList error pass up
			return;

		if (ExprList.equals(TabSym.intType))
			NegExpresions.struct = TabSym.intType;
		else
			print_error(line, "", "Incompatible type in negative expression, expected int!");
	}

	public void visit(ExpressionList ExpressionList)
	{
		int line = ExpressionList.getLine();
		String operator = (ExpressionList.getAddop() instanceof AddopAdd) ? "+" : "-";

		Struct ExprList = ExpressionList.getExprList().struct;
		Struct Term = ExpressionList.getTerm().struct;
		ExpressionList.struct = TabSym.noType;

		if (ExprList == TabSym.noType || Term == TabSym.noType)	// Term error pass up
			return;

		// Vector addition
		if (operator.equals("+") &&
		    ExprList.getKind() == Struct.Array && Term.getKind() == Struct.Array &&
		    ExprList.getElemType().equals(TabSym.intType) && Term.getElemType().equals(TabSym.intType))
		{
			ExpressionList.struct = new Struct(Struct.Array, TabSym.intType);
			return;
		}

		if (ExprList.equals(Term) && ExprList.equals(TabSym.intType) && Term.equals(TabSym.intType))
			ExpressionList.struct = TabSym.intType;
		else
			print_error(line, operator, "Incompatible types in arithmetic expression, expected int!");
	}

	public void visit(SingleExpression SingleExpression)
	{
		SingleExpression.struct = SingleExpression.getTerm().struct;
	}

// ----------------------------------------------------------- Term ----------------------------------------------------------- //

	public void visit(TermList TermList)
	{
		int line = TermList.getLine();
		String operator = (TermList.getMulop() instanceof MulopMul) ? "*" :
		                  (TermList.getMulop() instanceof MulopDiv) ? "/" : "%";

		Struct Term = TermList.getTerm().struct;
		Struct Factor = TermList.getFactor().struct;
		TermList.struct = TabSym.noType;

		if (Term == TabSym.noType || Factor == TabSym.noType)	// Factor error pass up
			return;

		// Vector * Scalar multiplication
		if (operator.equals("*") &&
		    (Term.getKind() == Struct.Array && Term.getElemType().equals(TabSym.intType) && Factor.equals(TabSym.intType) ||
		    Factor.getKind() == Struct.Array && Factor.getElemType().equals(TabSym.intType) && Term.equals(TabSym.intType)))
		{
			TermList.struct = new Struct(Struct.Array, TabSym.intType);
			return;
		}

		// Vector * Vector multiplication
		if (operator.equals("*") && Term.getKind() == Struct.Array && Factor.getKind() == Struct.Array) {
			Term = Term.getElemType();
			Factor = Factor.getElemType();
		}

		if (Term.equals(Factor) && Term.equals(TabSym.intType) && Factor.equals(TabSym.intType))
			TermList.struct = TabSym.intType;
		else
			print_error(line, operator, "Incompatible types in arithmetic expression, expected int!");
	}

	public void visit(SingleTerm SingleTerm)
	{
		SingleTerm.struct = SingleTerm.getFactor().struct;
	}

// ----------------------------------------------------------- Factor ----------------------------------------------------------- //		

	public void visit(FactorDesignator FactorDesignator)
	{
		FactorDesignator.struct = FactorDesignator.getDesignator().obj.getType();
	}

	public void visit(FactorProcCall FactorProcCall)
	{
		int line = FactorProcCall.getLine();
		String name = callName(FactorProcCall.getDesignator().obj, 0);
		String type = (methodClass.equals("") ? "Global function" : "Method");

		Obj procCall = FactorProcCall.getDesignator().obj;
		FactorProcCall.struct = TabSym.noType;

		if (procCall.equals(TabSym.noObj))	// Undeclared designator error
			return;

		if (procCall.getKind() != Obj.Meth) {
			print_error(line, name, "Designator '" + name + "' is not a declared function!");
			return;
		}

		actParsCheck(line, procCall);

		name = (methodClass.equals("") ? name : methodClass + "." + name);
		methodClass = "";

		print_info(type + " call '" + name + "' detected at line:" + line);
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
		int line = FactorNew.getLine();
		String name = FactorNew.getType().getName();
		FactorNew.struct = TabSym.noType;

		if (currType.equals(TabSym.noType))	// Type error
			return;

		if (currType.getKind() != Struct.Class) {
			print_error(line, "", "Designator '" + name + "' is not a declared class!");
			return;
		}

		print_info("Creation of class " + name + " object detected at line:" + line);
		FactorNew.struct = currType;
	}

	public void visit(FactorNewArray FactorNewArray)
	{
		int line = FactorNewArray.getLine();
		String name = FactorNewArray.getType().getName();

		Struct Expr = FactorNewArray.getExpr().struct;
		FactorNewArray.struct = TabSym.noType;

		if (currType.equals(TabSym.noType))	// Type error
			return;

		if (!Expr.equals(TabSym.intType))
			print_error(line, "", "Array size must be type int!");
		else 
			print_info("Creation of " + name + " array detected at line:" + line);

		FactorNewArray.struct = new Struct(Struct.Array, currType);
	}

	public void visit(FactorExpression FactorExpression)
	{
		FactorExpression.struct = FactorExpression.getExpr().struct;
	}
}
