package compiler;

//符号表类
public class SymbolTable {
	
	/*
	 * f---函数
	 * c---常量
	 * t---类型
	 * d---域名
	 * v---变量
	 */
	public enum CATE
	{
		f,c,t,d,v
	}
	
	/*
	 * name---标识符名字
	 * type---类型表下标
	 * cate---标识符种类
	 * addr---标识符(函数--函数表下标/常量--值/类型--长度/域名--长度/变量--相对地址)
	 */
	private String name;
	private int type;
	private CATE cate;
	private int addr;
	
}
