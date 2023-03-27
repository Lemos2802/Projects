package State;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private String username;
    private LocalDateTime timestamp;
    private Location initalLocation;

    public Reservation(Location l, String user) {
        this.username = user;
        this.timestamp = LocalDateTime.now();
        this.initalLocation = l;
    }


    public String getUser() {
        return this.username;
    }

    public double getCusto(Location l) {
        int distance = Math.abs(l.getX() - initalLocation.getX()) + Math.abs(l.getY() - initalLocation.getY());
        long time = timestamp.until(LocalDateTime.now(), ChronoUnit.SECONDS);

        return (int) time * distance * 0.05 * 0.5;
    }

    public Location getInitialLocation() {
        return this.initalLocation;
    }


    @Override
    public String toString() {
        return "State.Reservation{" +
                "User:" + username +
                ", Hora:" + timestamp +
                ", Inicio:" + initalLocation +
                '}';
    }

}
