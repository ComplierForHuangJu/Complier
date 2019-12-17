package compiler;

//���ű������
public class Symbol {
	/*
	 * cate:
	 * 0---����
	 * 1---����
	 * 2---����
	 * 3---����
	 * 4---����
	 * 
	 * name---��ʶ������
	 * type---��ʶ������(0--int,1--float,2--char)
	 * cate---��ʶ������
	 * addr---��ʶ��(����--�������±�/����--ֵ/����--����/����--����/����--��Ե�ַ)
	 * isInit---����Ƿ񱻳�ʼ��
	 */
	public String name;
	public int type;
	public int cate;
	public int addr;
	public boolean isInit = false;
	
	public Symbol() {}
	public Symbol(String s) {
		this.name=s;
	}

}
