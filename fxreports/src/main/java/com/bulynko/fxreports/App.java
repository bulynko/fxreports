package com.bulynko.fxreports;

/**
 * Author: Marcin Bulynko  , June 2018 
 *
 * Steps:
 * 1. Start , welcome messages
 * 2. Testing with smaller number of reports ( to be replaced with stpe3)
 * 3. create orders for the last 2 weeks  (14-days)  and 20xorders per day
 * 4. Process orders and prepare reports
 * 5. Print Reports
 * 6. serviceOrd.printInOutReport();
 */
public class App 
{
	static ServiceOrders serviceOrd=new ServiceOrders();
	
    public static void main( String[] args )
    {
    	//1. Start , welcome messages
    	TextPrinter.empty(1);
    	TextPrinter.msg(" Welcome to Trade Report Test ,  by Marcin Bulynko (June 2018) ...");
        TextPrinter.empty(2);
    	TextPrinter.msg(" Processing Orders received for last 2 weeks :");
        TextPrinter.empty(1);
        
       //2. Testing with smaller number of reports
       // serviceOrd.generateOrdersDays(0,0); 
       // serviceOrd.generateOrdersTest();
        
       //3. create orders for the last 2 weeks  (14-days)  and 20xorders per day
        serviceOrd.generateOrdersDays(14,20);  
        serviceOrd.updateSettelmentDate();
        serviceOrd.printAllOrders();
        TextPrinter.empty(3);
        
        //4. Process orders and prepare reports
        serviceOrd.prepreDailyReport();   
        
        //5. Print Reports
        serviceOrd.printBuyOrders();
        serviceOrd.printSellOrders();
        TextPrinter.empty(3);   
        serviceOrd.printBuyTotal();
        serviceOrd.printSellTotal();
        TextPrinter.empty(3); 
        //6. Print Final Settlement Report
        serviceOrd.printInOutReport();
        
    }
}
