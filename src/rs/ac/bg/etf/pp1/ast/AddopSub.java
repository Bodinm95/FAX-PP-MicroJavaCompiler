// generated with ast extension for cup
// version 0.8
// 13/7/2018 14:12:18


package rs.ac.bg.etf.pp1.ast;

public class AddopSub extends Addop {

    public AddopSub () {
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
        buffer.append("AddopSub(\n");

        buffer.append(tab);
        buffer.append(") [AddopSub]");
        return buffer.toString();
    }
}
