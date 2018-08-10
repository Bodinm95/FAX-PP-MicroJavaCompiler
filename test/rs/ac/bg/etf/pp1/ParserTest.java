package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

public class ParserTest {

	public static void parse(String filename) {

		Reader br = null;
		File sourceCode = new File(filename);

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

	public static void main(String[] args) {
		parse("test/ParserProgram.mj");
		System.out.println("");
		parse("test/ParserProgramError.mj");
	}
}
