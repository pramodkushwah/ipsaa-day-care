package com.synlabs.ipsaa;

import java.util.HashSet;
import java.util.Set;

public class main {
    public static void main(String aa[]){
        Set<String> sets=new HashSet<>();
        String s=new String("me");

        String s1=new String("me");
        if(s==s1){
            System.out.println("true");
        }
        sets.add(s1);
        sets.add(s1);
        System.out.println(sets);
    }
}
