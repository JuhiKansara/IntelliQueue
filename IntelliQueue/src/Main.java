import controller.QueueController;
import javax.swing.*;
import model.QueueManager;
import view.MainView;

/**
 * IntelliQueue - Real-time Queue Management System
 * Features:
 * - Automatic queue processing (no manual buttons)
 * - Real-time countdown with progress bars
 * - Separate counters for Senior and Normal queues
 * - Live statistics tracking
 * - Thread-safe operations
 * - Sound notifications
 */
public class Main {
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default
        }
        
        SwingUtilities.invokeLater(() -> {
            QueueManager manager = new QueueManager();
            MainView view = new MainView();
            QueueController controller = new QueueController(manager, view);
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                controller.shutdown();
                System.out.println("Application closed gracefully.");
            }));
        });
    }
}