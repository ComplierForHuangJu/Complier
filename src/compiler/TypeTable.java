package compiler;

//���ͱ�
public class TypeTable {
	/*
	 * tval---���ʹ���(0--δ֪����;1--int;2--float;3--char)
	 * tpointer---����ָ��
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
