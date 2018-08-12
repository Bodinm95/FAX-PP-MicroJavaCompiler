// generated with ast extension for cup
// version 0.8
// 12/7/2018 20:53:14


package rs.ac.bg.etf.pp1.ast;

public class FactorBool extends Factor {

    private String B1;

    public FactorBool (String B1) {
        this.B1=B1;
    }

    public String getB1() {
        return B1;
    }

    public void setB1(String B1) {
        this.B1=B1;
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
        buffer.append("FactorBool(\n");

        buffer.append(" "+tab+B1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorBool]");
        return buffer.toString();
    }
}
