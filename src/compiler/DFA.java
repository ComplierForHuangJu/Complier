
package compiler;

import java.util.HashMap;
public class DFA {

	//鍏抽敭瀛楁垨鏍囪瘑绗︾殑鑷姩鏈�
	public class kriDFA{
		private int map[][]= {{0,0,0,0},{0,1,0,0},{0,2,2,3},{0,0,0,0}};
		
	    public int getVn(char c) {
	    	if ((c >= 65 && c <= 106) || (c >= 97 && c <= 122) || (c == 95))return 1;//瀛楁瘝鎴栦笅鍒掔嚎
	    	else if ((c >= 48 && c <= 57))return 2;//鏁板瓧
	    	else  return 3;//鍚庣户绗�
	    }
		
		public int getState(int i,int j) {
			return map[i][j];
		}
	    
	}
	
	public class nconDFA{
		/*
		 * map[][]---鍙樻崲琛�
		 * n---灏炬暟鍊煎彉閲�
		 * p---鎸囨暟鍊煎彉閲�
		 * m---灏忔暟浣嶆暟鍙橀噺
		 * e---鎸囨暟鐨勭鍙峰彉閲�
		 * value---鏈�缁堝父鏁板��
		 */
		private int map[][]= {{0,0,0,0,0,0},{0,2,0,0,0,0},{0,2,3,7,4,4},{0,5,0,0,0,0},
				{0,0,0,0,0,0},{0,5,6,7,6,6},{0,0,0,0,0,0},{0,8,0,0,9,0},{0,8,6,6,6,6},{0,8,0,0,0,0}};
		private int n=0;
		private int p=0;
		private int m=0;
		private int e=1;
		private float value;
		
	    public int getVn(char c) {
	    	if ((c >= 48 && c <= 57))return 1;//鏁板瓧
	    	else if(c=='.')return 2;
	    	else if(c=='e'||c=='E')return 3;
	    	else if (c=='+'||c=='-')return 4;
	    	else  return 5;//鍚庣户绗�
	    }
	    
	    //甯告暟鐨勭炕璇戝櫒
	    public void tran(int i,char c) {
	    	if(i==2) {
	    		n=10*n+(c-48);
	    	}
	    	else if(i==5) {
	    		n=10*n+(c-48);
	    	}
	    	else if(i==8) {
	    		p=10*p+(c-48);
	    	}
	    	else if(i==9) {
	    		if(c=='-') {
	    			e=-1;
	    		}
	    	}
	    }
		
	    //鐘舵�佽浆鎹紝i鏄綋鍓嶇姸鎬侊紝j鏄鍏ュ瓧绗︾紪鐮侊紝c鏄綋鍓嶆墍璇诲瓧绗�
		public int getState(int i,int j,char c) {
			tran(map[i][j],c);
			return map[i][j];
		}  
		
		//绠楀嚭鏈�鍚庣殑甯告暟鍊�
		public float getValue() {
			int i,j;
			j=this.e*this.p-this.m;
			i=(int)Math.pow(10, j);
			value=n*i;	
			return value;
		}
		
	}
	
	//璇嗗埆瀛楃甯搁噺
	public class cconDFA{
		private int map[][]= {{0,0,0,0},{0,2,0,0},{0,4,3,6},{0,4,0,0},
				{0,5,5,5},{0,0,0,0},{0,8,7,7},{0,4,0,0},{0,4,0,0}};
		
	    public int getVn(char c) {
	    	if (c==39 )return 1;//'
	    	else if(c=='\\')return 3;//鈥榎鈥�
	    	else return 2;
	    }
		
		public int getState(int i,int j) {
			return map[i][j];
		}  
	}
	//璇嗗埆瀛楃涓插父閲�
	public class sconDFA{
		private int map[][]= {{0,0,0,0},{0,4,0,0},{0,0,0,0},{0,2,2,2},
				{0,3,5,4},{0,4,4,4}};
		
	    public int getVn(char c) {
	    	if (c==34 )return 1;//"
	    	else if(c=='\\')return 2;//\
	    	else return 3;
	    }
		
		public int getState(int i,int j) {
			return map[i][j];
		}  
	}
}

