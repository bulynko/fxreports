package com.bulynko.fxreports;

import java.util.Comparator;

public class ComparatorOrderDate  implements Comparator<Order>
{
    public int compare(Order d1, Order d2)
    {
        return  (int) ( d1.getsettlementDate().getTime() -  d2.getsettlementDate().getTime())/1000;
    }
}