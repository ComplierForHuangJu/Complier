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
	 * KEYTABLE---�ؼ��ֱ�
	 * DELIMITABLE---�����
	 * symbolTable---���ű�
	 * constantTable---������
	 * stringTable---�ַ�����
	 * charTable---�ַ���
	 * token---token������
	 * fileName---��Ҫ������Դ������ļ���
	 * filePointer---�����ļ���ָ��
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

	//���캯��
	public LexicalAnalyzer(String name) {
		//��ʼ���ؼ��ֱ�ͽ����
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
	//�ж�ch�Ƿ�����ĸ���»���
	public boolean isCharacter(char ch) {
		if ((ch >= 65 && ch <= 106) || (ch >= 97 && ch <= 122) || (ch == 95)) { return true; }
		else return false;
	}
	
	//�ж��Ƿ��ǹؼ��֣������ر���λ��
	public int isKey(String str) {
		int i;
		for (i = 1; i <= 33; i++) {
			if (str.equals(KEYTABLE[i])) break;
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
		if(i!=-1)return i+1;
		else {
			constantTable1.add(n);
			return constantTable1.size();
		}
	}
	
	//�ж��Ƿ���С�������������������ӵ���β,������n�ڱ��е�λ��
	public int constantList(float n) {
		int i;
		i = constantTable2.indexOf(n);
		if(i!=-1)return i+1;
		else {
			constantTable2.add(n);
			return constantTable2.size();
		}
	}
		
	//�ж��Ƿ��ڱ�ʶ���������������ӵ���β,������n�ڱ��е�λ��	
	public int identifierList(String str) {
		int i;
		i = identifierTable.indexOf(str);
		if(i!=-1)return i+1;
		else {
			identifierTable.add(str);
			return identifierTable.size();
		}
	}
	
	//�ж��ַ�c�Ƿ��ǽ��
	public boolean isDelimiter(char c) {
		int i;
		String ch=String.valueOf(c);
		for (i = 1; i <= 39; i++) {
			if (ch.equals(DELIMITERTABLE[i])) break;
		}
		if (i <= 39) return true;
		else return false;
	}
	//�ж��ַ���c�Ƿ��ǽ��
	public int isDelimiter(String str) {
		int i;
		for (i = 1; i <= 39; i++) {
			if (str.equals(DELIMITERTABLE[i]) ) break;
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
		String str=new String();//����˴�ɨ��ƴд���ĵ���
		int p=0;
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
			ch=(char) rfile.readByte();
			//�˵��ո���س���������ʱ�������ǿո��س��������һ��
			while ((ch == ' ' || ch == '\n')&&rfile.getFilePointer()!=l)
			{
				ch=(char) rfile.readByte();
			}
			//�����ĵ�һ���ַ�Ϊ��ĸ�����»��ߣ��ؼ��ֻ��Ǳ�ʶ��
			if(isCharacter(ch)) {
			    DFA.kriDFA dfa=d.new kriDFA();
				while(nowState!=3&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch));
					str=str+ch;
					//��ֹ�ļ�ָ��Խ��
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
				}
				//������ʱ�����ս�̬���򲻽��ܴ˵���
				if(nowState!=3) token=null;
				//���򣬽��ܴ˵��ʣ���������Ӧtoken������д���ű�
				else {
					filePointer=rfile.getFilePointer()-2;//��¼�ļ�ָ��
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
			//����һ���ַ�Ϊ���֣������Ϊ���ֳ���	
			else if(isNumber(ch)) {
				DFA.nconDFA dfa=d.new nconDFA();
				float num;
				while(nowState!=4&&nowState!=6&&nowState!=0&&rfile.getFilePointer()!=l+1) {
					nowState=dfa.getState(nowState,dfa.getVn(ch),ch);
					str=str+ch;
					//��ֹ�ļ�ָ��Խ��
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
					}
				//������ʱ�����ս�̬���򲻽��ܴ˵���
				if(nowState!=4&&nowState!=6) token=null;
				//���򣬽��ܴ˵��ʣ���������Ӧtoken������д������
				else {
					filePointer=rfile.getFilePointer()-2;//��¼�ļ�ָ��
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
					//��ֹ�ļ�ָ��Խ��
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}	
					}
				//������ʱ�����ս�̬���򲻽��ܴ˵���
				if(nowState!=5) token=null;
				//���򣬽��ܴ˵��ʣ���������Ӧtoken������д�ַ�������
				else {
					filePointer=rfile.getFilePointer()-2;//��¼�ļ�ָ��
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
					//��ֹ�ļ�ָ��Խ��
					if(rfile.getFilePointer()!=l) {
						ch=(char) rfile.readByte();	
					}
					}
				//������ʱ�����ս�̬���򲻽��ܴ˵���
				if(nowState!=2) token=null;
				//���򣬽��ܴ˵��ʣ���������Ӧtoken������д�ַ���������
				else {
					filePointer=rfile.getFilePointer()-2;//��¼�ļ�ָ��
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
				str=str+ch;
				//��ֹ�ļ�ָ��Խ��
				if(rfile.getFilePointer()!=l) {
					ch=(char) rfile.readByte();	
				}
				if(ch=='='||ch=='*'||ch=='&'||ch=='|'||ch=='<'||ch=='>'||ch=='/'||ch=='\\'||ch=='+'||ch=='-') {
					str=str+ch;
					//��˫Ŀ�����������Ӧtoken��
					if(isDelimiter(str)!=0) {
						filePointer=rfile.getFilePointer();//��¼�ļ�ָ��
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
					filePointer=rfile.getFilePointer()-1;//��¼�ļ�ָ��
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
	//���ļ������ӽ�������#
	public void addEnd() {
		FileWriter fw;
		try {
			fw = new FileWriter(this.fileName,true);
			PrintWriter pw=new PrintWriter(fw);
			pw.println("#");
			pw.close ();
			fw.close ();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	
}

