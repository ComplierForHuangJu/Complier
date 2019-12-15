package compiler;
/**
 * 
 * 项目名称：Complier
 * 活跃信息生成表
 * @author 北溟有鱼。
 * @time 2019年12月14日下午9:32:57
 * @version V1.0
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import compiler.Token.TYPE;

public class ActiveInf {

	/* 活跃信息生成表  */
	private HashMap<Token,Character> SYMBL;
	
	/* 活跃信息生成栈  */
	private Stack<Token> stack;
	
	/**
	 * 初始化信息表
	 * @param quatList
	 */
	private void initTable(ArrayList<Quat> quatList) {
		SYMBL = new HashMap<Token, Character>();
		for(int i = quatList.size() - 1; i >= 0;i--) {
			//先将操作数放入到Map中
			Quat quat = quatList.get(i);
			add(quat.res);
			add(quat.ope2);
			add(quat.ope1);		
		}
	}
	
	
	/**
	 * 	添加新的变量到信息表中
	 * @param token
	 */
	private void add(Token token) {
		//如果表中没有当前变量
		if(!SYMBL.containsKey(token) && token.gettype() == TYPE.i)
			if(token.getSvalue().charAt(0) != 't')
				SYMBL.put(token, 'y');
			else
				SYMBL.put(token, 'n');
	}
	
	/**
	 * 更新活跃信息
	 * @param quatList
	 */
	private void updata(ArrayList<Quat> quatList) {
		/***
		 * 设计思路：逆序遍历四元式序列
		 * 		   将操作数加入到活跃信息表
		 */
		for(int i = quatList.size() - 1; i >= 0;i--) {
			Quat quat = quatList.get(i);
			//先将四元式中的数据更新
			quat.res.setIsActive(SYMBL.get(quat.res));
			quat.ope2.setIsActive(SYMBL.get(quat.ope2));
			quat.ope1.setIsActive(SYMBL.get(quat.ope1));
			//再反写活跃信息表
			//如果是res的话只需要改写n/y
			if(SYMBL.get(quat.res) != 'n')
				SYMBL.put(quat.res, 'n');
			else
				SYMBL.put(quat.res, 'y');
			
			//如果是操作数的话，则改写当前序号
			if(SYMBL.get(quat.ope2) != 'n')
				SYMBL.put(quat.ope2, 'n');
			else
				SYMBL.put(quat.ope2, (char)(i + '0'));
					
		}
	}
	
	
	/**
	 * 根据DAG优化后的四元式生成活跃信息表
	 * @param quatList
	 * @return
	 */
	public HashMap<Token, Character> creatInf(ArrayList<Quat> quatList){
		initTable(quatList);
		updata(quatList);
		return SYMBL;
	}
}
