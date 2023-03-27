package State;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 */

public class Location {

    private ReentrantLock lock;
    private final int x;
    private final int y;
    private int scooters;
    private Condition cond;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        this.lock = new ReentrantLock();
        this.scooters = 0;
        this.cond = lock.newCondition();
    }

    public Location(int x, int y,int scooters) {
        this.x = x;
        this.y = y;
        this.lock = new ReentrantLock();
        this.scooters = scooters;
        this.cond = lock.newCondition();
    }

    public void retirar(){
        lock.lock();
        try {
            this.scooters--;
            if(this.scooters==0) this.cond.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void somar(){
        lock.lock();
        try {
            this.scooters++;
        }finally {
            lock.unlock();
        }
    }

    public int scooters(){
        return scooters;
    }

    public void lockLocal (){
        lock.lock();
    }

    public void unlockLocal (){
        lock.unlock();
    }

    public int getScooters() {
        return scooters;
    }

    public void setScooters(int scooters) {
        this.scooters = scooters;
    }

    public Condition getCond() {
        return cond;
    }

    public void setCond(Condition cond) {
        this.cond = cond;
    }

    public Location clone() {
        return new Location(x,y,scooters);
    }

    public int getX() {
        try{
            lock.lock();
            return x;
        }
        finally {
            lock.unlock();
        }
    }


    public int getY() {
        try{
            lock.lock();
            return y;
        }
        finally {
            lock.unlock();
        }
    }


    @Override
    public String toString() {
        return "State.Location{" +
                "x=" + x +
                ", y=" + y +
                ", nº Scooters=" + scooters +
                '}';
    }
}