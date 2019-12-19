package compiler;
/**
 * 
 * ��Ŀ���ƣ�Complier
 * ��Ծ��Ϣ���ɱ�
 * @author �������㡣
 * @time 2019��12��14������9:32:57
 * @version V1.0
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import compiler.Token.TYPE;

public class ActiveInf {

	/* ��Ծ��Ϣ���ɱ�  */
	private HashMap<String,Boolean> SYMBL;
	
	/* ��Ծ��Ϣ����ջ  */
	private Stack<Token> stack;
	
	/**
	 * ��ʼ����Ϣ��
	 * @param quatList
	 */
	private void initTable(ArrayList<Quat> quatList) {
		SYMBL = new HashMap<String, Boolean>();
		for(int i = quatList.size() - 1; i >= 0;i--) {
			//�Ƚ����������뵽Map��
			Quat quat = quatList.get(i);
			add(quat.res);
			add(quat.ope2);
			add(quat.ope1);		
		}

	}
	
	
	/**
	 * 	����µı�������Ϣ����
	 * @param token
	 */
	private void add(Token token) {
		//�������û�е�ǰ����
		if(!SYMBL.containsKey(token.getSvalue()) && token.gettype() == TYPE.i) {
			if(token.getSvalue().charAt(0) != 't')
				SYMBL.put(token.getSvalue(), true);
			else
				SYMBL.put(token.getSvalue(), false);
		}	
			
	}
	
	/**
	 * ���»�Ծ��Ϣ
	 * @param quatList
	 */
	private void updata(ArrayList<Quat> quatList) {
		/***
		 * ���˼·�����������Ԫʽ����
		 * 		   �����������뵽��Ծ��Ϣ��
		 */
		for(int i = quatList.size() - 1; i >= 0;i--) {
			Quat quat = quatList.get(i);
			//�Ƚ���Ԫʽ�е����ݸ���
			//����Ǳ���
			if(SYMBL.containsKey(quat.res.getSvalue()))
				quat.setActiveRes(SYMBL.get(quat.res.getSvalue()));
			if(SYMBL.containsKey(quat.ope2.getSvalue()))
				quat.setActiveOpe2(SYMBL.get(quat.ope2.getSvalue()));
			if(SYMBL.containsKey(quat.ope1.getSvalue()))
				quat.setActiveOpe1(SYMBL.get(quat.ope1.getSvalue()));
			//�ٷ�д��Ծ��Ϣ��
			//�����res�Ļ�ֻ��Ҫ��дn/y
			if(SYMBL.containsKey(quat.res.getSvalue()))
				if(SYMBL.get(quat.res.getSvalue()))
					SYMBL.put(quat.res.getSvalue(), false);
				else
					SYMBL.put(quat.res.getSvalue(), true);
			
			//����ǲ������Ļ������д��ǰ���
			if(SYMBL.containsKey(quat.ope2.getSvalue()))
				if(SYMBL.get(quat.ope2.getSvalue()))
					SYMBL.put(quat.ope2.getSvalue(), false);
				else
					SYMBL.put(quat.ope2.getSvalue(), true);
			//����ǲ������Ļ������д��ǰ���
			if(SYMBL.containsKey(quat.ope1.getSvalue()))
				if(SYMBL.get(quat.ope1.getSvalue()))
					SYMBL.put(quat.ope1.getSvalue(), false);
				else
					SYMBL.put(quat.ope1.getSvalue(), true);
					
		}
	}
	
	
	/**
	 * ����DAG�Ż������Ԫʽ���ɻ�Ծ��Ϣ��
	 * @param quatList
	 * @return
	 */
	public HashMap<String, Boolean> creatInf(ArrayList<Quat> quatList){
		initTable(quatList);
		for(String token : SYMBL.keySet())
			System.out.println(token + "\t" + SYMBL.get(token));
		updata(quatList);
//		System.out.println("��Ԫʽ��Ϣ��");
//		for(Quat quat : quatList) {
//			System.out.println("("+quat.opc+","+quat.ope1.getSvalue()+"("+quat.isActiveOpe1()+"),"
//					+quat.ope2.getSvalue()+"("+quat.isActiveOpe2()+"),"
//					+quat.res.getSvalue()+"("+quat.isActiveRes()+") )"
//					);
//		}
		return SYMBL;
	}
}
