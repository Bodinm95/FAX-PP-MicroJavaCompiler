// generated with ast extension for cup
// version 0.8
// 23/7/2018 14:28:50


package rs.ac.bg.etf.pp1.ast;

public class PrintStatement extends Stmt {

    private Expr Expr;
    private PrintOpt PrintOpt;

    public PrintStatement (Expr Expr, PrintOpt PrintOpt) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.PrintOpt=PrintOpt;
        if(PrintOpt!=null) PrintOpt.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public PrintOpt getPrintOpt() {
        return PrintOpt;
    }

    public void setPrintOpt(PrintOpt PrintOpt) {
        this.PrintOpt=PrintOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(PrintOpt!=null) PrintOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(PrintOpt!=null) PrintOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(PrintOpt!=null) PrintOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStatement(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PrintOpt!=null)
            buffer.append(PrintOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStatement]");
        return buffer.toString();
    }
}
