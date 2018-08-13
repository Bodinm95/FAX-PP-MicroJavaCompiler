// generated with ast extension for cup
// version 0.8
// 13/7/2018 17:24:52


package rs.ac.bg.etf.pp1.ast;

public class IfStatement extends Stmt {

    private Cond Cond;
    private Stmt Stmt;
    private ElseOpt ElseOpt;

    public IfStatement (Cond Cond, Stmt Stmt, ElseOpt ElseOpt) {
        this.Cond=Cond;
        if(Cond!=null) Cond.setParent(this);
        this.Stmt=Stmt;
        if(Stmt!=null) Stmt.setParent(this);
        this.ElseOpt=ElseOpt;
        if(ElseOpt!=null) ElseOpt.setParent(this);
    }

    public Cond getCond() {
        return Cond;
    }

    public void setCond(Cond Cond) {
        this.Cond=Cond;
    }

    public Stmt getStmt() {
        return Stmt;
    }

    public void setStmt(Stmt Stmt) {
        this.Stmt=Stmt;
    }

    public ElseOpt getElseOpt() {
        return ElseOpt;
    }

    public void setElseOpt(ElseOpt ElseOpt) {
        this.ElseOpt=ElseOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Cond!=null) Cond.accept(visitor);
        if(Stmt!=null) Stmt.accept(visitor);
        if(ElseOpt!=null) ElseOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Cond!=null) Cond.traverseTopDown(visitor);
        if(Stmt!=null) Stmt.traverseTopDown(visitor);
        if(ElseOpt!=null) ElseOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Cond!=null) Cond.traverseBottomUp(visitor);
        if(Stmt!=null) Stmt.traverseBottomUp(visitor);
        if(ElseOpt!=null) ElseOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfStatement(\n");

        if(Cond!=null)
            buffer.append(Cond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Stmt!=null)
            buffer.append(Stmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseOpt!=null)
            buffer.append(ElseOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfStatement]");
        return buffer.toString();
    }
}
