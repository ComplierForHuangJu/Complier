package compiler;

import java.util.*;

//���ű��ࣨ��������
public class SymbolTable {
	
	public ArrayList<Quat> quaters;//��Ԫʽ��
	public Map<String, Symbol>IDsymbol;//��ʶ����
	public Map<String, Symbol> constArray;//������(������ֵ�������ַ�������)
	public ArrayList<Symbol> paramlist;//�����б�
	public Symbol funcReturn;//��������ֵ����
	
	
	public SymbolTable() {
		quaters=new ArrayList<Quat>();
		IDsymbol=new HashMap<String, Symbol>();
		constArray=new HashMap<String, Symbol>();
		paramlist=new ArrayList<Symbol>();
	}
	
	
	
	
	
}
