package compiler;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 
 * 项目名称：Complier
 * 基本块运算
 * @author 北溟有鱼。
 * @time 2019年12月11日下午7:02:51
 * @version V1.0
 */
public class BasicBlock {
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
