package rs.ac.bg.etf.pp1.util;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

public class TabSym extends Tab {

	public static final Struct boolType = new Struct(Struct.Bool);

	private static int currentLevel;

	public static void init() {
		Scope universe = currentScope = new Scope(null);
		
		universe.addToLocals(new Obj(Obj.Type, "int", intType));
		universe.addToLocals(new Obj(Obj.Type, "char", charType));
		universe.addToLocals(new Obj(Obj.Type, "bool", boolType));
		universe.addToLocals(new Obj(Obj.Con, "eol", charType, 10, 0));
		universe.addToLocals(new Obj(Obj.Con, "null", nullType, 0, 0));
		
		universe.addToLocals(chrObj = new Obj(Obj.Meth, "chr", charType, 0, 1));
		{
			openScope();
			currentScope.addToLocals(new Obj(Obj.Var, "i", intType, 0, 1));
			chrObj.setLocals(currentScope.getLocals());
			closeScope();
		}
		
		universe.addToLocals(ordObj = new Obj(Obj.Meth, "ord", intType, 0, 1));
		{
			openScope();
			currentScope.addToLocals(new Obj(Obj.Var, "ch", charType, 0, 1));
			ordObj.setLocals(currentScope.getLocals());
			closeScope();
		} 
		
		
		universe.addToLocals(lenObj = new Obj(Obj.Meth, "len", intType, 0, 1));
		{
			openScope();
			currentScope.addToLocals(new Obj(Obj.Var, "arr", new Struct(Struct.Array, noType), 0, 1));
			lenObj.setLocals(currentScope.getLocals());
			closeScope();
		}
		
		currentLevel = -1;
	}

	public static Obj insert(int kind, String name, Struct type) {
		// create a new Object node with kind, name, type
		Obj newObj = new Obj(kind, name, type, 0, currentLevel); 
		
		// append the node to the end of the symbol list
		if (!currentScope.addToLocals(newObj)) {
			Obj res = currentScope.findSymbol(name);
			return (res != null) ? res : noObj;
		}
		else 
			return newObj;
	}

	public static void openScope() {
		currentScope = new Scope(currentScope);
		currentLevel++;
	}

	public static void closeScope() {
		currentScope = currentScope.getOuter();
		currentLevel--;
	}
}
