package compiler;
/**
 * 
 * 项目名称：Complier
 * DAG结点信息
 * @author 北溟有鱼。
 * @time 2019年12月11日下午6:41:10
 * @version V1.0
 */

import java.util.ArrayList;
import java.util.HashSet;

public class NodeOfDAG {
	
	/* 结点编号  */
	int _ni;
	
	/* 运算符Token */
	String _opc;
	
	/* 主标记 */
	Token _mainPip;
	
	/* 附标记 */
	ArrayList<Token> _addTag;
	
	/* 前驱 */
	ArrayList<Integer> _pre;
	
	/* 后继  */
	ArrayList<Integer> _seq;
	
	
	NodeOfDAG()
	{
		
	}
	
	NodeOfDAG(int i){this._ni = i;}

	
	NodeOfDAG(int i,int pre)
	{
		this._ni = i;
		//向该节点前驱加入父亲
		this._pre.add(pre);
	}
	
	
	public int get_ni() {
		return _ni;
	}

	public void set_ni(int _ni) {
		this._ni = _ni;
	}

	public String get_opc() {
		return _opc;
	}

	public void set_opc(String opc) {
		this._opc = opc;
	}

	public Token get_mainPip() {
		return _mainPip;
	}

	public void set_mainPip(Token _mainPip) {
		this._mainPip = _mainPip;
	}

	public ArrayList<Token> get_addTag() {
		return _addTag;
	}

	public void set_addTag(ArrayList<Token> _addTag) {
		this._addTag = _addTag;
	}

	public ArrayList<Integer> get_pre() {
		return _pre;
	}

	public void set_pre(ArrayList<Integer> _pre) {
		this._pre = _pre;
	}

	public ArrayList<Integer> get_seq() {
		return _seq;
	}

	public void set_seq(ArrayList<Integer> _seq) {
		this._seq = _seq;
	}
	
	public void add_addTag(Token token) {
		this._addTag.add(token);
	}
	
	public void add_seq(int i) {
		this._seq.add(i);
	}
	public void add_pre(int i) {
		this._pre.add(i);
	}

	
}
