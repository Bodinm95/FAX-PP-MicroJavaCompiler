// generated with ast extension for cup
// version 0.8
// 24/7/2018 18:32:3


package rs.ac.bg.etf.pp1.ast;

public class RelopLS extends Relop {

    public RelopLS () {
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
        buffer.append("RelopLS(\n");

        buffer.append(tab);
        buffer.append(") [RelopLS]");
        return buffer.toString();
    }
}
