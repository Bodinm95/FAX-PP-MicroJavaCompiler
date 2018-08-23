// generated with ast extension for cup
// version 0.8
// 23/7/2018 22:19:45


package rs.ac.bg.etf.pp1.ast;

public class EmptyDeclarationList extends DeclList {

    public EmptyDeclarationList () {
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
        buffer.append("EmptyDeclarationList(\n");

        buffer.append(tab);
        buffer.append(") [EmptyDeclarationList]");
        return buffer.toString();
    }
}
