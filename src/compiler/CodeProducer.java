package compiler;
/**
 * 
 * ��Ŀ���ƣ�Complier
 * Ŀ���������
 * @author �������㡣
 * @time 2019��12��16������3:18:21
 * @version V1.0
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class CodeProducer {
	
	/* Ŀ������� */
	ArrayList<String> OBJ;
	
	/* �Ż������Ԫʽ���� */
	ArrayList<Quat> QT;
	
	ArrayList<String> DEFINE = new ArrayList<String>();
	
	/* ��Ծ��Ϣ�� */
	HashMap<Token,Character> SYMBL;
	
	/* �Ĵ���״̬������ */
	HashMap<String, Token> RDL;
	
	/* */
	SymbolTable table = new SymbolTable();
	
	/* ����ջ */
	Stack<Integer> SEM;
	
	/**
	 * ��ʼ��������Ϣ
	 */
	public void init() {
		OBJ = new ArrayList<String>();
		QT = Optimize._opQuat;
		SYMBL = new ActiveInf().creatInf(QT);
		//���������Ĵ���
		RDL = new HashMap<String, Token>();
		RDL.put("AX", new Token("0"));
		//RDL.put("BX", "0");
		//RDL.put("DX", "0");
	}
	
	/**
	 * ���ɱ�������Ŀ�����
	 */
	public void creatVaribleCode() {
		ArrayList<Token> list = new ArrayList<Token>();
		for(Token token : SYMBL.keySet()) {
			list.add(token);
			}
		Token[] array = (Token[])list.toArray(new Token[list.size()]);
		
		//����addr����
		for(int i = 0; i < list.size() - 1;i++) {
			for(int j = 0; j < list.size() -1 - i;j++) {
				if(table.IDsymbol.get(array[j].getSvalue()).addr > table.IDsymbol.get(array[j+1].getSvalue()).addr) {
					Token temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
			
		}
		
		
		for(int i = 0; i < list.size() - 1;i++) {
			DEFINE.add(array[i].getSvalue() + " DB "+table.IDsymbol.get(array[i + 1].getSvalue()).addr+" DUP(0)");
		}
		
		
			
	}
	
	/**
	 * ��������
	 * @param op  ������
	 * @param R   �Ĵ�������
	 * @param B	     ����
	 */
	public void Code(String op,String R,String B) {
		//������ű��д��ڵ�ǰ����
		switch(op) {
			case "LD": OBJ.add("MOV "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "ST":OBJ.add("MOV "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "+":OBJ.add("ADD "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "-":OBJ.add("SUB "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "*":OBJ.add("MUL "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "/":OBJ.add("DIV "+R+", ["+table.IDsymbol.get(B).addr+"]");break;
			case "end":OBJ.add("INT 21H");break;
			}
		}
	
	/**
	 * ��������
	 * @param op  ������
	 * @param R   �Ĵ�������
	 * @param B	     ����
	 */
	public void Code(String op,String R,int B) {
		//������ű��д��ڵ�ǰ����
		switch(op) {
			case "LD": OBJ.add("MOV "+R+","+B);break;
			case "+":OBJ.add("ADD "+R+","+B);break;
			case "-":OBJ.add("SUB "+R+","+B);break;
			case "*":OBJ.add("MUL "+R+","+B);break;
			case "/":OBJ.add("DIV "+R+","+B);break;
			case "end":OBJ.add("INT 21H");break;
			}
		}
	
	/**
	 * ����ַpk���pi��
	 * @param i
	 * @param j
	 */
	public void Back(int i,int j) {
		OBJ.get(i).concat(String.valueOf(j));
	}
	/**
	 * ʵ�ʴ������ɵ�����߼����ܿؼĴ���RDL�����Ϣ
	 */
	public void produce() {
		for(int i = 0; i < QT.size() - 1;i++) {
			Quat quat = QT.get(i);
			/**
			 * ���ݲ�ͬ�Ĳ������в�ͬ�Ĵ����߼�
			 */
			switch(quat.opc) {
				case "=":{
							//���жϼĴ����Ƿ�ռ��
							if(!RDL.get("AX").getSvalue().equals("0")) {
								//�����ǰ�Ĵ�����������Bռ��
								if(RDL.get("AX").getSvalue().equals(quat.ope1.getSvalue()))
								{
									//B�ǻ�Ծ��Ϣ
									if(quat.ope1.getIsActive() != 'n')
										Code("ST","AX",quat.ope1.getSvalue());
								}
								//�Ĵ�����������Ծ��Ϣռ��
								else
								{
									//��Ծ������Ҫ����
									if(RDL.get("AX").getIsActive() != 'n')
									{
										Code("ST","AX",RDL.get("AX").getSvalue());
										Code("LD","AX",quat.ope1.getSvalue());
									}
								}
							}else
								Code("LD","AX",quat.ope1.getSvalue());
							//����ǰ��������ȼ���RDL��
							RDL.put("AX", quat.res);
							break;
						}
				case "if":{
					//���жϼĴ����Ƿ�ռ��
					if(!RDL.get("AX").getSvalue().equals("0")) {
						//�����ǰ�Ĵ�����������Bռ��
						if(RDL.get("AX").getSvalue().equals(quat.ope1.getSvalue()))
						{
							//B�ǻ�Ծ��Ϣ
							if(quat.ope1.getIsActive() != 'n')
								Code("ST","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new Token("0"));
								
						}
						//�Ĵ�����������Ծ��Ϣռ��
						else
						{
							//��Ծ������Ҫ����
							if(RDL.get("AX").getIsActive() != 'n')
								Code("ST","AX",RDL.get("AX").getSvalue());
								
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new Token("0"));
						}
					}else
						{
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");
							SEM.push(i);
						}
					//����ǰ��������ȼ���RDL��
					RDL.put("AX", quat.res);
					break;
				}
				
				case "el":{
					if(RDL.get("AX").getIsActive() != 'n')
						Code("ST","AX",RDL.get("AX").getSvalue());
					int K = SEM.pop();
					Back(K,i+2);
					OBJ.add("JMP");
					SEM.push(i);
					break;
					}
				
				case "ie":{
					if(RDL.get("AX").getIsActive() != 'n')
						Code("ST","AX",RDL.get("AX").getSvalue());
					int K = SEM.pop();
					Back(K,i+1);
					break;
				}
				
				case "wh":SEM.push(i);break;
				
				case "do":{
					//���жϼĴ����Ƿ�ռ��
					if(!RDL.get("AX").getSvalue().equals("0")) {
						//�����ǰ�Ĵ�����������Bռ��
						if(RDL.get("AX").getSvalue().equals(quat.ope1.getSvalue()))
						{
							//B�ǻ�Ծ��Ϣ
							if(quat.ope1.getIsActive() != 'n')
								Code("ST","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new Token("0"));
								
						}
						//�Ĵ�����������Ծ��Ϣռ��
						else
						{
							//��Ծ������Ҫ����
							if(RDL.get("AX").getIsActive() != 'n')
								Code("ST","AX",RDL.get("AX").getSvalue());
								
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new Token("0"));
						}
					}else
						{
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");
							SEM.push(i);
						}
					//����ǰ��������ȼ���RDL��
					RDL.put("AX", quat.res);
					break;
				}
				
				case "we":{
					//��Ծ������Ҫ����
					if(RDL.get("AX").getIsActive() != 'n')
						Code("ST","AX",RDL.get("AX").getSvalue());
					RDL.put("AX", new Token("0"));
					int K = SEM.pop();
					Back(K,i+2);
					OBJ.add("JMP "+K);
					break;
				}
				
			default:{
				//���жϼĴ����Ƿ�ռ��
				if(!RDL.get("AX").getSvalue().equals("0")) {
					//�����ǰ�Ĵ�����������Bռ��
					if(RDL.get("AX").getSvalue().equals(quat.ope1.getSvalue()))
					{
						//B�ǻ�Ծ��Ϣ
						if(quat.ope1.getIsActive() != 'n')
							Code("ST","AX",quat.ope1.getSvalue());
					}
					//�Ĵ�����������Ծ��Ϣռ��
					else
					{
						//��Ծ������Ҫ����
						if(RDL.get("AX").getIsActive() != 'n')
						{
							Code("ST","AX",RDL.get("AX").getSvalue());
							Code("LD","AX",quat.ope1.getSvalue());
						}
					}
				}else
					Code("LD","AX",quat.ope1.getSvalue());
						
				
				Code(quat.res.getSvalue(),"AX",quat.ope2.getSvalue());
				//����ǰ��������ȼ���RDL��
				RDL.put("AX", quat.res);
				break;
			}
				
			}
			
			
		}
	}
	
	
	
	
}
