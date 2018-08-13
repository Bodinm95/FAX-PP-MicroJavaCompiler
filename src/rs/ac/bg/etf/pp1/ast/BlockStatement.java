// generated with ast extension for cup
// version 0.8
// 13/7/2018 17:24:52


package rs.ac.bg.etf.pp1.ast;

public class BlockStatement extends Stmt {

    private StmtList StmtList;

    public BlockStatement (StmtList StmtList) {
        this.StmtList=StmtList;
        if(StmtList!=null) StmtList.setParent(this);
    }

    public StmtList getStmtList() {
        return StmtList;
    }

    public void setStmtList(StmtList StmtList) {
        this.StmtList=StmtList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StmtList!=null) StmtList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StmtList!=null) StmtList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StmtList!=null) StmtList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BlockStatement(\n");

        if(StmtList!=null)
            buffer.append(StmtList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BlockStatement]");
        return buffer.toString();
    }
}
