package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.TabSym;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;


public class SemanticTest {

	public static void main(String[] args) {

		Reader br = null;
		File sourceCode = new File("test/SemanticProgram.mj");

		try {
			System.out.println("Compiling source file: " + sourceCode.getAbsolutePath());

			br = new BufferedReader(new FileReader(sourceCode));

			Yylex lexer = new Yylex(br);
			MJParser parser = new MJParser(lexer);
			Symbol sym = parser.parse();

			Program program = (sym.value instanceof Program) ? (Program)sym.value : null;
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

			TabSym.init();

			if (program != null)
				program.traverseBottomUp(semanticAnalyzer);

			Tab.dump();

			if (parser.error > 0 || semanticAnalyzer.error > 0) {
				System.err.println("\nParsing NOT successful: " + parser.error + " Syntax errors detected! " + semanticAnalyzer.error + " Semantic errors detected!");
			}
			else {
				System.out.println("\nParsing successfully done!");
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally { 
			if (br != null) 
				try { br.close(); } catch (IOException e) { System.out.println(e.getMessage()); }
		}
	}
}
