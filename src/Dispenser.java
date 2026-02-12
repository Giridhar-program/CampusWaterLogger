public class Dispenser {
    String id;
    String location;
    int refillsToday;
    String lastRefillTime;

    public Dispenser(String id, String location, int refillsToday, String lastRefillTime) {
        this.id = id;
        this.location = location;
        this.refillsToday = refillsToday;
        this.lastRefillTime = lastRefillTime;
    }
}