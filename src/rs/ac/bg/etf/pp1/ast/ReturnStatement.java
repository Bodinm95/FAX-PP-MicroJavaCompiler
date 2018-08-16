// generated with ast extension for cup
// version 0.8
// 16/7/2018 10:48:40


package rs.ac.bg.etf.pp1.ast;

public class ReturnStatement extends Stmt {

    private RetOpt RetOpt;

    public ReturnStatement (RetOpt RetOpt) {
        this.RetOpt=RetOpt;
        if(RetOpt!=null) RetOpt.setParent(this);
    }

    public RetOpt getRetOpt() {
        return RetOpt;
    }

    public void setRetOpt(RetOpt RetOpt) {
        this.RetOpt=RetOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RetOpt!=null) RetOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RetOpt!=null) RetOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RetOpt!=null) RetOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnStatement(\n");

        if(RetOpt!=null)
            buffer.append(RetOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnStatement]");
        return buffer.toString();
    }
}
