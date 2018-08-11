package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

public class Compiler {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Not enough arguments supplied! Usage: Compiler <source-file>");
			return;
		}

		Reader br = null;
		File sourceCode = new File(args[0]);

		try {
			System.out.println("Compiling source file: " + sourceCode.getAbsolutePath());

			br = new BufferedReader(new FileReader(sourceCode));

			Yylex lexer = new Yylex(br);
			MJParser parser = new MJParser(lexer);
			Symbol sym = parser.parse();

			if (parser.error) {
				System.err.println("\nParsing NOT successful: Syntax errors detected!");
			}
			else {
				System.out.println("\nParsing successfully done!");
				System.out.println("\nSyntax tree:\n" + sym.value.toString());
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally { 
			if (br != null) 
				try { br.close(); } catch (IOException e) { System.out.println(e.getMessage()); }
		}
	}
}
