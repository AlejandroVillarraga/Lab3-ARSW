package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Immortal extends Thread {

    private int health;
    
    private int defaultDamageValue;
    
    private boolean bandera=true;
    
    private final int posi;

   

    private final List<Immortal> immortalsPopulation;

    private final String name;
    
    private final Object lock;
    
    private final Random r = new Random(System.currentTimeMillis());

    boolean pause = false;

    public void pause() {
        pause = true;
    }

    public void pauseNot() {
        pause = false;
    }
    
    public void stopThreads() {
       bandera=false;
    }

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue,Object lock,int posi) {
        super(name);
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
        this.lock=lock;
        this.posi=posi;
    }

    public void run() {

        while (bandera) {
            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void fight(Immortal i2) {
        Immortal peleador;
        Immortal contrincante;  
        if(pause){
            synchronized(lock){
                try {
                    ControlFrame.threadCont.getAndIncrement();
                    lock.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Immortal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
              
        if(i2.getPosi()<this.getPosi()){
            peleador=this;
            contrincante=i2;
        }else{
            peleador=i2;
            contrincante=this;
        }
        synchronized (peleador) {
            synchronized (contrincante) {
                if (i2.getHealth() > 0) {
                    i2.changeHealth(i2.getHealth() - defaultDamageValue);
                    this.health += defaultDamageValue;
                    System.out.println("Fight: " + this + " vs " + i2);
                } else {
                    System.out.println(this + " says:" + i2 + " is already dead!");
                }
            }
        }


    }

    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }
    
    public int getPosi() {
        return posi;
    }

}
