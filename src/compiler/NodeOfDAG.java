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

public class NodeOfDAG {
	
	/* 结点编号  */
	int _ni;
	
	/* 运算符Token */
	Token _opc;
	
	/* 主标记 */
	Token _mainPip;
	
	/* 附标记 */
	ArrayList<Token> _addTag;
	
	/* 前驱 */
	ArrayList<Integer> _pre;
	
	/* 后继  */
	ArrayList<Integer> _seq;
	
	NodeOfDAG(int i){this._ni = i;}

	public int get_ni() {
		return _ni;
	}

	public void set_ni(int _ni) {
		this._ni = _ni;
	}

	public Token get_opc() {
		return _opc;
	}

	public void set_opc(Token _opc) {
		this._opc = _opc;
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
	
	
}
