package compiler;

import java.util.*;



public class GSAnalyzer {
	/*
	 * QuatList---存放生成的四元式
	 * lex---词法分析器
	 * SynStack---语法栈
	 * SemStack---语义栈
	 * allSymList---符号表集合
	 * currentSym---当前符号表
	 * addr---相对偏移量
	 */
	public ArrayList<Quat> QuatList= new ArrayList<Quat>();
	private LexicalAnalyzer lex;
	private  Stack<String> SynStack = new Stack<String>();
	private  Stack<Token> SemStack = new Stack<Token>();
	
	public Map<String,SymbolTable> allSymList;
	//public ArrayList<TypeTable> typeList; 类型表存在是为了知道一个自定义类型的长度，如果没有结构体，那么只需要知道数组的长度，而数组的信息也会在所处函数的SymbolTable当中存储，因此不需要此表
	//public ArrayList<FuncTable> funcList;
	
	public SymbolTable currentSym;
	private int addr = 0;

	
	//判断一个字符串是不是全是数字组成
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
	
	//初始化词法分析器,初始化类型表前三项
	public GSAnalyzer(String name)
	{
		//初始化词法分析器
		LexicalAnalyzer l=new LexicalAnalyzer(name);
		lex=l;
		//初始化类型表
		typeList.add(new TypeTable(1,(Integer) null));
		typeList.add(new TypeTable(2,(Integer) null));
		typeList.add(new TypeTable(3,(Integer) null));
	}
	
	//初始化语法分析栈
	public void initSynStack()//初始化语法栈
	{
		SynStack.push("#");
		SynStack.push(Grammar.Start);//把开始符号加入语法栈
	}
	
	//输出语法栈中所有元素
	public void showSynStack() {
		for(String a : this.SynStack) {
			System.out.print(a);
		}
	}
	
	//输出所有生成的四元式
	public void showQuatList() {
		for(int i=0 ; i < QuatList.size(); i++ ) {
			System.out.println("("+QuatList.get(i).opc+","+QuatList.get(i).ope1.getSvalue()+","+QuatList.get(i).ope2.getSvalue()+","+QuatList.get(i).res.getSvalue()+")");
	     }
	}
	
	public void Analyze() {
		lex.addEnd();
		initSynStack();//初始化语法分析栈
		Token T = new Token();
		T = lex.scan();//当前待匹配字符的Token
		boolean Flag = true; 
		Token it=null;//记录上一个标识符的token
		while(Flag) {
		while (SynStack.size() >0 ){
			showSynStack();
			System.out.print("          ");
			String s2 = null;
	
			//是#
			if(T.getLastState() == 36) {
				System.out.print(T.getSvalue());
				s2 = T.getSvalue();
			}
			//是标识符
			else if(T.gettype().name().equals("k")||T.gettype().name().equals("i")||T.gettype().name().equals("cc")
					||T.gettype().name().equals("sc")||T.gettype().name().equals("inc")||T.gettype().name().equals("fnc")) {
		    	System.out.print(T.gettype().name());
		    	s2 = T.gettype().name();
			}
			//是界符需要判断其值
			else if(T.gettype().name().equals("p")) {
				 System.out.print(T.getSvalue());
			     s2 =T.getSvalue();		
			}
			
			String s1 = SynStack.pop();
			//若栈顶元素是语义动作，则执行相应动作
			if(isNum(s1)) {
				int n = Integer.parseInt(s1);
				Token tem;
				semTran(n,it);
				System.out.println("操作："+s1);
			}
			else if(s1.equals(s2) && s1.equals("#")) {
				System.out.print("ACCEPTED!");
				Flag = false;
				return;
			}
			//匹配栈顶符号成功
			else if(s1.equals(s2)) {
				//若上一个读取的token是标识符/关键字，则记录以便压栈
				if(T.gettype().name().equals("i")||T.gettype().name().equals("k"))it=T;
				T = lex.scan();//读下一个待匹配字符的Token
				System.out.println("匹配："+s1);
			}
			//分析表中有对应产生式可进行推导
			else if(Grammar.Table[Grammar.getVnAIndex(s1)][Grammar.getVdAIndex(s2)] != 0) {
				int tg = Grammar.Table[Grammar.getVnAIndex(s1)][Grammar.getVdAIndex(s2)];
				if(!Grammar.W.get(tg-1).isRightNull()) {
					for(int i=Grammar.W.get(tg-1).getRT().size()-1;i>=0;i--) {//逆序压栈
						SynStack.push(Grammar.W.get(tg-1).getRT().get(i));
					}
				}
				System.out.print("推导：");
				Grammar.W.get(tg-1).showGrammar();
			}
			else {
				System.out.print("Error!");
				return;
			}
		  }
		}
	}

	//根据不同语义动作s，执行相应语义动作函数
	public void semTran(int s,Token t) {
		switch(s) {
		case 0 : proBegin();break;
		case 1 :proEnd();break;
		case 2 :varDefinition();break;
		case 3 :push(t);break;
		case 4 :push(t);break;
		}
		
	}

	//程序开始
	public void proBegin() {
		Token t=new Token();
		t.setSvalue("_");
		Quat q=new Quat("ProBegin",t,t,t);
	}
	//程序结束
	public void proEnd() {
		Token t=new Token();
		t.setSvalue("_");
		Quat q=new Quat("ProEnd",t,t,t);
	}
	//定义且不赋值语句
	public void varDefinition() {
		//将变量填入符号表(名字、种类、类型、相对地址)
		Token to = this.SemStack.pop();
		SymbolTable s = new SymbolTable();
		//填入名字
		s.name=to.getSvalue();
		//填入种类，并填类型表指针
		to = this.SemStack.pop();
		switch(to.getSvalue()) {
		case "int": s.type=1;break;
		case "float" : s.type = 2;break;
		case "char" : s.type = 3;break;
		default: s.type = 0;break;
		}
		//填入种类（变量）
		s.cate=4;
		//填入相对偏移量
		
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
	
	//将标识符压语义栈
	public void push(Token t) {
		this.SemStack.push(t);
	}
	//定义且赋值语句
	
	
}
