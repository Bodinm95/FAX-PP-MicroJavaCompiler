// generated with ast extension for cup
// version 0.8
// 13/7/2018 17:24:52


package rs.ac.bg.etf.pp1.ast;

public class WhileStatement extends Stmt {

    private Stmt Stmt;
    private Cond Cond;

    public WhileStatement (Stmt Stmt, Cond Cond) {
        this.Stmt=Stmt;
        if(Stmt!=null) Stmt.setParent(this);
        this.Cond=Cond;
        if(Cond!=null) Cond.setParent(this);
    }

    public Stmt getStmt() {
        return Stmt;
    }

    public void setStmt(Stmt Stmt) {
        this.Stmt=Stmt;
    }

    public Cond getCond() {
        return Cond;
    }

    public void setCond(Cond Cond) {
        this.Cond=Cond;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Stmt!=null) Stmt.accept(visitor);
        if(Cond!=null) Cond.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Stmt!=null) Stmt.traverseTopDown(visitor);
        if(Cond!=null) Cond.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Stmt!=null) Stmt.traverseBottomUp(visitor);
        if(Cond!=null) Cond.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("WhileStatement(\n");

        if(Stmt!=null)
            buffer.append(Stmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Cond!=null)
            buffer.append(Cond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [WhileStatement]");
        return buffer.toString();
    }
}
