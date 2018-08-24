// generated with ast extension for cup
// version 0.8
// 24/7/2018 16:52:58


package rs.ac.bg.etf.pp1.ast;

public class ElseOption extends ElseOpt {

    private ElseStmt ElseStmt;
    private Stmt Stmt;

    public ElseOption (ElseStmt ElseStmt, Stmt Stmt) {
        this.ElseStmt=ElseStmt;
        if(ElseStmt!=null) ElseStmt.setParent(this);
        this.Stmt=Stmt;
        if(Stmt!=null) Stmt.setParent(this);
    }

    public ElseStmt getElseStmt() {
        return ElseStmt;
    }

    public void setElseStmt(ElseStmt ElseStmt) {
        this.ElseStmt=ElseStmt;
    }

    public Stmt getStmt() {
        return Stmt;
    }

    public void setStmt(Stmt Stmt) {
        this.Stmt=Stmt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ElseStmt!=null) ElseStmt.accept(visitor);
        if(Stmt!=null) Stmt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ElseStmt!=null) ElseStmt.traverseTopDown(visitor);
        if(Stmt!=null) Stmt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ElseStmt!=null) ElseStmt.traverseBottomUp(visitor);
        if(Stmt!=null) Stmt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ElseOption(\n");

        if(ElseStmt!=null)
            buffer.append(ElseStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Stmt!=null)
            buffer.append(Stmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ElseOption]");
        return buffer.toString();
    }
}
