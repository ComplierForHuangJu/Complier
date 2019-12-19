package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import compiler.Token.TYPE;

public class CodeProducerTest {

	/* Ŀ������� */
	static ArrayList<String> OBJ;
	
	/* �Ż������Ԫʽ���� */
	static ArrayList<Quat> QT;
	
	static ArrayList<String> DEFINE = new ArrayList<String>();
	
	/* ��Ծ��Ϣ�� */
	static HashMap<String, Boolean> SYMBL;
	
	/* �Ĵ���״̬������ */
	static HashMap<String, ActiveTable> RDL;
	
	/* */
	static SymbolTable table = new SymbolTable();
	
	/* ����ջ */
	static Stack<Integer> SEM;
	
	/**
	 * ��ʼ��������Ϣ
	 */
	public static void init() {
		OBJ = new ArrayList<String>();
		//QT = Optimize._opQuat;
		//���������Ĵ���
		RDL = new HashMap<String, ActiveTable>();
		RDL.put("AX", new ActiveTable("0",false));
		SEM = new Stack<Integer>();
		//SEM.push(5);
		QT = new ArrayList<Quat>();
		Token token0 = new Token(TYPE.i,"a");
		Token token1 = new Token(TYPE.i,"b");
		Token token2 = new Token(TYPE.i,"t1");
		Token token3 = new Token(TYPE.i,"t2");
		Token token4 = new Token(TYPE.i,"t3");
		Token token5 = new Token(TYPE.i,"c");
		Token token6 = new Token(TYPE.i,"x");
		//QT.add(new Quat(">",token0,token1,token2));
		//QT.add(new Quat("if",token2,new Token("_"),new Token("_")));
		QT.add(new Quat("+",token0,token1,token3));
		QT.add(new Quat("*",token3,token5,token6));
		//QT.add(new Quat("el",new Token("_"),new Token("_"),new Token("_")));
//		QT.add(new Quat("*",token0,token1,token4));
//		QT.add(new Quat("-",new Token(TYPE.inc,"5"),token4,token6));
//		QT.add(new Quat("ie",new Token("_"),new Token("_"),new Token("_")));
		SYMBL = new ActiveInf().creatInf(QT);
		
	}
	
	/**
	 * ���ɱ�������Ŀ�����
	 */
	public static void creatVaribleCode() {
		ArrayList<String> list = new ArrayList<String>();
		for(String str : SYMBL.keySet()) {
			list.add(str);
			}

		for(String str : list)
			DEFINE.add(str + " DB "+"["+str+"]"+" DUP(0)");

			
	}
	
	/**
	 * ��������
	 * @param op  ������
	 * @param R   �Ĵ�������
	 * @param B	     ����
	 */
	public  static void Code(String op,String R,String B) {
		//������ű��д��ڵ�ǰ����
		switch(op) {
			case "LD": OBJ.add("MOV "+R+", ["+B+"]");break;
			case "ST":OBJ.add("MOV "+R+", ["+B+"]");break;
			case "+":OBJ.add("ADD "+R+", ["+B+"]");break;
			case "-":OBJ.add("SUB "+R+", ["+B+"]");break;
			case "*":OBJ.add("MUL "+R+", ["+B+"]");break;
			case "/":OBJ.add("DIV "+R+", ["+B+"]");break;
			case "end":OBJ.add("INT 21H");break;
			}
		}
	
	/**
	 * ��������
	 * @param op  ������
	 * @param R   �Ĵ�������
	 * @param B	     ����
	 */
	public  static void Code(String op,String R,int B) {
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
	public static void Back(int i,int j) {
		OBJ.get(i).concat(String.valueOf(j));
	}
	/**
	 * ʵ�ʴ������ɵ�����߼����ܿؼĴ���RDL�����Ϣ
	 */
	public static void produce() {
		for(int i = 0; i < QT.size();i++) {
			Quat quat = QT.get(i);
			/**
			 * ���ݲ�ͬ�Ĳ������в�ͬ�Ĵ����߼�
			 */
			switch(quat.opc) {
				case "=":{
							//���жϼĴ����Ƿ�ռ��
							if(!RDL.get("AX").isActive()) {
								//�����ǰ�Ĵ�����������Bռ��
								if(RDL.get("AX").getName().equals(quat.ope1.getSvalue()))
								{
									//B�ǻ�Ծ��Ϣ
									if(quat.isActiveOpe1())
										Code("ST",quat.ope1.getSvalue(),"AX");
								}
								//�Ĵ�����������Ծ��Ϣռ��
								else
								{
									//��Ծ������Ҫ����
									if(RDL.get("AX").isActive())
									{
										Code("ST",RDL.get("AX").getName(),"AX");
										Code("LD","AX",quat.ope1.getSvalue());
									}
								}
							}else
								Code("LD","AX",quat.ope1.getSvalue());
							//����ǰ��������ȼ���RDL��
							RDL.put("AX", new ActiveTable(quat.res.getSvalue(),quat.isActiveRes()));
							break;
						}
				case "if":{
					//���жϼĴ����Ƿ�ռ��
					if(!RDL.get("AX").getName().equals("0")) {
						//�����ǰ�Ĵ�����������Bռ��
						if(RDL.get("AX").getName().equals(quat.ope1.getSvalue()))
						{
							//B�ǻ�Ծ��Ϣ
							if(quat.isActiveOpe1())
								Code("ST",quat.ope1.getSvalue(),"AX");
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new ActiveTable("0",false));
								
						}
						//�Ĵ�����������Ծ��Ϣռ��
						else
						{
							//��Ծ������Ҫ����
							if(RDL.get("AX").isActive())
								Code("ST",RDL.get("AX").getName(),"AX");
								
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new ActiveTable("0",false));
						}
					}else
						{
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");
							SEM.push(i);
						}
					//����ǰ��������ȼ���RDL��
					RDL.put("AX", new ActiveTable(quat.res.getSvalue(),quat.isActiveRes()));
					System.out.println("ִ��if��");
					System.out.println(RDL.get("AX").getName() + RDL.get("AX").isActive());
					break;
				}
				
				case "el":{
					if(RDL.get("AX").isActive())
						Code("ST",quat.ope1.getSvalue(),"AX");
					int K = SEM.pop();
					Back(K,i+2);
					OBJ.add("JMP");
					SEM.push(i);
					break;
					}
				
				case "ie":{
					if(RDL.get("AX").isActive())
						Code("ST",RDL.get("AX").getName(),"AX");
					int K = SEM.pop();
					Back(K,i+1);
					break;
				}
				
				case "wh":SEM.push(i);break;
				
				case "do":{
					//���жϼĴ����Ƿ�ռ��
					if(!RDL.get("AX").equals("0")) {
						//�����ǰ�Ĵ�����������Bռ��
						if(RDL.get("AX").getName().equals(quat.ope1.getSvalue()))
						{
							//B�ǻ�Ծ��Ϣ
							if(quat.isActiveOpe1())
								Code("ST",quat.ope1.getSvalue(),"AX");
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new ActiveTable("0",false));
								
						}
						//�Ĵ�����������Ծ��Ϣռ��
						else
						{
							//��Ծ������Ҫ����
							if(RDL.get("AX").isActive())
								Code("ST","AX",RDL.get("AX").getName());
								
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");	
							SEM.push(i);
							RDL.put("AX", new ActiveTable("0",false));
						}
					}else
						{
							Code("LD","AX",quat.ope1.getSvalue());
							OBJ.add("FJ "+"AX,");
							SEM.push(i);
						}
					//����ǰ��������ȼ���RDL��
					RDL.put("AX", new ActiveTable(quat.res.getSvalue(),quat.isActiveRes()));
					break;
				}
				
				case "we":{
					//��Ծ������Ҫ����
					if(RDL.get("AX").isActive())
						Code("ST",RDL.get("AX").getName(),"AX");
					RDL.put("AX", new ActiveTable("0",false));
					int K = SEM.pop();
					Back(K,i+2);
					OBJ.add("JMP "+K);
					break;
				}
				
			default:{
				//���жϼĴ����Ƿ�ռ��
				if(!RDL.get("AX").getName().equals("0")) {
					//�����ǰ�Ĵ�����������Bռ��
					if(RDL.get("AX").getName().equals(quat.ope1.getSvalue()))
					{
						//B�ǻ�Ծ��Ϣ
						if(quat.isActiveOpe1())
							Code("ST",quat.ope1.getSvalue(),"AX");
					}
					//�Ĵ�����������Ծ��Ϣռ��
					else
					{
						//��Ծ������Ҫ����
						if(RDL.get("AX").isActive())
						{
							Code("ST",RDL.get("AX").getName(),"AX");
							Code("LD","AX",quat.ope1.getSvalue());
						}
					}
				}else
					Code("LD","AX",quat.ope1.getSvalue());
						
				
				Code(quat.opc,"AX",quat.ope2.getSvalue());
				//����ǰ��������ȼ���RDL��
				RDL.put("AX", new ActiveTable(quat.res.getSvalue(),quat.isActiveRes()));
				break;
			}
				
			}
			
			
		}
	}
	public static void main(String[] args) {
		init();
		System.out.println("��Ԫʽ��Ϣ��");
		for(Quat quat : QT) {
			System.out.println("("+quat.opc+","+quat.ope1.getSvalue()+"("+quat.isActiveOpe1()+"),"
					+quat.ope2.getSvalue()+"("+quat.isActiveOpe2()+"),"
					+quat.res.getSvalue()+"("+quat.isActiveRes()+") )"
					);
		}
		creatVaribleCode();
		System.out.println("����������Ŀ����룺");
		for(String str : DEFINE) {
			System.out.println(str);
		}
		
		produce();
		System.out.println("Ŀ����룺");
		for(String str : OBJ){
			System.out.println(str);
		}

	}

}
