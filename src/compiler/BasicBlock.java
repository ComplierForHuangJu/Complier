package compiler;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 
 * ��Ŀ���ƣ�Complier
 * ����������
 * @author �������㡣
 * @time 2019��12��11������7:02:51
 * @version V1.0
 */
public class BasicBlock {
	/* ��ʱ��Ԫʽ����  */
	public ArrayList<Quat> _quatList;
	/* ���Level */
	public int _level;
	
	/* ��Ԫʽ�±� */
	public int _index;
	
	/* �������������   */
	public ArrayList<ArrayList<Quat>> basicBlock;
	
	/* ��Ծ��Ϣ����ջ */
	public Stack<Quat> disposeStack;
	
	
	/**
	 * ���ֻ������㷨
	 * */
	public void produceBlock()
	{
		_level = 0;
		//�Ȼ��ֻ�����
		//da
		Quat quat = nextQuat();
	
		while(_index < _quatList.size())
		{
			if(quat.opc == "Ib" ||quat.opc == "Wh" )
				basicBlock.get(++_level).add(quat);
			else if(
					(quat.opc =="if") ||
					(quat.opc =="el") ||
					(quat.opc =="ie") ||
					(quat.opc =="do") ||
					(quat.opc =="we") ||
					(quat.opc =="gt"))
					{basicBlock.get(_level++).add(quat);}
			else 
				basicBlock.get(_level).add(quat);	
			
			//�ƽ�
			quat = nextQuat();
		}
		
	}
	
	
	/**
	 * @param _index ��Ԫʽ�±�
	 * @return ��һ����Ԫʽ
	 * */
	public Quat nextQuat()
	{
		if(_index < _quatList.size())
			return _quatList.get(_index++);
		return null;
	}
}
