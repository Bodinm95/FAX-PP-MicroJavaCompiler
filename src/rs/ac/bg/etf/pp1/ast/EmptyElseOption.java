// generated with ast extension for cup
// version 0.8
// 24/7/2018 15:22:49


package rs.ac.bg.etf.pp1.ast;

public class EmptyElseOption extends ElseOpt {

    public EmptyElseOption () {
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
        buffer.append("EmptyElseOption(\n");

        buffer.append(tab);
        buffer.append(") [EmptyElseOption]");
        return buffer.toString();
    }
}
