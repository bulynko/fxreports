package com.bulynko.fxreports;

import static org.junit.Assert.*;


import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestServiceOrders {


	static ServiceOrders serviceOrd;
	OrderRules  ordRules;
	
	 @Before
	 public void setup2()
	 {
		 System.out.println("Begin of testing method ");
		 serviceOrd=new ServiceOrders();
		 ordRules=new OrderRules();
		 
	 }

	 @After
	 public void cleanup2()
	 {
		 System.out.println("End of testing method ");
	 }

	

	  @Test(timeout=10000)
	  public void testGenerateOrdersDays_LIST()
	   {
		   int listSize;
		  
			System.out.println("--->  generateOrdersDays_LIST  : create order list");
			assertNull(null);
			
			serviceOrd.generateOrdersDays(8,10); // create orders:  8 days x 10.peer day = 100 orders
			listSize=serviceOrd.getlistOrders().size();
			assertEquals(80, listSize);

			serviceOrd.generateOrdersDays(-8,10); // create orders:  negative days  = 0 zero orders
			listSize=serviceOrd.getlistOrders().size();
			assertEquals(0, listSize);
			
	    }
	 
	
	  @Test
	  public void testGenerateOrdersDays_DATA()
	   {
			System.out.println("--->  generateOrdersDays_DATA  : check data on the list");
			
			serviceOrd.generateOrdersDays(10,10); // create orders:  10 days x 10.peer day = 100 orders
			
		   for (Order ord: serviceOrd.getlistOrders())
		   {
			   //check FX agreed rate
			   String cur=ord.getcurrency();
			   assertEquals(ordRules.getfx(cur), ord.getagreedfx(),0.0001);
			  
			   //check entity unit price
			   String ent=ord.getentity();
			   assertEquals(ordRules.getprc(ent), ord.getprice(),0.0001);
			  
		   }
	    }
	  
	  
	  @Test
	  public void testPrepareDailyReport_TOTAL()
	   {
			System.out.println("--->  testPrepareDailyReport_TOTAL  : check report data  IN-OUT total USD");
			
			//---preepare Test Data
			serviceOrd.generateOrdersDays(0,0);
			int listSize=serviceOrd.getlistOrders().size();
			assertEquals(0, listSize);
			//--Add Orders
			long nowT=System.currentTimeMillis();
			long day=24*3600*1000;
			Date d1=new Date();
			Date d2=new Date(nowT-3*day);
			// today
			serviceOrd.addOrder(1,OrderRules.ENT_FOO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d1, d1, 100, 10.5); // 1.5*100*10.5 =1575
			serviceOrd.addOrder(2,OrderRules.ENT_FOO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d1, d1, 120, 10.5); // 1.5*120*10.5 =1890
			serviceOrd.addOrder(3,OrderRules.ENT_FOO , OrderRules.SITE_SELL ,2,   OrderRules.CUR_EUR, d1, d1, 100, 20);  // 2*100*20 = 4000
			serviceOrd.addOrder(4,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,0.5 , OrderRules.CUR_SGP, d1, d1, 200, 10.5); // 0.5 * 200 * 10.5 = 1050
            // 3 days ago ....
			serviceOrd.addOrder(5,OrderRules.ENT_PKO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d2, d2, 10, 10); // 1.5*100*10.5 =150
			serviceOrd.addOrder(6,OrderRules.ENT_PKO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_SGP, d2, d2, 10, 10); // 1.5*100*10.5 =150
			serviceOrd.addOrder(7,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d2, d2, 120, 10.5); // 1.5*120*10.5 =1890
			serviceOrd.addOrder(8,OrderRules.ENT_BAR , OrderRules.SITE_SELL ,2,   OrderRules.CUR_EUR, d2, d2, 100, 30);  // 2*100*20 = 4000
			serviceOrd.addOrder(9,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,0.5 , OrderRules.CUR_SGP, d2, d2, 100, 5); // 0.5 * 100 * 5 = 250

			serviceOrd.updateSettelmentDate();
			serviceOrd.prepreDailyReport();
			   
			//TODAY total-buy: 1575+1890+1050 = 4515
			//TOTDAY total-sell: 4000
			//3days ago total-buy: 150+150+250+1890 =2440
			
			
			//serviceOrd.addOrder(4,OrderRules.ENT_FOO , OrderRules.SITE_BUY , ordRules.getfx( OrderRules.CUR_EUR) , OrderRules.CUR_EUR, d1, d1, 100, 30);
			Order ordTodayBuy= serviceOrd.getlistReportDailyTotalBuy().get(1);
			Order ordTodaySell=  serviceOrd.getlistReportDailyTotalSell().get(1);
			Order ord3daysBuy= serviceOrd.getlistReportDailyTotalBuy().get(0);

			
			assertEquals(4000,ordTodaySell.getamountUSD(),0.001);
			assertEquals(4515,ordTodayBuy.getamountUSD(),0.001);
			assertEquals(2440,ord3daysBuy.getamountUSD(),0.001);

	   }
	  
	  
	  @Test
	  public void testPrepareDailyReport_INOUT()
	   {
			System.out.println("--->  testPrepareDailyReport_INOUT  : check report data  IN-OUT per entity");
			
			//---preepare Test Data
			serviceOrd.generateOrdersDays(0,0);
			int listSize=serviceOrd.getlistOrders().size();
			assertEquals(0, listSize);
			//--Add Orders
			long nowT=System.currentTimeMillis();
			long day=24*3600*1000;
			Date d1=new Date();
			Date d2=new Date(nowT-3*day);
			// today
			serviceOrd.addOrder(1,OrderRules.ENT_FOO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d1, d1, 100, 10.5); // 1.5*100*10.5 =1575
			serviceOrd.addOrder(2,OrderRules.ENT_FOO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d1, d1, 120, 10.5); // 1.5*120*10.5 =1890
			serviceOrd.addOrder(3,OrderRules.ENT_FOO , OrderRules.SITE_SELL ,2,   OrderRules.CUR_EUR, d1, d1, 100, 20);  // 2*100*20 = 4000
			serviceOrd.addOrder(4,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,0.5 , OrderRules.CUR_SGP, d1, d1, 200, 10.5); // 0.5 * 200 * 10.5 = 1050
            // 3 days ago ....
			serviceOrd.addOrder(5,OrderRules.ENT_PKO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d2, d2, 10, 10); // 1.5*100*10.5 =150
			serviceOrd.addOrder(6,OrderRules.ENT_PKO , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_SGP, d2, d2, 10, 10); // 1.5*100*10.5 =150
			serviceOrd.addOrder(7,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,1.5 , OrderRules.CUR_EUR, d2, d2, 120, 10.5); // 1.5*120*10.5 =1890
			serviceOrd.addOrder(8,OrderRules.ENT_BAR , OrderRules.SITE_SELL ,2,   OrderRules.CUR_EUR, d2, d2, 100, 30);  // 2*100*30 = 6000
			serviceOrd.addOrder(9,OrderRules.ENT_BAR , OrderRules.SITE_BUY ,0.5 , OrderRules.CUR_SGP, d2, d2, 100, 5); // 0.5 * 100 * 5 = 250

			serviceOrd.updateSettelmentDate();
			serviceOrd.prepreDailyReport();
		
			//List of 4 objects:
			//[0]  3days ago total-PKO: 150+150 =300
			//[1]  3days ago total-BAR: 1890-6000+250 =-3860
			//[2]  TOTDAY total-BAR: 1050
			//[3]  TODAY total-FOO: 1575+1890 - 4000 = -535
			
			//serviceOrd.addOrder(4,OrderRules.ENT_FOO , OrderRules.SITE_BUY , ordRules.getfx( OrderRules.CUR_EUR) , OrderRules.CUR_EUR, d1, d1, 100, 30);
			Order ord3daysPKO= serviceOrd.getlistReportINOUT().get(0);   //
			Order ord3daysBAR= serviceOrd.getlistReportINOUT().get(1);
			Order ordTodayBAR=  serviceOrd.getlistReportINOUT().get(2);  
			Order ordTodayFOO=  serviceOrd.getlistReportINOUT().get(3);  //the last one

			
			assertEquals(300,ord3daysPKO.getamountUSD(),0.001);
			assertEquals(-3860,ord3daysBAR.getamountUSD(),0.001);
			assertEquals(1050,ordTodayBAR.getamountUSD(),0.001);
			assertEquals(-535,ordTodayFOO.getamountUSD(),0.001);

	   }
	  
	
}
