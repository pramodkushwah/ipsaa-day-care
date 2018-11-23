package com.synlabs.ipsaa;

import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;

public class main
{
    public static void main(String a[]){

        Q q=new Q();
        new producer(q);
        new consumer(q);
    }
}
class Q{
    int num;
    boolean isProduce=false;
    public void put(int num){
        if(isProduce){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isProduce=true;
        this.num=num;
        System.out.println("put "+this.num);
        notify();
    }
    public void get(){
        if(!isProduce){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("get "+this.num);
        notify();
    }
}
class producer implements Runnable{
    Q q;
    producer(Q q){
        this.q=q;
        Thread t=new Thread(this,"producer");
        t.start();
    }
    @Override
    public void run() {
        int i=0;
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            q.put(i++);
        }
    }
//    Process p = Runtime.getRuntime().exec(cmd);
//            p.waitFor();
//    String text = IOUtils.toString(p.getInputStream(), Charset.defaultCharset()).trim();
//            if (text.isEmpty()) {
//        throw new RuntimeException();
//    }
}

class consumer implements Runnable{
    Q q;
    consumer(Q q){
        this.q=q;
        Thread t=new Thread(this,"comsumer");
        t.start();
    }
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            q.get();
        }
    }
}
