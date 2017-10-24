/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Producer extends Thread {

    private Queue<Integer> queue = null;

    private int dataSeed = 0;
    private Random rand=null;
    private final long stockLimit;
    Object lock ;
    int MAXSTOCK;
    public Producer(Queue<Integer> queue,long stockLimit, Runnable r, Object lock,int MAXSTOCK) {
        this.queue = queue;
        rand = new Random(System.currentTimeMillis());
        this.stockLimit=stockLimit;
        this.lock = lock;
        this.MAXSTOCK = MAXSTOCK;
    }

    
    
    @Override
    public void run() {
        
            while (true) {             
                synchronized(lock){
                    if(queue.size()<MAXSTOCK){
                        System.out.println("Producer added " + dataSeed); 
                        dataSeed = dataSeed + rand.nextInt(100);
                        queue.add(dataSeed);
                    }else{
                        try {
                       // Thread.sleep(100);
                            //System.out.println("----- "+.currentThread().getState());                           
                           this.lock.wait();                                                      
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    this.lock.notify();
                }
                
            }

        
    }
}
