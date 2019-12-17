package compiler;
/*
 * 
 * 
 * 
 * */
public class Quat {
	public String opc;
	public Token ope1;
	public Token ope2;
	public Token res;
	
	
	

	//¹¹Ôìº¯Êý
	public Quat() {
		
	}

	public Quat(String opc, Token ope1, Token ope2, Token res) {
		super();
		this.opc = opc;
		this.ope1 = ope1;
		this.ope2 = ope2;
		this.res = res;
	}

	
}
