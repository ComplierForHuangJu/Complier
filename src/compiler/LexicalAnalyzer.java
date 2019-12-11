package compiler;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LexicalAnalyzer {

/* �ؼ��ֱ� */
 public static ArrayList<Token> KEYTABLE = new ArrayList<>();
/* ����� */
public static ArrayList<Token> DELIMITABLE = new ArrayList<>();
/* ���ű� */
public ArrayList<Token> SymbolTable = new ArrayList<>(); 

/* ������ */
public ArrayList<Token> ConstTable = new ArrayList<>();

/* �ַ����� */
public ArrayList<Token> StringTable = new ArrayList<>();

/* �ַ���  */
public ArrayList<Token> CharTable = new ArrayList<>();


	/* 
	 * KEYTABLE---�ؼ��ֱ�
	 * DELIMITABLE---�����
	 * symbolTable---���ű�
	 * constantTable---������
	 * stringTable---�ַ�����
	 * charTable---�ַ���
	 * token---token������
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
	 * fileName---��Ҫ������Դ������ļ���
	 * filePointer---�����ļ���ָ��
	 * 
	 */
	private String fileName;
	private int filePointer;
	//���캯��
	public LexicalAnalyzer(String name) {
		
		this.fileName=name;
		filePointer=0;
	}
	//�ж�ch�Ƿ�����ĸ���»���
	public boolean isCharacter(char ch) {
		if ((ch >= 65 && ch <= 106) || (ch >= 97 && ch <= 122) || (ch == 95)) { return true; }
		else return false;
	}
	
	//�ж��Ƿ��ǹؼ��֣������ر���λ��
	public int isKey(String str) {
		int i;
		for (i = 1; i <= 33; i++) {
			if (str == KEYTABLE[i]) break;
		}
		if (i <= 33) return i;
		else return 0;
	}
	
	//�ж�ch�Ƿ�������
	public boolean isNumber(char ch) {
		if (ch >= 48 && ch <= 57) return true;
		else return false;
	}
	
	//�ж��Ƿ������������������������ӵ���β,������n�ڱ��е�λ��
	public int constantList(int n) {
		int i;
		i = constantTable1.indexOf(n);
		if(i!=-1)return i;
		else {
			constantTable1.add(n);
			return constantTable1.size()-1;
		}
	}
	
	//�ж��Ƿ���С�������������������ӵ���β,������n�ڱ��е�λ��
		public int constantList(float n) {
			int i;
			i = constantTable2.indexOf(n);
			if(i!=-1)return i;
			else {
				constantTable2.add(n);
				return constantTable2.size()-1;
			}
		}
	
	//�ж��ַ�c�Ƿ��ǽ��
	public boolean isDelimiter(char c) {
		int i;
		String ch=String.valueOf(c);
		for (i = 1; i <= 39; i++) {
			if (ch == DELIMITERTABLE[i]) break;
		}
		if (i <= 39) return true;
		else return false;
	}
	//�ж��ַ���c�Ƿ��ǽ��
	public int isDelimiter(String str) {
		int i;
		for (i = 1; i <= 39; i++) {
			if (str == DELIMITERTABLE[i]) break;
		}
		if (i <= 39) return i;
		else return 0;
	}
	
	//�ж��Ƿ����ַ������������������ӵ���β,������str�ڱ��е�λ��
	public int charList(String str) {
		int i;
		i = charTable.indexOf(str);
		if(i!=-1)return i;
		else {
			charTable.add(str);
			return charTable.size()-1;
		}
	}
	
	//�ж��Ƿ����ַ��������������������ӵ���β,������str�ڱ��е�λ��
	public int stringList(String str) {
		int i;
		i = stringTable.indexOf(str);
		if(i!=-1)return i;
		else {
			stringTable.add(str);
			return stringTable.size()-1;
		}
	}
		
	//ɨ�躯��
	public Token scan() {
		char ch;//��ǰ�����ַ�
		Token token = new Token();
		String str=null;//����˴�ɨ��ƴд���ĵ���
		DFA d=new DFA();
		//��ɨ���ļ�
		File file=new File(this.fileName);
		try {
			//���ļ�ָ�붨λ���ϴ�ɨ��λ��
			RandomAccessFile rfile=new RandomAccessFile(file, "r");
			rfile.seek(filePointer);
			long l=rfile.length();
			//��ʼ����ʼ״̬
			int nowState=1;
			ch=rfile.readChar();
			//�˵��ո���س���������ʱ�������ǿո��س��������һ��
			if (ch == ' ' || ch == '\n')
			{
				ch=rfile.readChar();
			}
			//�����ĵ�һ���ַ�Ϊ��ĸ�����»��ߣ��ؼ��ֻ��Ǳ�ʶ��
			if(isCharacter(ch)) {
			    DFA.kriDFA dfa=d.new kriDFA();
				//DFA.kriDFA dfa=null;
				while(nowState!=3&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//������ʱ�����ս�̬���򲻽��ܴ˵���
				if(nowState!=3) token=null;
				//���򣬽��ܴ˵��ʣ���������Ӧtoken������д���ű�
				else {
					str=(String) str.subSequence(0, str.length()-1);
					int j = isKey(str);
					//��str�ǹؼ���
					if (j!=0) {
						Token.TYPE t=Token.TYPE.k;
						System.out.println("(k,"+j+")");
						token.settype(t);
						token.setindex(j);
						token.setlastState(nowState);
						this.tokens.add(token);
					}
					//����str�Ǳ�ʶ��
					else {
					}
				}
			}
			//����һ���ַ�Ϊ���֣������Ϊ���ֳ���	
			else if(isNumber(ch)) {
				DFA.nconDFA dfa=d.new nconDFA();
				float num;
				while(nowState!=4&&nowState!=6&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch),ch);
					str=str+ch;
					ch=rfile.readChar();	
					}
				//������ʱ�����ս�̬���򲻽��ܴ˵���
				if(nowState!=4&&nowState!=6) token=null;
				//���򣬽��ܴ˵��ʣ���������Ӧtoken������д������
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
			//�������ַ�Ϊ������������ַ�����
		    else if(ch == 39) {
		    	DFA.cconDFA dfa=d.new cconDFA();
		    	//��������������س���������ж�
				while(nowState!=5&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//������ʱ�����ս�̬���򲻽��ܴ˵���
				if(nowState!=5) token=null;
				//���򣬽��ܴ˵��ʣ���������Ӧtoken������д�ַ�������
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
			//������ĵ�һ���ַ���"����������ַ�������
			else if(ch == 34) {
				DFA.sconDFA dfa=d.new sconDFA();
		    	//��������������س���������ж�
				while(nowState!=2&&nowState!=0&&rfile.getFilePointer()!=l+1&&ch!='\n') {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					ch=rfile.readChar();	
					}
				//������ʱ�����ս�̬���򲻽��ܴ˵���
				if(nowState!=2) token=null;
				//���򣬽��ܴ˵��ʣ���������Ӧtoken������д�ַ���������
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
			//������ĵ�һ���ַ��ǽ����������ǽ��
			else if (isDelimiter(ch)) {
				ch=rfile.readChar();
				if(ch=='='||ch=='*'||ch=='&'||ch=='|'||ch=='<'||ch=='>'||ch=='/'||ch=='\\'||ch=='+'||ch=='-') {
					str=str+ch;
					//��˫Ŀ�����������Ӧtoken��
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
				//�ǵ�Ŀ���
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

