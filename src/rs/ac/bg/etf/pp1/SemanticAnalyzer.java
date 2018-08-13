package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.TabSym;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	public enum Scope {GLOBAL, LOCAL, CLASS, METHOD};
	Scope state;

	boolean error = false;

	Struct currType;
	Obj currClass;
	Obj currMethod;

	public void print_error(int line, String name, String msg) {
		error = true;
		System.err.println("Semantic error '" + name + "' at line:" + line + " - " + msg);
	}

// ----------------------------------------------------------------------------------------------------------------------------- //

	public void visit(Program Program) {
		String name = ((ProgramId)Program.getProgId()).getName();

		TabSym.chainLocalSymbols(Program.getProgId().obj);
		TabSym.closeScope();
		state = Scope.GLOBAL;
		System.out.println("Program '" + name + "' successfully processed.");
	}

	public void visit(ProgramId ProgramId) {
		String name = ProgramId.getName();
		int line = ProgramId.getLine();

		ProgramId.obj = TabSym.insert(Obj.Prog, name, TabSym.noType);
		System.out.println("Program '" + name + "' declared at line:" + line);
		TabSym.openScope();
		state = Scope.GLOBAL;
	}

	public void visit(Type Type) {
		String name = Type.getName();
		int line = Type.getLine();

		Type.struct = TabSym.noType;
		Obj typeObj = TabSym.find(name);
		if (typeObj == TabSym.noObj || typeObj.getKind() != Obj.Type)
			print_error(line, name, "Symbol '" + name + "' is not a type!");
		else
			Type.struct = typeObj.getType();

		currType = Type.struct;
	}

// ----------------------------------------------------------------------------------------------------------------------------- //

	public void visit(ConstDeclarationNum ConstDeclarationNum) {
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
		System.out.println("Symbolic num constant '" + name + " = " + value + "' declared at line:" + line);
	}

	public void visit(ConstDeclarationChar ConstDeclarationChar) {
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
		System.out.println("Symbolic char constant '" + name + " = " + value + "' declared at line:" + line);
	}

	public void visit(ConstDeclarationBool ConstDeclarationBool) {
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
		System.out.println("Symbolic char constant '" + name + " = " + value + "' declared at line:" + line);
	}

// ----------------------------------------------------------------------------------------------------------------------------- //

	public void visit(VarDeclarationPart VarDeclarationPart) {
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
			case GLOBAL: System.out.println("Global variable '" + name + "' declared at line:" + line); break;
			case LOCAL: System.out.println("Local variable '" + name + "' declared in function '" + currMethod.getName() + "' at line:" + line); break;
			case CLASS: System.out.println("Field '" + name + "' declared in class '" + currClass.getName() + "' at line:" + line); break;
			case METHOD: System.out.println(""); break;
			}
		}
	}

	public void visit(VarDeclarationPartArray VarDeclarationPartArray) {
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
			case GLOBAL: System.out.println("Global variable '" + name + "[]' declared at line:" + line); break;
			case LOCAL: System.out.println("Local variable '" + name + "[]' declared in function '" + currMethod.getName() +"' at line:" + line); break;
			case CLASS: System.out.println(""); break;
			case METHOD: System.out.println(""); break;
			}
		}
	}

// ----------------------------------------------------------------------------------------------------------------------------- //

	public void visit(ClassDeclaration ClassDeclaration) {
		String name = ClassDeclaration.getClassId().getName();

		TabSym.chainLocalSymbols(currClass.getType());
		TabSym.closeScope();
		state = Scope.GLOBAL;
		System.out.println("Class '" + name + "' successfully processed.");
		currClass = null;
	}

	public void visit(ClassId ClassId) {
		String name = ClassId.getName();
		int line = ClassId.getLine();

		if (TabSym.currentScope().findSymbol(name) != null) {
			print_error(line, name, "Symbol '" + name + "' already defined in current scope!");
			return;
		}
		currClass = TabSym.insert(Obj.Type, name, new Struct(Struct.Class));
		System.out.println("Class '" + name + "' declared at line:" + line);
		TabSym.openScope();
		state = Scope.CLASS;
	}

	public void visit(ExtendsDeclaration ExtendsDeclaration) {
		String name = ExtendsDeclaration.getType().getName();
		int line = ExtendsDeclaration.getLine();

		Obj parent = TabSym.find(name);
		if (parent == TabSym.noObj || parent.getType().getKind() != Struct.Class) {
			print_error(line, name, "Symbol '" + name + "' is not a declared class!");
			return;
		}
		for (Obj field : parent.getType().getMembers())
			if (!field.getName().equals("this")) {
				TabSym.insert(Obj.Fld, field.getName(), field.getType());
			}
		System.out.println("Class '" + currClass.getName() + "' extends class '" + name + "'");
	}
	
















































}
