// generated with ast extension for cup
// version 0.8
// 16/7/2018 10:48:40


package rs.ac.bg.etf.pp1.ast;

public class DoStatement extends DoStmt {

    public DoStatement () {
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
        buffer.append("DoStatement(\n");

        buffer.append(tab);
        buffer.append(") [DoStatement]");
        return buffer.toString();
    }
}