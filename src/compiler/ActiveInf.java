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
	private HashMap<Token,Character> SYMBL;
	
	/* ��Ծ��Ϣ����ջ  */
	private Stack<Token> stack;
	
	/**
	 * ��ʼ����Ϣ��
	 * @param quatList
	 */
	private void initTable(ArrayList<Quat> quatList) {
		SYMBL = new HashMap<Token, Character>();
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
		if(!SYMBL.containsKey(token) && token.gettype() == TYPE.i)
			if(token.getSvalue().charAt(0) != 't')
				SYMBL.put(token, 'y');
			else
				SYMBL.put(token, 'n');
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
			quat.res.setIsActive(SYMBL.get(quat.res));
			quat.ope2.setIsActive(SYMBL.get(quat.ope2));
			quat.ope1.setIsActive(SYMBL.get(quat.ope1));
			//�ٷ�д��Ծ��Ϣ��
			//�����res�Ļ�ֻ��Ҫ��дn/y
			if(SYMBL.get(quat.res) != 'n')
				SYMBL.put(quat.res, 'n');
			else
				SYMBL.put(quat.res, 'y');
			
			//����ǲ������Ļ������д��ǰ���
			if(SYMBL.get(quat.ope2) != 'n')
				SYMBL.put(quat.ope2, 'n');
			else
				SYMBL.put(quat.ope2, (char)(i + '0'));
					
		}
	}
	
	
	/**
	 * ����DAG�Ż������Ԫʽ���ɻ�Ծ��Ϣ��
	 * @param quatList
	 * @return
	 */
	public HashMap<Token, Character> creatInf(ArrayList<Quat> quatList){
		initTable(quatList);
		updata(quatList);
		return SYMBL;
	}
}
