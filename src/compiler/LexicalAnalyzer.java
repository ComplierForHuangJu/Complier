package compiler;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LexicalAnalyzer {

/* 关键字表 */
 public static ArrayList<Token> KEYTABLE = new ArrayList<>();
/* 界符表 */
public static ArrayList<Token> DELIMITABLE = new ArrayList<>();
/* 符号表 */
public ArrayList<Token> SymbolTable = new ArrayList<>(); 

/* 常量表 */
public ArrayList<Token> ConstTable = new ArrayList<>();

/* 字符串表 */
public ArrayList<Token> StringTable = new ArrayList<>();

/* 字符表  */
public ArrayList<Token> CharTable = new ArrayList<>();


	/* 
	 * KEYTABLE---关键字表
	 * DELIMITABLE---界符表
	 * symbolTable---符号表
	 * constantTable---常量表
	 * stringTable---字符串表
	 * charTable---字符表
	 * token---token串集合
	 * 
	 *  */
	private static String[] KEYTABLE = new String[34];
	private static String[] DELIMITERTABLE = new String[40];
	private ArrayList<Token> symbolTable = new ArrayList<>(); 
	private ArrayList<Integer> constantTable1 = new ArrayList<>();
	private ArrayList<Float> constantTable2 = new ArrayList<>();
	private ArrayList<String> charTable = new ArrayList<>();
	private ArrayList<String> stringTable = new ArrayList<>();
	private ArrayList<Token> tokens = new ArrayList<>();
	
	/*
	 * fileName---所要分析的源程序的文件名
	 * filePointer---所读文件的指针
	 * 
	 */
	private String fileName;
	private int filePointer;
	//构造函数
	public LexicalAnalyzer(String name) {
		
		this.fileName=name;
		filePointer=0;
	}
	//判断ch是否是字母或下划线
	public boolean isCharacter(char ch) {
		if ((ch >= 65 && ch <= 106) || (ch >= 97 && ch <= 122) || (ch == 95)) { return true; }
		else return false;
	}
	
	//判断是否是关键字，并返回表中位置
	public int isKey(String str) {
		int i;
		for (i = 1; i <= 33; i++) {
			if (str == KEYTABLE[i]) break;
		}
		if (i <= 33) return i;
		else return 0;
	}
	
	//判断ch是否是数字
	public boolean isNumber(char ch) {
		if (ch >= 48 && ch <= 57) return true;
		else return false;
	}
	
	//判断是否在整数常量表里，若不在则添加到表尾,并返回n在表中的位置
	public int constantList(int n) {
		int i;
		i = constantTable1.indexOf(n);
		if(i!=-1)return i;
		else {
			constantTable1.add(n);
			return constantTable1.size()-1;
		}
	}
	
	//判断是否在小数常量表里，若不在则添加到表尾,并返回n在表中的位置
		public int constantList(float n) {
			int i;
			i = constantTable2.indexOf(n);
			if(i!=-1)return i;
			else {
				constantTable2.add(n);
				return constantTable2.size()-1;
			}
		}
	
	//判断字符c是否是界符
	public boolean isDelimiter(char c) {
		int i;
		String ch=String.valueOf(c);
		for (i = 1; i <= 39; i++) {
			if (ch == DELIMITERTABLE[i]) break;
		}
		if (i <= 39) return true;
		else return false;
	}
	//判断字符串c是否是界符
	public int isDelimiter(String str) {
		int i;
		for (i = 1; i <= 39; i++) {
			if (str == DELIMITERTABLE[i]) break;
		}
		if (i <= 39) return i;
		else return 0;
	}
	
	//判断是否在字符常量表里，若不在则添加到表尾,并返回str在表中的位置
	public int charList(String str) {
		int i;
		i = charTable.indexOf(str);
		if(i!=-1)return i;
		else {
			charTable.add(str);
			return charTable.size()-1;
		}
	}
	
	//判断是否在字符串常量表里，若不在则添加到表尾,并返回str在表中的位置
	public int stringList(String str) {
		int i;
		i = stringTable.indexOf(str);
		if(i!=-1)return i;
		else {
			stringTable.add(str);
			return stringTable.size()-1;
		}
	}
		
	//扫描函数
	public Token scan() {
		char ch;//当前所读字符
		Token token = new Token();
		String str=null;//保存此次扫描拼写出的单词
		DFA d=new DFA();
		//打开扫描文件
		File file=new File(this.fileName);
		try {
			//将文件指针定位至上次扫描位置
			RandomAccessFile rfile=new RandomAccessFile(file, "r");
			rfile.seek(filePointer);
			long l=rfile.length();
			//初始化开始状态
			int nowState=1;
			ch=rfile.readChar();
			//滤掉空格与回车。若跳出时读出的是空格或回车，则读下一个
			if (ch == ' ' || ch == '\n')
			{
				ch=rfile.readChar();
			}
			//读到的第一个字符为字母或是下划线：关键字或是标识符
			if(isCharacter(ch)) {
			    DFA.kriDFA dfa=d.new kriDFA();
				//DFA.kriDFA dfa=null;
				while(nowState!=3&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//若跳出时不在终结态，则不接受此单词
				if(nowState!=3) token=null;
				//否则，接受此单词，且生成相应token串，填写符号表
				else {
					str=(String) str.subSequence(0, str.length()-1);
					int j = isKey(str);
					//若str是关键字
					if (j!=0) {
						Token.TYPE t=Token.TYPE.k;
						System.out.println("(k,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
					//否则str是标识符
					else {
					}
				}
			}
			//若第一个字符为数字，则可能为数字常量	
			else if(isNumber(ch)) {
				DFA.nconDFA dfa=d.new nconDFA();
				float num;
				while(nowState!=4&&nowState!=6&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch),ch);
					str=str+ch;
					ch=rfile.readChar();	
					}
				//若跳出时不在终结态，则不接受此单词
				if(nowState!=4&&nowState!=6) token=null;
				//否则，接受此单词，且生成相应token串，填写常量表
				else {
					num=dfa.getValue();
					if(nowState==4) {
						int j=constantList((int)num);
						Token.TYPE t=Token.TYPE.inc;
						System.out.println("(inc,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
					else {
						int j=constantList(num);
						Token.TYPE t=Token.TYPE.fnc;
						System.out.println("(fnc,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
				}
			}
			//若读入字符为‘，则可能是字符常量
		    else if(ch == 39) {
		    	DFA.cconDFA dfa=d.new cconDFA();
		    	//特殊情况是遇到回车，则结束判断
				while(nowState!=5&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//若跳出时不在终结态，则不接受此单词
				if(nowState!=5) token=null;
				//否则，接受此单词，且生成相应token串，填写字符常量表
				else {
					str=(String) str.subSequence(0, str.length()-1);
					int j = charList(str);
					Token.TYPE t=Token.TYPE.cc;
					System.out.println("(cc,"+j+")");
					token.settype(t);
					token.setindex(j);
					token.setlastState(nowState);
					this.tokens.add(token);
				}
		    }
			//若读入的第一个字符是"，则可能是字符串常量
			else if(ch == 34) {
				DFA.sconDFA dfa=d.new sconDFA();
		    	//特殊情况是遇到回车，则结束判断
				while(nowState!=2&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//若跳出时不在终结态，则不接受此单词
				if(nowState!=2) token=null;
				//否则，接受此单词，且生成相应token串，填写字符串常量表
				else {
					str=(String) str.subSequence(0, str.length()-1);
					int j = stringList(str);
					Token.TYPE t=Token.TYPE.sc;
					System.out.println("(sc,"+j+")");
					token.settype(t);
					token.setindex(j);
					token.setlastState(nowState);
					this.tokens.add(token);
				}
			}
			//若读入的第一个字符是界符，则可能是界符
			else if (isDelimiter(ch)) {
				ch=rfile.readChar();
				if(ch=='='||ch=='*'||ch=='&'||ch=='|'||ch=='<'||ch=='>'||ch=='/'||ch=='\\'||ch=='+'||ch=='-') {
					str=str+ch;
					//是双目界符，生成相应token串
					if(isDelimiter(str)!=0) {
						nowState=1;
						int j = isDelimiter(str);
						Token.TYPE t=Token.TYPE.p;
						System.out.println("(p,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
					else token=null;
				}
				//是单目界符
				else {
					nowState=7;
					str=str+ch;
					int j = isDelimiter(str);
					Token.TYPE t=Token.TYPE.p;
					System.out.println("(p,"+j+")");
					token.settype(t);
					token.setindex(j);
					token.setlastState(nowState);
					this.tokens.add(token);
				}
			}
			else if(ch=='#') {
				token.setlastState(36);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return token;
	}
>>>>>>> huang
	
}

