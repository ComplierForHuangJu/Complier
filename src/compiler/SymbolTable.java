package compiler;

//���ű���
public class SymbolTable {
	
	/*
	 * f---����
	 * c---����
	 * t---����
	 * d---����
	 * v---����
	 */
	public enum CATE
	{
		f,c,t,d,v
	}
	
	/*
	 * name---��ʶ������
	 * type---���ͱ��±�
	 * cate---��ʶ������
	 * addr---��ʶ��(����--�������±�/����--ֵ/����--����/����--����/����--��Ե�ַ)
	 */
	private String name;
	private int type;
	private CATE cate;
	private int addr;
	
}
