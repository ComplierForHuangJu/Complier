	package compiler;

public class Token {

	/*
	 * k---�ؼ���
	 * i---��ʶ��
	 * inc---�������ֳ���
	 * fnc---ʵ���ַ�����
	 * cc---�ַ�����
	 * sc---�ַ�������
	 * p---���
	 *  
	 */
	public enum TYPE
	{
		k,i,inc,fnc,cc,sc,p
	}
	/*
	 * type---��¼����
	 * index---��¼����λ��
	 * lastState---��¼�����Զ���������״̬
	 * svalue---���ǹؼ���/��ʶ��/�ַ�����/�ַ�������/��� ,��˱���Ϊ��ֵ
	 * ivalue---�����������ֱ��� ,��˱���Ϊ��ֵ
	 * fvalue---����ʵ�����ֳ���,��˱���Ϊ��ֵ
	 * isTemp---�Ƿ�����ʱ����
	 * */
	private TYPE type;
	private int index;
	private int lastState;

	private String svalue;
	private int ivalue;
	private float fvalue;
	
	public boolean isTemp = false;
	
	/********����ӵĹ��ڻ�Ծ��Ϣ���ɲ��ֵĳ�Ա����********/
	
	private char isActive;
	
	public char getIsActive() {
		return isActive;
	}
	public void setIsActive(char isActive) {
		this.isActive = isActive;
	}
	/*********************************************/
	
	//���캯��
	public Token() {
		
	}

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
	public int getLastState() {
		return lastState;
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
	}
	
	
}
