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

