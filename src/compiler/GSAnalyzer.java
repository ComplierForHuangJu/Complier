package compiler;

import java.util.*;



public class GSAnalyzer {
	/*
	 * QuatList---������ɵ���Ԫʽ
	 * lex---�ʷ�������
	 * SynStack---�﷨ջ
	 * SemStack---����ջ
	 * allSymList---���ű���
	 * currentSym---��ǰ���ű�
	 * addr---���ƫ����
	 */
	public ArrayList<Quat> QuatList= new ArrayList<Quat>();
	private LexicalAnalyzer lex;
	private  Stack<String> SynStack = new Stack<String>();
	private  Stack<Token> SemStack = new Stack<Token>();
	
	public Map<String,SymbolTable> allSymList;
	//public ArrayList<TypeTable> typeList; ���ͱ������Ϊ��֪��һ���Զ������͵ĳ��ȣ����û�нṹ�壬��ôֻ��Ҫ֪������ĳ��ȣ����������ϢҲ��������������SymbolTable���д洢����˲���Ҫ�˱�
	//public ArrayList<FuncTable> funcList;
	
	public SymbolTable currentSym;
	private int addr = 0;

	
	//�ж�һ���ַ����ǲ���ȫ���������
	public boolean isNum(String a) {
		boolean b = true;
		//int b = Integer.parseInt(a);
		for(int i=0;i<a.length();i++)
		{
			if(!Character.isDigit(a.charAt(i)))
				b = false;
		}
		return b;
	}
	
	//��ʼ���ʷ�������,��ʼ�����ͱ�ǰ����
	public GSAnalyzer(String name)
	{
		//��ʼ���ʷ�������
		LexicalAnalyzer l=new LexicalAnalyzer(name);
		lex=l;
		//��ʼ�����ͱ�
		typeList.add(new TypeTable(1,(Integer) null));
		typeList.add(new TypeTable(2,(Integer) null));
		typeList.add(new TypeTable(3,(Integer) null));
	}
	
	//��ʼ���﷨����ջ
	public void initSynStack()//��ʼ���﷨ջ
	{
		SynStack.push("#");
		SynStack.push(Grammar.Start);//�ѿ�ʼ���ż����﷨ջ
	}
	
	//����﷨ջ������Ԫ��
	public void showSynStack() {
		for(String a : this.SynStack) {
			System.out.print(a);
		}
	}
	
	//����������ɵ���Ԫʽ
	public void showQuatList() {
		for(int i=0 ; i < QuatList.size(); i++ ) {
			System.out.println("("+QuatList.get(i).opc+","+QuatList.get(i).ope1.getSvalue()+","+QuatList.get(i).ope2.getSvalue()+","+QuatList.get(i).res.getSvalue()+")");
	     }
	}
	
	public void Analyze() {
		lex.addEnd();
		initSynStack();//��ʼ���﷨����ջ
		Token T = new Token();
		T = lex.scan();//��ǰ��ƥ���ַ���Token
		boolean Flag = true; 
		Token it=null;//��¼��һ����ʶ����token
		while(Flag) {
		while (SynStack.size() >0 ){
			showSynStack();
			System.out.print("          ");
			String s2 = null;
	
			//��#
			if(T.getLastState() == 36) {
				System.out.print(T.getSvalue());
				s2 = T.getSvalue();
			}
			//�Ǳ�ʶ��
			else if(T.gettype().name().equals("k")||T.gettype().name().equals("i")||T.gettype().name().equals("cc")
					||T.gettype().name().equals("sc")||T.gettype().name().equals("inc")||T.gettype().name().equals("fnc")) {
		    	System.out.print(T.gettype().name());
		    	s2 = T.gettype().name();
			}
			//�ǽ����Ҫ�ж���ֵ
			else if(T.gettype().name().equals("p")) {
				 System.out.print(T.getSvalue());
			     s2 =T.getSvalue();		
			}
			
			String s1 = SynStack.pop();
			//��ջ��Ԫ�������嶯������ִ����Ӧ����
			if(isNum(s1)) {
				int n = Integer.parseInt(s1);
				Token tem;
				semTran(n,it);
				System.out.println("������"+s1);
			}
			else if(s1.equals(s2) && s1.equals("#")) {
				System.out.print("ACCEPTED!");
				Flag = false;
				return;
			}
			//ƥ��ջ�����ųɹ�
			else if(s1.equals(s2)) {
				//����һ����ȡ��token�Ǳ�ʶ��/�ؼ��֣����¼�Ա�ѹջ
				if(T.gettype().name().equals("i")||T.gettype().name().equals("k"))it=T;
				T = lex.scan();//����һ����ƥ���ַ���Token
				System.out.println("ƥ�䣺"+s1);
			}
			//���������ж�Ӧ����ʽ�ɽ����Ƶ�
			else if(Grammar.Table[Grammar.getVnAIndex(s1)][Grammar.getVdAIndex(s2)] != 0) {
				int tg = Grammar.Table[Grammar.getVnAIndex(s1)][Grammar.getVdAIndex(s2)];
				if(!Grammar.W.get(tg-1).isRightNull()) {
					for(int i=Grammar.W.get(tg-1).getRT().size()-1;i>=0;i--) {//����ѹջ
						SynStack.push(Grammar.W.get(tg-1).getRT().get(i));
					}
				}
				System.out.print("�Ƶ���");
				Grammar.W.get(tg-1).showGrammar();
			}
			else {
				System.out.print("Error!");
				return;
			}
		  }
		}
	}

	//���ݲ�ͬ���嶯��s��ִ����Ӧ���嶯������
	public void semTran(int s,Token t) {
		switch(s) {
		case 0 : proBegin();break;
		case 1 :proEnd();break;
		case 2 :varDefinition();break;
		case 3 :push(t);break;
		case 4 :push(t);break;
		}
		
	}

	//����ʼ
	public void proBegin() {
		Token t=new Token();
		t.setSvalue("_");
		Quat q=new Quat("ProBegin",t,t,t);
	}
	//�������
	public void proEnd() {
		Token t=new Token();
		t.setSvalue("_");
		Quat q=new Quat("ProEnd",t,t,t);
	}
	//�����Ҳ���ֵ���
	public void varDefinition() {
		//������������ű�(���֡����ࡢ���͡���Ե�ַ)
		Token to = this.SemStack.pop();
		SymbolTable s = new SymbolTable();
		//��������
		s.name=to.getSvalue();
		//�������࣬�������ͱ�ָ��
		to = this.SemStack.pop();
		switch(to.getSvalue()) {
		case "int": s.type=1;break;
		case "float" : s.type = 2;break;
		case "char" : s.type = 3;break;
		default: s.type = 0;break;
		}
		//�������ࣨ������
		s.cate=4;
		//�������ƫ����
		
		switch(to.getSvalue()) {
		case "int": 
		{
			s.addr=this.addr;
			addr = addr+4;
			break;
		}
		case "float" : {
			s.addr=this.addr;
			addr = addr+4;
			break;
		}
		case "char" : {
			s.addr=this.addr;
			addr = addr+1;
			break;
		}
		}
		symList.add(s);
	}
	
	//����ʶ��ѹ����ջ
	public void push(Token t) {
		this.SemStack.push(t);
	}
	//�����Ҹ�ֵ���
	
	
}
