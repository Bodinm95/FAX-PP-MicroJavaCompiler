// generated with ast extension for cup
// version 0.8
// 13/7/2018 12:9:24


package rs.ac.bg.etf.pp1.ast;

public class EmptyMethodDeclarationList extends MethodDeclList {

    public EmptyMethodDeclarationList () {
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
        buffer.append("EmptyMethodDeclarationList(\n");

        buffer.append(tab);
        buffer.append(") [EmptyMethodDeclarationList]");
        return buffer.toString();
    }
}
