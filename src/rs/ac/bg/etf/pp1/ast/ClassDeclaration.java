// generated with ast extension for cup
// version 0.8
// 23/7/2018 16:43:48


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclaration extends ClassDecl {

    private ClassId ClassId;
    private ExtendsDecl ExtendsDecl;
    private ListVarDecl ListVarDecl;
    private ClassMethodDecl ClassMethodDecl;

    public ClassDeclaration (ClassId ClassId, ExtendsDecl ExtendsDecl, ListVarDecl ListVarDecl, ClassMethodDecl ClassMethodDecl) {
        this.ClassId=ClassId;
        if(ClassId!=null) ClassId.setParent(this);
        this.ExtendsDecl=ExtendsDecl;
        if(ExtendsDecl!=null) ExtendsDecl.setParent(this);
        this.ListVarDecl=ListVarDecl;
        if(ListVarDecl!=null) ListVarDecl.setParent(this);
        this.ClassMethodDecl=ClassMethodDecl;
        if(ClassMethodDecl!=null) ClassMethodDecl.setParent(this);
    }

    public ClassId getClassId() {
        return ClassId;
    }

    public void setClassId(ClassId ClassId) {
        this.ClassId=ClassId;
    }

    public ExtendsDecl getExtendsDecl() {
        return ExtendsDecl;
    }

    public void setExtendsDecl(ExtendsDecl ExtendsDecl) {
        this.ExtendsDecl=ExtendsDecl;
    }

    public ListVarDecl getListVarDecl() {
        return ListVarDecl;
    }

    public void setListVarDecl(ListVarDecl ListVarDecl) {
        this.ListVarDecl=ListVarDecl;
    }

    public ClassMethodDecl getClassMethodDecl() {
        return ClassMethodDecl;
    }

    public void setClassMethodDecl(ClassMethodDecl ClassMethodDecl) {
        this.ClassMethodDecl=ClassMethodDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassId!=null) ClassId.accept(visitor);
        if(ExtendsDecl!=null) ExtendsDecl.accept(visitor);
        if(ListVarDecl!=null) ListVarDecl.accept(visitor);
        if(ClassMethodDecl!=null) ClassMethodDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassId!=null) ClassId.traverseTopDown(visitor);
        if(ExtendsDecl!=null) ExtendsDecl.traverseTopDown(visitor);
        if(ListVarDecl!=null) ListVarDecl.traverseTopDown(visitor);
        if(ClassMethodDecl!=null) ClassMethodDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassId!=null) ClassId.traverseBottomUp(visitor);
        if(ExtendsDecl!=null) ExtendsDecl.traverseBottomUp(visitor);
        if(ListVarDecl!=null) ListVarDecl.traverseBottomUp(visitor);
        if(ClassMethodDecl!=null) ClassMethodDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclaration(\n");

        if(ClassId!=null)
            buffer.append(ClassId.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExtendsDecl!=null)
            buffer.append(ExtendsDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ListVarDecl!=null)
            buffer.append(ListVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassMethodDecl!=null)
            buffer.append(ClassMethodDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclaration]");
        return buffer.toString();
    }
}
