package compiler;

public class Token {

	/*
	 * k---关键字
	 * i---标识符
	 * inc---整型数字常量
	 * fnc---实型字符常量
	 * cc---字符常量
	 * sc---字符串常量
	 * p---界符
	 *  
	 */
	public enum TYPE
	{
		k,i,inc,fnc,cc,sc,p
	}
	/*
	 * type---记录类型
	 * index---记录表中位置
	 * lastState---记录其在自动机中最后的状态
	 * svalue---若是关键字/标识符/字符常量/字符串常量/界符 ,则此变量为其值
	 * ivalue---若是整型数字变量 ,则此变量为其值
	 * fvalue---若是实型数字常量,则此变量为其值
	 * */
	private TYPE type;
	private int index;
	private int lastState;

	private String svalue;
	private int ivalue;
	private float fvalue;

	private String value;
	

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

	public String getSvalue() {
		return svalue;
	}
	public void setSvalue(String svalue) {
		this.svalue = svalue;
	}
	public int getIvalue() {
		return ivalue;
	}
	public void setIvalue(int ivalue) {
		this.ivalue = ivalue;
	}
	public float getFvalue() {
		return fvalue;
	}
	public void setFvalue(float fvalue) {
		this.fvalue = fvalue;

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;

	}
	
	
}
