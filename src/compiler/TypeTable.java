package compiler;

//类型表
public class TypeTable {
	/*
	 * tval---类型代码(0--未知类型;1--int;2--float;3--char)
	 * tpointer---类型指针
	 * 
	 */
	public int tval;
    public int tpointer;
    
    public TypeTable(int i,int j) {
    	this.tval = i;
    	this.tpointer = j;
    }

	public TypeTable() {
	}
}
