<<<<<<< HEAD
package compiler;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LexicalAnalyzer {

/* 关键字表 */
 public static ArrayList<Token> KEYTABLE1 = new ArrayList<>();
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

	
}

=======
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

/* 鍏抽敭瀛楄〃 */
 public static ArrayList<Token> KEYTABLE = new ArrayList<>();
/* 鐣岀琛� */
public static ArrayList<Token> DELIMITABLE = new ArrayList<>();
/* 绗﹀彿琛� */
public ArrayList<Token> SymbolTable = new ArrayList<>(); 

/* 甯搁噺琛� */
public ArrayList<Token> ConstTable = new ArrayList<>();

/* 瀛楃涓茶〃 */
public ArrayList<Token> StringTable = new ArrayList<>();

/* 瀛楃琛�  */
public ArrayList<Token> CharTable = new ArrayList<>();


	/* 
	 * KEYTABLE---鍏抽敭瀛楄〃
	 * DELIMITABLE---鐣岀琛�
	 * symbolTable---绗﹀彿琛�
	 * constantTable---甯搁噺琛�
	 * stringTable---瀛楃涓茶〃
	 * charTable---瀛楃琛�
	 * token---token涓查泦鍚�
	 * fileName---鎵�瑕佸垎鏋愮殑婧愮▼搴忕殑鏂囦欢鍚�
	 * filePointer---鎵�璇绘枃浠剁殑鎸囬拡
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

	//鏋勯�犲嚱鏁�
	public LexicalAnalyzer(String name) {
		//鍒濆鍖栧叧閿瓧琛ㄥ拰鐣岀琛�
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
	//鍒ゆ柇ch鏄惁鏄瓧姣嶆垨涓嬪垝绾�
	public boolean isCharacter(char ch) {
		if ((ch >= 65 && ch <= 106) || (ch >= 97 && ch <= 122) || (ch == 95)) { return true; }
		else return false;
	}
	
	//鍒ゆ柇鏄惁鏄叧閿瓧锛屽苟杩斿洖琛ㄤ腑浣嶇疆
	public int isKey(String str) {
		int i;
		for (i = 1; i <= 33; i++) {
			if (str.equals(KEYTABLE[i])) break;
		}
		if (i <= 33) return i;
		else return 0;
	}
	
	//鍒ゆ柇ch鏄惁鏄暟瀛�
	public boolean isNumber(char ch) {
		if (ch >= 48 && ch <= 57) return true;
		else return false;
	}
	
	//鍒ゆ柇鏄惁鍦ㄦ暣鏁板父閲忚〃閲岋紝鑻ヤ笉鍦ㄥ垯娣诲姞鍒拌〃灏�,骞惰繑鍥瀗鍦ㄨ〃涓殑浣嶇疆
	public int constantList(int n) {
		int i;
		i = constantTable1.indexOf(n);
		if(i!=-1)return i+1;
		else {
			constantTable1.add(n);
			return constantTable1.size();
		}
	}
	
	//鍒ゆ柇鏄惁鍦ㄥ皬鏁板父閲忚〃閲岋紝鑻ヤ笉鍦ㄥ垯娣诲姞鍒拌〃灏�,骞惰繑鍥瀗鍦ㄨ〃涓殑浣嶇疆
	public int constantList(float n) {
		int i;
		i = constantTable2.indexOf(n);
		if(i!=-1)return i+1;
		else {
			constantTable2.add(n);
			return constantTable2.size();
		}
	}
		
	//鍒ゆ柇鏄惁鍦ㄦ爣璇嗙琛ㄩ噷锛岃嫢涓嶅湪鍒欐坊鍔犲埌琛ㄥ熬,骞惰繑鍥瀗鍦ㄨ〃涓殑浣嶇疆	
	public int identifierList(String str) {
		int i;
		i = identifierTable.indexOf(str);
		if(i!=-1)return i+1;
		else {
			identifierTable.add(str);
			return identifierTable.size();
		}
	}
	
	//鍒ゆ柇瀛楃c鏄惁鏄晫绗�
	public boolean isDelimiter(char c) {
		int i;
		String ch=String.valueOf(c);
		for (i = 1; i <= 39; i++) {
			if (ch.equals(DELIMITERTABLE[i])) break;
		}
		if (i <= 39) return true;
		else return false;
	}
	//鍒ゆ柇瀛楃涓瞔鏄惁鏄晫绗�
	public int isDelimiter(String str) {
		int i;
		for (i = 1; i <= 39; i++) {
			if (str.equals(DELIMITERTABLE[i]) ) break;
		}
		if (i <= 39) return i;
		else return 0;
	}
	
	//鍒ゆ柇鏄惁鍦ㄥ瓧绗﹀父閲忚〃閲岋紝鑻ヤ笉鍦ㄥ垯娣诲姞鍒拌〃灏�,骞惰繑鍥瀞tr鍦ㄨ〃涓殑浣嶇疆
	public int charList(String str) {
		int i;
		i = charTable.indexOf(str);
		if(i!=-1)return i;
		else {
			charTable.add(str);
			return charTable.size()-1;
		}
	}
	
	//鍒ゆ柇鏄惁鍦ㄥ瓧绗︿覆甯搁噺琛ㄩ噷锛岃嫢涓嶅湪鍒欐坊鍔犲埌琛ㄥ熬,骞惰繑鍥瀞tr鍦ㄨ〃涓殑浣嶇疆
	public int stringList(String str) {
		int i;
		i = stringTable.indexOf(str);
		if(i!=-1)return i;
		else {
			stringTable.add(str);
			return stringTable.size()-1;
		}
	}
		
	//鎵弿鍑芥暟
	public Token scan() {
		char ch;//褰撳墠鎵�璇诲瓧绗�
		Token token = new Token();
		String str=new String();//淇濆瓨姝ゆ鎵弿鎷煎啓鍑虹殑鍗曡瘝
		int p=0;
		DFA d=new DFA();
		//鎵撳紑鎵弿鏂囦欢
		File file=new File(this.fileName);
		try {
			//灏嗘枃浠舵寚閽堝畾浣嶈嚦涓婃鎵弿浣嶇疆
			RandomAccessFile rfile=new RandomAccessFile(file, "r");
			rfile.seek(filePointer);
			long l=rfile.length();
			//鍒濆鍖栧紑濮嬬姸鎬�
			int nowState=1;
			ch=(char) rfile.readByte();
			//婊ゆ帀绌烘牸涓庡洖杞︺�傝嫢璺冲嚭鏃惰鍑虹殑鏄┖鏍兼垨鍥炶溅锛屽垯璇讳笅涓�涓�
			while ((ch == ' ' || ch == '\n')&&rfile.getFilePointer()!=l)
			{
				ch=(char) rfile.readByte();
			}
			//璇诲埌鐨勭涓�涓瓧绗︿负瀛楁瘝鎴栨槸涓嬪垝绾匡細鍏抽敭瀛楁垨鏄爣璇嗙
			if(isCharacter(ch)) {
			    DFA.kriDFA dfa=d.new kriDFA();
				while(nowState!=3&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					//闃叉鏂囦欢鎸囬拡瓒婄晫
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
				}
				//鑻ヨ烦鍑烘椂涓嶅湪缁堢粨鎬侊紝鍒欎笉鎺ュ彈姝ゅ崟璇�
				if(nowState!=3) token=null;
				//鍚﹀垯锛屾帴鍙楁鍗曡瘝锛屼笖鐢熸垚鐩稿簲token涓诧紝濉啓绗﹀彿琛�
				else {
					filePointer=rfile.getFilePointer()-2;//璁板綍鏂囦欢鎸囬拡
					str=(String) str.subSequence(0, str.length()-1);
					int j = isKey(str);
					//鑻tr鏄叧閿瓧
					if (j!=0) {
						Token.TYPE t=Token.TYPE.k;
						System.out.println("(k,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
					//鍚﹀垯str鏄爣璇嗙
					else {
						j = identifierList(str);
						Token.TYPE t=Token.TYPE.i;
						System.out.println("(i,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
				}
			}
			//鑻ョ涓�涓瓧绗︿负鏁板瓧锛屽垯鍙兘涓烘暟瀛楀父閲�	
			else if(isNumber(ch)) {
				DFA.nconDFA dfa=d.new nconDFA();
				float num;
				while(nowState!=4&&nowState!=6&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch),ch);
					str=str+ch;
					//闃叉鏂囦欢鎸囬拡瓒婄晫
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
					}
				//鑻ヨ烦鍑烘椂涓嶅湪缁堢粨鎬侊紝鍒欎笉鎺ュ彈姝ゅ崟璇�
				if(nowState!=4&&nowState!=6) token=null;
				//鍚﹀垯锛屾帴鍙楁鍗曡瘝锛屼笖鐢熸垚鐩稿簲token涓诧紝濉啓甯搁噺琛�
				else {
					filePointer=rfile.getFilePointer()-2;//璁板綍鏂囦欢鎸囬拡
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
			//鑻ヨ鍏ュ瓧绗︿负鈥橈紝鍒欏彲鑳芥槸瀛楃甯搁噺
		    else if(ch == 39) {
		    	DFA.cconDFA dfa=d.new cconDFA();
		    	//鐗规畩鎯呭喌鏄亣鍒板洖杞︼紝鍒欑粨鏉熷垽鏂�
				while(nowState!=5&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					//闃叉鏂囦欢鎸囬拡瓒婄晫
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}	
					}
				//鑻ヨ烦鍑烘椂涓嶅湪缁堢粨鎬侊紝鍒欎笉鎺ュ彈姝ゅ崟璇�
				if(nowState!=5) token=null;
				//鍚﹀垯锛屾帴鍙楁鍗曡瘝锛屼笖鐢熸垚鐩稿簲token涓诧紝濉啓瀛楃甯搁噺琛�
				else {
					filePointer=rfile.getFilePointer()-2;//璁板綍鏂囦欢鎸囬拡
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
			//鑻ヨ鍏ョ殑绗竴涓瓧绗︽槸"锛屽垯鍙兘鏄瓧绗︿覆甯搁噺
			else if(ch == 34) {
				DFA.sconDFA dfa=d.new sconDFA();
		    	//鐗规畩鎯呭喌鏄亣鍒板洖杞︼紝鍒欑粨鏉熷垽鏂�
				while(nowState!=2&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					//闃叉鏂囦欢鎸囬拡瓒婄晫
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
					}
				//鑻ヨ烦鍑烘椂涓嶅湪缁堢粨鎬侊紝鍒欎笉鎺ュ彈姝ゅ崟璇�
				if(nowState!=2) token=null;
				//鍚﹀垯锛屾帴鍙楁鍗曡瘝锛屼笖鐢熸垚鐩稿簲token涓诧紝濉啓瀛楃涓插父閲忚〃
				else {
					filePointer=rfile.getFilePointer()-2;//璁板綍鏂囦欢鎸囬拡
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
			//鑻ヨ鍏ョ殑绗竴涓瓧绗︽槸鐣岀锛屽垯鍙兘鏄晫绗�
			else if (isDelimiter(ch)) {
				str=str+ch;
				//闃叉鏂囦欢鎸囬拡瓒婄晫
				if(rfile.getFilePointer()!=l) {
					ch=(char) rfile.readByte();	
				}
				if(ch=='='||ch=='*'||ch=='&'||ch=='|'||ch=='<'||ch=='>'||ch=='/'||ch=='\\'||ch=='+'||ch=='-') {
					str=str+ch;
					//鏄弻鐩晫绗︼紝鐢熸垚鐩稿簲token涓�
					if(isDelimiter(str)!=0) {
						filePointer=rfile.getFilePointer();//璁板綍鏂囦欢鎸囬拡
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
				//鏄崟鐩晫绗�
				else {
					filePointer=rfile.getFilePointer()-1;//璁板綍鏂囦欢鎸囬拡
					nowState=7;
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
			rfile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return token;
	}

	//鍦ㄦ枃浠舵渶鍚庢坊鍔犵粨鏉熺鍙�#
	public void addEnd() {
		FileWriter fw;
		try {
			fw = new FileWriter(this.fileName,true);
			PrintWriter pw=new PrintWriter(fw);
			pw.println("#");
			pw.close ();
			fw.close ();
		} catch (IOException e) {
			// TODO 鑷姩鐢熸垚鐨� catch 鍧�
			e.printStackTrace();
		}
		
	}
}

>>>>>>> 6967fb63eb8a0e1e21f5fcb11578844983a511d6
