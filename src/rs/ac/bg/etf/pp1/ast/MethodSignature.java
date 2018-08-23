// generated with ast extension for cup
// version 0.8
// 23/7/2018 16:43:48


package rs.ac.bg.etf.pp1.ast;

public class MethodSignature extends MethodSign {

    private RetType RetType;
    private MethodId MethodId;
    private FormPars FormPars;

    public MethodSignature (RetType RetType, MethodId MethodId, FormPars FormPars) {
        this.RetType=RetType;
        if(RetType!=null) RetType.setParent(this);
        this.MethodId=MethodId;
        if(MethodId!=null) MethodId.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
    }

    public RetType getRetType() {
        return RetType;
    }

    public void setRetType(RetType RetType) {
        this.RetType=RetType;
    }

    public MethodId getMethodId() {
        return MethodId;
    }

    public void setMethodId(MethodId MethodId) {
        this.MethodId=MethodId;
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RetType!=null) RetType.accept(visitor);
        if(MethodId!=null) MethodId.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RetType!=null) RetType.traverseTopDown(visitor);
        if(MethodId!=null) MethodId.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RetType!=null) RetType.traverseBottomUp(visitor);
        if(MethodId!=null) MethodId.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodSignature(\n");

        if(RetType!=null)
            buffer.append(RetType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodId!=null)
            buffer.append(MethodId.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodSignature]");
        return buffer.toString();
    }
}
