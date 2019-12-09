package compiler;

public class Token {

	private enum TYPE
	{
		
	}
	private TYPE _type;
	private int _index;
	public TYPE get_type() {
		return _type;
	}
	public void set_type(TYPE _type) {
		this._type = _type;
	}
	public int get_index() {
		return _index;
	}
	public void set_index(int _index) {
		this._index = _index;
	}
	
	
}
