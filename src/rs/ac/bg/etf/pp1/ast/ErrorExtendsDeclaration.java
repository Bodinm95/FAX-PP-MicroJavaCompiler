// generated with ast extension for cup
// version 0.8
// 17/7/2018 16:12:13


package rs.ac.bg.etf.pp1.ast;

public class ErrorExtendsDeclaration extends ExtendsDecl {

    public ErrorExtendsDeclaration () {
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
        buffer.append("ErrorExtendsDeclaration(\n");

        buffer.append(tab);
        buffer.append(") [ErrorExtendsDeclaration]");
        return buffer.toString();
    }
}
