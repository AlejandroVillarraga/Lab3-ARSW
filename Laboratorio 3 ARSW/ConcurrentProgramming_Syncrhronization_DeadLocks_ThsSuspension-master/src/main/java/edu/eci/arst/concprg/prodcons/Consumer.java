/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    
    private Queue<Integer> queue;
    Object lock;
    int MAXSTOCK;
    public Consumer(Queue<Integer> queue, Object lock, int MAXSTOCK){
        this.queue=queue;        
        this.lock = lock;
        this.MAXSTOCK = MAXSTOCK;
    }
    
    @Override
    public void run() {
        
            while (true) {
                synchronized(lock){                    
                    if (queue.size() > 0) {
                        int elem=queue.poll();
                        System.out.println("Consumer consumes "+elem);

                    }else{
                        try {
                            Thread.sleep(1000);
                            this.lock.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    lock.notify();
                }
            }
    }
}
