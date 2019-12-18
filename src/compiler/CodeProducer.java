package compiler;
/**
 * 
 * 项目名称：Complier
 * 目标代码生成
 * @author 北溟有鱼。
 * @time 2019年12月16日下午3:18:21
 * @version V1.0
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class CodeProducer {
	
	/* 目标代码区 */
	ArrayList<String> OBJ;
	
	/* 优化后的四元式序列 */
	ArrayList<Quat> QT;
	
	ArrayList<String> DEFINE = new ArrayList<String>();
	
	/* 活跃信息表 */
	HashMap<Token,Character> SYMBL;
	
	/* 寄存器状态描述表 */
	HashMap<String, Token> RDL;
	
	/* */
	SymbolTable table = new SymbolTable();
	
	/* 语义栈 */
	Stack<Integer> SEM;
	
	/**
	 * 初始化容器信息
	 */
	public void init() {
		OBJ = new ArrayList<String>();
		QT = Optimize._opQuat;
		SYMBL = new ActiveInf().creatInf(QT);
		//设置三个寄存器
		RDL = new HashMap<String, Token>();
		RDL.put("AX", new Token("0"));
		//RDL.put("BX", "0");
		//RDL.put("DX", "0");
	}
	
	/**
	 * 生成变量声明目标代码
	 */
	public void creatVaribleCode() {
		ArrayList<Token> list = new ArrayList<Token>();
		for(Token token : SYMBL.keySet()) {
			list.add(token);
			}
		Token[] array = (Token[])list.toArray(new Token[list.size()]);
		
		//根据addr排序
		for(int i = 0; i < list.size() - 1;i++) {
			for(int j = 0; j < list.size() -1 - i;j++) {
				if(table.IDsymbol.get(array[j].getSvalue()).addr > table.IDsymbol.get(array[j+1].getSvalue()).addr) {
					Token temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
			
		}
		
		
		for(int i = 0; i < list.size() - 1;i++) {
			DEFINE.add(array[i].getSvalue() + " DB "+table.IDsymbol.get(array[i + 1].getSvalue()).addr+" DUP(0)");
		}
		
		
			
	}
	
	/**
	 * 代码生成
	 * @param op  操作符
	 * @param R   寄存器名字
	 * @param B	     变量
	 */
	public void Code(String op,String R,String B) {
		//如果符号表中存在当前变量
		switch(op) {
			case "LD": OBJ.add("MOV "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "ST":OBJ.add("MOV "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "+":OBJ.add("ADD "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "-":OBJ.add("SUB "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "*":OBJ.add("MUL "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "/":OBJ.add("DIV "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "end":OBJ.add("INT 21H");break;
			}
		}
	
	/**
	 * 代码生成
	 * @param op  操作符
	 * @param R   寄存器名字
	 * @param B	     变量
	 */
	public void Code(String op,String R,int B) {
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
	public void Back(int i,int j) {
		OBJ.get(i).concat(String.valueOf(j));
	}
	/**
	 * 实际代码生成的相关逻辑，管控寄存器RDL组等信息
	 */
	public void produce() {
		for(int i = 0; i < QT.size() - 1;i++) {
			Quat quat = QT.get(i);
			/**
			 * 根据不同的操作符有不同的处理逻辑
			 */
			switch(quat.opc) {
				case "=":{
							//先判断寄存器是否被占用
							if(!RDL.get("AX").getSvalue().equals("0")) {
								//如果当前寄存器被操作数B占用
								if(RDL.get("AX").getSvalue().equals(quat.ope1.getSvalue()))
								{
									//B是活跃信息
									if(quat.ope1.getIsActive() != 'n')
										Code("ST","AX",quat.ope1.getSvalue());
								}
								//寄存器被其它活跃信息占用
								else
								{
									//活跃变量需要存入
									if(RDL.get("AX").getIsActive() != 'n')
									{
										Code("ST","AX",RDL.get("AX").getSvalue());
										Code("LD","AX",quat.ope1.getSvalue());
									}
								}
							}else
								Code("LD","AX",quat.ope1.getSvalue());
							//将当前结果变量等级到RDL中
							RDL.put("AX", quat.res);
							break;
						}
				case "if":{
					//先判断寄存器是否被占用
					if(!RDL.get("AX").getSvalue().equals("0")) {
						//如果当前寄存器被操作数B占用
						if(RDL.get("AX").getSvalue().equals(quat.ope1.getSvalue()))
						{
							//B是活跃信息
							if(quat.ope1.getIsActive() != 'n')
								Code("ST","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new Token("0"));
								
						}
						//寄存器被其它活跃信息占用
						else
						{
							//活跃变量需要存入
							if(RDL.get("AX").getIsActive() != 'n')
								Code("ST","AX",RDL.get("AX").getSvalue());
								
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new Token("0"));
						}
					}else
						{
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");
							SEM.push(i);
						}
					//将当前结果变量等级到RDL中
					RDL.put("AX", quat.res);
					break;
				}
				
				case "el":{
					if(RDL.get("AX").getIsActive() != 'n')
						Code("ST","AX",RDL.get("AX").getSvalue());
					int K = SEM.pop();
					Back(K,i+2);
					OBJ.add("JMP");
					SEM.push(i);
					break;
					}
				
				case "ie":{
					if(RDL.get("AX").getIsActive() != 'n')
						Code("ST","AX",RDL.get("AX").getSvalue());
					int K = SEM.pop();
					Back(K,i+1);
					break;
				}
				
				case "wh":SEM.push(i);break;
				
				case "do":{
					//先判断寄存器是否被占用
					if(!RDL.get("AX").getSvalue().equals("0")) {
						//如果当前寄存器被操作数B占用
						if(RDL.get("AX").getSvalue().equals(quat.ope1.getSvalue()))
						{
							//B是活跃信息
							if(quat.ope1.getIsActive() != 'n')
								Code("ST","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new Token("0"));
								
						}
						//寄存器被其它活跃信息占用
						else
						{
							//活跃变量需要存入
							if(RDL.get("AX").getIsActive() != 'n')
								Code("ST","AX",RDL.get("AX").getSvalue());
								
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new Token("0"));
						}
					}else
						{
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");
							SEM.push(i);
						}
					//将当前结果变量等级到RDL中
					RDL.put("AX", quat.res);
					break;
				}
				
				case "we":{
					//活跃变量需要存入
					if(RDL.get("AX").getIsActive() != 'n')
						Code("ST","AX",RDL.get("AX").getSvalue());
					RDL.put("AX", new Token("0"));
					int K = SEM.pop();
					Back(K,i+2);
					OBJ.add("JMP "+K);
					break;
				}
				
			default:{
				//先判断寄存器是否被占用
				if(!RDL.get("AX").getSvalue().equals("0")) {
					//如果当前寄存器被操作数B占用
					if(RDL.get("AX").getSvalue().equals(quat.ope1.getSvalue()))
					{
						//B是活跃信息
						if(quat.ope1.getIsActive() != 'n')
							Code("ST","AX",quat.ope1.getSvalue());
					}
					//寄存器被其它活跃信息占用
					else
					{
						//活跃变量需要存入
						if(RDL.get("AX").getIsActive() != 'n')
						{
							Code("ST","AX",RDL.get("AX").getSvalue());
							Code("LD","AX",quat.ope1.getSvalue());
						}
					}
				}else
					Code("LD","AX",quat.ope1.getSvalue());
						
				
				Code(quat.res.getSvalue(),"AX",quat.ope2.getSvalue());
				//将当前结果变量等级到RDL中
				RDL.put("AX", quat.res);
				break;
			}
				
			}
			
			
		}
	}
	
	
	
	
}
