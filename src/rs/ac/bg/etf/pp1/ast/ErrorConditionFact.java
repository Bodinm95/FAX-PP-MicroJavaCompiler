// generated with ast extension for cup
// version 0.8
// 23/7/2018 21:9:26


package rs.ac.bg.etf.pp1.ast;

public class ErrorConditionFact extends CondFact {

    public ErrorConditionFact () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ErrorConditionFact(\n");

        buffer.append(tab);
        buffer.append(") [ErrorConditionFact]");
        return buffer.toString();
    }
}
