// generated with ast extension for cup
// version 0.8
// 23/7/2018 22:19:45


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Mulop Mulop);
    public void visit(MethodDecl MethodDecl);
    public void visit(Relop Relop);
    public void visit(Addop Addop);
    public void visit(DeclPart DeclPart);
    public void visit(PrintOpt PrintOpt);
    public void visit(Prog Prog);
    public void visit(Factor Factor);
    public void visit(FormParsPart FormParsPart);
    public void visit(CondTerm CondTerm);
    public void visit(RetOpt RetOpt);
    public void visit(ConstDeclPart ConstDeclPart);
    public void visit(DeclList DeclList);
    public void visit(MethodSign MethodSign);
    public void visit(Designator Designator);
    public void visit(Term Term);
    public void visit(FormParsList FormParsList);
    public void visit(RetType RetType);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(ActParsList ActParsList);
    public void visit(DoStmt DoStmt);
    public void visit(ExtendsDecl ExtendsDecl);
    public void visit(VarDeclPart VarDeclPart);
    public void visit(StmtList StmtList);
    public void visit(ExprList ExprList);
    public void visit(VarDeclList VarDeclList);
    public void visit(Expr Expr);
    public void visit(Cond Cond);
    public void visit(ActPars ActPars);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(VarDecl VarDecl);
    public void visit(Stmt Stmt);
    public void visit(ProgId ProgId);
    public void visit(ClassDecl ClassDecl);
    public void visit(ConstDecl ConstDecl);
    public void visit(CondFact CondFact);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(ListVarDecl ListVarDecl);
    public void visit(FormPars FormPars);
    public void visit(ClassMethodDecl ClassMethodDecl);
    public void visit(ElseOpt ElseOpt);
    public void visit(MulopMod MulopMod);
    public void visit(MulopDiv MulopDiv);
    public void visit(MulopMul MulopMul);
    public void visit(AddopSub AddopSub);
    public void visit(AddopAdd AddopAdd);
    public void visit(RelopLSEQ RelopLSEQ);
    public void visit(RelopLS RelopLS);
    public void visit(RelopGREQ RelopGREQ);
    public void visit(RelopGR RelopGR);
    public void visit(RelopNEQ RelopNEQ);
    public void visit(RelopEQ RelopEQ);
    public void visit(FactorExpression FactorExpression);
    public void visit(FactorNewArray FactorNewArray);
    public void visit(FactorNew FactorNew);
    public void visit(FactorBool FactorBool);
    public void visit(FactorChar FactorChar);
    public void visit(FactorNum FactorNum);
    public void visit(FactorProcCall FactorProcCall);
    public void visit(FactorDesignator FactorDesignator);
    public void visit(SingleTerm SingleTerm);
    public void visit(TermList TermList);
    public void visit(SingleExpression SingleExpression);
    public void visit(ExpressionList ExpressionList);
    public void visit(NegExpresions NegExpresions);
    public void visit(Expressions Expressions);
    public void visit(ErrorConditionFact ErrorConditionFact);
    public void visit(ConditionFactRelop ConditionFactRelop);
    public void visit(ConditionFact ConditionFact);
    public void visit(SingleConditionTerm SingleConditionTerm);
    public void visit(ConditionTerms ConditionTerms);
    public void visit(SingleCondition SingleCondition);
    public void visit(Conditions Conditions);
    public void visit(SingleActualParam SingleActualParam);
    public void visit(ActualParamsList ActualParamsList);
    public void visit(EmptyActualParams EmptyActualParams);
    public void visit(ActualParams ActualParams);
    public void visit(ArrayDesignator ArrayDesignator);
    public void visit(DesignatorIdent DesignatorIdent);
    public void visit(DesignatorArray DesignatorArray);
    public void visit(DesignatorField DesignatorField);
    public void visit(Decrement Decrement);
    public void visit(Increment Increment);
    public void visit(ProcCall ProcCall);
    public void visit(ErrorAssignment ErrorAssignment);
    public void visit(Assignment Assignment);
    public void visit(EmptyPrintOption EmptyPrintOption);
    public void visit(PrintOption PrintOption);
    public void visit(EmptyReturnOption EmptyReturnOption);
    public void visit(ReturnOption ReturnOption);
    public void visit(EmptyElseOption EmptyElseOption);
    public void visit(ElseOption ElseOption);
    public void visit(DoStatement DoStatement);
    public void visit(BlockStatement BlockStatement);
    public void visit(PrintStatement PrintStatement);
    public void visit(ReadStatement ReadStatement);
    public void visit(ReturnStatement ReturnStatement);
    public void visit(ContinueStatement ContinueStatement);
    public void visit(BreakStatement BreakStatement);
    public void visit(WhileStatement WhileStatement);
    public void visit(IfStatement IfStatement);
    public void visit(Statement Statement);
    public void visit(EmptyStatementList EmptyStatementList);
    public void visit(StatementList StatementList);
    public void visit(Type Type);
    public void visit(ErrorFormalParamPart ErrorFormalParamPart);
    public void visit(FormalParamPartArray FormalParamPartArray);
    public void visit(FormalParamPart FormalParamPart);
    public void visit(SingleFormalParam SingleFormalParam);
    public void visit(FormalParamList FormalParamList);
    public void visit(EmptyFormalParams EmptyFormalParams);
    public void visit(FormalParams FormalParams);
    public void visit(ReturnTypeVoid ReturnTypeVoid);
    public void visit(ReturnType ReturnType);
    public void visit(MethodId MethodId);
    public void visit(MethodSignature MethodSignature);
    public void visit(MethodDeclaration MethodDeclaration);
    public void visit(EmptyMethodDeclarationList EmptyMethodDeclarationList);
    public void visit(MethodDeclarationList MethodDeclarationList);
    public void visit(EmptyClassMethodDeclaration EmptyClassMethodDeclaration);
    public void visit(ClassMethodDeclaration ClassMethodDeclaration);
    public void visit(ErrorExtendsDeclaration ErrorExtendsDeclaration);
    public void visit(EmptyExtendsDeclaration EmptyExtendsDeclaration);
    public void visit(ExtendsDeclaration ExtendsDeclaration);
    public void visit(ClassId ClassId);
    public void visit(ClassDeclaration ClassDeclaration);
    public void visit(EmptyListVarDeclaration EmptyListVarDeclaration);
    public void visit(ListVarDeclaration ListVarDeclaration);
    public void visit(ErrorVarDeclarationPart ErrorVarDeclarationPart);
    public void visit(VarDeclarationPartArray VarDeclarationPartArray);
    public void visit(VarDeclarationPart VarDeclarationPart);
    public void visit(SingleVarDeclaration SingleVarDeclaration);
    public void visit(VarDeclarationList VarDeclarationList);
    public void visit(ErrorVarDeclaration ErrorVarDeclaration);
    public void visit(VarDeclaration VarDeclaration);
    public void visit(ConstDeclarationBool ConstDeclarationBool);
    public void visit(ConstDeclarationChar ConstDeclarationChar);
    public void visit(ConstDeclarationNum ConstDeclarationNum);
    public void visit(SingleConstDeclaration SingleConstDeclaration);
    public void visit(ConstDeclarationList ConstDeclarationList);
    public void visit(ConstDeclaration ConstDeclaration);
    public void visit(DeclarationPartClass DeclarationPartClass);
    public void visit(DeclarationPartVar DeclarationPartVar);
    public void visit(DeclarationPartConst DeclarationPartConst);
    public void visit(EmptyDeclarationList EmptyDeclarationList);
    public void visit(DeclarationList DeclarationList);
    public void visit(ProgramId ProgramId);
    public void visit(Program Program);

}
