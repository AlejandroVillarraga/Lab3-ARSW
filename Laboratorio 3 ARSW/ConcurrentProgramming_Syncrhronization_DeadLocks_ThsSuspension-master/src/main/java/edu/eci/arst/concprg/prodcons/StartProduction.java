/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import com.sun.javafx.font.t2k.T2KFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartProduction {
    
    private static int MAXSTOCK = 5;
    static   Object lock  = new Object();
    public static  void main(String[] args) {
        
        Queue<Integer> queue=new LinkedBlockingQueue<>();
        
        
        //generating STOCK
        
        Runnable r =() ->{
            for(int i = 0;i<6; i++ ){
                System.out.println("prueba:" +i);
            }
              
        };
        Producer p = new Producer(queue,Long.MAX_VALUE,r,lock,MAXSTOCK);
        p.setName("producer");
        p.start();
        
        //p.run();
   
        
        try {                                           
            System.out.println(Thread.currentThread().getName());
             Thread.sleep(5000);
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(StartProduction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        new Consumer(queue,lock,MAXSTOCK).start();
        
    }
    

}