import ClientUI.*;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Client {
    private static Scanner scin;
    private static Demultiplexer m;
    private static String userID;
    private static boolean hasReservation;
    private static boolean notifications; // -> true = ON, false = OFF
    private static Menu menu;
    private static Thread nots; // Tratar aqui das rewards????
    
    public static void run() throws IOException, InterruptedException {
        System.out.println("\033[1;35mWelcome to ScooterGO!\033[0m");
        nots.start();
        menuPrincipal();
        nots.interrupt();
        m.close();
        System.out.println("\033[1;36m"+"Bye!"+"\033[0m");
    }

    private static void menuPrincipal() throws InterruptedException {
        menu = new Menu(new String[]{
                "Registar",
                "Login"});

        menu.setHandler(1, Client::efetuarRegisto);
        menu.setHandler(2, Client::efetuarLogin);
        menu.run(0);
    }


    private static void userMenu() throws InterruptedException {
        Menu menu = new Menu(new String[]{
                "List all locations with available scooters near given location",
                "List rewards starting close to a given location",
                "Reserve the nearest available scooter to a given location", // so vai estar disponivel se o user n tiver reserva ativa
                "Park Scooter", // so vai estar disponivel se o user tiver reserva ativa
                "Turn ON reward notifications",
                "Turn OFF reward notifications",
                "Logout"
        });

        menu.setPreCondition(3, ()->!hasReservation); // Pre condicao nao tem reserva ativa
        menu.setPreCondition(4, ()->hasReservation); // Pre condicao tem reserva ativa
        menu.setPreCondition(5, ()->!notifications); // Pre condicao tem notificacoes ativas
        menu.setPreCondition(6, ()->notifications); // Pre condicao nao tem not ativa

        // Registar os handlers
        menu.setHandler(1, Client::listNearestScooters);
        menu.setHandler(2, Client::listRewards);
        menu.setHandler(3, Client::reserveScooter);
        menu.setHandler(4, Client::parkScooter);
        menu.setHandler(5, Client::onNotifications);
        menu.setHandler(6, Client::offNotifications);
        menu.setHandler(7, Client::logout);
        menu.run(1);

    }

    static void efetuarRegisto() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                System.out.println("Insira um nome de Utilizador: ");
                String user = scin.nextLine();
                System.out.println("Inserir password: ");
                String pass = scin.nextLine();
                hasReservation = false;
                notifications = false;

                m.send(1, (user+" "+pass+" ").getBytes());
                byte[] b = m.receive(1);
                int excecao = Integer.parseInt(new String(b));
                byte[] b1 = m.receive(1);

                if (excecao==0){
                    System.out.println("\033[1;36m"+ new String(b1)+"\033[0m");
                    userID = user;
                    userMenu();
                }
                else menu.printExcecao(new String(b1) + "\nRegisto não efetuado.");

            } catch (NullPointerException | IOException | InterruptedException e) {
                menu.printExcecao(e.getMessage());
            }
        });
        t.start();
        t.join();
    }

    static void efetuarLogin() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                System.out.println("Insira nome de Utilizador: ");
                String user = scin.nextLine();
                System.out.println("Inserir password: ");
                String pass = scin.nextLine();              
                hasReservation = false;
                notifications = false;
                
                m.send(2, (user+" "+pass+" ").getBytes());

                byte[] b = m.receive(2);
                int excecao = Integer.parseInt(new String(b));
                byte[] b1 = m.receive(2);
                if (excecao==0) {
                    System.out.println("\033[1;36m"+ new String(b1) +"\033[0m");
                    userID = user;
                    userMenu();
                }
                else menu.printExcecao(new String(b1) + "\nFalha na autenticação.");

            } catch (NullPointerException | IOException | InterruptedException e) {
                menu.printExcecao(e.getMessage());
            }
        });
        t.start();
        t.join();

    }

    static void listNearestScooters() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                System.out.println("Insira a coordenada x: ");
                int x = scin.nextInt();
                System.out.println("Insira a coordenada y: ");
                int y = scin.nextInt();

                m.send(3, (x+" "+y+" ").getBytes());

                byte[] b = m.receive(3);
                int excecao = Integer.parseInt(new String(b));
                byte[] b1 = m.receive(3);
                if (excecao == 0) System.out.println("\033[1;36m» List off all available Scooters: " +"\033[0m"+ new String(b1));
                else menu.printExcecao(new String(b1));

            } catch (NullPointerException | IOException | InterruptedException e) {
                menu.printExcecao(e.getMessage());
            }
            catch (InputMismatchException valIncorretos) {
                menu.printExcecao("Valor não é um número inteiro!");
            }
        });
        t.start();
        t.join();
    }

    static void listRewards() throws InterruptedException {
        Thread t = new Thread(() -> {
            
            try {
                System.out.println("Insira a coordenada x: ");
                int x = scin.nextInt();
                System.out.println("Insira a coordenada y: ");
                int y = scin.nextInt();

                m.send(7, (x+" "+y+" ").getBytes());

                byte[] b = m.receive(7);
                int excecao = Integer.parseInt(new String(b));
                byte[] b1 = m.receive(7);
                if (excecao == 0) System.out.println("\033[1;36m» List of rewards in a given location: " +"\033[0m"+ new String(b1));
                else menu.printExcecao(new String(b1));

            } catch (NullPointerException | IOException | InterruptedException e) {
                menu.printExcecao(e.getMessage());
            }
            catch (InputMismatchException valIncorretos) {
                menu.printExcecao("Valor não é um número inteiro!");
            }
            
        });
        t.start();
        t.join();
    }
    
    static void reserveScooter() throws InterruptedException {
        Thread t = new Thread(() -> {         
            try {
                System.out.println("Insira a coordenada x: ");
                int x = scin.nextInt();
                System.out.println("Insira a coordenada y: ");
                int y = scin.nextInt();

                m.send(4, (x+" "+y+" "+userID+" ").getBytes());
                
                byte[] b = m.receive(4);
                int excecao = Integer.parseInt(new String(b));
                byte[] b1 = m.receive(4);

                if (excecao == 0) {
                    String[] tokens = new String(b1).split(":");
                    int id = Integer.parseInt(tokens[0]);
                    if (id != -1) {
                        hasReservation = true;
                        System.out.println("\033[1;36m» Reservation: " +"\033[0m"+ "ID: " + id + " Location: " + tokens[1]);
                    } 
                    else System.out.println("\033[1;36m» Reservation: " +"\033[0m" + "No available scooters in given location");
                }
                else menu.printExcecao(new String(b1));

            } catch (NullPointerException | IOException | InterruptedException e) {
                menu.printExcecao(e.getMessage());
            }
            catch (InputMismatchException valIncorretos) {
                menu.printExcecao("Valor não é um número inteiro!");
            }
            
        });
        t.start();
        t.join();
    }

    static void parkScooter() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                System.out.println("Introduce the reservation code: ");
                int code = scin.nextInt();
                System.out.println("Introduce the X coordinate: ");
                int x = scin.nextInt();
                System.out.println("Introduce the Y coordinate: ");
                int y = scin.nextInt();

                m.send(5, (x+" "+y+" "+userID+" "+code+ " ").getBytes());

                byte[] b = m.receive(5);
                int excecao = Integer.parseInt(new String(b));
                byte[] b1 = m.receive(5);
                if (excecao == 0) {
                    String[] tokens = new String(b1).split(":");
                    hasReservation = false;
                    if (Double.parseDouble(tokens[1]) != -1) {
                        System.out.println("\033[1;36m» Parked successfully " +"\033[0m"+ " Cost: " +tokens[0]+ " Reward: " + tokens[1]);
                    } else {
                        System.out.println("\033[1;36m» Parked successfully " +"\033[0m"+ " Cost: " +tokens[0]);
                    }
                }
                else menu.printExcecao(new String(b1));

            } catch (NullPointerException | IOException | InterruptedException e) {
                menu.printExcecao(e.getMessage());
            }
            catch (InputMismatchException valIncorretos) {
                menu.printExcecao("Valor não é um número inteiro!");
            }
        });
        t.start();
        t.join();
    }

    static void onNotifications() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                System.out.println("Introduce the X coordinate: ");
                int x = scin.nextInt();
                System.out.println("Introduce the Y coordinate: ");
                int y = scin.nextInt();

                m.send(8, (x+" "+y+" "+userID+" ").getBytes());

                byte[] b = m.receive(8);
                int excecao = Integer.parseInt(new String(b));
                byte[] b1 = m.receive(8);
                if (excecao == 0) {
                    notifications = true;
                    System.out.println("\033[1;36m» Notifications set ON " +"\033[0m");
                }
                else menu.printExcecao(new String(b1));

            } catch (NullPointerException | IOException | InterruptedException e) {
                menu.printExcecao(e.getMessage());
            }
            catch (InputMismatchException valIncorretos) {
                menu.printExcecao("Valor não é um número inteiro!");
            }
        });
        t.start();  
        t.join();
    }

    static void offNotifications() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                m.send(9, (userID+" ").getBytes());
            
                byte[] b = m.receive(9);
                int excecao = Integer.parseInt(new String(b));
                byte[] b1 = m.receive(9);
                if (excecao == 0) {
                    notifications = false;
                    System.out.println("\033[1;36m» Notifications set OFF" +"\033[0m");
                }
                else menu.printExcecao(new String(b1));
            
            
            } catch (NullPointerException | IOException | InterruptedException e) {
                menu.printExcecao(e.getMessage());
            }
        });
        t.start();
        t.join();
    }


    static void logout() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                nots.interrupt();
                m.close();
                //s.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        t.start();
        t.join();
    }

    public static void main(String[] args) throws Exception {
        scin = new Scanner(System.in);
        Socket s = new Socket("localhost", 12343);
        m = new Demultiplexer(new TaggedConnection(s));
        nots = new Thread(() -> {
            try {
                while (true) {
                    byte[] b2 = m.receive(6);
                    System.out.println("\n\033[1;31m» " + new String(b2) + "\033[0m");
                }
            } catch (IOException | InterruptedException e) {

            }
        });
        m.start();
        run();

    }
}
