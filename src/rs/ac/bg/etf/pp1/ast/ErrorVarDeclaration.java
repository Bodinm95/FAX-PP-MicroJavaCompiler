// generated with ast extension for cup
// version 0.8
// 23/7/2018 14:12:22


package rs.ac.bg.etf.pp1.ast;

public class ErrorVarDeclaration extends VarDecl {

    public ErrorVarDeclaration () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ErrorVarDeclaration(\n");

        buffer.append(tab);
        buffer.append(") [ErrorVarDeclaration]");
        return buffer.toString();
    }
}
