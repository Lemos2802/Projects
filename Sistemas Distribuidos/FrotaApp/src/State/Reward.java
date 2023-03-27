package State;

public class Reward {
    private Location initialLocation;
    private Location finalLocation;
    private double Reward;
    

        
    public Reward(Location initialLocation, Location finalLocation) {
        this.initialLocation = initialLocation;
        this.finalLocation = finalLocation;
        generateRewardValue();
    }

    public void generateRewardValue() {
        int distance = Math.abs(initialLocation.getX() - finalLocation.getX()) + Math.abs(initialLocation.getY() - finalLocation.getY());
        this.Reward = distance * 0.5;
    }

    public Location getInitialLocation() {
        return initialLocation;
    }


    public Location getFinalLocation() {
        return finalLocation;
    }


    public double getReward() {
        return Reward;
    }
    
    @Override
    public String toString() {
        return "State.Reward{" +
                "InitialLoc: " + initialLocation +
                ", FinalLoc: " + finalLocation +
                ", Reward:" + Reward +
                '}';
    }
    
}