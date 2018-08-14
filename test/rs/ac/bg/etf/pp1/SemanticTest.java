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
		File sourceCode = new File("test/SemanticProgramError.mj");

		try {
			System.out.println("Compiling source file: " + sourceCode.getAbsolutePath());

			br = new BufferedReader(new FileReader(sourceCode));

			Yylex lexer = new Yylex(br);
			MJParser parser = new MJParser(lexer);
			Symbol sym = parser.parse();

			Program program = (Program)sym.value;
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

			TabSym.init();
			program.traverseBottomUp(semanticAnalyzer);

			Tab.dump();

			if (parser.error) {
				System.err.println("\nParsing NOT successful: Syntax errors detected!");
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
