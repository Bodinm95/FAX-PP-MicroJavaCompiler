// generated with ast extension for cup
// version 0.8
// 13/7/2018 14:12:18


package rs.ac.bg.etf.pp1.ast;

public class Conditions extends Cond {

    private Cond Cond;
    private CondTerm CondTerm;

    public Conditions (Cond Cond, CondTerm CondTerm) {
        this.Cond=Cond;
        if(Cond!=null) Cond.setParent(this);
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
    }

    public Cond getCond() {
        return Cond;
    }

    public void setCond(Cond Cond) {
        this.Cond=Cond;
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Cond!=null) Cond.accept(visitor);
        if(CondTerm!=null) CondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Cond!=null) Cond.traverseTopDown(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Cond!=null) Cond.traverseBottomUp(visitor);
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Conditions(\n");

        if(Cond!=null)
            buffer.append(Cond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Conditions]");
        return buffer.toString();
    }
}
