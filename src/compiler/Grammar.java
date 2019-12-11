package compiler;

public class Grammar {

	public static void main(String[] args) {
		package Compiler;


		import java.io.FileReader;
		import java.util.*;


		import java.io.BufferedReader;


		public class Grammar {
			private String left;
			private ArrayList<String> right = new ArrayList<String>();
			private int id;//����ʽ���
			static public String Start;//����ʽ��ʼ����
			static ArrayList<Grammar> W = new ArrayList<Grammar>();//�洢���в���ʽ������
			static private HashSet<String> VnSet = new HashSet<String>();//�洢���з��ս��
		    static private HashSet<String> VdSet = new HashSet<String>();//�洢�����ս��
		    static private HashMap<String,HashSet<String>> firstSet = new  HashMap<String, HashSet<String>>();//���ս�����׸��ż���
			static private HashMap<String,HashSet<String>> followSet = new HashMap<String, HashSet<String>>();//���ս����follow����
			//static public HashMap<String, ArrayList<ArrayList<String>>> expSet = new HashMap<String, ArrayList<ArrayList<String>>>();//�����б��ʽ
		    public Grammar() {}
			public Grammar(int c) {//���캯��
				id=c;
			}
			
			public void showGrammar()//����ķ�
			{
				System.out.print(left);
				System.out.print("->");
				for(int i=0;i<right.size();i++)//��ӡ����ʽ�Ҳ�
				{
					//System.out.print(right.size());
					System.out.print(right.get(i));
					System.out.print(" ");
				}
				
			}
			public void setR(String a)//��a��ӵ��Ҳ���
			{
				right.add(a);
			}
			public ArrayList<String> getRT()//���������Ҳ�
			{
				return right;
			}
			public String getRI(int i)//�����Ҳ���i��Ԫ��
			{
				String a=right.get(i);
				return a;
			}
			public void setL(String a)//���ò���ʽ��
			{
				left=a;
			}
			public String getL(int i)//���ز���ʽ��
			{
				return left;
			}
			public void assiR(ArrayList<String> a)//��a���Ƹ��Ҳ�
			{
				for(int i=0;i<a.size();i++)
				{
					right.add(a.get(i));
				}
			}
			public boolean Contain(String a)//�ж��Ҳ��Ƿ���a
			{
				int f=0;
				for(int i=0;i<right.size();i++)
				{
					if(a.equals(right.get(i)))
					{
					  f=1;
					  break;
					}
					
				}
				if(f==1)
					return true;
				else
					return false;
			}
			
		    public int getIndex(String a)//����a���Ҳ��е�λ��
		    {
		    	int index=-1;
		    	for(int i=0;i<right.size();i++)
		    	{
		    		if(a.equals(right.get(i)))
		    			index=i;
		    			
		    	}
		    	return index;
		    }
			
			 public static void initGrammar()//��ʼ���ķ������������ս���ͷ��ս��
			{
				 
				 BufferedReader bufferedReader = null;
				try {
					bufferedReader = new BufferedReader(new FileReader("WENFA.txt"));
					String string;
					String lefts;
					int idd=0;
					while((string=bufferedReader.readLine())!=null)
			    	{
						
						//System.out.print(string);
						idd++;
						Grammar g = new Grammar(idd);
						String s;
			    		string=string.trim();
			    		String[] b = string.split("->");
			    		lefts = b[0];
			    		
			    		if(idd==1)
			    			Start = lefts;
			    		if(!VnSet.contains(lefts))
			    		{
			    			VnSet.add(lefts);
			    		}
			    		g.left=lefts;
			    		s = b[1];
			    		String[] c = s.split(" ");
			    		for(int i=0; i<c.length; i++)
			    		{
			    			g.right.add(c[i]);
			    		}
			    		
			    		g.showGrammar();
			    		System.out.print("\n");
			    		W.add(g);	    		
			    	}
				
			}catch(Exception e) {
				e.printStackTrace();
				
			}
				
				//List<String> wt= new ArrayList<String>(W);
			for(int i=0;i<W.size();i++)//���������ս����
			{
				for(int j=0;j<W.get(i).getRT().size();j++)
				{
					if((!VdSet.contains(W.get(i).getRI(j))) && (!VnSet.contains(W.get(i).getRI(j))))
						VdSet.add(W.get(i).getRI(j));
					else
						continue;
				}
			}
				
		}
		public static void getFirstSet(String target)//�����׷��ż���
			{
				int countEmpty = 0;
				int isEmpty = 0;
				HashSet<String> H = firstSet.containsKey(target) ? firstSet.get(target) : new HashSet<String>();
				//HashSet<String> H = new HashSet<String>();//��ʱ��¼ÿһ�����ս�����׷��ż���
			    for(int i=0;i<W.size();i++)
			    {
			    	if(W.get(i).left.equals(target))
			    	{
			    		if(VdSet.contains(W.get(i).right.get(0))) //���ʽ�Ҳ���һ����ĸ���ս��
			    		{
			    			H.add(W.get(i).right.get(0));
			    		}
			    		else
			    		{
			    			for(int j=0;j<W.get(i).right.size();j++)// X->Y1..Yj..Yk��һ������ʽ
			    			{
			    				if(VdSet.contains(W.get(i).right.get(j)))
			    				{
			    					if(!H.contains(W.get(i).right.get(j))){// Yj���ս��(���ܲ�����),FIRST(Yj)=Yj����FIRST(X),���ܼ�������,����
			    						H.add(W.get(i).right.get(j));
			    					}
			    					break;
			    				}
			    				getFirstSet(W.get(i).right.get(j));
			    				HashSet<String> Set = firstSet.get(W.get(i).right.get(j));
			    				
			    				if(Set.contains("$")) {
			    					isEmpty = 1;
			    				}
			    				else//��FIRST(Yj)�еķǿվͼ���FIRST(X)
			    					{
			    						Set.remove("$");
			    						//H.addAll(Set);
			    						for(String a : Set)
			    						{
			    							if(!H.contains(a))
			    								H.add(a);
			    							else
			    								continue;
			    						}
			    						
			    					}
			    				if(isEmpty == 0)
			    					break;
			    				else
			    				{
			    					countEmpty += isEmpty;
			    					isEmpty = 0;
			    				}
			    			}
			    			if(countEmpty == W.get(i).right.size())//�����Ҳ�first(Y)����$(��),��$����FIRST(X)��
			    			{
			    				H.add("$");
			    			}
			    			
			    		}
			    		}
			    			
			    	}
			    firstSet.put(target,H);
			
			}
		public static void getFollowSet(String target)//����ս��target��follow����
		{
			int isEmpty = 0;
			HashSet<String> set = (followSet.containsKey(target))? followSet.get(target) : new HashSet<String>();
				if(Start.equals(target)) 
					set.add("#");  			//�������ż���follow(S)		
				 for (int i=0;i<W.size();i++)
				 {  //�������в���ʽ
					 if(W.get(i).Contain(target))//��i������ʽ�Ҳ����д�����ս��
					 {
						 int index = W.get(i).getIndex(target);//�÷��ս�����Ҳ��е�λ��
						 
						 if(index == W.get(i).right.size()-1 && (!target.equals(W.get(i).left)))//��ǰ�������Ҳ����һ������A->....T
						 {
							 getFollowSet(W.get(i).left);//�ݹ�������󲿵�follow���ϼ��뵱ǰ����follow������
							 HashSet<String> set1 = followSet.get(W.get(i).left);
							 for(String a: set1)
							 {
								 set.add(a);
							 }
							 
						 }
						 //if(VdSet.contains(W.get(i).right.get(index+1)) && index!= W.get(i).right.size()-1)//�����Ҳ����һ�����Һ���һ���������ս��
						 else if(index != W.get(i).right.size()-1)//�����Ҳ����һ��
						 {
							if(VdSet.contains(W.get(i).right.get(index+1)))
							{  //��һ�����ս��
							 set.add(W.get(i).right.get(index+1));
							 //break;
							}
						 
						 else //A->...TB....
						 {
							 int id=index;
							 int len = W.get(i).right.size() - index-1;//T���м�������
							 int lenth=len;
							 while(len>0)
							 {
							    String nex = W.get(i).right.get(id++);
							    if(!(firstSet.get(nex).contains("$")))//���һ�����ս����first���ϲ��ɿ�
							    {
								    set.addAll(firstSet.get(nex));//�Ѻ�һ�����ս����first����ȫ������
								    break;//����ѭ��
							    }
							    else//��һ�����ſɿ�
							    {
							    for(String a : firstSet.get(nex))
							    {
								 if(a.equals("$"))//B�к��п��ַ�
									 isEmpty++;
								  else
									 set.add(a);//��B��first���������зǿ��ַ�����T��follow����
							     }
							     
							    }
							    len--;
							 }
							 if(isEmpty==lenth)//��߷���ȫ�����ǿ�
							 {
								 getFollowSet(W.get(i).left);
								 HashSet<String> set2 = followSet.get(W.get(i).left);
								 for(String a : set2)
								 {
									 set.add(a);
								 }
							 }
								 
						 }
					   
					 }
				 }
				 
			}
				 followSet.put(target, set);
		}
		 
		 public static void main(String[] args)throws Exception {
		     Grammar.initGrammar();
		     System.out.println("���ս������");
		    for(String a : VnSet)
		    {
		    	System.out.println(a);
		    }
		    System.out.println("�ս������");
		    for(String a : VdSet) {
		    	System.out.println(a);
		    }
		    for(String a : VnSet)
		    {
		    	//System.out.print(a);
		    	getFirstSet(a);
		    	getFollowSet(a);
		    	HashSet<String> Q = new HashSet<String>();
		    	Q = followSet.get(a);
		    	for(String b : Q)
		    	{
		    		System.out.print(b);
		    	}
		    	
		    }
		    for(String a : VnSet)
		    {
		    	System.out.print(a+"firstset: ");
		    	for(String b : firstSet.get(a))
		    	{
		    		
		    		System.out.print(b);
		    	}
		    	System.out.print("\n");
		    		
		    }
		    for(String a : VnSet)
		    {
		    	System.out.print(a+"followset: ");
		    	for(String b : followSet.get(a))
		    	{
		    		
		    		System.out.print(b);
		    	}
		    	System.out.print("\n");
		    		
		    }
		  
				}
						
		}





	}

}
