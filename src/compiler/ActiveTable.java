package compiler;
/**
 * 
 * ��Ŀ���ƣ�Complier
 * ��Ծ��Ϣ��
 * @author �������㡣
 * @time 2019��12��14������9:28:08
 * @version V1.0
 */
public class ActiveTable {
	private String name = "";
	private boolean isActive = false;

	
	public ActiveTable() {
		super();
		
	}
	
	public ActiveTable(String name,boolean isActive) {
		super();
		this.name = name;
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "ActiveTable [name=" + name + ", isActive=" + isActive + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
