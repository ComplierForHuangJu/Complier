package compiler;

import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.soap.Node;

import compiler.Token;
import compiler.Token.TYPE;

/**
 * 
 * 项目名称：Complier
 * 四元式优化
 * @author 北溟有鱼。
 * @time 2019年12月9日下午4:26:17
 * @version V1.0
 */
public class Optimize {
	
	/* 优化后的四元式序列  */
	public static ArrayList<Quat> _opQuat;
	
	/* 移进四元式下标   */
	public int _index;
	
	/* 根据基本块生成DAG序列  */
	public ArrayList<NodeOfDAG> DAG;
	
	/**
	 * 
	 * @param defaultQuat 基本块的四元式序列
	 * @return 生成的基本块DAG树
	 */
	public ArrayList<NodeOfDAG> creatDAG(ArrayList<Quat> defaultQuat)
	{
		_index = 0;
		DAG = new ArrayList<NodeOfDAG>();
		
		/**
		 * 每个四元式的操作数、结果Token都应该是一个结点
		 * 
		 * */
		for(Quat quat:defaultQuat)
		{
			//如果当前四元式为新的节点则生成新的节点
			if(quat.opc == "=" && !contains(DAG,quat.res))
			{
				NodeOfDAG child = new NodeOfDAG(_index++);
				child.set_mainPip(quat.res);
				child.set_opc(quat.opc);
				DAG.add(child);
			}

			
			
			//如果操作符为双目运算符的话,生成对应的树支
			if(quat.opc == "+" || quat.opc== "-" ||quat.opc == "*" ||
			   quat.opc == "/" )
				{			
				
					NodeOfDAG leftChild = contains(DAG,quat.ope1) ? DAG.get(getNode(DAG, quat.ope1)) : new NodeOfDAG(_index++);
					NodeOfDAG rightChild = contains(DAG,quat.ope2) ? DAG.get(getNode(DAG, quat.ope2)) : new NodeOfDAG(_index++);
					
					//如果主标记为空，就添加到主标记上，否则添加到附加标记上
					if(leftChild.get_mainPip() == null)
						leftChild.set_mainPip(quat.ope1);
					else
						changeTag(leftChild,quat.ope1);
						
					
					if(rightChild.get_mainPip() == null)
						rightChild.set_mainPip(quat.ope2);
					else
						changeTag(rightChild,quat.ope2);
								
					
					//创建父亲结点
					NodeOfDAG rootNode =contains(DAG,quat.res) ? DAG.get(getNode(DAG, quat.res)) : new NodeOfDAG(_index++);
					
					if(rootNode.get_mainPip() == null)
						{
							rootNode.set_mainPip(quat.res);
							rootNode.set_opc(quat.opc);
						}
					else
						changeTag(rootNode,quat.res);
					
					
					
					//添加后继
					rootNode.add_seq(leftChild.get_ni());
					rootNode.add_seq(rightChild.get_ni());
					
					//添加前驱
					leftChild.add_pre(rootNode.get_ni());
					rightChild.add_pre(rootNode.get_ni());
					
					//去重
					leftChild.set_pre(new ArrayList<Integer>(new HashSet<Integer>(leftChild.get_pre())));
					rightChild.set_pre(new ArrayList<Integer>(new HashSet<Integer>(rightChild.get_pre())));
					rootNode.set_seq(new ArrayList<Integer>(new HashSet<Integer>(rootNode.get_seq())));
					
				
					
				}
			
			
				
		}
		
		return null;
	}
	
	/**
	 * 更新结点的标记信息
	 * @param node	结点
	 * @param token	操作数
	 */
	public void changeTag(NodeOfDAG node,Token token)
	{
		//如果当前操作数是常量
		if(token.gettype() == TYPE.inc)
		{
			//将原来的主标记添加到附标记上
			node.add_addTag(node.get_mainPip());
			node.set_mainPip(token);
		}
		//如果当前操作数为非临时变量
		else if(token.getSvalue().charAt(0) != 't')
		{
			//将原来的主标记添加到附标记上
			node.add_addTag(node.get_mainPip());
			node.set_mainPip(token);
		}
		else
			//附加标记中不存在当前操作数则加上
			if(!node.get_addTag().contains(token))
				node._addTag.add(token);
	}
	/**
	 * 
	 * @param dAG
	 * @param token
	 * @return 检索遍历DAG中的每一个节点的主标记和附标记，查看是否存在一样的节点
	 */
	
	public boolean contains(ArrayList<NodeOfDAG> dAG,Token token) {
		
		for(NodeOfDAG node : dAG)
		{
			if(node._mainPip.equals(token) || node._addTag.contains(token))
				return true;
		}
		
		return false;
	}

	
	public int getNode(ArrayList<NodeOfDAG> dAG,Token token) {
		for(NodeOfDAG node : dAG)
		{
			if(node._mainPip.equals(token) || node._addTag.contains(token))
				return node._ni;
		}
		
		return -1;
	}
	
	
	
	public NodeOfDAG creatNode(int i) {
		NodeOfDAG node = new NodeOfDAG(i);
		return null;
	}
	
	/* 移进  */
	public Quat nextQuat(ArrayList<Quat> defaultQuat)
	{
		if(_index < defaultQuat.size())
			return defaultQuat.get(_index++);
		return null;
	}
	
}
