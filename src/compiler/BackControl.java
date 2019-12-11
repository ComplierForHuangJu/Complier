package compiler;
/**
 * 
 * 项目名称：Complier
 * 后端控制程序
 * @author 北溟有鱼。
 * @time 2019年12月11日下午7:03:45
 * @version V1.0
 */

import java.util.ArrayList;
import java.util.Stack;

public class BackControl {
	
	/* 临时四元式队列  */
	public ArrayList<Quat> _quatList;
	/* 层次Level */
	public int _level;
	
	/* 四元式下标 */
	public int _index;
	
	/* 基本块队列容器   */
	public ArrayList<ArrayList<Quat>> basicBlock;
	
	/* 活跃信息处理栈 */
	public Stack<Quat> disposeStack;
	
	
	/**
	 * 划分基本块算法
	 * */
	public void produceBlock()
	{
		_level = 0;
		//先划分基本块
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
			
			//移进
			quat = nextQuat();
		}
		
	}
	
	
	/**
	 * @param _index 四元式下标
	 * @return 下一个四元式
	 * */
	public Quat nextQuat()
	{
		if(_index < _quatList.size())
			return _quatList.get(_index++);
		return null;
	}
	
}
