package State;

import Exceptions.*;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class State {
    private ReentrantLock lock;
    private Condition generate;
    private Condition notifications;
    private Mapa map;
    private Map<String, User> logs;
    private long reservationID = 0;
    private Map<String, Reservation> reservas;
    private ArrayList<Reward> recompensas;
    private Thread rewardsT;

    public State() {
        this.lock = new ReentrantLock();
        this.generate = lock.newCondition();
        this.notifications = lock.newCondition();
        this.map = new Mapa();
        this.logs = new HashMap<>();
        this.reservas = new HashMap<>();
        this.recompensas = new ArrayList<>();
        rewardsT = new Thread(() -> {
            try {
                this.generateRewards();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        rewardsT.start();
    }


    public String createID() {
        try {
            lock.lock();
            return String.valueOf(reservationID++);
        }
        finally {
            lock.unlock();
        }
        
    }
    

    public Reward getReward (Location initial, Location end) {
        try {
            lock.lock();
            for(Reward r : recompensas) {
                if(r.getInitialLocation() == initial && r.getFinalLocation() == end) {
                    return r;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }


    public ArrayList<Reward> getRecompensas(int x, int y) {
        try {
            lock.lock();
            ArrayList<Reward> res = new ArrayList<>();
            for(Reward r : recompensas) {
                int distance = Math.abs(x - r.getInitialLocation().getX()) + Math.abs(y - r.getInitialLocation().getY());
                if(distance <= this.map.getD()) {
                    res.add(r);
                }
            }
            return res;
        } finally {
            lock.unlock();
        }
    }

    public void generateRewards() throws InterruptedException {
        try {
        lock.lock();
        
            while(true) {
                ArrayList<Reward> res = new ArrayList<>();
                ArrayList<Location> initialPositions = this.map.getInitialLocations();
                ArrayList<Location> finalPositions = this.map.getEmptySpots();

                for(Location initialP : initialPositions) {
                    for(Location finalP : finalPositions) {
                        Reward r = new Reward(initialP, finalP);
                        res.add(r);
                    }
                }

                this.recompensas = res;
                notifications.signalAll();
                generate.await();
            
            }
        } finally {
            lock.unlock();
        }
    }


    public ArrayList<Reward> getRewardsNotification(String user,int x, int y) throws InterruptedException, NotificationsOFF {
        try {
            lock.lock();
            
            notifications.await();
            
            if(!logs.get(user).getNotificacoes()) throw new NotificationsOFF("NotificationsDisabled");
            
            ArrayList<Reward> r = getRecompensas(x, y);
            
            return r;            

        } finally {
            lock.unlock();
        }
    }

    public void notificationsON_OFF(String user, boolean bool) {
        try {
            lock.lock();
            this.logs.get(user).setON_OFF(bool);
            notifications.signalAll();
        } finally {
            lock.unlock();
        }
    }


    public User parseLine (String userInput) {
        String[] tokens = userInput.split(" ");
        return new User(
                tokens[0],
                tokens[1]
                );
    }

    public String registaUser(String data) throws UserAlreadyExists {
        User u = parseLine(data);
        addUser(u);
        try {
            u.l.lock();
            return u.getUsername();
        }
        finally {
            u.l.unlock();
        }

    }

    public void addUser(User u) throws UserAlreadyExists {
        try {
            lock.lock();
            if (logs.containsKey(u.getUsername())) throw new UserAlreadyExists("User already exists");
            logs.put(u.getUsername(), u);
        }
        finally {
            lock.unlock();
        }
    }

    public boolean verificaUser(String userID, String pass) throws InvalidUsername, WrongPassword {
        try {
            lock.lock();
            if (!logs.containsKey(userID)) throw new InvalidUsername("Username doesnt exist");
            String passW = logs.get(userID).getPassword();
            if (!passW.equals(pass)) throw new WrongPassword("Wrong password");
            return true;
        }
        finally {
            lock.unlock();
        }
    }

    public boolean verifyCoords(int x,int y) throws WrongCoords {
        int N = map.getN();
        if (x<0 || x>= N || y<0 || y>= N) throw new WrongCoords("Coordenadas incorretas!");
        return true;
    }

    public boolean verifyReservation(String user, String code) throws InvalidReservation {
        if(!this.reservas.containsKey(code) || !this.reservas.get(code).getUser().equals(user))
            throw new InvalidReservation("Invalid Reservation Code!");
        else return true;
    }

    // VER LOCKS

    public AbstractMap.SimpleEntry<String,String> reserveScooter(int x, int y, String user) {
        try {
            lock.lock();
            Location l = this.map.getNearestLocationScooter(x, y);
            if (l != null) {
                String ID = createID();
                Reservation r = new Reservation(l, user);
                addReservation(ID, r);
                removeScooter(l.getX(), l.getY());
                return new AbstractMap.SimpleEntry<>(ID, l.toString());
            }

            else return new AbstractMap.SimpleEntry<>(String.valueOf(-1), "");
        } finally {
            lock.unlock();
        }
    }


    public Reservation getReservation(String code) {
        try {
            lock.lock();
            return this.reservas.get(code);
        }
        finally {
            lock.unlock();
        }
    }

    
    public void removeReservation(String ID){
        try {
            lock.lock();
            reservas.remove(ID);
        }
        finally {
            lock.unlock();
        }
    }
    
    public void addReservation(String ID, Reservation r){
        try {
            lock.lock();
            reservas.put(ID, r);
        }
        finally {
            lock.unlock();
        }
    }

    // CONDITION PARA ACORDAR O generateRewards
    public void removeScooter(int x, int y)  {
        try {
            lock.lock();
            this.map.getLocation(x,y).retirar();
            generate.signal();
        } finally {
            lock.unlock();
        }

    }


    // CONDITION PARA ACORDAR O generateRewards
    public void addScooter(int x, int y) {
        try {
            lock.lock();
            this.map.getLocation(x,y).somar();
            generate.signal();
        } finally {
            lock.unlock();
        }

    }

    public User getUser(String nome) {
        try {
            lock.lock();
            return this.logs.get(nome);
        } finally {
            lock.unlock();
        }
    }

    public Mapa getMapa(){
        try {
            map.lock.lock();
            return map;
        }
        finally {
            map.lock.unlock();
        }
    }





}