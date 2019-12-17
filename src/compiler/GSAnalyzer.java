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
	 * isSemTrue---�����Ƿ���ȷ��������ȷ��ֹͣ����
	 */
	public ArrayList<Quat> QuatList= new ArrayList<Quat>();
	private LexicalAnalyzer lex;
	private  Stack<String> SynStack = new Stack<String>();
	private  Stack<Token> SemStack = new Stack<Token>();
	
	public Map<String,SymbolTable> allSymList = new HashMap<String, SymbolTable>();
	
	public SymbolTable currentSym;
	private int addr = 0;
	
	private boolean isSemTrue = true;

	
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
				if(!isSemTrue)break;
				System.out.println("������"+s1);
			}
			else if(s1.equals(s2) && s1.equals("#")) {
				System.out.print("ACCEPTED!");
				Flag = false;
				return;
			}
			//ƥ��ջ�����ųɹ�
			else if(s1.equals(s2)) {
				//����һ����ȡ��token�Ǳ�ʶ��/�ؼ���/����С�ڵȣ����¼�Ա�ѹջ
				//if(T.gettype().name().equals("i")||T.gettype().name().equals("k")||s2.equals(">")||s2.equals("<")||s2.equals(">=")||
					//	s2.equals("<=")||s2.equals("==")||s2.equals("!="))
					it=T;
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
		if(!isSemTrue)break;
		}
	}

	//���ݲ�ͬ���嶯��s��ִ����Ӧ���嶯������
	public void semTran(int s,Token t) {
		switch(s) {
		case 0 : 
			isType(t);
			if(isSemTrue)push(t);
			break;
		case 1 :
			push(t);
			break;
		case 2 :
			varDefinition1();
			break;
		case 3 :
			varDefinition2();
			break;
		case 4 :
			ifBegin();
			break;
		case 5 :
			ifEnd();
			break;
		case 6 :
			elseBegin();
			break;
		case 7 :
			whileBegin();
			break;
		case 8 :
			dowhile();
			break;
		case 9 :
			whileEnd();
			break;
		case 10 :
			forJudge();
			break;
		case 11 :
			dofor();
			break;
		case 12 :
			forEnd();
			break;
		case 13 :
			popSem();
			break;
		case 14 :
			juge1(t);
			break;
		case 15 :
			jugeResult();
			break;
		}
		
	}
	
	//�ж��Ƿ����Ѷ��������(int,float,char)
	public void isType(Token t) {
		  if(!(t.getSvalue().equals("int")||t.getSvalue().equals("float")||t.getSvalue().equals("char"))) {
			  isSemTrue=false;
			  System.out.println("����Ϊ���壡");
		  }  
	}
	//�����Ҳ���ֵ���
	public void varDefinition1() {
		String name ;
		int type = 0;
		//��������������ջ�е���,�����ʶ����
		Token to = this.SemStack.pop();
		name = to.getSvalue();
		//�жϴ˱����Ƿ��ظ�����
		if(!currentSym.IDsymbol.containsKey(name)){
			currentSym.IDsymbol.put(name,new Symbol(name));
			//���������ʹ�����ջ�е����������ʶ�����д˱�ʶ��������
			to =  this.SemStack.pop();
			switch(to.getSvalue()) {
			case "int" :
				type = 0;
				break;
			case "float" :
				type = 1;
				break;
			case "char" :
				type = 2;
				break;
			}
			currentSym.IDsymbol.get(name).type = type;
			//�������ࣨ������
			currentSym.IDsymbol.get(name).cate = 4;
			//�������ƫ����	
			switch(to.getSvalue()) {
			case "int": 
				currentSym.IDsymbol.get(name).addr = this.addr;
				addr = addr+4;
				break;
			case "float" :
				currentSym.IDsymbol.get(name).addr = this.addr;
				addr = addr+4;
				break;
			case "char" : 
				currentSym.IDsymbol.get(name).addr = this.addr;
				addr = addr+1;
				break;
			}
		}
		else {
			isSemTrue = false;
			System.out.println("����"+name+"�ظ����壡");
		}
	}
	
	//����ʶ��ѹ����ջ
	public void push(Token t) {
		this.SemStack.push(t);
	}
	//�����Ҹ�ֵ���
	public void varDefinition2() {
		Token t1;//��¼ջ��
		Token t2;//��¼��ջ��
		//��ջ�����ʽ��token����
		t1 = SemStack.pop();
		//��¼��ջ����ʶ����token
		t2 = SemStack.peek();
		//������ı�ʶ���������Ϣ����ű�
		varDefinition1();
		//���ɸ�ֵ��Ԫʽ
		if(isSemTrue) {
			Token t = new Token();
			t.setSvalue("_");
			currentSym.quaters.add(new Quat("=",t1,t,t2));
		}
	}
	//����if��ʼ��Ԫʽ
	public void ifBegin() {
		Token t;
		//���жϽ��������ջ������
		t = SemStack.pop();
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("if",t,te,te));
	}
	//����if������Ԫʽ
	public void ifEnd() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("ie",te,te,te));
	}
	//����else��ʼ��Ԫʽ
	public void elseBegin() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("el",te,te,te));
	}
	//����while��ʼ��Ԫʽ
	public void whileBegin() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("wh",te,te,te));
	}
	//����if��ʼ��Ԫʽ
	public void dowhile() {
		Token t;
		//���жϽ��������ջ������
		t = SemStack.pop();
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("do",t,te,te));
	}
	//����while������Ԫʽ
	public void whileEnd() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("we",te,te,te));
	}
	//����forѭ����ʼ��Ԫʽ
	public void forJudge() {
		Token t;
		//���жϽ��������ջ������
		t = SemStack.pop();
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("forF",t,te,te));
		currentSym.quaters.add(new Quat("forT",te,te,te));
	}
	//����for��ʼִ����Ԫʽ
	public void dofor() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("T",te,te,te));
	}
	//����for������Ԫʽ
	public void forEnd() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("fe",te,te,te));
	}
	//������ջջ��Ԫ�س�ջ
	public void popSem() {
		SemStack.pop();
	}
	//
	public void juge1(Token t) {
		if(currentSym.IDsymbol.get(t.getSvalue()).isInit) {
			push(t);
		}
		else {
			isSemTrue = false;
			System.out.println("����"+t.getSvalue()+"��ʹ��ʱδ��ʼ����");
		}
	}
	//�ж�juge�����������Ԫʽ���������ѹջ
	public void jugeResult() {
		Token t1 = SemStack.pop();
		Token t2 = SemStack.pop();
		Token t3 = SemStack.pop();
		
		//��t1,t3��char���ͣ�����ֵת��Ϊfloat��
		float v1,v2;
		if(t1.gettype()==Token.TYPE.cc) v1 = (float)Integer.parseInt(t1.getSvalue());
		else if(t1.gettype()==Token.TYPE.inc) v1 = t1.getIvalue();
		else v1 = t1.getFvalue();
		if(t2.gettype()==Token.TYPE.cc) v2 = (float)Integer.parseInt(t2.getSvalue());
		else if(t2.gettype()==Token.TYPE.inc) v2 = t2.getIvalue();
		else v2 = t2.getFvalue();
		
		Token t = new Token();//��ʱ����t��Ҫ�����ʶ�����������룬����������ʱ���������ʱ����
		switch(t2.getSvalue()) {
		case ">" :
			if(v2>v1) t.setIvalue(1);
			else t.setIvalue(0);
			currentSym.quaters.add(new Quat(">",t3,t1,t));
			SemStack.add(t);
		case "<" :
			if(v2<v1) t.setIvalue(1);
			else t.setIvalue(0);
			currentSym.quaters.add(new Quat("<",t3,t1,t));
			SemStack.add(t);
		case ">=" :
			if(v2>=v1) t.setIvalue(1);
			else t.setIvalue(0);
			currentSym.quaters.add(new Quat(">=",t3,t1,t));
			SemStack.add(t);
		case "<=" :
			if(v2<=v1) t.setIvalue(1);
			else t.setIvalue(0);
			currentSym.quaters.add(new Quat("<=",t3,t1,t));
			SemStack.add(t);
		case "==" :
			if(v2==v1) t.setIvalue(1);
			else t.setIvalue(0);
			currentSym.quaters.add(new Quat("==",t3,t1,t));
			SemStack.add(t);
		case "!=" :
			if(v2<v1) t.setIvalue(1);
			else t.setIvalue(0);
			currentSym.quaters.add(new Quat("!=",t3,t1,t));
			SemStack.add(t);
		}
	}
	
}
