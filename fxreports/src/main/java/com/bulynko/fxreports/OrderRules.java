package com.bulynko.fxreports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderRules {
	/*
	 *  Repository for:
	 *  Currency
	 *  Entities
	 *  Exchange Rates
	 *  etc. 
	 */

	public static final char SITE_BUY='B';
	public static final char SITE_SELL='S';
	public static final char SITE_NEUTRAL='N';
	
	public static final String CUR_USD="USD";
	public static final String CUR_EUR="EUR";
	//public static final String CUR_GBP="GBP";
	public static final String CUR_CHF="CHF";
	//public static final String CUR_PLN="PLN";
	public static final String CUR_SGP="SGP";
	public static final String CUR_AED="AED";
	public static final String CUR_SAR="SAR";

	public static final String ENT_PKO="PKO";
	public static final String ENT_EMAR="EMAR";
	public static final String ENT_RFV="RFV";
	public static final String ENT_FOO="FOO";
	public static final String ENT_BAR="BAR";


	public final static List<String> entList=Arrays.asList(ENT_EMAR,ENT_PKO,ENT_RFV,ENT_FOO,ENT_BAR);
	public final static  List<String> curList=Arrays.asList(CUR_AED, CUR_SAR, CUR_EUR , CUR_CHF) ;
	
	//=============================================================================================
	// mapping weekends to currency
	class WeekCurrency {
		 private int firstDay;   //day-of-the-week
		 private int weekend1;   //day-of-the-week
		 private int weekend2;   //day-of-the-week
		 private String cur;     //currency code e.g. "USD"
		 //------------------------
		 public WeekCurrency(int d1,int w1,int w2,String c)
		 {
			 firstDay=d1;
			 weekend1=w1;
			 weekend2=w2;
			 cur=c;
		 }
		 public int getfirstDay() {return firstDay;} 
		 public int getweekend1() {return weekend1;}
		 public int getweekend2() {return weekend2;}
		 public boolean isweekend(int day) {return  ((weekend1==day) || weekend2==day);}
		 public boolean isCur(String c) {return (cur.equals(c)); }
	}
	//========================================================================================
	// FX rates to USD , e.g.  1AED = 0.2USD , 1EUR= 1.1USD
	class prcRate {
		private String currency_entity;
		private double prc;
   	   //------------------------
		public prcRate(String cur, double x)
		{
			currency_entity=cur;
			prc=x;
		}
		public String getcurrency() {return currency_entity;}
		//public String getentity() {return currency_entity;}
		public double getprc() {return prc;}
		public boolean iscurrency(String cur) {return currency_entity.equals(cur);}
		//public boolean isentity(String cur) {return currency_entity.equals(cur);}
	}
	//==========================================================================================
	
	
	private List<WeekCurrency>  listWeekCurrency;
	private WeekCurrency defaultWeekCurrency;
	private List<prcRate>  listFX,listENT;
	
	//======================================================
	
	public OrderRules()
	{
		 initCurrencyData();
	}
	
	// Set weekends for each currency e.g. AED weekend is on 
	private void initCurrencyData()
	{
		listWeekCurrency=new ArrayList<WeekCurrency>();
		listWeekCurrency.add(new WeekCurrency(Calendar.SUNDAY,Calendar.FRIDAY,Calendar.SATURDAY,CUR_AED));  //TH:4,FR:5
		listWeekCurrency.add(new WeekCurrency(Calendar.SUNDAY,Calendar.FRIDAY,Calendar.SATURDAY,CUR_SAR));		
		defaultWeekCurrency=new WeekCurrency(Calendar.MONDAY,Calendar.SATURDAY,Calendar.SUNDAY,"ANY");         //SAT, SUN

		listFX=new ArrayList<prcRate>();
		listFX.add(new prcRate(CUR_AED,0.2));
		listFX.add(new prcRate(CUR_SAR,0.25));
		listFX.add(new prcRate(CUR_SGP,0.5));
		listFX.add(new prcRate(CUR_EUR,1.1));
		listFX.add(new prcRate(CUR_CHF,0.98));
		listFX.add(new prcRate(CUR_USD,1));

		listENT=new ArrayList<prcRate>();
		listENT.add(new prcRate(ENT_PKO,82.5));
		listENT.add(new prcRate(ENT_RFV,110.0));
		listENT.add(new prcRate(ENT_EMAR,105.25));
		listENT.add(new prcRate(ENT_FOO,20.75));
		listENT.add(new prcRate(ENT_BAR,22.5));


	}
	
	//-------------------------------------------------------
	public boolean isWeekend(String cur, Date d)
	{
		 boolean res=false;
		 Calendar cal= Calendar.getInstance();
		 cal.setTime(d);
		 int dayWeek=cal.get(Calendar.DAY_OF_WEEK);
		
		 if (defaultWeekCurrency.isweekend(dayWeek)) res=true; 
		 
	     for  (WeekCurrency w:  listWeekCurrency)
	       if  (w.isCur(cur)  )
	    	{		   
	    		if ( w.isweekend(dayWeek)) res=true;
	    		else res=false;
	    		break;
             }
	      
	    //  if (checkDeafult && defaultWeekCurrency.isweekend(dayWeek)) res=true; 
	    //  if (res) System.out.println("\n week-day:"+dayWeek+" : "+cur+"  : "+d+"   weekend:"+res);
		 return res;
	}
	
	//-------------------------------------------------------
	public Date nextWorkingDate(String cur, Date d)
	{
		 Calendar cal= Calendar.getInstance();
		 cal.setTime(d);
		 int dayWeek=cal.get(Calendar.DAY_OF_WEEK);
		 WeekCurrency ptrCUR=defaultWeekCurrency;
			 
	     for  (WeekCurrency w:  listWeekCurrency)
	       if  (w.isCur(cur)  ) 
	    	{		   
	    	    ptrCUR=w;
	    		break;
             }
	      
	     int delataDays=( dayWeek==ptrCUR.getweekend1() ? 2 : 
	    	            ( dayWeek==ptrCUR.getweekend2() ? 1 : 0) );
	     

		 return new Date (d.getTime()+delataDays*24*3600*1000);
	}
	
   // Get FX exchange rate for input CURRENCY
   // Used to generate random orders
   //---------------------------------------
	public double getfx(String cur)
	{
		return chkprc( cur, listFX);
	}

   // Get Unit price for each Entity 
   // Used to generate random orders
   //---------------------------------------
	public double getprc(String ent)
	{
		return chkprc( ent, listENT);
	}
	
	
	private double chkprc(String cur, List<prcRate> lst)
	{
		double res=-1;
		int s=lst.size();
				
	    for (int idx=0;idx<s;idx++) 
	    	if (lst.get(idx).iscurrency(cur))
	    	{
	    		res=lst.get(idx).getprc();
	            break;
	    	}
	    	
		return res;
	}
	
	
}
