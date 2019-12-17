package compiler;

import java.util.*;

public class Array {
	public final int startIndex=0;//开始位置
	public int endIndex;
	public String arrayName;
	public int num;//数组含有的元素个数
	public int length;//数组长度
	public int type;//数组类型
	public int[] isInit = new int[endIndex];//检查是否被初始化（0--未初始化，1--已初始化）
	public ArrayList<Object>number;//数组内的数据

	//构造函数
	public Array() {
	}
	
	public Array(int end,String name,int n,int l) {
		this.endIndex = end;
		this.arrayName = name;
		this.num = n;
		this.length = l;
		for(int i=0;i<length;i++) {
			this.isInit[i] = 0 ;
		}
	}
}
