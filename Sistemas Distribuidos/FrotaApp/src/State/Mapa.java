package State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Mapa {

    ReentrantLock lock;
    private Location[][] mapa;   /** em cada posição do array temos a Location nessa posição */
    int N;                          /** tamanho do mapa */
    int totalScooters; // numero de trotinetes existentes no mapa
    int D; // distancia fixa 

    // gera um mapa de N por N dimensoes com N*N trotinetes distribuidas aleatoriamente

    
    public Mapa() {
        this.lock = new ReentrantLock();
        this.N = 3;
        this.D = 1;
        this.totalScooters = N * N;
        
        Random num = new Random();
        int aux = totalScooters;
        int aux2;
        
        mapa = new Location[N][N];
       
        while (aux > 0) {    
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (aux > 2) {   
                        aux2 = num.nextInt(3);
                        mapa[i][j] = new Location(i,j,aux2);
                        aux -= aux2;
                    }
                    else if (aux <= 2) {
                        if (aux > 0) {
                            aux2 = num.nextInt(aux+1);
                            aux -= aux2;
                     }
                        else aux2 = 0;
                        mapa[i][j] = new Location(i,j,aux2);
                        
                    }
                }
            }
       }
    }



    // VER LOCKS

    public ArrayList<String> getLocations(int x, int y) {
        ArrayList<String> locs = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int distance = Math.abs(x - i) + Math.abs(y - j);
                Location aux = this.mapa[i][j];
                if (distance <= this.D && aux.getScooters() > 0) 
                    locs.add(aux.toString());
            }
        }
        return locs;
    }

    // VER LOCKS

    public Location getNearestLocationScooter(int x, int y) {
        try {
            lock.lock();
            Location res = null, aux;
            int dist = D+1, dist_aux;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    dist_aux = Math.abs(x - i) + Math.abs(y - j);
                    aux = getLocation(i,j);
                    if (dist_aux <= this.D && dist_aux < dist && aux.getScooters() > 0) {
                        res = aux;
                        dist = dist_aux;
                    } 
                        
                }
            }
            return res;
        } finally {
            lock.unlock();
        }
    }

    public ArrayList<Location> getEmptySpots() {
        try {
            lock.lock();
            ArrayList<Location> res = new ArrayList<>();
            for(Location l : getEmptySpots1()) {
                if(getEmptySpots2(getNearByLocations(l))) {
                    res.add(l);
                }
            }
            return res;
        } finally {
            lock.unlock();
        }
    }

    public ArrayList<Location> getNearByLocations(Location l) {
        ArrayList<Location> res = new ArrayList<>();
        int dist_aux;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dist_aux = Math.abs(l.getX() - i) + Math.abs(l.getY() - j);
                if (dist_aux <= D) {
                    res.add(this.mapa[i][j]);
                }
            }
        }
        return res;
    }

    public ArrayList<Location> getEmptySpots1() {
        ArrayList<Location> res = new ArrayList<>();
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(this.mapa[i][j].getScooters() == 0) {
                    res.add(this.mapa[i][j]);
                }
            }
        }
        return res;
    }

    public boolean getEmptySpots2(ArrayList<Location> locs) {
        for(Location l : locs) {
            if(l.getScooters() > 0)
                return false;
        }
        return true;
    }


    public ArrayList<Location> getInitialLocations() {
        ArrayList<Location> locs = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Location aux = this.mapa[i][j];
                if (aux.getScooters() > 1) 
                    locs.add(aux);
            }
        }
        return locs;
    }



    
    public Location getLocation(int x, int y) {
        try {
            this.mapa[x][y].lockLocal();
            return this.mapa[x][y];
        }
        finally {
            this.mapa[x][y].unlockLocal();
        }
    }

    public Location[][] getMapa() {
        Location[][] newMapa = new Location[N][N];
        try {
            lock.lock();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    newMapa[i][j] = mapa[i][j].clone();
                }
            }
        }
        finally {
            lock.unlock();
        }
        return newMapa;
    }

    public int getN(){
        try {
            lock.lock();
            return this.N;
        }
        finally {
            lock.unlock();
        }
    }

    public int getD(){
        return this.D;
    }

    public void setN(int x){
        try {
            lock.lock();
            this.N = x;
        }
        finally {
            lock.unlock();
        }
    }

    public void getLocalVazio(int x, int y) throws InterruptedException {
        mapa[x][y].lockLocal();
        try {
            while (mapa[x][y].getScooters() > 0){
                mapa[x][y].getCond().await();
            }
            throw new InterruptedException();
        }
        finally {
            mapa[x][y].unlockLocal();
        }
    }


    @Override
    public String toString() {
        return "GestInfo.Mapa{" +
                "mapa=" + Arrays.toString(mapa) +
                '}';
    }
}

