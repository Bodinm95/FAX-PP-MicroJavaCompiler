package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.TabSym;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.mj.runtime.Run;
import rs.etf.pp1.mj.runtime.disasm;
import rs.etf.pp1.symboltable.Tab;

public class Compiler {

	public static void tsdump() { TabSym.dump(); }

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

			if (parser.error == 0)
				System.out.println("\nSyntax tree:\n" + sym.value.toString());
			System.out.println("");

			Program program = (sym.value instanceof Program) ? (Program)sym.value : null;
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

			TabSym.init();

			if (program != null)
				program.traverseBottomUp(semanticAnalyzer);

			tsdump();

			if (parser.error > 0 || semanticAnalyzer.error > 0) {
				System.err.println("\nParsing NOT successful: " + parser.error + " Syntax errors detected! " + semanticAnalyzer.error + " Semantic errors detected!");
			}
			else {
				System.out.println("\nParsing successfully done!");


				File objectFile = new File(args[1]);
				System.out.println("\nGenerating object file: " + objectFile.getAbsolutePath());

				CodeGenerator codegen = new CodeGenerator();
				program.traverseBottomUp(codegen);

				if (objectFile.exists())
					objectFile.delete();

				Code.write(new FileOutputStream(objectFile));

				System.out.println("\nDisassembling object file:");
				disasm.main(new String[] {args[1]});

				System.out.println("\nRunning object file:");
				Run.main(new String[] {args[1]});
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally { 
			if (br != null) 
				try { br.close(); } catch (IOException e) { System.out.println(e.getMessage()); }
		}
	}
}
