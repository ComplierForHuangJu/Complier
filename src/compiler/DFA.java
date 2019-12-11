package compiler;

import java.util.HashMap;
public class DFA {

	//�ؼ��ֻ��ʶ�����Զ���
	public class kriDFA{
		private int map[][]= {{0,0,0,0},{0,1,0,0},{0,2,2,3},{0,0,0,0}};
		
	    public int getVn(char c) {
	    	if ((c >= 65 && c <= 106) || (c >= 97 && c <= 122) || (c == 95))return 1;//��ĸ���»���
	    	else if ((c >= 48 && c <= 57))return 2;//����
	    	else  return 3;//��̷�
	    }
		
		public int getState(int i,int j) {
			return map[i][j];
		}
	    
	}
	
	public class nconDFA{
		/*
		 * map[][]---�任��
		 * n---β��ֵ����
		 * p---ָ��ֵ����
		 * m---С��λ������
		 * e---ָ���ķ��ű���
		 * value---���ճ���ֵ
		 */
		private int map[][]= {{0,0,0,0,0,0},{0,2,0,0,0,0},{0,2,3,7,4,4},{0,5,0,0,0,0},
				{0,0,0,0,0,0},{0,5,6,7,6,6},{0,0,0,0,0,0},{0,8,0,0,9,0},{0,8,6,6,6,6},{0,8,0,0,0,0}};
		private int n=0;
		private int p=0;
		private int m=0;
		private int e=1;
		private float value;
		
	    public int getVn(char c) {
	    	if ((c >= 48 && c <= 57))return 1;//����
	    	else if(c=='.')return 2;
	    	else if(c=='e'||c=='E')return 3;
	    	else if (c=='+'||c=='-')return 4;
	    	else  return 5;//��̷�
	    }
	    
	    //�����ķ�����
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
		
	    //״̬ת����i�ǵ�ǰ״̬��j�Ƕ����ַ����룬c�ǵ�ǰ�����ַ�
		public int getState(int i,int j,char c) {
			tran(map[i][j],c);
			return map[i][j];
		}  
		
		//������ĳ���ֵ
		public float getValue() {
			int i,j;
			j=this.e*this.p-this.m;
			i=(int)Math.pow(10, j);
			value=n*i;	
			return value;
		}
		
	}
	
	//ʶ���ַ�����
	public class cconDFA{
		private int map[][]= {{0,0,0,0},{0,2,0,0},{0,4,3,6},{0,4,0,0},
				{0,5,5,5},{0,0,0,0},{0,8,7,7},{0,4,0,0},{0,4,0,0}};
		
	    public int getVn(char c) {
	    	if (c==39 )return 1;//'
	    	else if(c=='\\')return 3;//��\��
	    	else return 2;
	    }
		
		public int getState(int i,int j) {
			return map[i][j];
		}  
	}
	//ʶ���ַ�������
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
