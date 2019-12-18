package compiler;

import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.soap.Node;

import compiler.Token;
import compiler.Token.TYPE;

/**
 * 
 * ��Ŀ���ƣ�Complier
 * ��Ԫʽ�Ż�
 * @author �������㡣
 * @time 2019��12��9������4:26:17
 * @version V1.0
 */
public class Optimize {
	
	/* �Ż������Ԫʽ����  */
	public static ArrayList<Quat> _opQuat;
	
	/* �ƽ���Ԫʽ�±�   */
	public int _index;
	
	/* ���ݻ���������DAG����  */
	public ArrayList<NodeOfDAG> DAG;
	
	/**
	 * 
	 * @param defaultQuat ���������Ԫʽ����
	 * @return ���ɵĻ�����DAG��
	 */
	public ArrayList<NodeOfDAG> creatDAG(ArrayList<Quat> defaultQuat)
	{
		_index = 0;
		DAG = new ArrayList<NodeOfDAG>();
		
		/**
		 * ÿ����Ԫʽ�Ĳ����������Token��Ӧ����һ�����
		 * 
		 * */
		for(Quat quat:defaultQuat)
		{
			//�����ǰ��ԪʽΪ�µĽڵ��������µĽڵ�
			if(quat.opc == "=" && !contains(DAG,quat.res))
			{
				NodeOfDAG child = new NodeOfDAG(_index++);
				child.set_mainPip(quat.res);
				child.set_opc(quat.opc);
				DAG.add(child);
			}

			
			
			//���������Ϊ˫Ŀ������Ļ�,���ɶ�Ӧ����֧
			if(quat.opc == "+" || quat.opc== "-" ||quat.opc == "*" ||
			   quat.opc == "/" )
				{			
				
					NodeOfDAG leftChild = contains(DAG,quat.ope1) ? DAG.get(getNode(DAG, quat.ope1)) : new NodeOfDAG(_index++);
					NodeOfDAG rightChild = contains(DAG,quat.ope2) ? DAG.get(getNode(DAG, quat.ope2)) : new NodeOfDAG(_index++);
					
					//��������Ϊ�գ�����ӵ�������ϣ�������ӵ����ӱ����
					if(leftChild.get_mainPip() == null)
						leftChild.set_mainPip(quat.ope1);
					else
						changeTag(leftChild,quat.ope1);
						
					
					if(rightChild.get_mainPip() == null)
						rightChild.set_mainPip(quat.ope2);
					else
						changeTag(rightChild,quat.ope2);
								
					
					//�������׽��
					NodeOfDAG rootNode =contains(DAG,quat.res) ? DAG.get(getNode(DAG, quat.res)) : new NodeOfDAG(_index++);
					
					if(rootNode.get_mainPip() == null)
						{
							rootNode.set_mainPip(quat.res);
							rootNode.set_opc(quat.opc);
						}
					else
						changeTag(rootNode,quat.res);
					
					
					
					//��Ӻ��
					rootNode.add_seq(leftChild.get_ni());
					rootNode.add_seq(rightChild.get_ni());
					
					//���ǰ��
					leftChild.add_pre(rootNode.get_ni());
					rightChild.add_pre(rootNode.get_ni());
					
					//ȥ��
					leftChild.set_pre(new ArrayList<Integer>(new HashSet<Integer>(leftChild.get_pre())));
					rightChild.set_pre(new ArrayList<Integer>(new HashSet<Integer>(rightChild.get_pre())));
					rootNode.set_seq(new ArrayList<Integer>(new HashSet<Integer>(rootNode.get_seq())));
					
				
					
				}
			
			
				
		}
		
		return null;
	}
	
	/**
	 * ���½��ı����Ϣ
	 * @param node	���
	 * @param token	������
	 */
	public void changeTag(NodeOfDAG node,Token token)
	{
		//�����ǰ�������ǳ���
		if(token.gettype() == TYPE.inc)
		{
			//��ԭ�����������ӵ��������
			node.add_addTag(node.get_mainPip());
			node.set_mainPip(token);
		}
		//�����ǰ������Ϊ����ʱ����
		else if(token.getSvalue().charAt(0) != 't')
		{
			//��ԭ�����������ӵ��������
			node.add_addTag(node.get_mainPip());
			node.set_mainPip(token);
		}
		else
			//���ӱ���в����ڵ�ǰ�����������
			if(!node.get_addTag().contains(token))
				node._addTag.add(token);
	}
	/**
	 * 
	 * @param dAG
	 * @param token
	 * @return ��������DAG�е�ÿһ���ڵ������Ǻ͸���ǣ��鿴�Ƿ����һ���Ľڵ�
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
	
	/* �ƽ�  */
	public Quat nextQuat(ArrayList<Quat> defaultQuat)
	{
		if(_index < defaultQuat.size())
			return defaultQuat.get(_index++);
		return null;
	}
	
}
