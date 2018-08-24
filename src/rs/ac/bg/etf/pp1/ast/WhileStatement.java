// generated with ast extension for cup
// version 0.8
// 24/7/2018 16:52:58


package rs.ac.bg.etf.pp1.ast;

public class WhileStatement extends Stmt {

    private DoStmt DoStmt;
    private Stmt Stmt;
    private Condition Condition;

    public WhileStatement (DoStmt DoStmt, Stmt Stmt, Condition Condition) {
        this.DoStmt=DoStmt;
        if(DoStmt!=null) DoStmt.setParent(this);
        this.Stmt=Stmt;
        if(Stmt!=null) Stmt.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
    }

    public DoStmt getDoStmt() {
        return DoStmt;
    }

    public void setDoStmt(DoStmt DoStmt) {
        this.DoStmt=DoStmt;
    }

    public Stmt getStmt() {
        return Stmt;
    }

    public void setStmt(Stmt Stmt) {
        this.Stmt=Stmt;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoStmt!=null) DoStmt.accept(visitor);
        if(Stmt!=null) Stmt.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoStmt!=null) DoStmt.traverseTopDown(visitor);
        if(Stmt!=null) Stmt.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoStmt!=null) DoStmt.traverseBottomUp(visitor);
        if(Stmt!=null) Stmt.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("WhileStatement(\n");

        if(DoStmt!=null)
            buffer.append(DoStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Stmt!=null)
            buffer.append(Stmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [WhileStatement]");
        return buffer.toString();
    }
}
