package compiler;

public class Token {

	/*
	 * type ��¼����
	 * index ��¼����λ��
	 * lastState ��¼�����Զ���������״̬
	 * 
	 * */
	public enum TYPE
	{
		k,i,nc,cc,sc,p
	}
	private TYPE type;
	private int index;
	private int lastState;
	
	
	public TYPE gettype() {
		return type;
	}
	public void settype(TYPE type) {
		this.type = type;
	}
	public int getindex() {
		return index;
	}
	public void setindex(int index) {
		this.index = index;
	}
	public void setlastState(int state) {
		this.lastState = state;
	}
	
	
}
