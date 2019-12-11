<<<<<<< HEAD
package compiler;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LexicalAnalyzer {

/* ¹Ø¼ü×Ö±í */
 public static ArrayList<Token> KEYTABLE1 = new ArrayList<>();
/* ½ç·û±í */
public static ArrayList<Token> DELIMITABLE = new ArrayList<>();
/* ·ûºÅ±í */
public ArrayList<Token> SymbolTable = new ArrayList<>(); 

/* ³£Á¿±í */
public ArrayList<Token> ConstTable = new ArrayList<>();

/* ×Ö·û´®±í */
public ArrayList<Token> StringTable = new ArrayList<>();

/* ×Ö·û±í  */
public ArrayList<Token> CharTable = new ArrayList<>();


	/* 
	 * KEYTABLE---¹Ø¼ü×Ö±í
	 * DELIMITABLE---½ç·û±í
	 * symbolTable---·ûºÅ±í
	 * constantTable---³£Á¿±í
	 * stringTable---×Ö·û´®±í
	 * charTable---×Ö·û±í
	 * token---token´®¼¯ºÏ
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
	 * fileName---ËùÒª·ÖÎöµÄÔ´³ÌĞòµÄÎÄ¼şÃû
	 * filePointer---Ëù¶ÁÎÄ¼şµÄÖ¸Õë
	 * 
	 */
	private String fileName;
	private int filePointer;
	//¹¹Ôìº¯Êı
	public LexicalAnalyzer(String name) {
		
		this.fileName=name;
		filePointer=0;
	}
	//ÅĞ¶ÏchÊÇ·ñÊÇ×ÖÄ¸»òÏÂ»®Ïß
	public boolean isCharacter(char ch) {
		if ((ch >= 65 && ch <= 106) || (ch >= 97 && ch <= 122) || (ch == 95)) { return true; }
		else return false;
	}
	
	//ÅĞ¶ÏÊÇ·ñÊÇ¹Ø¼ü×Ö£¬²¢·µ»Ø±íÖĞÎ»ÖÃ
	public int isKey(String str) {
		int i;
		for (i = 1; i <= 33; i++) {
			if (str == KEYTABLE[i]) break;
		}
		if (i <= 33) return i;
		else return 0;
	}
	
	//ÅĞ¶ÏchÊÇ·ñÊÇÊı×Ö
	public boolean isNumber(char ch) {
		if (ch >= 48 && ch <= 57) return true;
		else return false;
	}
	
	//ÅĞ¶ÏÊÇ·ñÔÚÕûÊı³£Á¿±íÀï£¬Èô²»ÔÚÔòÌí¼Óµ½±íÎ²,²¢·µ»ØnÔÚ±íÖĞµÄÎ»ÖÃ
	public int constantList(int n) {
		int i;
		i = constantTable1.indexOf(n);
		if(i!=-1)return i;
		else {
			constantTable1.add(n);
			return constantTable1.size()-1;
		}
	}
	
	//ÅĞ¶ÏÊÇ·ñÔÚĞ¡Êı³£Á¿±íÀï£¬Èô²»ÔÚÔòÌí¼Óµ½±íÎ²,²¢·µ»ØnÔÚ±íÖĞµÄÎ»ÖÃ
		public int constantList(float n) {
			int i;
			i = constantTable2.indexOf(n);
			if(i!=-1)return i;
			else {
				constantTable2.add(n);
				return constantTable2.size()-1;
			}
		}
	
	//ÅĞ¶Ï×Ö·ûcÊÇ·ñÊÇ½ç·û
	public boolean isDelimiter(char c) {
		int i;
		String ch=String.valueOf(c);
		for (i = 1; i <= 39; i++) {
			if (ch == DELIMITERTABLE[i]) break;
		}
		if (i <= 39) return true;
		else return false;
	}
	//ÅĞ¶Ï×Ö·û´®cÊÇ·ñÊÇ½ç·û
	public int isDelimiter(String str) {
		int i;
		for (i = 1; i <= 39; i++) {
			if (str == DELIMITERTABLE[i]) break;
		}
		if (i <= 39) return i;
		else return 0;
	}
	
	//ÅĞ¶ÏÊÇ·ñÔÚ×Ö·û³£Á¿±íÀï£¬Èô²»ÔÚÔòÌí¼Óµ½±íÎ²,²¢·µ»ØstrÔÚ±íÖĞµÄÎ»ÖÃ
	public int charList(String str) {
		int i;
		i = charTable.indexOf(str);
		if(i!=-1)return i;
		else {
			charTable.add(str);
			return charTable.size()-1;
		}
	}
	
	//ÅĞ¶ÏÊÇ·ñÔÚ×Ö·û´®³£Á¿±íÀï£¬Èô²»ÔÚÔòÌí¼Óµ½±íÎ²,²¢·µ»ØstrÔÚ±íÖĞµÄÎ»ÖÃ
	public int stringList(String str) {
		int i;
		i = stringTable.indexOf(str);
		if(i!=-1)return i;
		else {
			stringTable.add(str);
			return stringTable.size()-1;
		}
	}
		
	//É¨Ãèº¯Êı
	public Token scan() {
		char ch;//µ±Ç°Ëù¶Á×Ö·û
		Token token = new Token();
		String str=null;//±£´æ´Ë´ÎÉ¨ÃèÆ´Ğ´³öµÄµ¥´Ê
		DFA d=new DFA();
		//´ò¿ªÉ¨ÃèÎÄ¼ş
		File file=new File(this.fileName);
		try {
			//½«ÎÄ¼şÖ¸Õë¶¨Î»ÖÁÉÏ´ÎÉ¨ÃèÎ»ÖÃ
			RandomAccessFile rfile=new RandomAccessFile(file, "r");
			rfile.seek(filePointer);
			long l=rfile.length();
			//³õÊ¼»¯¿ªÊ¼×´Ì¬
			int nowState=1;
			ch=rfile.readChar();
			//ÂËµô¿Õ¸ñÓë»Ø³µ¡£ÈôÌø³öÊ±¶Á³öµÄÊÇ¿Õ¸ñ»ò»Ø³µ£¬Ôò¶ÁÏÂÒ»¸ö
			if (ch == ' ' || ch == '\n')
			{
				ch=rfile.readChar();
			}
			//¶Áµ½µÄµÚÒ»¸ö×Ö·ûÎª×ÖÄ¸»òÊÇÏÂ»®Ïß£º¹Ø¼ü×Ö»òÊÇ±êÊ¶·û
			if(isCharacter(ch)) {
			    DFA.kriDFA dfa=d.new kriDFA();
				//DFA.kriDFA dfa=null;
				while(nowState!=3&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//ÈôÌø³öÊ±²»ÔÚÖÕ½áÌ¬£¬Ôò²»½ÓÊÜ´Ëµ¥´Ê
				if(nowState!=3) token=null;
				//·ñÔò£¬½ÓÊÜ´Ëµ¥´Ê£¬ÇÒÉú³ÉÏàÓ¦token´®£¬ÌîĞ´·ûºÅ±í
				else {
					str=(String) str.subSequence(0, str.length()-1);
					int j = isKey(str);
					//ÈôstrÊÇ¹Ø¼ü×Ö
					if (j!=0) {
						Token.TYPE t=Token.TYPE.k;
						System.out.println("(k,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
					//·ñÔòstrÊÇ±êÊ¶·û
					else {
					}
				}
			}
			//ÈôµÚÒ»¸ö×Ö·ûÎªÊı×Ö£¬Ôò¿ÉÄÜÎªÊı×Ö³£Á¿	
			else if(isNumber(ch)) {
				DFA.nconDFA dfa=d.new nconDFA();
				float num;
				while(nowState!=4&&nowState!=6&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch),ch);
					str=str+ch;
					ch=rfile.readChar();	
					}
				//ÈôÌø³öÊ±²»ÔÚÖÕ½áÌ¬£¬Ôò²»½ÓÊÜ´Ëµ¥´Ê
				if(nowState!=4&&nowState!=6) token=null;
				//·ñÔò£¬½ÓÊÜ´Ëµ¥´Ê£¬ÇÒÉú³ÉÏàÓ¦token´®£¬ÌîĞ´³£Á¿±í
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
			//Èô¶ÁÈë×Ö·ûÎª¡®£¬Ôò¿ÉÄÜÊÇ×Ö·û³£Á¿
		    else if(ch == 39) {
		    	DFA.cconDFA dfa=d.new cconDFA();
		    	//ÌØÊâÇé¿öÊÇÓöµ½»Ø³µ£¬Ôò½áÊøÅĞ¶Ï
				while(nowState!=5&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//ÈôÌø³öÊ±²»ÔÚÖÕ½áÌ¬£¬Ôò²»½ÓÊÜ´Ëµ¥´Ê
				if(nowState!=5) token=null;
				//·ñÔò£¬½ÓÊÜ´Ëµ¥´Ê£¬ÇÒÉú³ÉÏàÓ¦token´®£¬ÌîĞ´×Ö·û³£Á¿±í
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
			//Èô¶ÁÈëµÄµÚÒ»¸ö×Ö·ûÊÇ"£¬Ôò¿ÉÄÜÊÇ×Ö·û´®³£Á¿
			else if(ch == 34) {
				DFA.sconDFA dfa=d.new sconDFA();
		    	//ÌØÊâÇé¿öÊÇÓöµ½»Ø³µ£¬Ôò½áÊøÅĞ¶Ï
				while(nowState!=2&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//ÈôÌø³öÊ±²»ÔÚÖÕ½áÌ¬£¬Ôò²»½ÓÊÜ´Ëµ¥´Ê
				if(nowState!=2) token=null;
				//·ñÔò£¬½ÓÊÜ´Ëµ¥´Ê£¬ÇÒÉú³ÉÏàÓ¦token´®£¬ÌîĞ´×Ö·û´®³£Á¿±í
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
			//Èô¶ÁÈëµÄµÚÒ»¸ö×Ö·ûÊÇ½ç·û£¬Ôò¿ÉÄÜÊÇ½ç·û
			else if (isDelimiter(ch)) {
				ch=rfile.readChar();
				if(ch=='='||ch=='*'||ch=='&'||ch=='|'||ch=='<'||ch=='>'||ch=='/'||ch=='\\'||ch=='+'||ch=='-') {
					str=str+ch;
					//ÊÇË«Ä¿½ç·û£¬Éú³ÉÏàÓ¦token´®
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
				//ÊÇµ¥Ä¿½ç·û
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

/* å…³é”®å­—è¡¨ */
 public static ArrayList<Token> KEYTABLE = new ArrayList<>();
/* ç•Œç¬¦è¡¨ */
public static ArrayList<Token> DELIMITABLE = new ArrayList<>();
/* ç¬¦å·è¡¨ */
public ArrayList<Token> SymbolTable = new ArrayList<>(); 

/* å¸¸é‡è¡¨ */
public ArrayList<Token> ConstTable = new ArrayList<>();

/* å­—ç¬¦ä¸²è¡¨ */
public ArrayList<Token> StringTable = new ArrayList<>();

/* å­—ç¬¦è¡¨  */
public ArrayList<Token> CharTable = new ArrayList<>();


	/* 
	 * KEYTABLE---å…³é”®å­—è¡¨
	 * DELIMITABLE---ç•Œç¬¦è¡¨
	 * symbolTable---ç¬¦å·è¡¨
	 * constantTable---å¸¸é‡è¡¨
	 * stringTable---å­—ç¬¦ä¸²è¡¨
	 * charTable---å­—ç¬¦è¡¨
	 * token---tokenä¸²é›†åˆ
	 * fileName---æ‰€è¦åˆ†æçš„æºç¨‹åºçš„æ–‡ä»¶å
	 * filePointer---æ‰€è¯»æ–‡ä»¶çš„æŒ‡é’ˆ
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

	//æ„é€ å‡½æ•°
	public LexicalAnalyzer(String name) {
		//åˆå§‹åŒ–å…³é”®å­—è¡¨å’Œç•Œç¬¦è¡¨
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
	//åˆ¤æ–­chæ˜¯å¦æ˜¯å­—æ¯æˆ–ä¸‹åˆ’çº¿
	public boolean isCharacter(char ch) {
		if ((ch >= 65 && ch <= 106) || (ch >= 97 && ch <= 122) || (ch == 95)) { return true; }
		else return false;
	}
	
	//åˆ¤æ–­æ˜¯å¦æ˜¯å…³é”®å­—ï¼Œå¹¶è¿”å›è¡¨ä¸­ä½ç½®
	public int isKey(String str) {
		int i;
		for (i = 1; i <= 33; i++) {
			if (str.equals(KEYTABLE[i])) break;
		}
		if (i <= 33) return i;
		else return 0;
	}
	
	//åˆ¤æ–­chæ˜¯å¦æ˜¯æ•°å­—
	public boolean isNumber(char ch) {
		if (ch >= 48 && ch <= 57) return true;
		else return false;
	}
	
	//åˆ¤æ–­æ˜¯å¦åœ¨æ•´æ•°å¸¸é‡è¡¨é‡Œï¼Œè‹¥ä¸åœ¨åˆ™æ·»åŠ åˆ°è¡¨å°¾,å¹¶è¿”å›nåœ¨è¡¨ä¸­çš„ä½ç½®
	public int constantList(int n) {
		int i;
		i = constantTable1.indexOf(n);
		if(i!=-1)return i+1;
		else {
			constantTable1.add(n);
			return constantTable1.size();
		}
	}
	
	//åˆ¤æ–­æ˜¯å¦åœ¨å°æ•°å¸¸é‡è¡¨é‡Œï¼Œè‹¥ä¸åœ¨åˆ™æ·»åŠ åˆ°è¡¨å°¾,å¹¶è¿”å›nåœ¨è¡¨ä¸­çš„ä½ç½®
	public int constantList(float n) {
		int i;
		i = constantTable2.indexOf(n);
		if(i!=-1)return i+1;
		else {
			constantTable2.add(n);
			return constantTable2.size();
		}
	}
		
	//åˆ¤æ–­æ˜¯å¦åœ¨æ ‡è¯†ç¬¦è¡¨é‡Œï¼Œè‹¥ä¸åœ¨åˆ™æ·»åŠ åˆ°è¡¨å°¾,å¹¶è¿”å›nåœ¨è¡¨ä¸­çš„ä½ç½®	
	public int identifierList(String str) {
		int i;
		i = identifierTable.indexOf(str);
		if(i!=-1)return i+1;
		else {
			identifierTable.add(str);
			return identifierTable.size();
		}
	}
	
	//åˆ¤æ–­å­—ç¬¦cæ˜¯å¦æ˜¯ç•Œç¬¦
	public boolean isDelimiter(char c) {
		int i;
		String ch=String.valueOf(c);
		for (i = 1; i <= 39; i++) {
			if (ch.equals(DELIMITERTABLE[i])) break;
		}
		if (i <= 39) return true;
		else return false;
	}
	//åˆ¤æ–­å­—ç¬¦ä¸²cæ˜¯å¦æ˜¯ç•Œç¬¦
	public int isDelimiter(String str) {
		int i;
		for (i = 1; i <= 39; i++) {
			if (str.equals(DELIMITERTABLE[i]) ) break;
		}
		if (i <= 39) return i;
		else return 0;
	}
	
	//åˆ¤æ–­æ˜¯å¦åœ¨å­—ç¬¦å¸¸é‡è¡¨é‡Œï¼Œè‹¥ä¸åœ¨åˆ™æ·»åŠ åˆ°è¡¨å°¾,å¹¶è¿”å›stråœ¨è¡¨ä¸­çš„ä½ç½®
	public int charList(String str) {
		int i;
		i = charTable.indexOf(str);
		if(i!=-1)return i;
		else {
			charTable.add(str);
			return charTable.size()-1;
		}
	}
	
	//åˆ¤æ–­æ˜¯å¦åœ¨å­—ç¬¦ä¸²å¸¸é‡è¡¨é‡Œï¼Œè‹¥ä¸åœ¨åˆ™æ·»åŠ åˆ°è¡¨å°¾,å¹¶è¿”å›stråœ¨è¡¨ä¸­çš„ä½ç½®
	public int stringList(String str) {
		int i;
		i = stringTable.indexOf(str);
		if(i!=-1)return i;
		else {
			stringTable.add(str);
			return stringTable.size()-1;
		}
	}
		
	//æ‰«æå‡½æ•°
	public Token scan() {
		char ch;//å½“å‰æ‰€è¯»å­—ç¬¦
		Token token = new Token();
		String str=new String();//ä¿å­˜æ­¤æ¬¡æ‰«ææ‹¼å†™å‡ºçš„å•è¯
		int p=0;
		DFA d=new DFA();
		//æ‰“å¼€æ‰«ææ–‡ä»¶
		File file=new File(this.fileName);
		try {
			//å°†æ–‡ä»¶æŒ‡é’ˆå®šä½è‡³ä¸Šæ¬¡æ‰«æä½ç½®
			RandomAccessFile rfile=new RandomAccessFile(file, "r");
			rfile.seek(filePointer);
			long l=rfile.length();
			//åˆå§‹åŒ–å¼€å§‹çŠ¶æ€
			int nowState=1;
			ch=(char) rfile.readByte();
			//æ»¤æ‰ç©ºæ ¼ä¸å›è½¦ã€‚è‹¥è·³å‡ºæ—¶è¯»å‡ºçš„æ˜¯ç©ºæ ¼æˆ–å›è½¦ï¼Œåˆ™è¯»ä¸‹ä¸€ä¸ª
			while ((ch == ' ' || ch == '\n')&&rfile.getFilePointer()!=l)
			{
				ch=(char) rfile.readByte();
			}
			//è¯»åˆ°çš„ç¬¬ä¸€ä¸ªå­—ç¬¦ä¸ºå­—æ¯æˆ–æ˜¯ä¸‹åˆ’çº¿ï¼šå…³é”®å­—æˆ–æ˜¯æ ‡è¯†ç¬¦
			if(isCharacter(ch)) {
			    DFA.kriDFA dfa=d.new kriDFA();
				while(nowState!=3&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					//é˜²æ­¢æ–‡ä»¶æŒ‡é’ˆè¶Šç•Œ
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
				}
				//è‹¥è·³å‡ºæ—¶ä¸åœ¨ç»ˆç»“æ€ï¼Œåˆ™ä¸æ¥å—æ­¤å•è¯
				if(nowState!=3) token=null;
				//å¦åˆ™ï¼Œæ¥å—æ­¤å•è¯ï¼Œä¸”ç”Ÿæˆç›¸åº”tokenä¸²ï¼Œå¡«å†™ç¬¦å·è¡¨
				else {
					filePointer=rfile.getFilePointer()-2;//è®°å½•æ–‡ä»¶æŒ‡é’ˆ
					str=(String) str.subSequence(0, str.length()-1);
					int j = isKey(str);
					//è‹¥stræ˜¯å…³é”®å­—
					if (j!=0) {
						Token.TYPE t=Token.TYPE.k;
						System.out.println("(k,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
					//å¦åˆ™stræ˜¯æ ‡è¯†ç¬¦
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
			//è‹¥ç¬¬ä¸€ä¸ªå­—ç¬¦ä¸ºæ•°å­—ï¼Œåˆ™å¯èƒ½ä¸ºæ•°å­—å¸¸é‡	
			else if(isNumber(ch)) {
				DFA.nconDFA dfa=d.new nconDFA();
				float num;
				while(nowState!=4&&nowState!=6&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch),ch);
					str=str+ch;
					//é˜²æ­¢æ–‡ä»¶æŒ‡é’ˆè¶Šç•Œ
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
					}
				//è‹¥è·³å‡ºæ—¶ä¸åœ¨ç»ˆç»“æ€ï¼Œåˆ™ä¸æ¥å—æ­¤å•è¯
				if(nowState!=4&&nowState!=6) token=null;
				//å¦åˆ™ï¼Œæ¥å—æ­¤å•è¯ï¼Œä¸”ç”Ÿæˆç›¸åº”tokenä¸²ï¼Œå¡«å†™å¸¸é‡è¡¨
				else {
					filePointer=rfile.getFilePointer()-2;//è®°å½•æ–‡ä»¶æŒ‡é’ˆ
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
			//è‹¥è¯»å…¥å­—ç¬¦ä¸ºâ€˜ï¼Œåˆ™å¯èƒ½æ˜¯å­—ç¬¦å¸¸é‡
		    else if(ch == 39) {
		    	DFA.cconDFA dfa=d.new cconDFA();
		    	//ç‰¹æ®Šæƒ…å†µæ˜¯é‡åˆ°å›è½¦ï¼Œåˆ™ç»“æŸåˆ¤æ–­
				while(nowState!=5&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					//é˜²æ­¢æ–‡ä»¶æŒ‡é’ˆè¶Šç•Œ
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}	
					}
				//è‹¥è·³å‡ºæ—¶ä¸åœ¨ç»ˆç»“æ€ï¼Œåˆ™ä¸æ¥å—æ­¤å•è¯
				if(nowState!=5) token=null;
				//å¦åˆ™ï¼Œæ¥å—æ­¤å•è¯ï¼Œä¸”ç”Ÿæˆç›¸åº”tokenä¸²ï¼Œå¡«å†™å­—ç¬¦å¸¸é‡è¡¨
				else {
					filePointer=rfile.getFilePointer()-2;//è®°å½•æ–‡ä»¶æŒ‡é’ˆ
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
			//è‹¥è¯»å…¥çš„ç¬¬ä¸€ä¸ªå­—ç¬¦æ˜¯"ï¼Œåˆ™å¯èƒ½æ˜¯å­—ç¬¦ä¸²å¸¸é‡
			else if(ch == 34) {
				DFA.sconDFA dfa=d.new sconDFA();
		    	//ç‰¹æ®Šæƒ…å†µæ˜¯é‡åˆ°å›è½¦ï¼Œåˆ™ç»“æŸåˆ¤æ–­
				while(nowState!=2&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					//é˜²æ­¢æ–‡ä»¶æŒ‡é’ˆè¶Šç•Œ
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
					}
				//è‹¥è·³å‡ºæ—¶ä¸åœ¨ç»ˆç»“æ€ï¼Œåˆ™ä¸æ¥å—æ­¤å•è¯
				if(nowState!=2) token=null;
				//å¦åˆ™ï¼Œæ¥å—æ­¤å•è¯ï¼Œä¸”ç”Ÿæˆç›¸åº”tokenä¸²ï¼Œå¡«å†™å­—ç¬¦ä¸²å¸¸é‡è¡¨
				else {
					filePointer=rfile.getFilePointer()-2;//è®°å½•æ–‡ä»¶æŒ‡é’ˆ
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
			//è‹¥è¯»å…¥çš„ç¬¬ä¸€ä¸ªå­—ç¬¦æ˜¯ç•Œç¬¦ï¼Œåˆ™å¯èƒ½æ˜¯ç•Œç¬¦
			else if (isDelimiter(ch)) {
				str=str+ch;
				//é˜²æ­¢æ–‡ä»¶æŒ‡é’ˆè¶Šç•Œ
				if(rfile.getFilePointer()!=l) {
					ch=(char) rfile.readByte();	
				}
				if(ch=='='||ch=='*'||ch=='&'||ch=='|'||ch=='<'||ch=='>'||ch=='/'||ch=='\\'||ch=='+'||ch=='-') {
					str=str+ch;
					//æ˜¯åŒç›®ç•Œç¬¦ï¼Œç”Ÿæˆç›¸åº”tokenä¸²
					if(isDelimiter(str)!=0) {
						filePointer=rfile.getFilePointer();//è®°å½•æ–‡ä»¶æŒ‡é’ˆ
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
				//æ˜¯å•ç›®ç•Œç¬¦
				else {
					filePointer=rfile.getFilePointer()-1;//è®°å½•æ–‡ä»¶æŒ‡é’ˆ
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

	//åœ¨æ–‡ä»¶æœ€åæ·»åŠ ç»“æŸç¬¦å·#
	public void addEnd() {
		FileWriter fw;
		try {
			fw = new FileWriter(this.fileName,true);
			PrintWriter pw=new PrintWriter(fw);
			pw.println("#");
			pw.close ();
			fw.close ();
		} catch (IOException e) {
			// TODO è‡ªåŠ¨ç”Ÿæˆçš„ catch å—
			e.printStackTrace();
		}
		
	}
}

>>>>>>> 6967fb63eb8a0e1e21f5fcb11578844983a511d6
