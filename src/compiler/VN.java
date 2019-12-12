package compiler;


public class VN {
	public static void main(String[] args)throws Exception {
	     Grammar.initGrammar();
	     System.out.println("非终结符集合");
	    for(String a : Grammar.VnSet)
	    {
	    	System.out.println(a);
	    }
	    System.out.println("终结符集合");
	    for(String a : Grammar.VdSet) {
	    	System.out.println(a);
	    }
	    for(String a : Grammar.VnSet)
	    {
	    	//System.out.print(a);
	    	Grammar.getFirstSet(a);
	    	
	    }
	    for(String a : Grammar.VnSet)
	    {
	    	
	    	Grammar.getFollowSet(a);
	    	/*HashSet<String> Q = new HashSet<String>();
	    	Q = followSet.get(a);
	    	for(String b : Q)
	    	{
	    		System.out.print(b);
	    	}*/
	    	
	    }
	    for(String a : Grammar.VnSet)
	    {
	    	System.out.print(a+"firstset: ");
	    	for(String b : Grammar.firstSet.get(a))
	    	{
	    		
	    		System.out.print(b);
	    	}
	    	System.out.print("\n");
	    		
	    }
	    for(String a : Grammar.VnSet)
	    {
	    	System.out.print(a+"followset: ");
	    	for(String b : Grammar.followSet.get(a))
	    	{
	    		
	    		System.out.print(b);
	    	}
	    	System.out.print("\n");
	    		
	    }
	    Grammar.getTable();
	    for(int i =0;i<Grammar.VnArray.length;i++)
	    {
	    	for(int j =0;j<Grammar.VdArray.length+1;j++)
	    	{
	    		System.out.print(Grammar.Table[i][j]);
	    		System.out.print(" ");
	    	}
	    	System.out.print("\n");
	    }
	  
		}
	

}
