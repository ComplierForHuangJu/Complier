package compiler;


import java.io.FileReader;
import java.util.*;


import java.io.BufferedReader;


public class Grammar {
	private String left;
	private ArrayList<String> right = new ArrayList<String>();
	private int id;//����ʽ���
	static public String Start;//����ʽ��ʼ����
	static public ArrayList<Grammar> W = new ArrayList<Grammar>();//�洢���в���ʽ������
	static public HashSet<String> VnSet = new HashSet<String>();//�洢���з��ս��
    static public HashSet<String> VdSet = new HashSet<String>();//�洢�����ս��
    static public HashMap<String,HashSet<String>> firstSet = new  HashMap<String, HashSet<String>>();//���ս�����׸��ż���
	static public HashMap<String,HashSet<String>> followSet = new HashMap<String, HashSet<String>>();//���ս����follow����
	//static public HashMap<String, ArrayList<ArrayList<String>>> expSet = new HashMap<String, ArrayList<ArrayList<String>>>();//�����б��ʽ
    static public int[][] Table;//������
    static public Object[] VdArray;//�ս��hashsetת����������
    static public Object[] VnArray; //���ս��hashsetת����������
    static public String[] VdA;
    static public String[] VnA;
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
		//System.out.print(id);
		System.out.print("\n");
		
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
	public String getL()//���ز���ʽ��
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
	 public boolean isRightNull()//�ж�һ������ʽ�Ҳ��Ƿ�Ϊ��
	{
		if(right.size()==1 && right.get(0).equals("$")) {
			return true;
		}
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
    			
    		//else System.out.print(right.get(i));
    	}
    	return index;
    }
    static public int getVnIndex(String a)//���ط��ս��������ĳ��Ԫ���±�
    {
    	int index = -1;
    	for(int i=0;i<VnArray.length;i++)
    	{
    		if(a.equals(VnArray[i]))
    			index=i;
    	}
    	return index;
    }
    static public int getVdIndex(String a)//�����ս��������ĳ��Ԫ���±�
    {
    	int index = -1;
    	for(int i=0;i<VdArray.length;i++)
    	{
    		if(a.equals(VdArray[i]))
    			index=i;
    	}
    	return index;
    }
  static public int getVdAIndex(String a)//������VdA�з���ָ��Ԫ��λ��
   {
	   int index =-1;
	   for(int i=0;i<VdA.length;i++)
	   {
		   if(VdA[i].equals(a))
			   index=i;
	   }
	   return index;
   }
  static public int getVnAIndex(String a)//������VnA�з���ָ��Ԫ��λ��
  {
	   int index =-1;
	   for(int i=0;i<VnA.length;i++)
	   {
		   if(VnA[i].equals(a))
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
			while((string=bufferedReader.readLine())!=null)//���ļ��ж�ȡ�ķ�
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
	    	//System.out.print(W.get(i).left);
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
	    				if(!W.get(i).left.equals(W.get(i).right.get(j))) {
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
	//int isEmpty = 0;
	HashSet<String> set = (followSet.containsKey(target))? followSet.get(target) : new HashSet<String>();
		if(Start.equals(target)) 
			set.add("#");  			//�������ż���follow(S)		
		 for (int i=0;i<W.size();i++)
		 {  //�������в���ʽ
			 int isEmpty = 0;
			 if(W.get(i).Contain(target))//��i������ʽ�Ҳ����д�����ս��
			 {
				 int index = W.get(i).getIndex(target);//�÷��ս�����Ҳ��е�λ��
				 
				 if(index == W.get(i).right.size()-1 && (!target.equals(W.get(i).left)))//��ǰ�������Ҳ����һ������A->....T
				 {
					 //System.out.println(target+"�ݹ����"+W.get(i).left);
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
					 //System.out.print(id);
					 int len = W.get(i).right.size() - index-1;//T���м�������
					 int lenth=len;
					 //System.out.print(len);
					 while(len>0)
					 {
					    String nex = W.get(i).right.get(++id);
					    //System.out.print(nex);
					    if(VdSet.contains(nex)) {
					    	set.add(nex);
					    	break;
					    }
					 else if(!(firstSet.get(nex).contains("$")))//���һ�����ս����first���ϲ��ɿ�
					    {
						    set.addAll(firstSet.get(nex));//�Ѻ�һ�����ս����first����ȫ������
						    break;//����ѭ��
					    }
					    else if(firstSet.get(nex).contains("$"))//��һ�����ſɿ�
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
						 if(!W.get(i).left.equals(target)) {
							 //System.out.println(target+"�ݹ����"+W.get(i).left);
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
		 
	}
		 followSet.put(target, set);
		// System.out.print(target);
		 /*for(String a : followSet.get(target)) {
			 System.out.print(a);
		 }
		 System.out.print("\n");*/
}


static public void  getTable()//���ɷ�����
{
	//VdSet.add("#");
	VdArray = VdSet.toArray();
	VnArray = VnSet.toArray();
	VdA = new String[VdArray.length+1];//�����һ��#
	VnA = new String[VnArray.length];
	for(int i=0;i<VdArray.length;i++)
	{
		VdA[i]=VdArray[i].toString();
	}
	for(int i=0;i<VnArray.length;i++)
	{
		VnA[i]=VnArray[i].toString();
	}
	VdA[VdArray.length] = "#";//���ս���м���#�����ڷ��ű������һ����#
    int l = VdArray.length;
    Table = new int[VnArray.length][VdArray.length+1];
    for(int i=0;i<W.size();i++)
    {
    	int row = getVnAIndex(W.get(i).left);
    	int emptyCount = 0;
    	for(int j=0; j< W.get(i).right.size();j++)
    	{
    		String temp = W.get(i).right.get(j);
    		if(VdSet.contains(temp))
    		{
    			if(!temp.equals("$"))
    				Table[row][getVdAIndex(temp)] = i+1;
    			if(temp.equals("$"))
    				emptyCount++;
    			break;
    		}
    		else// tmp�Ƿ��ս��
    		{
    			//int tempIndex = getVnIndex(temp);
    			for(int k = 0; k< firstSet.get(temp).size();k++)
    			{
    				 Object[] firstArray = firstSet.get(temp).toArray();
    				
    				Table[row][getVdAIndex(firstArray[k].toString())] = i+1;
    			}
    			if(firstSet.get(temp).contains("$"))
    				emptyCount++;
    			else
    				break;
    			
    		}
    	}
    	if(emptyCount == W.get(i).right.size())
    	{
    		for(int k=0;k<followSet.get(W.get(i).left).size();k++)
    		{
    			Object[] followArray = followSet.get(W.get(i).left).toArray();
    			Table[row][getVdAIndex(followArray[k].toString())] = i+1;
    		}
    	}
    
  }   
     
 }
}



