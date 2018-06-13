package com.bulynko.fxreports;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


//import junit.framework.TestCase;

public class TestOrderRules  {

	OrderRules ord;
	
	
	 @Before
	 public void setup2()
	 {
		 ord=new OrderRules();
		 System.out.println("Begin of testing methods ");
	 }

	 @After
	 public void cleanup2()
	 {
		 System.out.println("End of testing methods ");
	 }
	 

    
    @Test
    public void testGetfx()
    {
		System.out.println("--->  testGetfx  : test exchange rate for currency");
		assertEquals(1.0, ord.getfx("USD"),0.00001);	
		assertEquals(0.2, ord.getfx("AED"),0.00001);
		assertEquals(0.98, ord.getfx("CHF"),0.00001);

    }
	
    
    @Test
    public void testIsWeekend()
    {
    	Date d=null;
    	
    	//Fiday test
    	try {
			 d=new SimpleDateFormat ("yyyy-MM-dd").parse("2018-06-01");  // 1-JUN: FR
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--->  testIsWeekend  : test if weekend  for currency "+d);
		assertEquals(false, ord.isWeekend(OrderRules.CUR_EUR, d));	
		assertEquals(false, ord.isWeekend(OrderRules.CUR_SGP, d));	
		assertEquals(true, ord.isWeekend(OrderRules.CUR_AED, d));	

    	//Sunday test
    	try {
			 d=new SimpleDateFormat ("yyyy-MM-dd").parse("2018-06-03");  // 1-JUN: FR
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--->  testIsWeekend  : test if weekend  for currency "+d);
		assertEquals(true, ord.isWeekend(OrderRules.CUR_EUR, d));	
		assertEquals(true, ord.isWeekend(OrderRules.CUR_SGP, d));	
		assertEquals(false, ord.isWeekend(OrderRules.CUR_AED, d));

    }
    
    
}
