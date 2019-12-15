package compiler;
public class compiler {

	public static void main(String[] args) {
		LexicalAnalyzer lex =new LexicalAnalyzer("program.txt");
		Token t;
		lex.addEnd();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();	
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		t=lex.scan();
		System.out.print("1");

	}

}
