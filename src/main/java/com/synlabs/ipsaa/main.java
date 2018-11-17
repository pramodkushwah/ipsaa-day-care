package com.synlabs.ipsaa;

import java.util.HashSet;
import java.util.Set;

public class main {
    public static void main(String aa[]){
        A obj=new D();
        if(obj.getData() instanceof Integer)
        System.out.println(obj.getData()+" this is integer");
        else if(obj.getData() instanceof String)
            System.out.println(obj.getData()+" this is String");
    }
}
abstract class A{
   abstract Object getData();
}
class B extends A{
    @Override
    Object getData() {
        return new Integer(1);
    }
}
class D extends A{
    @Override
    Object getData() {
        return new Float(1.00);
    }
}
class C extends A{
    @Override
    Object getData() {
        return new String("shubham");
    }
}