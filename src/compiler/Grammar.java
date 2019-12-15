package compiler;


import java.io.FileReader;
import java.util.*;


import java.io.BufferedReader;


public class Grammar {
	private String left;
	private ArrayList<String> right = new ArrayList<String>();
	private int id;//产生式编号
	static public String Start;//产生式开始符号
	static public ArrayList<Grammar> W = new ArrayList<Grammar>();//存储所有产生式的容器
	static public HashSet<String> VnSet = new HashSet<String>();//存储所有非终结符
    static public HashSet<String> VdSet = new HashSet<String>();//存储所有终结符
    static public HashMap<String,HashSet<String>> firstSet = new  HashMap<String, HashSet<String>>();//非终结符的首付号集合
	static public HashMap<String,HashSet<String>> followSet = new HashMap<String, HashSet<String>>();//非终结符的follow集合
	//static public HashMap<String, ArrayList<ArrayList<String>>> expSet = new HashMap<String, ArrayList<ArrayList<String>>>();//存所有表达式
    static public int[][] Table;//分析表
    static public Object[] VdArray;//终结符hashset转换成了数组
    static public Object[] VnArray; //非终结符hashset转换成了数组
    static public String[] VdA;
    static public String[] VnA;
    public Grammar() {}
	public Grammar(int c) {//构造函数
		id=c;
	}
	
	public void showGrammar()//输出文法
	{
		System.out.print(left);
		System.out.print("->");
		for(int i=0;i<right.size();i++)//打印产生式右部
		{
			//System.out.print(right.size());
			System.out.print(right.get(i));
			System.out.print(" ");
		}
		//System.out.print(id);
		System.out.print("\n");
		
	}
	public void setR(String a)//把a添加到右部中
	{
		right.add(a);
	}
	public ArrayList<String> getRT()//返回整个右部
	{
		return right;
	}
	public String getRI(int i)//返回右部第i个元素
	{
		String a=right.get(i);
		return a;
	}
	public void setL(String a)//设置产生式左部
	{
		left=a;
	}
	public String getL()//返回产生式左部
	{
		return left;
	}
	public void assiR(ArrayList<String> a)//把a复制给右部
	{
		for(int i=0;i<a.size();i++)
		{
			right.add(a.get(i));
		}
	}
	public boolean Contain(String a)//判断右部是否含有a
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
	 public boolean isRightNull()//判断一个产生式右部是否为空
	{
		if(right.size()==1 && right.get(0).equals("$")) {
			return true;
		}
		else
			return false;
	}
    public int getIndex(String a)//返回a在右部中的位置
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
    static public int getVnIndex(String a)//返回非终结符数组中某个元素下标
    {
    	int index = -1;
    	for(int i=0;i<VnArray.length;i++)
    	{
    		if(a.equals(VnArray[i]))
    			index=i;
    	}
    	return index;
    }
    static public int getVdIndex(String a)//返回终结符数组中某个元素下标
    {
    	int index = -1;
    	for(int i=0;i<VdArray.length;i++)
    	{
    		if(a.equals(VdArray[i]))
    			index=i;
    	}
    	return index;
    }
  static public int getVdAIndex(String a)//在数组VdA中返回指定元素位置
   {
	   int index =-1;
	   for(int i=0;i<VdA.length;i++)
	   {
		   if(VdA[i].equals(a))
			   index=i;
	   }
	   return index;
   }
  static public int getVnAIndex(String a)//在数组VnA中返回指定元素位置
  {
	   int index =-1;
	   for(int i=0;i<VnA.length;i++)
	   {
		   if(VnA[i].equals(a))
			   index=i;
	   }
	   return index;
  }
	
public static void initGrammar()//初始化文法，生成所有终结符和非终结符
	{
		 
		 BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader("WENFA.txt"));
			String string;
			String lefts;
			int idd=0;
			while((string=bufferedReader.readLine())!=null)//从文件中读取文法
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
	for(int i=0;i<W.size();i++)//生成所有终结符表
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
public static void getFirstSet(String target)//生成首符号集合
	{
		int countEmpty = 0;
		int isEmpty = 0;
		HashSet<String> H = firstSet.containsKey(target) ? firstSet.get(target) : new HashSet<String>();
		//HashSet<String> H = new HashSet<String>();//临时记录每一个非终结符的首符号集合
	    for(int i=0;i<W.size();i++)
	    {
	    	//System.out.print(W.get(i).left);
	    	if(W.get(i).left.equals(target))
	    	{
	    		if(VdSet.contains(W.get(i).right.get(0))) //表达式右部第一个字母是终结符
	    		{
	    			H.add(W.get(i).right.get(0));
	    		}
	    		else
	    		{
	    			for(int j=0;j<W.get(i).right.size();j++)// X->Y1..Yj..Yk是一个产生式
	    			{
	    				if(VdSet.contains(W.get(i).right.get(j)))
	    				{
	    					if(!H.contains(W.get(i).right.get(j))){// Yj是终结符(不能产生空),FIRST(Yj)=Yj加入FIRST(X),不能继续迭代,结束
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
	    				else//将FIRST(Yj)中的非空就加入FIRST(X)
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
	    			if(countEmpty == W.get(i).right.size())//所有右部first(Y)都有$(空),将$加入FIRST(X)中
	    			{
	    				H.add("$");
	    			}
	    			
	    		}
	    		}
	    			
	    	}
	    firstSet.put(target,H);
	
	}
public static void getFollowSet(String target)//求非终结符target的follow集合
{
	//int isEmpty = 0;
	HashSet<String> set = (followSet.containsKey(target))? followSet.get(target) : new HashSet<String>();
		if(Start.equals(target)) 
			set.add("#");  			//结束符号加入follow(S)		
		 for (int i=0;i<W.size();i++)
		 {  //遍历所有产生式
			 int isEmpty = 0;
			 if(W.get(i).Contain(target))//第i个产生式右部含有待求非终结符
			 {
				 int index = W.get(i).getIndex(target);//该非终结符在右部中的位置
				 
				 if(index == W.get(i).right.size()-1 && (!target.equals(W.get(i).left)))//当前符号是右部最后一个符号A->....T
				 {
					 //System.out.println(target+"递归进入"+W.get(i).left);
					 getFollowSet(W.get(i).left);//递归求出其左部的follow集合加入当前待求follow集合中
					 HashSet<String> set1 = followSet.get(W.get(i).left);
					 for(String a: set1)
					 {
						 set.add(a);
					 }
					 
				 }
				 //if(VdSet.contains(W.get(i).right.get(index+1)) && index!= W.get(i).right.size()-1)//不是右部最后一个，且后面一个符号是终结符
				 else if(index != W.get(i).right.size()-1)//不是右部最后一个
				 {
					if(VdSet.contains(W.get(i).right.get(index+1)))
					{  //下一个是终结符
					 set.add(W.get(i).right.get(index+1));
					 //break;
					}
				 
				 else //A->...TB....
				 {
					 int id=index;
					 //System.out.print(id);
					 int len = W.get(i).right.size() - index-1;//T后还有几个符号
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
					 else if(!(firstSet.get(nex).contains("$")))//后边一个非终结符的first集合不可空
					    {
						    set.addAll(firstSet.get(nex));//把后一个非终结符的first集合全部加入
						    break;//跳出循环
					    }
					    else if(firstSet.get(nex).contains("$"))//后一个符号可空
					    {
					    for(String a : firstSet.get(nex))
					    {
						 if(a.equals("$"))//B中含有空字符
							 isEmpty++;
						  else
							 set.add(a);//把B的first集合中所有非空字符加入T的follow集合
					     }
					     
					    }
					    len--;
					 }
					 if(isEmpty==lenth)//后边符号全可以是空
					 {
						 if(!W.get(i).left.equals(target)) {
							 //System.out.println(target+"递归进入"+W.get(i).left);
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


static public void  getTable()//生成分析表
{
	//VdSet.add("#");
	VdArray = VdSet.toArray();
	VnArray = VnSet.toArray();
	VdA = new String[VdArray.length+1];//多加入一个#
	VnA = new String[VnArray.length];
	for(int i=0;i<VdArray.length;i++)
	{
		VdA[i]=VdArray[i].toString();
	}
	for(int i=0;i<VnArray.length;i++)
	{
		VnA[i]=VnArray[i].toString();
	}
	VdA[VdArray.length] = "#";//在终结符中加入#，即在符号表里加入一列是#
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
    		else// tmp是非终结符
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



