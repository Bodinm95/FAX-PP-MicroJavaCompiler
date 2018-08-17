// generated with ast extension for cup
// version 0.8
// 17/7/2018 15:31:48


package rs.ac.bg.etf.pp1.ast;

public class FormalParamList extends FormParsList {

    private FormParsList FormParsList;
    private FormParsPart FormParsPart;

    public FormalParamList (FormParsList FormParsList, FormParsPart FormParsPart) {
        this.FormParsList=FormParsList;
        if(FormParsList!=null) FormParsList.setParent(this);
        this.FormParsPart=FormParsPart;
        if(FormParsPart!=null) FormParsPart.setParent(this);
    }

    public FormParsList getFormParsList() {
        return FormParsList;
    }

    public void setFormParsList(FormParsList FormParsList) {
        this.FormParsList=FormParsList;
    }

    public FormParsPart getFormParsPart() {
        return FormParsPart;
    }

    public void setFormParsPart(FormParsPart FormParsPart) {
        this.FormParsPart=FormParsPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsList!=null) FormParsList.accept(visitor);
        if(FormParsPart!=null) FormParsPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsList!=null) FormParsList.traverseTopDown(visitor);
        if(FormParsPart!=null) FormParsPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsList!=null) FormParsList.traverseBottomUp(visitor);
        if(FormParsPart!=null) FormParsPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParamList(\n");

        if(FormParsList!=null)
            buffer.append(FormParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsPart!=null)
            buffer.append(FormParsPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParamList]");
        return buffer.toString();
    }
}
