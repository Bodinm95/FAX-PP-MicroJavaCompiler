package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

public class Test {

	public static void main(String[] args) {
		Reader br = null;
		try {
			File sourceCode = new File("test/program.mj");	
			System.out.println("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			
			Yylex lexer = new Yylex(br);
			Symbol currToken = null;
			while ((currToken = lexer.next_token()).sym != sym.EOF) {
				if (currToken != null && currToken.value != null)
					System.out.println(currToken.toString() + " " + currToken.value.toString());
			}
		} 
		catch (IOException e) { System.out.println(e.getMessage()); }
		finally { 
			if (br != null) 
				try { br.close(); } catch (IOException e) { System.out.println(e.getMessage()); }
			}
	}
}
