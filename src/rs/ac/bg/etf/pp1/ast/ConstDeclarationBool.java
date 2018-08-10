// generated with ast extension for cup
// version 0.8
// 10/7/2018 13:1:29


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclarationBool extends ConstDeclAlt {

    private String B1;

    public ConstDeclarationBool (String B1) {
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
        buffer.append("ConstDeclarationBool(\n");

        buffer.append(" "+tab+B1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclarationBool]");
        return buffer.toString();
    }
}
