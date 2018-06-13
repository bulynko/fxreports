package com.bulynko.fxreports;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Order {

	private int id;
	private String entity;
	private char site;             //B-buy  S-Sell
	private double agreedfx;
	private String currency;       //e.g. USD, EUR
	private Date instructionDate;
	private Date settlementDate;
	private int units;
	private double price;
	
	private final static SimpleDateFormat dtFormat=new SimpleDateFormat("dd-MM-yyyy");
	private final static SimpleDateFormat dtFormatW=new SimpleDateFormat("dd-MM-yyyy (EEE)");
	
	//Report Only----------------
	private double amountUSD;
	
	//====================================
     public Order(int xid,String xentity,char xsite,double fx, String xcur ,Date xdate1,Date xdate2,int xunits, double xprice)
     {
    		id=xid;
    		entity=xentity;
    		site=xsite;  
    		agreedfx=fx;
    		currency=xcur;
    		instructionDate=xdate1;
    		settlementDate=xdate2;
    		units=xunits;
    		price=xprice; 
    		amountUSD=0;
     }
     //------------------------------------
     public Order(Order o)
     {
    		id=o.id;
    		entity=o.entity;
    		site=o.site;  //B-buy  S-Sell
    		agreedfx=o.agreedfx;
    		currency=o.currency;
    		instructionDate=new Date( o.instructionDate.getTime() );
    		settlementDate=new Date( o.settlementDate.getTime() );
    		units=o.units;
    		price=o.price; 
    		amountUSD=o.amountUSD;
     }
     
     //------------------------------------
     public void copyObj(Order o)
     {
    		id=o.id;
    		entity=o.entity;
    		site=o.site;  //B-buy  S-Sell
    		agreedfx=o.agreedfx;
    		currency=o.currency;
    		instructionDate=new Date( o.instructionDate.getTime() );
    		settlementDate=new Date( o.settlementDate.getTime() );
    		units=o.units;
    		price=o.price; 
     }
     

	
	//--------------------------------------
    public int getid()
 	{
 		return id;
 	}
 	
    public void setid(int i)
 	{
 		 id=i;
 	}
 	//-----------------
 	public String getentity()
 	{
 		return entity;
 	}
 	
 	public void setentity(String s)
 	{
 		entity=s ;
 	}
 	//-----------------
 	public char getsite()
 	{
 		return site;
 	}
 	
 	
 	public void setsite(char c)
 	{
 		site=c ;
 	}
 	
 	public String getinout()
 	{
 		String res="ERR";
 		switch (site)
 		{
 		case OrderRules.SITE_BUY:   res="OUT(B)"; break;
 		case OrderRules.SITE_SELL:   res="IN(S)"; break;
 		case OrderRules.SITE_NEUTRAL:res="ZERO"; break;
 		
 		}

 		return res;
 	}
 	
 	//-----------------
 	public double getagreedfx()
 	{
 		return agreedfx;
 	}
 	
 	public void setagreedfx(double d)
 	{
 		agreedfx=d ;
 	}
 	//-----------------
 	public String getcurrency()
 	{
 		return currency;
 	}
 	
 	public void setcurrency(String s)
 	{
 		 currency=s;
 	}
 	
 	
 	
 	//-----------------
 	public Date getinstructionDate()
 	{
 		return instructionDate;
 	}

 	public String getinstructionDateDT()
 	{
 		return dtFormat.format( instructionDate);
 	}
 	
 	public String getinstructionDateDW()
 	{
 		return dtFormatW.format( instructionDate);
 	}
 	
 	public void setinstructionDate(Date d)
 	{
 		instructionDate=d ;
 	}
 	//-----------------
 	public Date getsettlementDate()
 	{
 		return settlementDate;
 	}
 	
 	public String getsettlementDateDT()
 	{
 		return  dtFormat.format(  settlementDate );
 	}
 	
 	public String getsettlementDateDW()
 	{
 		return  dtFormatW.format(  settlementDate );
 	}
 	
 	public int getsettlementDateDY() //day in the year
 	{
    	Calendar cal= Calendar.getInstance();
    	
    	cal.setTime(settlementDate);
    	

    	return cal.get(Calendar.DAY_OF_YEAR);
 	}
 	
 	
 	public void setelmentDate(Date d)
 	{
 		settlementDate=d ;
 	}
 	//-----------------
 	public int getunits()
 	{
 		return units;
 	}
 	
 	public void setunits(int d)
 	{
 		units=d;
 	}
 	
 	public void addunits(int d)
 	{
 		units=units+d;
 	}
 	//-----------------
 	public double getprice()
 	{
 		return price;
 	}
 	
 	public void setprice(double d)
 	{
 		price=d ;
 	}
 	//-----------------

	//Report Only----------------
	public double getamountUSD()
	{
		return amountUSD ;
	}

	public void  setamountUSD(double d)
	{
		amountUSD=d ;
	}
	
	public void  setamountUSD(Order o)
	{
		amountUSD= o.agreedfx*o.units*o.price ;
	}
	
	public void  addamountUSD(double d)
	{
		amountUSD= amountUSD+d ;
	}
	
	public void  addamountUSD(Order o)
	{
		amountUSD= amountUSD+o.agreedfx*o.units*o.price ;
	}
	
	public void  subamountUSD(Order o)
	{
		amountUSD= amountUSD-o.agreedfx*o.units*o.price ;
	}
	

}
