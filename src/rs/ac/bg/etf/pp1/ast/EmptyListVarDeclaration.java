// generated with ast extension for cup
// version 0.8
// 15/7/2018 19:36:24


package rs.ac.bg.etf.pp1.ast;

public class EmptyListVarDeclaration extends ListVarDecl {

    public EmptyListVarDeclaration () {
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
        buffer.append("EmptyListVarDeclaration(\n");

        buffer.append(tab);
        buffer.append(") [EmptyListVarDeclaration]");
        return buffer.toString();
    }
}
