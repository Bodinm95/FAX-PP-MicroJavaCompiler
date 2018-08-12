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

