package compiler;

import java.util.*;

public class Array {
	public final int startIndex=0;//��ʼλ��
	public int endIndex;
	public String arrayName;
	public int num;//���麬�е�Ԫ�ظ���
	public int length;//���鳤��
	public int type;//��������
	public int[] isInit = new int[endIndex];//����Ƿ񱻳�ʼ����0--δ��ʼ����1--�ѳ�ʼ����
	public ArrayList<Object>number;//�����ڵ�����

	//���캯��
	public Array() {
	}
	
	public Array(int end,String name,int n,int l) {
		this.endIndex = end;
		this.arrayName = name;
		this.num = n;
		this.length = l;
		for(int i=0;i<length;i++) {
			this.isInit[i] = 0 ;
		}
	}
}
