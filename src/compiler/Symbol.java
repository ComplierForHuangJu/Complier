package compiler;

//符号表基本项
public class Symbol {
	/*
	 * cate:
	 * 0---函数
	 * 1---常量
	 * 2---类型
	 * 3---域名
	 * 4---变量
	 * 
	 * name---标识符名字
	 * type---类型表下标
	 * cate---标识符种类
	 * addr---标识符(函数--函数表下标/常量--值/类型--长度/域名--长度/变量--相对地址)
	 */
	public String name;
	public int type;
	public int cate;
	public int addr;

}
