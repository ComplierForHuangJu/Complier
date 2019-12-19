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
	private HashMap<String,Boolean> SYMBL;
	
	/* 活跃信息生成栈  */
	private Stack<Token> stack;
	
	/**
	 * 初始化信息表
	 * @param quatList
	 */
	private void initTable(ArrayList<Quat> quatList) {
		SYMBL = new HashMap<String, Boolean>();
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
		if(!SYMBL.containsKey(token.getSvalue()) && token.gettype() == TYPE.i) {
			if(token.getSvalue().charAt(0) != 't')
				SYMBL.put(token.getSvalue(), true);
			else
				SYMBL.put(token.getSvalue(), false);
		}	
			
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
			//处理非变量
			if(SYMBL.containsKey(quat.res.getSvalue()))
				quat.setActiveRes(SYMBL.get(quat.res.getSvalue()));
			if(SYMBL.containsKey(quat.ope2.getSvalue()))
				quat.setActiveOpe2(SYMBL.get(quat.ope2.getSvalue()));
			if(SYMBL.containsKey(quat.ope1.getSvalue()))
				quat.setActiveOpe1(SYMBL.get(quat.ope1.getSvalue()));
			//再反写活跃信息表
			//如果是res的话只需要改写n/y
			if(SYMBL.containsKey(quat.res.getSvalue()))
				if(SYMBL.get(quat.res.getSvalue()))
					SYMBL.put(quat.res.getSvalue(), false);
				else
					SYMBL.put(quat.res.getSvalue(), true);
			
			//如果是操作数的话，则改写当前序号
			if(SYMBL.containsKey(quat.ope2.getSvalue()))
				if(SYMBL.get(quat.ope2.getSvalue()))
					SYMBL.put(quat.ope2.getSvalue(), false);
				else
					SYMBL.put(quat.ope2.getSvalue(), true);
			//如果是操作数的话，则改写当前序号
			if(SYMBL.containsKey(quat.ope1.getSvalue()))
				if(SYMBL.get(quat.ope1.getSvalue()))
					SYMBL.put(quat.ope1.getSvalue(), false);
				else
					SYMBL.put(quat.ope1.getSvalue(), true);
					
		}
	}
	
	
	/**
	 * 根据DAG优化后的四元式生成活跃信息表
	 * @param quatList
	 * @return
	 */
	public HashMap<String, Boolean> creatInf(ArrayList<Quat> quatList){
		initTable(quatList);
		for(String token : SYMBL.keySet())
			System.out.println(token + "\t" + SYMBL.get(token));
		updata(quatList);
//		System.out.println("四元式信息：");
//		for(Quat quat : quatList) {
//			System.out.println("("+quat.opc+","+quat.ope1.getSvalue()+"("+quat.isActiveOpe1()+"),"
//					+quat.ope2.getSvalue()+"("+quat.isActiveOpe2()+"),"
//					+quat.res.getSvalue()+"("+quat.isActiveRes()+") )"
//					);
//		}
		return SYMBL;
	}
}
