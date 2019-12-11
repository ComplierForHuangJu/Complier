package compiler;
/**
 * 
 * ��Ŀ���ƣ�Complier
 * DAG�����Ϣ
 * @author �������㡣
 * @time 2019��12��11������6:41:10
 * @version V1.0
 */

import java.util.ArrayList;

public class NodeOfDAG {
	
	/* �����  */
	int _ni;
	
	/* �����Token */
	Token _opc;
	
	/* ����� */
	Token _mainPip;
	
	/* ����� */
	ArrayList<Token> _addTag;
	
	/* ǰ�� */
	ArrayList<Integer> _pre;
	
	/* ���  */
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