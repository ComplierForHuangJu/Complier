package compiler;
/**
 * 
 * ��Ŀ���ƣ�Complier
 * ��˿��Ƴ���
 * @author �������㡣
 * @time 2019��12��11������7:03:45
 * @version V1.0
 */

import java.util.ArrayList;
import java.util.Stack;

public class BackControl {
	
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
		//
		Quat quat = nextQuat();
	
		while(_index < _quatList.size())
		{
			if(quat.opc.getValue() == "Ib" ||quat.opc.getValue() == "Wh" )
				basicBlock.get(++_level).add(quat);
			else if(
					(quat.opc.getValue() =="if") ||
					(quat.opc.getValue() =="el") ||
					(quat.opc.getValue() =="ie") ||
					(quat.opc.getValue() =="do") ||
					(quat.opc.getValue() =="we") ||
					(quat.opc.getValue() =="gt"))
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
