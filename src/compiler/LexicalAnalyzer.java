package compiler;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class LexicalAnalyzer {
	/* 
	 * KEYTABLE---关键字表
	 * DELIMITABLE---界符表
	 * symbolTable---符号表
	 * constantTable---常量表
	 * stringTable---字符串表
	 * charTable---字符表
	 * token---token串集合
	 * fileName---所要分析的源程序的文件名
	 * filePointer---所读文件的指针
	 *  */
	private static String[] KEYTABLE = new String[34];
	private static String[] DELIMITERTABLE = new String[40];
	private ArrayList<String> identifierTable = new ArrayList<>();
	//private ArrayList<Token> symbolTable = new ArrayList<>(); 
	private ArrayList<Integer> constantTable1 = new ArrayList<>();
	private ArrayList<Float> constantTable2 = new ArrayList<>();
	private ArrayList<String> charTable = new ArrayList<>();
	private ArrayList<String> stringTable = new ArrayList<>();
	private ArrayList<Token> tokens = new ArrayList<>();
	private String fileName;
	private long filePointer;

	//构造函数
	public LexicalAnalyzer(String name) {
		//初始化关键字表和界符表
		File file=new File("keyword.txt");
		int i=1;
		try {
			BufferedReader reader =new BufferedReader(new FileReader(file));
			for(i=1;i<34;i++) {
				String str = reader.readLine();
				KEYTABLE[i]=str;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		file=new File("delimiter.txt");
		try {
			BufferedReader reader =new BufferedReader(new FileReader(file));
			for(i=1;i<40;i++) {
				String str = reader.readLine();
				DELIMITERTABLE[i]=str;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			if (str.equals(KEYTABLE[i])) break;
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
		if(i!=-1)return i+1;
		else {
			constantTable1.add(n);
			return constantTable1.size();
		}
	}
	
	//判断是否在小数常量表里，若不在则添加到表尾,并返回n在表中的位置
	public int constantList(float n) {
		int i;
		i = constantTable2.indexOf(n);
		if(i!=-1)return i+1;
		else {
			constantTable2.add(n);
			return constantTable2.size();
		}
	}
		
	//判断是否在标识符表里，若不在则添加到表尾,并返回n在表中的位置	
	public int identifierList(String str) {
		int i;
		i = identifierTable.indexOf(str);
		if(i!=-1)return i+1;
		else {
			identifierTable.add(str);
			return identifierTable.size();
		}
	}
	
	//判断字符c是否是界符
	public boolean isDelimiter(char c) {
		int i;
		String ch=String.valueOf(c);
		for (i = 1; i <= 39; i++) {
			if (ch.equals(DELIMITERTABLE[i])) break;
		}
		if (i <= 39) return true;
		else return false;
	}
	//判断字符串c是否是界符
	public int isDelimiter(String str) {
		int i;
		for (i = 1; i <= 39; i++) {
			if (str.equals(DELIMITERTABLE[i]) ) break;
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
		String str=new String();//保存此次扫描拼写出的单词
		int p=0;
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
			ch=(char) rfile.readByte();
			//滤掉空格与回车。若跳出时读出的是空格或回车，则读下一个
			while ((ch == ' ' || ch == '\n')&&rfile.getFilePointer()!=l)
			{
				ch=(char) rfile.readByte();
			}
			//读到的第一个字符为字母或是下划线：关键字或是标识符
			if(isCharacter(ch)) {
			    DFA.kriDFA dfa=d.new kriDFA();
				while(nowState!=3&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					//防止文件指针越界
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
				}
				//若跳出时不在终结态，则不接受此单词
				if(nowState!=3) token=null;
				//否则，接受此单词，且生成相应token串，填写符号表
				else {
					filePointer=rfile.getFilePointer()-2;//记录文件指针
					str=(String) str.subSequence(0, str.length()-1);
					int j = isKey(str);
					//若str是关键字
					if (j!=0) {
						Token.TYPE t=Token.TYPE.k;
						System.out.println("(k,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						token.setSvalue(str);
						this.tokens.add(token);
					}
					//否则str是标识符
					else {
						j = identifierList(str);
						Token.TYPE t=Token.TYPE.i;
						System.out.println("(i,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						token.setSvalue(str);
						this.tokens.add(token);
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
					//防止文件指针越界
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
					}
				//若跳出时不在终结态，则不接受此单词
				if(nowState!=4&&nowState!=6) token=null;
				//否则，接受此单词，且生成相应token串，填写常量表
				else {
					filePointer=rfile.getFilePointer()-2;//记录文件指针
					num=dfa.getValue();
					if(nowState==4) {
						int j=constantList((int)num);
						Token.TYPE t=Token.TYPE.inc;
						System.out.println("(inc,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						token.setIvalue((int)num);
						this.tokens.add(token);
					}
					else {
						int j=constantList(num);
						Token.TYPE t=Token.TYPE.fnc;
						System.out.println("(fnc,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						token.setFvalue(num);
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
					//防止文件指针越界
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}	
					}
				//若跳出时不在终结态，则不接受此单词
				if(nowState!=5) token=null;
				//否则，接受此单词，且生成相应token串，填写字符常量表
				else {
					filePointer=rfile.getFilePointer()-2;//记录文件指针
					str=(String) str.subSequence(0, str.length()-1);
					int j = charList(str);
					Token.TYPE t=Token.TYPE.cc;
					System.out.println("(cc,"+j+")");
					token.settype(t);
					token.setindex(j);
					token.setlastState(nowState);
					token.setSvalue(str);
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
					//防止文件指针越界
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
					}
				//若跳出时不在终结态，则不接受此单词
				if(nowState!=2) token=null;
				//否则，接受此单词，且生成相应token串，填写字符串常量表
				else {
					filePointer=rfile.getFilePointer()-2;//记录文件指针
					str=(String) str.subSequence(0, str.length()-1);
					int j = stringList(str);
					Token.TYPE t=Token.TYPE.sc;
					System.out.println("(sc,"+j+")");
					token.settype(t);
					token.setindex(j);
					token.setlastState(nowState);
					token.setSvalue(str);
					this.tokens.add(token);
				}
			}
			//若读入的第一个字符是界符，则可能是界符
			else if (isDelimiter(ch)) {
				str=str+ch;
				//防止文件指针越界
				if(rfile.getFilePointer()!=l) {
					ch=(char) rfile.readByte();	
				}
				if(ch=='='||ch=='*'||ch=='&'||ch=='|'||ch=='<'||ch=='>'||ch=='/'||ch=='\\'||ch=='+'||ch=='-') {
					str=str+ch;
					//是双目界符，生成相应token串
					if(isDelimiter(str)!=0) {
						filePointer=rfile.getFilePointer();//记录文件指针
						nowState=1;
						int j = isDelimiter(str);
						Token.TYPE t=Token.TYPE.p;
						System.out.println("(p,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						token.setSvalue(str);
						this.tokens.add(token);
					}
					else token=null;
				}
				//是单目界符
				else {
					filePointer=rfile.getFilePointer()-1;//记录文件指针
					nowState=7;
					int j = isDelimiter(str);
					Token.TYPE t=Token.TYPE.p;
					System.out.println("(p,"+j+")");
					token.settype(t);
					token.setindex(j);
					token.setlastState(nowState);
					token.setSvalue(str);
					this.tokens.add(token);
				}
			}
			else if(ch=='#') {
				token.setlastState(36);
			}	
			rfile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return token;
	}
	
	//在文件最后添加结束符号#
	public void addEnd() {
		FileWriter fw;
		try {
			fw = new FileWriter(this.fileName,true);
			PrintWriter pw=new PrintWriter(fw);
			pw.println("#");
			pw.close ();
			fw.close ();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//根据标号i得到相应关键字
	public String getKey(int i) {
		return KEYTABLE[i];
	}
	
	//根据标号i得到相应界符
	public String getDelimiter(int i) {
		return DELIMITERTABLE[i];
	}
	
	//根据标号i得到相应整型数字常量
	public Integer getInc(int i) {
		return constantTable1.get(i);
	}
	
	//根据标号i得到相应实型数字常量
	public Float getFnc(int i) {
		return constantTable2.get(i);
	}
	
	//根据标号i得到相应字符常量
	public String getChar(int i) {
		return charTable.get(i);
	}
	
	//根据标号i得到相应字符串常量
	public String getString(int i) {
		return stringTable.get(i);
	}
	
}

