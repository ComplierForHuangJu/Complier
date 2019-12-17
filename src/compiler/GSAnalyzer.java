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
	 * isSemTrue---语义是否正确，弱不正确则停止分析
	 */
	public ArrayList<Quat> QuatList= new ArrayList<Quat>();
	private LexicalAnalyzer lex;
	private  Stack<String> SynStack = new Stack<String>();
	private  Stack<Token> SemStack = new Stack<Token>();
	
	public Map<String,SymbolTable> allSymList = new HashMap<String, SymbolTable>();
	
	public SymbolTable currentSym;
	private int addr = 0;
	
	private boolean isSemTrue = true;

	
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
				if(!isSemTrue)break;
				System.out.println("操作："+s1);
			}
			else if(s1.equals(s2) && s1.equals("#")) {
				System.out.print("ACCEPTED!");
				Flag = false;
				return;
			}
			//匹配栈顶符号成功
			else if(s1.equals(s2)) {
				//若上一个读取的token是标识符/关键字/大于小于等，则记录以便压栈
				//if(T.gettype().name().equals("i")||T.gettype().name().equals("k")||s2.equals(">")||s2.equals("<")||s2.equals(">=")||
					//	s2.equals("<=")||s2.equals("==")||s2.equals("!="))
					it=T;
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
		if(!isSemTrue)break;
		}
	}

	//根据不同语义动作s，执行相应语义动作函数
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
	
	//判断是否是已定义的类型(int,float,char)
	public void isType(Token t) {
		  if(!(t.getSvalue().equals("int")||t.getSvalue().equals("float")||t.getSvalue().equals("char"))) {
			  isSemTrue=false;
			  System.out.println("类型为定义！");
		  }  
	}
	//定义且不赋值语句
	public void varDefinition1() {
		String name ;
		int type = 0;
		//将变量名从语义栈中弹出,并填标识符表
		Token to = this.SemStack.pop();
		name = to.getSvalue();
		//判断此变量是否重复定义
		if(!currentSym.IDsymbol.containsKey(name)){
			currentSym.IDsymbol.put(name,new Symbol(name));
			//将变量类型从语义栈中弹出，并填标识符表中此标识符的类型
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
			//填入种类（变量）
			currentSym.IDsymbol.get(name).cate = 4;
			//填入相对偏移量	
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
			System.out.println("变量"+name+"重复定义！");
		}
	}
	
	//将标识符压语义栈
	public void push(Token t) {
		this.SemStack.push(t);
	}
	//定义且赋值语句
	public void varDefinition2() {
		Token t1;//记录栈顶
		Token t2;//记录次栈顶
		//将栈顶表达式的token弹出
		t1 = SemStack.pop();
		//记录次栈顶标识符的token
		t2 = SemStack.peek();
		//将定义的标识符的相关信息填符号表
		varDefinition1();
		//生成赋值四元式
		if(isSemTrue) {
			Token t = new Token();
			t.setSvalue("_");
			currentSym.quaters.add(new Quat("=",t1,t,t2));
		}
	}
	//生成if开始四元式
	public void ifBegin() {
		Token t;
		//将判断结果从语义栈顶弹出
		t = SemStack.pop();
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("if",t,te,te));
	}
	//生成if结束四元式
	public void ifEnd() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("ie",te,te,te));
	}
	//生成else开始四元式
	public void elseBegin() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("el",te,te,te));
	}
	//生成while开始四元式
	public void whileBegin() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("wh",te,te,te));
	}
	//生成if开始四元式
	public void dowhile() {
		Token t;
		//将判断结果从语义栈顶弹出
		t = SemStack.pop();
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("do",t,te,te));
	}
	//生成while结束四元式
	public void whileEnd() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("we",te,te,te));
	}
	//生成for循环开始四元式
	public void forJudge() {
		Token t;
		//将判断结果从语义栈顶弹出
		t = SemStack.pop();
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("forF",t,te,te));
		currentSym.quaters.add(new Quat("forT",te,te,te));
	}
	//生成for开始执行四元式
	public void dofor() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("T",te,te,te));
	}
	//生成for结束四元式
	public void forEnd() {
		Token te = new Token();
		te.setSvalue("_");
		currentSym.quaters.add(new Quat("fe",te,te,te));
	}
	//将语义栈栈顶元素出栈
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
			System.out.println("变量"+t.getSvalue()+"在使用时未初始化！");
		}
	}
	//判断juge结果，生成四元式，并将结果压栈
	public void jugeResult() {
		Token t1 = SemStack.pop();
		Token t2 = SemStack.pop();
		Token t3 = SemStack.pop();
		
		//若t1,t3是char类型，则将其值转化为float型
		float v1,v2;
		if(t1.gettype()==Token.TYPE.cc) v1 = (float)Integer.parseInt(t1.getSvalue());
		else if(t1.gettype()==Token.TYPE.inc) v1 = t1.getIvalue();
		else v1 = t1.getFvalue();
		if(t2.gettype()==Token.TYPE.cc) v2 = (float)Integer.parseInt(t2.getSvalue());
		else if(t2.gettype()==Token.TYPE.inc) v2 = t2.getIvalue();
		else v2 = t2.getFvalue();
		
		Token t = new Token();//临时变量t需要加入标识符表吗，若加入，怎样区分临时变量与非临时变量
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
