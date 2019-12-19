package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import compiler.Token.TYPE;

public class CodeProducerTest {

	/* 目标代码区 */
	static ArrayList<String> OBJ;
	
	/* 优化后的四元式序列 */
	static ArrayList<Quat> QT;
	
	static ArrayList<String> DEFINE = new ArrayList<String>();
	
	/* 活跃信息表 */
	static HashMap<String, Boolean> SYMBL;
	
	/* 寄存器状态描述表 */
	static HashMap<String, ActiveTable> RDL;
	
	/* */
	static SymbolTable table = new SymbolTable();
	
	/* 语义栈 */
	static Stack<Integer> SEM;
	
	/**
	 * 初始化容器信息
	 */
	public static void init() {
		OBJ = new ArrayList<String>();
		//QT = Optimize._opQuat;
		//设置三个寄存器
		RDL = new HashMap<String, ActiveTable>();
		RDL.put("AX", new ActiveTable("0",false));
		SEM = new Stack<Integer>();
		//SEM.push(5);
		QT = new ArrayList<Quat>();
		Token token0 = new Token(TYPE.i,"a");
		Token token1 = new Token(TYPE.i,"b");
		Token token2 = new Token(TYPE.i,"t1");
		Token token3 = new Token(TYPE.i,"t2");
		Token token4 = new Token(TYPE.i,"t3");
		Token token5 = new Token(TYPE.i,"c");
		Token token6 = new Token(TYPE.i,"x");
		//QT.add(new Quat(">",token0,token1,token2));
		//QT.add(new Quat("if",token2,new Token("_"),new Token("_")));
		QT.add(new Quat("+",token0,token1,token3));
		QT.add(new Quat("*",token3,token5,token6));
		//QT.add(new Quat("el",new Token("_"),new Token("_"),new Token("_")));
//		QT.add(new Quat("*",token0,token1,token4));
//		QT.add(new Quat("-",new Token(TYPE.inc,"5"),token4,token6));
//		QT.add(new Quat("ie",new Token("_"),new Token("_"),new Token("_")));
		SYMBL = new ActiveInf().creatInf(QT);
		
	}
	
	/**
	 * 生成变量声明目标代码
	 */
	public static void creatVaribleCode() {
		ArrayList<String> list = new ArrayList<String>();
		for(String str : SYMBL.keySet()) {
			list.add(str);
			}

		for(String str : list)
			DEFINE.add(str + " DB "+"["+str+"]"+" DUP(0)");

			
	}
	
	/**
	 * 代码生成
	 * @param op  操作符
	 * @param R   寄存器名字
	 * @param B	     变量
	 */
	public  static void Code(String op,String R,String B) {
		//如果符号表中存在当前变量
		switch(op) {
			case "LD": OBJ.add("MOV "+R+", ["+B+"]");break;
			case "ST":OBJ.add("MOV "+R+", ["+B+"]");break;
			case "+":OBJ.add("ADD "+R+", ["+B+"]");break;
			case "-":OBJ.add("SUB "+R+", ["+B+"]");break;
			case "*":OBJ.add("MUL "+R+", ["+B+"]");break;
			case "/":OBJ.add("DIV "+R+", ["+B+"]");break;
			case "end":OBJ.add("INT 21H");break;
			}
		}
	
	/**
	 * 代码生成
	 * @param op  操作符
	 * @param R   寄存器名字
	 * @param B	     变量
	 */
	public  static void Code(String op,String R,int B) {
		//如果符号表中存在当前变量
		switch(op) {
			case "LD": OBJ.add("MOV "+R+","+B);break;
			case "+":OBJ.add("ADD "+R+","+B);break;
			case "-":OBJ.add("SUB "+R+","+B);break;
			case "*":OBJ.add("MUL "+R+","+B);break;
			case "/":OBJ.add("DIV "+R+","+B);break;
			case "end":OBJ.add("INT 21H");break;
			}
		}
	
	/**
	 * 将地址pk反填到pi中
	 * @param i
	 * @param j
	 */
	public static void Back(int i,int j) {
		OBJ.get(i).concat(String.valueOf(j));
	}
	/**
	 * 实际代码生成的相关逻辑，管控寄存器RDL组等信息
	 */
	public static void produce() {
		for(int i = 0; i < QT.size();i++) {
			Quat quat = QT.get(i);
			/**
			 * 根据不同的操作符有不同的处理逻辑
			 */
			switch(quat.opc) {
				case "=":{
							//先判断寄存器是否被占用
							if(!RDL.get("AX").isActive()) {
								//如果当前寄存器被操作数B占用
								if(RDL.get("AX").getName().equals(quat.ope1.getSvalue()))
								{
									//B是活跃信息
									if(quat.isActiveOpe1())
										Code("ST",quat.ope1.getSvalue(),"AX");
								}
								//寄存器被其它活跃信息占用
								else
								{
									//活跃变量需要存入
									if(RDL.get("AX").isActive())
									{
										Code("ST",RDL.get("AX").getName(),"AX");
										Code("LD","AX",quat.ope1.getSvalue());
									}
								}
							}else
								Code("LD","AX",quat.ope1.getSvalue());
							//将当前结果变量等级到RDL中
							RDL.put("AX", new ActiveTable(quat.res.getSvalue(),quat.isActiveRes()));
							break;
						}
				case "if":{
					//先判断寄存器是否被占用
					if(!RDL.get("AX").getName().equals("0")) {
						//如果当前寄存器被操作数B占用
						if(RDL.get("AX").getName().equals(quat.ope1.getSvalue()))
						{
							//B是活跃信息
							if(quat.isActiveOpe1())
								Code("ST",quat.ope1.getSvalue(),"AX");
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new ActiveTable("0",false));
								
						}
						//寄存器被其它活跃信息占用
						else
						{
							//活跃变量需要存入
							if(RDL.get("AX").isActive())
								Code("ST",RDL.get("AX").getName(),"AX");
								
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new ActiveTable("0",false));
						}
					}else
						{
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");
							SEM.push(i);
						}
					//将当前结果变量等级到RDL中
					RDL.put("AX", new ActiveTable(quat.res.getSvalue(),quat.isActiveRes()));
					System.out.println("执行if后：");
					System.out.println(RDL.get("AX").getName() + RDL.get("AX").isActive());
					break;
				}
				
				case "el":{
					if(RDL.get("AX").isActive())
						Code("ST",quat.ope1.getSvalue(),"AX");
					int K = SEM.pop();
					Back(K,i+2);
					OBJ.add("JMP");
					SEM.push(i);
					break;
					}
				
				case "ie":{
					if(RDL.get("AX").isActive())
						Code("ST",RDL.get("AX").getName(),"AX");
					int K = SEM.pop();
					Back(K,i+1);
					break;
				}
				
				case "wh":SEM.push(i);break;
				
				case "do":{
					//先判断寄存器是否被占用
					if(!RDL.get("AX").equals("0")) {
						//如果当前寄存器被操作数B占用
						if(RDL.get("AX").getName().equals(quat.ope1.getSvalue()))
						{
							//B是活跃信息
							if(quat.isActiveOpe1())
								Code("ST",quat.ope1.getSvalue(),"AX");
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new ActiveTable("0",false));
								
						}
						//寄存器被其它活跃信息占用
						else
						{
							//活跃变量需要存入
							if(RDL.get("AX").isActive())
								Code("ST","AX",RDL.get("AX").getName());
								
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new ActiveTable("0",false));
						}
					}else
						{
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");
							SEM.push(i);
						}
					//将当前结果变量等级到RDL中
					RDL.put("AX", new ActiveTable(quat.res.getSvalue(),quat.isActiveRes()));
					break;
				}
				
				case "we":{
					//活跃变量需要存入
					if(RDL.get("AX").isActive())
						Code("ST",RDL.get("AX").getName(),"AX");
					RDL.put("AX", new ActiveTable("0",false));
					int K = SEM.pop();
					Back(K,i+2);
					OBJ.add("JMP "+K);
					break;
				}
				
			default:{
				//先判断寄存器是否被占用
				if(!RDL.get("AX").getName().equals("0")) {
					//如果当前寄存器被操作数B占用
					if(RDL.get("AX").getName().equals(quat.ope1.getSvalue()))
					{
						//B是活跃信息
						if(quat.isActiveOpe1())
							Code("ST",quat.ope1.getSvalue(),"AX");
					}
					//寄存器被其它活跃信息占用
					else
					{
						//活跃变量需要存入
						if(RDL.get("AX").isActive())
						{
							Code("ST",RDL.get("AX").getName(),"AX");
							Code("LD","AX",quat.ope1.getSvalue());
						}
					}
				}else
					Code("LD","AX",quat.ope1.getSvalue());
						
				
				Code(quat.opc,"AX",quat.ope2.getSvalue());
				//将当前结果变量等级到RDL中
				RDL.put("AX", new ActiveTable(quat.res.getSvalue(),quat.isActiveRes()));
				break;
			}
				
			}
			
			
		}
	}
	public static void main(String[] args) {
		init();
		System.out.println("四元式信息：");
		for(Quat quat : QT) {
			System.out.println("("+quat.opc+","+quat.ope1.getSvalue()+"("+quat.isActiveOpe1()+"),"
					+quat.ope2.getSvalue()+"("+quat.isActiveOpe2()+"),"
					+quat.res.getSvalue()+"("+quat.isActiveRes()+") )"
					);
		}
		creatVaribleCode();
		System.out.println("变量声明的目标代码：");
		for(String str : DEFINE) {
			System.out.println(str);
		}
		
		produce();
		System.out.println("目标代码：");
		for(String str : OBJ){
			System.out.println(str);
		}

	}

}
