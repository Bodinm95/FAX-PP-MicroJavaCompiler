// generated with ast extension for cup
// version 0.8
// 10/7/2018 13:1:29


package rs.ac.bg.etf.pp1.ast;

public class EmptyPrintOption extends PrintOpt {

    public EmptyPrintOption () {
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
        buffer.append("EmptyPrintOption(\n");

        buffer.append(tab);
        buffer.append(") [EmptyPrintOption]");
        return buffer.toString();
    }
}