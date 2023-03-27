import Exceptions.*;
import State.Reward;
import State.Location;
import State.State;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static State model;
    private static Map<String,TaggedConnection> tg = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(12343);
        
        model = new State(); // gerar mapa com distribuiçao aleatoria de um numero fixo de trotinetes pelo mapa
        // model.generateRewards();
        
        while(true) {
            Socket s = ss.accept();
            TaggedConnection c = new TaggedConnection(s);

            Runnable worker = () -> {
                try {
                    for (;;) {
                            int cond = 0;
                            TaggedConnection.Frame frame = c.receive();
                            int tag = frame.tag;
                            String data = new String(frame.data);
                        try {
                            // RECOMPENSAS 
                            if (frame.tag == 0) {
 
                            }
                            // REGISTO DE USER
                            else if (frame.tag == 1) {
                                String id = model.registaUser(data);

                                tg.put(id,c);
                                c.send(frame.tag, String.valueOf(cond).getBytes());
                                c.send(frame.tag, "Registo Concluído!!".getBytes());
                            }
                            // LOGIN DE USER
                            else if (frame.tag == 2) {
                                String[] tokens = data.split(" ");
                                if (model.verificaUser(tokens[0],tokens[1])) {
                                    System.out.println("Replying to: " + data);
                                    tg.put(tokens[0],c);
                                    
                                    c.send(frame.tag, String.valueOf(cond).getBytes());
                                    c.send(frame.tag, ("Login Concluído!!").getBytes());
                                }
                            }

                            else if (frame.tag == 3) {
                                // Tratar da parte de devolver todas as trotinetes disponiveis
                                String[] tokens = data.split(" ");
                                if (model.verifyCoords(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]))) {
                                    ArrayList<String> n = model.getMapa().getLocations(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                                    c.send(frame.tag, String.valueOf(cond).getBytes());
                                    c.send(frame.tag, String.valueOf(n).getBytes());
                                }
                            }

                            else if (frame.tag == 4){
                                // Tratar da parte da reserva da trotinete
                                String[] tokens = data.split(" ");
                                if (model.verifyCoords(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]))) {
                                    AbstractMap.SimpleEntry<String,String> n = model.reserveScooter(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), tokens[2]);
                                    // ATUALIZAR REWARDS
                                    c.send(frame.tag, String.valueOf(cond).getBytes());
                                    c.send(frame.tag,(n.getKey()+":"+n.getValue()+":").getBytes());                               
                                }

            
                            }

                            else if(frame.tag==5) {
                                // Tratar da parte do estacionamento da trotinente
                                String[] tokens = data.split(" ");
                                
                                int x = Integer.parseInt(tokens[0]);
                                int y = Integer.parseInt(tokens[1]);
                                
                                if (model.verifyReservation(tokens[2], tokens[3]) && model.verifyCoords(x,y)) {
                                    
                                    Location initial = model.getReservation(tokens[3]).getInitialLocation();
                                    Location end = model.getMapa().getLocation(x,y);
                                    Reward r = model.getReward(initial,end);
                                    
                                    double reward = -1;
                                    if (r != null) reward = r.getReward();
                                    double custo = model.getReservation(tokens[3]).getCusto(end);
                                    
                                    model.removeReservation(tokens[3]);
                                    
                                    model.addScooter(x,y);
                                    
                                    
                                    // ATUALIZAR REWARDS
                                    
                                    
                                    c.send(frame.tag, String.valueOf(cond).getBytes());
                                    c.send(frame.tag,String.valueOf(custo+":"+reward+":").getBytes());
                                }

                            }
                            else if(frame.tag==6){
                                //Não recebe nada.
                                //Apenas envia da tag 0 para a 6 quando há infetados.
                            }
                            else if(frame.tag==7){
                                // Devolve lista de recompensas dado um local
                                String[] tokens = data.split(" ");
                                if (model.verifyCoords(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]))) {
                                    ArrayList<Reward> rewards = model.getRecompensas(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                                    c.send(frame.tag, String.valueOf(cond).getBytes());
                                    c.send(frame.tag,String.valueOf(rewards).getBytes());
                                }
                            }
                            else if (frame.tag==8) {
                                 
                                String[] tokens = data.split(" ");
                                
                                int x = Integer.parseInt(tokens[0]);
                                int y = Integer.parseInt(tokens[1]);
                                
                                if (model.verifyCoords(x,y)) {
                                
                                    Runnable notificationsWorker = () -> {
                                        try {
                                            model.notificationsON_OFF(tokens[2],true);
                                            TaggedConnection con = tg.get(tokens[2]);
                                            while(true) {
                                                ArrayList<Reward> r;
                                                try {
                                                   r = model.getRewardsNotification(tokens[2],x, y);
                                                } catch (NotificationsOFF e) {
                                                    break;
                                                }
                                                
                                                con.send(6, String.valueOf(r).getBytes());
                                            }
                                            
                                        } catch (InterruptedException e) {
                                                
                                        } catch (IOException ioException) {
                                             ioException.printStackTrace();
                                        }  
                                    };
                                    new Thread(notificationsWorker).start();
                                }

                                c.send(frame.tag, String.valueOf(cond).getBytes());
                                c.send(frame.tag, String.valueOf(cond).getBytes());
                                
                            }

                            else if (frame.tag == 9) {
                                String[] tokens = data.split(" ");
                                model.notificationsON_OFF(tokens[0],false);
                                c.send(frame.tag, String.valueOf(cond).getBytes());
                                c.send(frame.tag, String.valueOf(cond).getBytes());
                            }



                        } catch ( UserAlreadyExists | WrongPassword | InvalidUsername | WrongCoords | InvalidReservation e) {
                            cond = 1;
                            c.send(frame.tag, String.valueOf(cond).getBytes());
                            c.send(frame.tag, e.getMessage().getBytes());
                        }
                    }
                } catch (IOException e) {
                    e.getMessage();
                }
            };

            new Thread(worker).start();

        }
    }
}

