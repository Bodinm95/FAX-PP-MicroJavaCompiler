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
	boolean error = false;

	Struct currType;
	Obj currClass;
	Obj currMethod;

	boolean returnFound;

	List<Obj> formParamList = new ArrayList<Obj>();;
	List<Struct> actParamList = new ArrayList<Struct>();

	String methodClass = "";

	public void print_error(int line, String name, String msg) {
		global_error = true;
		error = true;
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
		String name = ((ProgramId)Program.getProgId()).getName();

		TabSym.chainLocalSymbols(Program.getProgId().obj);
		TabSym.closeScope();

		state.pop();

		print_info("Program '" + name + "' " + (global_error ? "" : "successfully ") + "processed.");
	}

	public void visit(ProgramId ProgramId)
	{
		String name = ProgramId.getName();
		int line = ProgramId.getLine();

		ProgramId.obj = TabSym.insert(Obj.Prog, name, TabSym.noType);
		TabSym.openScope();
		
		error = false;
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
			int kind = (state.peek() == Scope.CLASS) ? Obj.Fld : Obj.Var;
			TabSym.insert(kind, name, currType);

			switch (state.peek()) {
			case GLOBAL: print_info("Global variable '" + name + "' declared at line:" + line); break;
			case LOCAL: print_info("Local variable '" + name + "' declared in function '" + currMethod.getName() + "' at line:" + line); break;
			case CLASS: print_info("Field '" + name + "' declared in class '" + currClass.getName() + "' at line:" + line); break;
			case METHOD: print_info("Local variable '" + name + "' declared in class '" + currClass.getName() + "' method '" + currMethod.getName() + "' at line:" + line); break;
			default: break;
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
			int kind = (state.peek() == Scope.CLASS) ? Obj.Fld : Obj.Var;
			TabSym.insert(kind, name, new Struct(Struct.Array, currType));

			switch (state.peek()) {
			case GLOBAL: print_info("Global variable '" + name + "[]' declared at line:" + line); break;
			case LOCAL: print_info("Local variable '" + name + "[]' declared in function '" + currMethod.getName() +"' at line:" + line); break;
			case CLASS: print_info("Field '" + name + "[]' declared in class '" + currClass.getName() + "' at line:" + line); break;
			case METHOD: print_info("Local variable '" + name + "[]' declared in class '" + currClass.getName() + "' method '" + currMethod.getName() + "' at line:" + line); break;
			default: break;
			}
		}
	}

// ----------------------------------------------- ClassDecl ----------------------------------------------------------- //

	public void visit(ClassDeclaration ClassDeclaration)
	{
		String name = ClassDeclaration.getClassId().getName();

		TabSym.chainLocalSymbols(currClass.getType());
		TabSym.closeScope();

		state.pop();
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
		state.push(Scope.CLASS);
	}

	public void visit(ExtendsDeclaration ExtendsDeclaration)
	{
		String name = ExtendsDeclaration.getType().getName();
		int line = ExtendsDeclaration.getLine();

		Obj parent = TabSym.find(name);

		if (parent == TabSym.noObj || parent.getType().getKind() != Struct.Class) {
			print_error(line, name, "'" + name + "' is not a declared class!");
			TabSym.currentScope.getOuter().getLocals().deleteKey(currClass.getName());

			return;
		}

		for (Obj field : parent.getType().getMembers())
			if (!field.getName().equals("this")) {
				TabSym.insert(Obj.Fld, field.getName(), field.getType());
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
			print_info("Function '" + name + "' " + (error ? "" : "successfully ") + "processed.");
			break;
		case METHOD:
			state.pop();
			print_info("Method '" + name + "' " + (error ? "" : "successfully ") + "processed.");
			break;
		default :
			break;
		}

		currMethod = null;
	}

	public void visit(MethodSignature MethodSignature)
	{
		if (!error) {
			// Save formal params from TabSym and close method scope used for inserting formal params
			SymbolDataStructure formPars = TabSym.currentScope().getLocals();
			TabSym.closeScope();

			// Insert method object in TabSym and set number of formal params to 0
			currMethod = TabSym.insert(currMethod.getKind(), currMethod.getName(), currMethod.getType());
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
		error = false;	// Reset error flags
		returnFound = false;
	}

	public void visit(MethodId MethodId)
	{
		int line = MethodId.getLine();
		String name = MethodId.getName();
		String type = TabSym.findTypeName(currType);

		error = false;	// Reset method error

		if (currType.equals(TabSym.errorType))	// Type error
			error = true;

		if (TabSym.currentScope().findSymbol(name) != null)
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");

		currMethod = new Obj(Obj.Meth, name, currType);
		currMethod.setLevel(0);

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
		String name = func.getName() + (actParamList.isEmpty() ? "()" : "(...)");

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
				String actual = TabSym.findTypeName(actParamList.get(i));
				String formal = TabSym.findTypeName(formParamList.get(i).getType());
				print_error(line, name, "Actual '" + actual + "' parameter does not match formal '" + formal + "' parameter!");
			}

		formParamList.clear();
		actParamList.clear();
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

		if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld || left.getType().getKind() == Struct.Array) {
			print_error(line, name, "Designator '" + name + "' must denote a variable, an array element or an object field!");
			return;
		}
		if (!right.assignableTo(left.getType()))
			print_error(line, name, "Invalid assignment, incompatible types!");
	}

	public void visit(ProcCall ProcCall)
	{
		int line = ProcCall.getLine();
		String name = ProcCall.getDesignator().obj.getName() + (actParamList.isEmpty() ? "()" : "(...)");
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
					break;
				String type = (symbol.getFpPos() > 0) ? "Formal argument" :
				              (symbol.getLevel() == 0) ? "Global variable" : "Local variable";
				print_info(type + " '" + dataType + " " + name + "' detected at line:" + line);
				break;

			case Obj.Fld:
				name = (symbol.getType().getKind() == Struct.Array ? name + "[]" : name);
				String fieldName = currClass.getName() + "." + name;
				String methName = currClass.getName() + "." + currMethod.getName() + (formParamList.isEmpty() ? "()" : "(...)");
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
		String name = FactorProcCall.getDesignator().obj.getName() + (actParamList.isEmpty() ? "()" : "(...)");
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
