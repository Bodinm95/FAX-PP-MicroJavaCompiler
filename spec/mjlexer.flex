package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{
	private Symbol symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}
%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return symbol(sym.EOF);
%eofval}

%%

" "     { }
"\b"    { }
"\t"    { }
"\r\n"  { }
"\f"    { }

"program"   { return symbol(sym.PROGRAM, yytext()); }
"break"     { return symbol(sym.BREAK, yytext()); }
"class"     { return symbol(sym.CLASS, yytext()); }
"else"      { return symbol(sym.ELSE, yytext()); }
"if"        { return symbol(sym.IF, yytext()); }
"new"       { return symbol(sym.NEW, yytext()); }
"print"     { return symbol(sym.PRINT, yytext()); }
"read"      { return symbol(sym.READ, yytext()); }
"return"    { return symbol(sym.RETURN, yytext()); }
"void"      { return symbol(sym.VOID, yytext()); }
"do"        { return symbol(sym.DO, yytext()); }
"while"     { return symbol(sym.WHILE, yytext()); }
"extends"   { return symbol(sym.EXTENDS, yytext()); }
"continue"  { return symbol(sym.CONTINUE, yytext()); }
"const"     { return symbol(sym.CONST, yytext()); }

"+"     { return symbol(sym.ADD, yytext()); }
"-"     { return symbol(sym.SUB, yytext()); }
"*"     { return symbol(sym.MUL, yytext()); }
"/"     { return symbol(sym.DIV, yytext()); }
"%"     { return symbol(sym.MOD, yytext()); }
"=="    { return symbol(sym.EQ, yytext()); }
"!="    { return symbol(sym.NEQ, yytext()); }
">"     { return symbol(sym.GR, yytext()); }
">="    { return symbol(sym.GREQ, yytext()); }
"<"     { return symbol(sym.LS, yytext()); }
"<="    { return symbol(sym.LSEQ, yytext()); }
"&&"    { return symbol(sym.AND, yytext()); }
"||"    { return symbol(sym.OR, yytext()); }
"="     { return symbol(sym.ASSIGN, yytext()); }
"++"    { return symbol(sym.INC, yytext()); }
"--"    { return symbol(sym.DEC, yytext()); }

";"     { return symbol(sym.SEMI, yytext()); }
","     { return symbol(sym.COMMA, yytext()); }
"."     { return symbol(sym.DOT, yytext()); }

"("     { return symbol(sym.LPAREN, yytext()); }
")"     { return symbol(sym.RPAREN, yytext()); }
"["     { return symbol(sym.LSQUARE, yytext()); }
"]"     { return symbol(sym.RSQUARE, yytext()); }
"{"     { return symbol(sym.LBRACE, yytext()); }
"}"     { return symbol(sym.RBRACE, yytext()); }

[0-9]+                          { return symbol(sym.NUM, new Integer (yytext())); }
"'"[\040-\176]"'"               { return symbol(sym.CHAR, new Character (yytext().charAt(1))); }
("true"|"false")                { return symbol(sym.BOOL, yytext()); }

([a-z]|[A-Z])[a-z|A-Z|0-9|_]*   { return symbol(sym.IDENT, yytext()); }

"//"             { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

. { System.err.println("Lexical error: ("+yytext()+") at line: "+(yyline+1)); }