package com.bulynko.fxreports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ServiceOrders {

/*
 * Order Processing Engine:
 * -Generates list of random orders
 * -Process daily, settlement reports
 * -etc.
 * 		
 */

	private OrderRules  ordRules;
	
	private List<Order> listOrders;     // All imprted/generated orders
	private List<Order> listReportDailyBuy,listReportDailySell;
	private List<Order> listReportDailyTotalBuy,listReportDailyTotalSell;
	private List<Order> listReportINOUT;  // final report data daily IN/OUT

	ComparatorOrderDate compareOrdersDate;
	ComparatorOrderDateEnt compareOrdersDateEnt;
	
	//============================
	public ServiceOrders()
	{
		ordRules=new OrderRules();
		listOrders=null;
		listReportDailyBuy=null;
		listReportDailySell=null;
		compareOrdersDate=new ComparatorOrderDate();
		compareOrdersDateEnt=new ComparatorOrderDateEnt(); 
	}
	
	//----------------------------
	public void addOrder(int cnt, String ent, char buysell, double fx, String cur,Date dtI, Date dtS,int units, double prc)
	{
		 Order tmp_ord=new Order(cnt, ent,buysell , fx, cur, dtI ,dtS , units, prc );
		 listOrders.add(tmp_ord);
	}
	
	// For Testing only -  to be removed ....
	//-----------------------------
	public void  generateOrdersTest()
	{
		long nowT=System.currentTimeMillis();
		long day=24*3600*1000;
		Date d1=new Date();
		Date d2=new Date(nowT-3*day);
		// today
		addOrder(1,OrderRules.ENT_FOO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d1, d1, 100, 10.5); // 1.5*100*10.5 =1575
		addOrder(2,OrderRules.ENT_FOO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d1, d1, 120, 10.5); // 1.5*120*10.5 =1890
		addOrder(3,OrderRules.ENT_FOO , OrderRules.SITE_SELL ,2,   OrderRules.CUR_EUR, d1, d1, 100, 20);  // 2*100*20 = 4000
		addOrder(4,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,0.5 , OrderRules.CUR_SGP, d1, d1, 200, 10.5); // 0.5 * 200 * 10.5 = 1050
        // 3 days ago ....
		addOrder(5,OrderRules.ENT_PKO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d2, d2, 10, 10); // 1.5*100*10.5 =150
		addOrder(6,OrderRules.ENT_PKO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_SGP, d2, d2, 10, 10); // 1.5*100*10.5 =150
		addOrder(7,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d2, d2, 120, 10.5); // 1.5*120*10.5 =1890
		addOrder(8,OrderRules.ENT_BAR , OrderRules.SITE_SELL ,2,   OrderRules.CUR_EUR, d2, d2, 100, 30);  // 2*100*20 = 4000
		addOrder(9,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,0.5 , OrderRules.CUR_SGP, d2, d2, 100, 5); // 0.5 * 100 * 5 = 250
	}
	
	// Creates list of new orders to be processed
	//-----------------------------
	public void  generateOrdersDays(int ndays,int perday)
	{
		Random rnd=new Random();
	    int idx;
	    int cnt=0;
		List<String> curLst=OrderRules.curList;
		List<String> entLst=OrderRules.entList;
		int cnt_cur= curLst.size();
		int cnt_ent= entLst.size();
		Date dtI,dtS,nowD;
		
		nowD=new Date();
		
		listOrders=new ArrayList<Order>();
		for (int d1=ndays;d1>0;d1--)
		{
			dtI=new Date (nowD.getTime() - 24*3600*1000* d1);
			dtS=dtI;
		 for (int d2=0;d2<perday;d2++)
		 {
			cnt++;
			idx=rnd.nextInt(cnt_cur) ;
			String c=curLst.get(idx);

			idx=rnd.nextInt(cnt_ent);
			String e=entLst.get(idx);
			
			int u= rnd.nextInt(100);			
		    double fx= ordRules.getfx(c);
		    double prc= ordRules.getprc(e);
			char buysell= OrderRules.SITE_BUY;
			if (rnd.nextInt(100) < 50)  buysell= OrderRules.SITE_SELL;
			
			addOrder(cnt, e,  buysell,  fx,  c, dtI,  dtS, u,  prc);
			
		}}
		
	//	prn.msg("Order List Ready !");
	}
	
	//----------------------------------------------------
	public void updateSettelmentDate()
	{
		for (Order o: listOrders ) 
	  	 if ( ordRules.isWeekend(o.getcurrency(), o.getinstructionDate() ))
			{
				o.setelmentDate( ordRules.nextWorkingDate(o.getcurrency(), o.getinstructionDate()));
			}
		Collections.sort(listOrders, compareOrdersDate);
	}
	
	//----------------------------------------------------


	public void prepreDailyReport()
	{
		listReportDailyBuy=new ArrayList<Order>();
		listReportDailySell=new ArrayList<Order>();		
		listReportDailyTotalBuy=new ArrayList<Order>();
		listReportDailyTotalSell=new ArrayList<Order>();
		listReportINOUT=new ArrayList<Order>();
	    
	    prepareDailyEntReport(OrderRules.SITE_BUY, listReportDailyBuy);
	    prepareDailyEntReport(OrderRules.SITE_SELL,listReportDailySell);

	    prepareDailyTotalReport(OrderRules.SITE_BUY,listReportDailyTotalBuy);
	    prepareDailyTotalReport(OrderRules.SITE_SELL,listReportDailyTotalSell);
	    
	    prepareDailyInOutReport( listReportINOUT );

		Collections.sort(listReportINOUT, compareOrdersDateEnt);
		Collections.sort(listReportDailyBuy, compareOrdersDateEnt);
		Collections.sort(listReportDailySell, compareOrdersDateEnt);
		Collections.sort(listReportDailyTotalBuy, compareOrdersDate);
		Collections.sort(listReportDailyTotalSell, compareOrdersDate);

	}

	//------------------------------------------------------------------------------
	public void prepareDailyEntReport(char site, List<Order> lst)
	{
		boolean chknew=false;
		
		for (Order o1: listOrders )
		 if (o1.getsite() == site)
		 {
		    chknew=true;
		    for (Order o2: lst )
		    if (( o2.getsettlementDateDY() == o1.getsettlementDateDY())
		    	&& ( o2.getentity().equals(o1.getentity()))) 
		    	{
		    	  o2.addunits(o1.getunits());
		    	  o2.addamountUSD(o1);
		    	  o2.setagreedfx(-1);
		    	  o2.setcurrency(OrderRules.CUR_USD);
		    	  chknew=false;
		    	  break;
		    	}
		    
			if (chknew)
			{
				 Order tmpOrd=new  Order(o1);
				 tmpOrd.setamountUSD(o1);
				 lst.add(tmpOrd);
			}
		}
	}
	
	//------------------------------------------------------------------------------
	public void prepareDailyInOutReport( List<Order> lst)
	{
		boolean chknew=false;
		
		for (Order o1: listOrders )
		 {
		    chknew=true;
		    for (Order o2: lst )
		    if (( o2.getsettlementDateDY() == o1.getsettlementDateDY())
		    	&& ( o2.getentity().equals(o1.getentity()))) 
		    	{
		    	  if ( o1.getsite() == OrderRules.SITE_BUY ) o2.addamountUSD(o1);
		    	  if ( o1.getsite() == OrderRules.SITE_SELL ) o2.subamountUSD(o1);
		    	  if ( o2.getamountUSD() > 0 ) o2.setsite(OrderRules.SITE_BUY);
		    	  if ( o2.getamountUSD() < 0 ) o2.setsite(OrderRules.SITE_SELL);
		    	  if ( o2.getamountUSD() == 0 ) o2.setsite(OrderRules.SITE_NEUTRAL);
		    	  
		    	  o2.setagreedfx(-1);
		    	  o2.setcurrency(OrderRules.CUR_USD);
		    	  chknew=false;
		    	  break;
		    	}
		    
			if (chknew)
			{
				 Order tmpOrd=new  Order(o1);
				 tmpOrd.setamountUSD(0);
		    	  if ( o1.getsite() == OrderRules.SITE_BUY ) tmpOrd.addamountUSD(o1);
		    	  if ( o1.getsite() == OrderRules.SITE_SELL ) tmpOrd.subamountUSD(o1);
		    	  if ( tmpOrd.getamountUSD() > 0 ) tmpOrd.setsite(OrderRules.SITE_BUY);
		    	  if ( tmpOrd.getamountUSD() < 0 ) tmpOrd.setsite(OrderRules.SITE_SELL);
		    	  if ( tmpOrd.getamountUSD() == 0 ) tmpOrd.setsite(OrderRules.SITE_NEUTRAL);
		    	  tmpOrd.setagreedfx(-1);
		    	  tmpOrd.setcurrency(OrderRules.CUR_USD);
				 lst.add(tmpOrd);
			}
		}
	}
	
	//------------------------------------------------------------------------------
	public void prepareDailyTotalReport(char site, List<Order> lst)
	{
		boolean chknew=false;
		
		for (Order o1: listOrders )
		 if (o1.getsite() == site)
		 {
		    chknew=true;
		    for (Order o2: lst )
		    if (( o2.getsettlementDateDY() == o1.getsettlementDateDY()))
		    	{
		    	  o2.addunits(o1.getunits());
		    	  o2.addamountUSD(o1);
		    	  o2.setagreedfx(-1);
		    	  o2.setcurrency(OrderRules.CUR_USD);
		    	  chknew=false;
		    	  break;
		    	}
		    
			if (chknew)
			{
				 Order tmpOrd=new  Order(o1);
				 tmpOrd.setamountUSD(o1);
				 tmpOrd.setagreedfx(-1);
				 tmpOrd.setcurrency(OrderRules.CUR_USD);
				 lst.add(tmpOrd);
			}
		}
	}
	
	//----------------------------------------------------
	public void printAllOrders()
	{
	    if (listOrders!=null) printOrderTable(listOrders," ALL Orders:" ) ;
	}

	public void printInOutReport()
	{
	    if (listReportDailyBuy!=null) printOrderReportUSD(listReportINOUT," Daily settlement Report (IN-OUT):"  ) ;
	}
	
	public void printBuyOrders()
	{
	    if (listReportDailyBuy!=null) printOrderReportUSD(listReportDailyBuy," BUY Orders:"  ) ;
	}
	
	public void printSellOrders()
	{
	    if (listReportDailySell!=null) printOrderReportUSD(listReportDailySell," SELL Orders:"  ) ;
	}
	
	public void printBuyTotal()
	{
	    if (listReportDailyBuy!=null) printTotalsTable(listReportDailyTotalBuy," BUY total daily USD:"  ) ;
	}
	
	public void printSellTotal()
	{
	    if (listReportDailySell!=null) printTotalsTable(listReportDailyTotalSell," SELL total daily USD:"  ) ;
	}
	
	
	//-----------------------------------------------------
	private void printOrderTable( List<Order> lst, String title)
	{
		 int lsize=lst.size();
		 
		 TextPrinter.empty(2);
		 TextPrinter.line('=');
		 TextPrinter.msg(String.format(" %s [%d]", title , lsize));
         TextPrinter.line('=');
		 TextPrinter.printHead() ;
		 TextPrinter.line();
		for (Order o: lst ) TextPrinter.printme(o);
		TextPrinter.line('=');
		TextPrinter.empty(2);
	}
	//-----------------------------------------------------
	private void printTotalsTable( List<Order> lst, String title)
	{
		 int lsize=lst.size();
		 
		 TextPrinter.empty(2);
		 TextPrinter.line('=');
		 TextPrinter.msg(String.format(" %s [%d]", title , lsize));
         TextPrinter.line('=');
		 TextPrinter.printHeadTotal() ;
		 TextPrinter.line();
		for (Order o: lst ) TextPrinter.printTotal(o);
		TextPrinter.line('=');
		TextPrinter.empty(2);
	}
	
	//-----------------------------------------------------
	private void printOrderReportUSD( List<Order> lst, String title)
	{
		 int lsize=lst.size();
		 int lastDay=-1;
		 
		 TextPrinter.empty(2);
		 TextPrinter.line('=');
		 TextPrinter.msg(String.format(" %s [%d]", title , lsize));
         TextPrinter.line('=');
		 TextPrinter.printHeadReportEnt();
		 TextPrinter.line();
		for (Order o: lst )
		{	
			if ((lastDay > 0) && (lastDay != o.getsettlementDateDY())) TextPrinter.line();
			lastDay=o.getsettlementDateDY();
			TextPrinter.printReportEnt(o);
		}
		TextPrinter.line('=');
		TextPrinter.empty(2);
	}
	
	//-------------------------------------------------------

	
	
	//--------------------------------------------------------
	public List<Order> getlistOrders()
	{
		return listOrders;
	}
	//--------------------------------------------------------
	
	public List<Order> getlistReportDailyBuy()
	{
		return listReportDailyBuy;
	}
	//--------------------------------------------------------

	public List<Order> getlistReportDailySell()
	{
		return listReportDailySell;
	}
	//--------------------------------------------------------
	
	
	public List<Order> getlistReportDailyTotalBuy()
	{
		return listReportDailyTotalBuy;
	}
	//--------------------------------------------------------
	
	public List<Order> getlistReportDailyTotalSell()
	{
		return listReportDailyTotalSell;
	}
	//--------------------------------------------------------
	public List<Order> getlistReportINOUT()
	{
		return listReportINOUT;
	}

	
}
