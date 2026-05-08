package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Thread-safe manager for two queues: senior and normal.
 * Provides blocking wait methods and statistics tracking.
 */
public class QueueManager {
    private final Queue<Customer> seniorQueue = new LinkedList<>();
    private final Queue<Customer> normalQueue = new LinkedList<>();
    
    // Statistics
    private int totalServed = 0;
    private long totalWaitTime = 0; // in seconds
    private int seniorServed = 0;
    private int normalServed = 0;
    
    public synchronized void addCustomer(Customer c) {
        if (c.getPriority() == 1) {
            seniorQueue.add(c);
        } else {
            normalQueue.add(c);
        }
        notifyAll(); // Wake up waiting counter threads
    }
    
    /** Blocking wait until a senior customer is available */
    public synchronized void waitForSenior() throws InterruptedException {
        while (seniorQueue.isEmpty()) {
            wait();
        }
    }
    
    /** Blocking wait until a normal customer is available */
    public synchronized void waitForNormal() throws InterruptedException {
        while (normalQueue.isEmpty()) {
            wait();
        }
    }
    
    /** Poll senior queue and update statistics */
    public synchronized Customer pollSenior() {
        Customer c = seniorQueue.poll();
        if (c != null) {
            updateStats(c);
            seniorServed++;
        }
        return c;
    }
    
    /** Poll normal queue and update statistics */
    public synchronized Customer pollNormal() {
        Customer c = normalQueue.poll();
        if (c != null) {
            updateStats(c);
            normalServed++;
        }
        return c;
    }
    
    private void updateStats(Customer c) {
        totalServed++;
        totalWaitTime += c.getWaitTime();
    }
    
    /** Snapshot copies for UI */
    public synchronized List<Customer> snapshotSenior() {
        return new ArrayList<>(seniorQueue);
    }
    
    public synchronized List<Customer> snapshotNormal() {
        return new ArrayList<>(normalQueue);
    }
    
    // Statistics getters
    public synchronized int getTotalServed() { return totalServed; }
    public synchronized int getSeniorServed() { return seniorServed; }
    public synchronized int getNormalServed() { return normalServed; }
    public synchronized int getSeniorQueueSize() { return seniorQueue.size(); }
    public synchronized int getNormalQueueSize() { return normalQueue.size(); }
    
    public synchronized double getAverageWaitTime() {
        return totalServed > 0 ? (double) totalWaitTime / totalServed : 0.0;
    }
}