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
	static public int Snum;//四元式数量，用于定义当前四元式结果编号
	
	
	
	
	public Quat(Token opc, Token ope1, Token ope2, Token res) {
		super();
		this.opc = opc;
		this.ope1 = ope1;
		this.ope2 = ope2;
		this.res = res;
	}

	
}
