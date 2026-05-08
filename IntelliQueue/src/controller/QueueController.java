package controller;

import java.util.List;
import javax.swing.*;
import model.Customer;
import model.QueueManager;
import view.MainView;

/**
 * Controller with two dedicated counter threads.
 * Counter 1: serves Normal queue (priority==2)
 * Counter 2: serves Senior queue (priority==1)
 * 
 * Includes real-time countdown and statistics updates.
 */
public class QueueController {
    
    private final QueueManager manager;
    private final MainView view;
    private volatile boolean running = true;
    
    public QueueController(QueueManager manager, MainView view) {
        this.manager = manager;
        this.view = view;
        view.setController(this);
        
        updateViewSnapshot();
        startStatisticsUpdater();
        startNormalCounter();
        startSeniorCounter();
    }
    
    public void addCustomer(String name, int priority, int time) {
        manager.addCustomer(new Customer(name, priority, time));
        updateViewSnapshot();
        updateStatistics();
    }
    
    private void updateViewSnapshot() {
        List<Customer> seniors = manager.snapshotSenior();
        List<Customer> normals = manager.snapshotNormal();
        view.updateList(seniors, normals);
    }
    
    private void updateStatistics() {
        view.updateStatistics(
            manager.getTotalServed(),
            manager.getSeniorServed(),
            manager.getNormalServed(),
            manager.getSeniorQueueSize(),
            manager.getNormalQueueSize(),
            manager.getAverageWaitTime()
        );
    }
    
    /** Updates statistics every 500ms */
    private void startStatisticsUpdater() {
        Timer statsTimer = new Timer(500, e -> updateStatistics());
        statsTimer.start();
    }
    
    private void startNormalCounter() {
        Thread normal = new Thread(() -> {
            while (running) {
                try {
                    manager.waitForNormal();
                    Customer c;
                    synchronized (manager) {
                        c = manager.pollNormal();
                    }
                    if (c == null) continue;
                    
                    runService(1, c, false);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Counter-Normal");
        normal.setDaemon(true);
        normal.start();
    }
    
    private void startSeniorCounter() {
        Thread senior = new Thread(() -> {
            while (running) {
                try {
                    manager.waitForSenior();
                    Customer c;
                    synchronized (manager) {
                        c = manager.pollSenior();
                    }
                    if (c == null) continue;
                    
                    runService(2, c, true);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Counter-Senior");
        senior.setDaemon(true);
        senior.start();
    }
    
    /**
     * Run serving logic with per-second UI updates using Swing Timer.
     * Controller thread waits until service completes.
     */
    private void runService(int counter, Customer c, boolean isSenior) throws InterruptedException {
        final int[] remaining = { c.getTimeNeeded() };
        
        // Show serving status immediately
        view.showServing(counter, c.getName(), remaining[0], c.getTimeNeeded(), isSenior);
        updateViewSnapshot();
        updateStatistics();
        
        final Object lock = new Object();
        
        // Swing Timer updates UI every second on EDT
        Timer timer = new Timer(1000, null);
        timer.addActionListener(ev -> {
            remaining[0]--;
            
            if (remaining[0] > 0) {
                view.showServing(counter, c.getName(), remaining[0], c.getTimeNeeded(), isSenior);
            } else {
                timer.stop();
                
                // Service complete
                String servedText = String.format("Counter %d served: %s (waited %ds)", 
                    counter, c.toString(), c.getWaitTime());
                view.showServed(servedText);
                view.resetServing(counter);
                
                updateViewSnapshot();
                updateStatistics();
                
                // Play sound notification (optional)
                java.awt.Toolkit.getDefaultToolkit().beep();
                
                synchronized (lock) {
                    lock.notify();
                }
            }
        });
        
        // Start timer on EDT
        SwingUtilities.invokeLater(timer::start);
        
        // Wait for service completion
        synchronized (lock) {
            lock.wait();
        }
    }
    
    public void shutdown() {
        running = false;
    }
}