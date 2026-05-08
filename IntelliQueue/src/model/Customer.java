package model;

public class Customer {
    private static int idCounter = 1;
    
    private final int id;
    private final String name;
    private final int priority;   // 1 = Senior, 2 = Normal
    private final int timeNeeded; // seconds
    private final long arrivalTime; // for wait time calculation
    
    public Customer(String name, int priority, int timeNeeded) {
        this.id = idCounter++;
        this.name = name;
        this.priority = priority;
        this.timeNeeded = timeNeeded;
        this.arrivalTime = System.currentTimeMillis();
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public int getPriority() { return priority; }
    public int getTimeNeeded() { return timeNeeded; }
    public long getArrivalTime() { return arrivalTime; }
    
    public long getWaitTime() {
        return (System.currentTimeMillis() - arrivalTime) / 1000; // seconds
    }
    
    @Override
    public String toString() {
        return String.format("#%03d %s (%s, %ds)", 
            id, name, 
            priority == 1 ? "Senior" : "Normal", 
            timeNeeded);
    }
}