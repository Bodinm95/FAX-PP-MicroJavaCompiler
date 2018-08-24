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

public class CodeGenTest {

	public static void main(String[] args) {

		Reader br = null;
		File sourceCode = new File("test/CodeGenProgram.mj");

		try {
			System.out.println("Compiling source file: " + sourceCode.getAbsolutePath());

			br = new BufferedReader(new FileReader(sourceCode));

			Yylex lexer = new Yylex(br);
			MJParser parser = new MJParser(lexer);
			Symbol sym = parser.parse();

			Program program = (sym != null && sym.value instanceof Program) ? (Program)sym.value : null;
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

			TabSym.init();

			if (program != null)
				program.traverseBottomUp(semanticAnalyzer);

			TabSym.dump();

			if (parser.error > 0 || semanticAnalyzer.error > 0) {
				System.err.println("\nParsing NOT successful: " + parser.error + " Syntax errors detected! " + semanticAnalyzer.error + " Semantic errors detected!");
			}
			else {
				System.out.println("\nParsing successfully done!");

				File objectFile = new File("test/CodeGenProgram.obj");
				System.out.println("\nGenerating object file: test/CodeGenProgram.obj");

				CodeGenerator codegen = new CodeGenerator();
				program.traverseBottomUp(codegen);

				if (objectFile.exists())
					objectFile.delete();

				Code.write(new FileOutputStream(objectFile));

				System.out.println("\nDisassembling object file:");
				disasm.main(new String[] {"test/CodeGenProgram.obj"});

				System.out.println("\nRunning object file:");
				Run.main(new String[] {"-debug", "test/CodeGenProgram.obj"});
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally { 
			if (br != null) 
				try { br.close(); } catch (IOException e) { System.out.println(e.getMessage()); }
		}
	}
}
