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
		TabSym.chainLocalSymbols(Program.getProgId().obj);
		TabSym.closeScope();
		state = Scope.GLOBAL;
		System.out.println("Program '" + ((ProgramId)Program.getProgId()).getName() + "' successfully processed.");
	}

	public void visit(ProgramId ProgramId) {
		ProgramId.obj = TabSym.insert(Obj.Prog, ProgramId.getName(), TabSym.noType);
		System.out.println("Program '" + ProgramId.getName() + "' declared at line:" + ProgramId.getLine());
		TabSym.openScope();
		state = Scope.GLOBAL;
	}

	public void visit(Type Type) {
		Type.struct = TabSym.noType;
		Obj typeObj = TabSym.find(Type.getName());
		if (typeObj == TabSym.noObj || typeObj.getKind() != Obj.Type)
			print_error(Type.getLine(), Type.getName(), "Symbol '" + Type.getName() + "' is not a type!");
		else
			Type.struct = typeObj.getType();

		currType = Type.struct;
	}

// ----------------------------------------------------------------------------------------------------------------------------- //

	public void visit(ConstDeclarationNum ConstDeclarationNum) {
		if (!currType.equals(TabSym.intType)) {
			print_error(ConstDeclarationNum.getLine(), ConstDeclarationNum.getName(), "Incompatible types, expected int!");
			return;
		}
		if (TabSym.currentScope().findSymbol(ConstDeclarationNum.getName()) != null) {
			print_error(ConstDeclarationNum.getLine(), ConstDeclarationNum.getName(), "Symbol '" + ConstDeclarationNum.getName() + "' already defined in current scope!");
			return;
		}
		Obj constant = TabSym.insert(Obj.Con, ConstDeclarationNum.getName(), currType);
		constant.setAdr(ConstDeclarationNum.getValue().intValue());
		System.out.println("Symbolic num constant '" + ConstDeclarationNum.getName() + " = " + ConstDeclarationNum.getValue() + "' declared at line:" + ConstDeclarationNum.getLine());
	}

	public void visit(ConstDeclarationChar ConstDeclarationChar) {
		if (!currType.equals(TabSym.charType)) {
			print_error(ConstDeclarationChar.getLine(), ConstDeclarationChar.getName(), "Incompatible types, expected char!");
			return;
		}
		if (TabSym.currentScope().findSymbol(ConstDeclarationChar.getName()) != null) {
			print_error(ConstDeclarationChar.getLine(), ConstDeclarationChar.getName(), "Symbol '" + ConstDeclarationChar.getName() + "' already defined in current scope!");
			return;
		}
		Obj constant = TabSym.insert(Obj.Con, ConstDeclarationChar.getName(), currType);
		constant.setAdr(ConstDeclarationChar.getValue().charValue());
		System.out.println("Symbolic char constant '" + ConstDeclarationChar.getName() + " = " + ConstDeclarationChar.getValue() + "' declared at line:" + ConstDeclarationChar.getLine());
	}

	public void visit(ConstDeclarationBool ConstDeclarationBool) {
		if (!currType.equals(TabSym.boolType)) {
			print_error(ConstDeclarationBool.getLine(), ConstDeclarationBool.getName(), "Incompatible types, expected bool!");
			return;
		}
		if (TabSym.currentScope().findSymbol(ConstDeclarationBool.getName()) != null) {
			print_error(ConstDeclarationBool.getLine(), ConstDeclarationBool.getName(), "Symbol '" + ConstDeclarationBool.getName() + "' already defined in current scope!");
			return;
		}
		Obj constant = TabSym.insert(Obj.Con, ConstDeclarationBool.getName(), currType);
		constant.setAdr((ConstDeclarationBool.getValue().equals("true")) ? 1 : 0);
		System.out.println("Symbolic char constant '" + ConstDeclarationBool.getName() + " = " + ConstDeclarationBool.getValue() + "' declared at line:" + ConstDeclarationBool.getLine());
	}

// ----------------------------------------------------------------------------------------------------------------------------- //

	public void visit(VarDeclarationPart VarDeclarationPart) {
		if (TabSym.currentScope().findSymbol(VarDeclarationPart.getName()) != null) {
			print_error(VarDeclarationPart.getLine(), VarDeclarationPart.getName(), "Symbol '" + VarDeclarationPart.getName() + "' already defined in current scope!");
			return;
		}
		if (!currType.equals(TabSym.noType)) {
			TabSym.insert(Obj.Var, VarDeclarationPart.getName(), currType);
			switch (state) {
			case GLOBAL: System.out.println("Global variable '" + VarDeclarationPart.getName() + "' declared at line:" + VarDeclarationPart.getLine()); break;
			case LOCAL: System.out.println("Local variable '" + VarDeclarationPart.getName() + "' declared in function '" + currMethod.getName() + "' at line:" + VarDeclarationPart.getLine()); break;
			case CLASS: System.out.println(""); break;
			case METHOD: System.out.println(""); break;
			}
		}
	}

	public void visit(VarDeclarationPartArray VarDeclarationPartArray) {
		if (TabSym.currentScope().findSymbol(VarDeclarationPartArray.getName()) != null) {
			print_error(VarDeclarationPartArray.getLine(), VarDeclarationPartArray.getName(), "Symbol '" + VarDeclarationPartArray.getName() + "' already defined in current scope!");
			return;
		}
		if (!currType.equals(TabSym.noType)) {
			TabSym.insert(Obj.Var, VarDeclarationPartArray.getName(), new Struct(Struct.Array, currType));
			switch (state) {
			case GLOBAL: System.out.println("Global variable '" + VarDeclarationPartArray.getName() + "[]' declared at line:" + VarDeclarationPartArray.getLine()); break;
			case LOCAL: System.out.println("Local variable '" + VarDeclarationPartArray.getName() + "[]' declared in function '" + currMethod.getName() +"' at line:" + VarDeclarationPartArray.getLine()); break;
			case CLASS: System.out.println(""); break;
			case METHOD: System.out.println(""); break;
			}
		}
	}

