// generated with ast extension for cup
// version 0.8
// 24/7/2018 18:32:3


package rs.ac.bg.etf.pp1.ast;

public class SingleVarDeclaration extends VarDeclList {

    private VarDeclPart VarDeclPart;

    public SingleVarDeclaration (VarDeclPart VarDeclPart) {
        this.VarDeclPart=VarDeclPart;
        if(VarDeclPart!=null) VarDeclPart.setParent(this);
    }

    public VarDeclPart getVarDeclPart() {
        return VarDeclPart;
    }

    public void setVarDeclPart(VarDeclPart VarDeclPart) {
        this.VarDeclPart=VarDeclPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclPart!=null) VarDeclPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclPart!=null) VarDeclPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclPart!=null) VarDeclPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleVarDeclaration(\n");

        if(VarDeclPart!=null)
            buffer.append(VarDeclPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleVarDeclaration]");
        return buffer.toString();
    }
}
