// generated with ast extension for cup
// version 0.8
// 10/7/2018 13:1:29


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclarationPart extends ConstDeclPart {

    private String I1;
    private ConstDeclAlt ConstDeclAlt;

    public ConstDeclarationPart (String I1, ConstDeclAlt ConstDeclAlt) {
        this.I1=I1;
        this.ConstDeclAlt=ConstDeclAlt;
        if(ConstDeclAlt!=null) ConstDeclAlt.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public ConstDeclAlt getConstDeclAlt() {
        return ConstDeclAlt;
    }

    public void setConstDeclAlt(ConstDeclAlt ConstDeclAlt) {
        this.ConstDeclAlt=ConstDeclAlt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclAlt!=null) ConstDeclAlt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclAlt!=null) ConstDeclAlt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclAlt!=null) ConstDeclAlt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclarationPart(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(ConstDeclAlt!=null)
            buffer.append(ConstDeclAlt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclarationPart]");
        return buffer.toString();
    }
}
