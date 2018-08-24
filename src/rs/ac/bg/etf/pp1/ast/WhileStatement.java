// generated with ast extension for cup
// version 0.8
// 25/7/2018 0:15:35


package rs.ac.bg.etf.pp1.ast;

public class WhileStatement extends Stmt {

    private DoStmt DoStmt;
    private Stmt Stmt;
    private WhileStmt WhileStmt;
    private Condition Condition;

    public WhileStatement (DoStmt DoStmt, Stmt Stmt, WhileStmt WhileStmt, Condition Condition) {
        this.DoStmt=DoStmt;
        if(DoStmt!=null) DoStmt.setParent(this);
        this.Stmt=Stmt;
        if(Stmt!=null) Stmt.setParent(this);
        this.WhileStmt=WhileStmt;
        if(WhileStmt!=null) WhileStmt.setParent(this);
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

    public WhileStmt getWhileStmt() {
        return WhileStmt;
    }

    public void setWhileStmt(WhileStmt WhileStmt) {
        this.WhileStmt=WhileStmt;
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
        if(WhileStmt!=null) WhileStmt.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoStmt!=null) DoStmt.traverseTopDown(visitor);
        if(Stmt!=null) Stmt.traverseTopDown(visitor);
        if(WhileStmt!=null) WhileStmt.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoStmt!=null) DoStmt.traverseBottomUp(visitor);
        if(Stmt!=null) Stmt.traverseBottomUp(visitor);
        if(WhileStmt!=null) WhileStmt.traverseBottomUp(visitor);
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

        if(WhileStmt!=null)
            buffer.append(WhileStmt.toString("  "+tab));
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
