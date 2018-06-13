package com.bulynko.fxreports;

public class TextPrinter {

  /*
   *  Just simple printing and message formating  tool
   * 
   */
	
	
	public static void msg(String s)
	{
		System.out.println(s);
	}

	//------------------------------------

	public static void empty(int n)
	{
		for (int i=0 ; i<n ;i++ ) System.out.println();
	}
	
	//------------------------------------
	
	public static void line()
	{
		 System.out.println(String.format("%79s"," ").replace(" ","-"));
	}
	
	public static void line(char c)
	{
		 System.out.println(String.format("%79s"," ").replace(" ",String.valueOf(c)));
	}
	
	//=========================================================

   
 	public static void  printReportEnt(Order o)
 	{
 		String res=String.format(" %s| %7s | %5s |%4d | %9.2f | %s| %s|" , o.getcurrency(), o.getinout() ,  o.getentity() ,   o.getunits(),  o.getamountUSD() , o.getinstructionDateDW() , o.getsettlementDateDW() ) ;
 		msg(res);		
 	   			
 	}
 	
 	public static void   printHeadReportEnt()
 	{
 		String res=" cur| In/Out  | Entity|units|  amount   |  date-Istruction|  date-Settlement|" ;
 		msg(res);					
 	}
	
	//=========================================================

 	
 	public static void  printme(Order o)
 	{
 		String res=String.format(" %3d|%s| %s | %5s | %.2f |%4d | %6.2f | %s| %s|" , o.getid(), o.getcurrency() ,String.valueOf(o.getsite()) , o.getentity() , o.getagreedfx(),o.getunits(), o.getprice() , o.getinstructionDateDW() , o.getsettlementDateDW() ) ;
 				
 	    msg( res);			
 	}
 	
 	public  static void   printHead()
 	{
 		String res="  id|cur|B/S| Entity|  FX  |units| prc/unt|  date-Istruction|  date-Settlement|" ;
 				
 	    msg(res);			
 	}

	//=========================================================

 	
 	public static void  printTotal(Order o)
 	{
 		String res=String.format(" %s | %7s | %4d | %9.2f | %s| %s|" ,  o.getcurrency() ,o.getinout()  ,  o.getunits(), o.getamountUSD() , o.getinstructionDateDW() , o.getsettlementDateDW() ) ;
 				
 	    msg( res);			
 	}
 	
 	public static  void  printHeadTotal()
 	{
 		String res=" cur |  In/Out | units|  amount   |  date-Istruction|  date-Settlement|" ;
 				
 	    msg(res);			
 	}

	
	
}
