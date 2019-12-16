package compiler;

import java.util.*;

//符号表类（面向函数）
public class SymbolTable {
	
	public ArrayList<Quat> quaters;//四元式表；
	public Map<String, Symbol>IDsymbol;//标识符表
	public Map<String, Symbol> constArray;//常量表(包括数值常量、字符常量等)
	public ArrayList<Symbol> paramlist;//参数列表
	public Symbol funcReturn;//函数返回值类型
	
	
	public SymbolTable() {
		quaters=new ArrayList<Quat>();
		IDsymbol=new HashMap<String, Symbol>();
		constArray=new HashMap<String, Symbol>();
		paramlist=new ArrayList<Symbol>();
	}
	
	
	
	
	
}
