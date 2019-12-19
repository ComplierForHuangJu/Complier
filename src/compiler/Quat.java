package compiler;
/*
 * 
 * 
 * 
 * */
public class Quat {
	public String opc;
	public Token ope1;
	private boolean activeOpe1;
	public Token ope2;
	private boolean activeOpe2;
	public Token res;
	private boolean activeRes;
	
	
	

	public boolean isActiveOpe1() {
		return activeOpe1;
	}

	public boolean isActiveOpe2() {
		return activeOpe2;
	}

	public boolean isActiveRes() {
		return activeRes;
	}

	public void setActiveOpe1(boolean activeOpe1) {
		this.activeOpe1 = activeOpe1;
	}

	public void setActiveOpe2(boolean activeOpe2) {
		this.activeOpe2 = activeOpe2;
	}

	public void setActiveRes(boolean activeRes) {
		this.activeRes = activeRes;
	}

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
