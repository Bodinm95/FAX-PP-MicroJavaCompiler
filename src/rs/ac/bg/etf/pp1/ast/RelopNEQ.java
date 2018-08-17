// generated with ast extension for cup
// version 0.8
// 17/7/2018 15:31:48


package rs.ac.bg.etf.pp1.ast;

public class RelopNEQ extends Relop {

    public RelopNEQ () {
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
        buffer.append("RelopNEQ(\n");

        buffer.append(tab);
        buffer.append(") [RelopNEQ]");
        return buffer.toString();
    }
}
