// generated with ast extension for cup
// version 0.8
// 23/7/2018 16:43:48


package rs.ac.bg.etf.pp1.ast;

public class ElseOption extends ElseOpt {

    private Stmt Stmt;

    public ElseOption (Stmt Stmt) {
        this.Stmt=Stmt;
        if(Stmt!=null) Stmt.setParent(this);
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
        if(Stmt!=null) Stmt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Stmt!=null) Stmt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Stmt!=null) Stmt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ElseOption(\n");

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
