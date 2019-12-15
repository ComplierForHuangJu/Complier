package compiler;
/**
 * 
 * 项目名称：Complier
 * 活跃信息表
 * @author 北溟有鱼。
 * @time 2019年12月14日下午9:28:08
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
