package compiler;
public class compiler {

	public static void main(String[] args) {
		LexicalAnalyzer lex =new LexicalAnalyzer("program.txt");
		lex.addEnd();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();
		lex.scan();

	}

}
