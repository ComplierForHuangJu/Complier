package compiler;
/*
 * 
 * 
 * 
 * */
public class Quat {
	public Token opc;
	public Token ope1;
	public Token ope2;
	public Token res;
	static public int Snum;//��Ԫʽ���������ڶ��嵱ǰ��Ԫʽ������
	
	
	
	
	public Quat(Token opc, Token ope1, Token ope2, Token res) {
		super();
		this.opc = opc;
		this.ope1 = ope1;
		this.ope2 = ope2;
		this.res = res;
	}

	
}
