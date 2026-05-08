package view;

import controller.QueueController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Customer;

/**
 * Enhanced Modern UI for IntelliQueue with statistics and progress bars.
 */
public class MainView extends JFrame {
    
    private QueueController controller;
    
    private final DefaultListModel<String> seniorModel = new DefaultListModel<>();
    private final DefaultListModel<String> normalModel = new DefaultListModel<>();
    private final DefaultListModel<String> servedModel = new DefaultListModel<>();
    
    private final JLabel counter1Label = new JLabel("Counter 1: Idle", SwingConstants.CENTER);
    private final JLabel counter2Label = new JLabel("Counter 2: Idle", SwingConstants.CENTER);
    private final JProgressBar progress1 = new JProgressBar();
    private final JProgressBar progress2 = new JProgressBar();
    
    private final JTextField nameField = new JTextField();
    private final JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Normal", "Senior"});
    private final JTextField timeField = new JTextField("30");
    
    // Statistics labels
    private final JLabel statsLabel = new JLabel();
    
    public MainView() {
        setTitle("IntelliQueue — Modern Simulator (Real-time)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        root.setBackground(Color.decode("#F6F8FB"));
        setContentPane(root);
        
        // Top input card
        root.add(createTopPanel(), BorderLayout.NORTH);
        
        // Center columns
        root.add(createCenterPanel(), BorderLayout.CENTER);
        
        // Bottom statistics
        root.add(createStatsPanel(), BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JPanel createTopPanel() {
        JPanel topCard = cardPanel();
        topCard.setLayout(new BorderLayout(8, 8));
        
        JLabel heading = new JLabel("Add Customer", SwingConstants.LEFT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        topCard.add(heading, BorderLayout.NORTH);
        
        JPanel inputRow = new JPanel(new GridBagLayout());
        inputRow.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0; c.gridy = 0; c.weightx = 0.4;
        inputRow.add(labeledPanel("Name", nameField), c);
        
        c.gridx = 1; c.weightx = 0.2;
        inputRow.add(labeledPanel("Priority", priorityCombo), c);
        
        c.gridx = 2; c.weightx = 0.2;
        inputRow.add(labeledPanel("Time (sec)", timeField), c);
        
        c.gridx = 3; c.weightx = 0;
        JButton addBtn = new JButton("Add Customer");
        styleButton(addBtn, "#2E7DFA");
        addBtn.addActionListener(e -> onAdd());
        inputRow.add(addBtn, c);
        
        topCard.add(inputRow, BorderLayout.CENTER);
        return topCard;
    }
    
    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new GridLayout(1, 3, 12, 12));
        center.setOpaque(false);
        
        // Senior Queue
        JPanel seniorCard = cardPanel();
        seniorCard.setLayout(new BorderLayout(8, 8));
        JLabel seniorTitle = new JLabel("Senior Queue (Priority)", SwingConstants.CENTER);
        seniorTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        seniorTitle.setForeground(Color.decode("#1976D2"));
        seniorCard.add(seniorTitle, BorderLayout.NORTH);
        
        JList<String> seniorList = new JList<>(seniorModel);
        seniorList.setFont(new Font("Monospaced", Font.PLAIN, 13));
        seniorCard.add(new JScrollPane(seniorList), BorderLayout.CENTER);
        
        // Normal Queue
        JPanel normalCard = cardPanel();
        normalCard.setLayout(new BorderLayout(8, 8));
        JLabel normalTitle = new JLabel("Normal Queue", SwingConstants.CENTER);
        normalTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        normalTitle.setForeground(Color.decode("#2E7D32"));
        normalCard.add(normalTitle, BorderLayout.NORTH);
        
        JList<String> normalList = new JList<>(normalModel);
        normalList.setFont(new Font("Monospaced", Font.PLAIN, 13));
        normalCard.add(new JScrollPane(normalList), BorderLayout.CENTER);
        
        // Serving & Served Panel
        JPanel rightCard = cardPanel();
        rightCard.setLayout(new BorderLayout(8, 8));
        
        JLabel servingTitle = new JLabel("Currently Serving", SwingConstants.CENTER);
        servingTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        rightCard.add(servingTitle, BorderLayout.NORTH);
        
        JPanel servingPanel = new JPanel(new GridLayout(4, 1, 6, 6));
        servingPanel.setOpaque(false);
        
        counter1Label.setFont(new Font("SansSerif", Font.BOLD, 13));
        counter2Label.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        progress1.setStringPainted(true);
        progress2.setStringPainted(true);
        
        servingPanel.add(wrap(counter1Label));
        servingPanel.add(wrap(progress1));
        servingPanel.add(wrap(counter2Label));
        servingPanel.add(wrap(progress2));
        
        rightCard.add(servingPanel, BorderLayout.NORTH);
        
        JPanel servedPanel = new JPanel(new BorderLayout());
        servedPanel.setOpaque(false);
        JLabel servedTitle = new JLabel("Recently Served", SwingConstants.LEFT);
        servedTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        servedPanel.add(servedTitle, BorderLayout.NORTH);
        
        JList<String> servedList = new JList<>(servedModel);
        servedList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        servedPanel.add(new JScrollPane(servedList), BorderLayout.CENTER);
        
        rightCard.add(servedPanel, BorderLayout.CENTER);
        
        center.add(seniorCard);
        center.add(normalCard);
        center.add(rightCard);
        
        return center;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsCard = cardPanel();
        statsCard.setLayout(new BorderLayout(8, 8));
        
        JLabel title = new JLabel("Statistics", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        statsCard.add(title, BorderLayout.NORTH);
        
        statsLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
        statsLabel.setText("Waiting for customers...");
        statsCard.add(statsLabel, BorderLayout.CENTER);
        
        return statsCard;
    }
    
    private JPanel cardPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E6ECF3"), 2),
                new EmptyBorder(12, 12, 12, 12)
        ));
        return p;
    }
    
    private JPanel labeledPanel(String labelText, Component comp) {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setOpaque(false);
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        p.add(l, BorderLayout.NORTH);
        comp.setPreferredSize(new Dimension(120, 30));
        p.add(comp, BorderLayout.CENTER);
        return p;
    }
    
    private void styleButton(JButton btn, String color) {
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    private JPanel wrap(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(c, BorderLayout.CENTER);
        return p;
    }
    
    private void onAdd() {
        if (controller == null) return;
        String name = nameField.getText().trim();
        String timeTxt = timeField.getText().trim();
        int priority = priorityCombo.getSelectedIndex() == 1 ? 1 : 2;
        
        if (name.isEmpty() || timeTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter name and time (seconds).");
            return;
        }
        try {
            int t = Integer.parseInt(timeTxt);
            if (t <= 0) throw new NumberFormatException();
            controller.addCustomer(name, priority, t);
            nameField.setText("");
            // Keep time field for convenience
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Time must be a positive integer (seconds).");
        }
    }
    
    public void setController(QueueController controller) {
        this.controller = controller;
    }
    
    public void updateList(List<Customer> senior, List<Customer> normal) {
        SwingUtilities.invokeLater(() -> {
            seniorModel.clear();
            for (Customer c : senior) seniorModel.addElement(c.toString());
            normalModel.clear();
            for (Customer c : normal) normalModel.addElement(c.toString());
        });
    }
    
    public void showServing(int counter, String name, int remainingSeconds, int totalSeconds, boolean isSenior) {
        SwingUtilities.invokeLater(() -> {
            String txt = name + "  (" + remainingSeconds + "s left)";
            int progress = (int) (((double)(totalSeconds - remainingSeconds) / totalSeconds) * 100);
            
            if (counter == 1) {
                counter1Label.setText("Counter 1: " + txt);
                colorCounterLabel(counter1Label, Color.decode("#2E7D32"));
                progress1.setValue(progress);
                progress1.setString(progress + "%");
                progress1.setForeground(Color.decode("#4CAF50"));
            } else {
                counter2Label.setText("Counter 2: " + txt);
                colorCounterLabel(counter2Label, Color.decode("#1976D2"));
                progress2.setValue(progress);
                progress2.setString(progress + "%");
                progress2.setForeground(Color.decode("#2196F3"));
            }
        });
    }
    
    public void resetServing(int counter) {
        SwingUtilities.invokeLater(() -> {
            if (counter == 1) {
                counter1Label.setText("Counter 1: Idle");
                colorCounterLabel(counter1Label, Color.GRAY);
                progress1.setValue(0);
                progress1.setString("Ready");
            } else {
                counter2Label.setText("Counter 2: Idle");
                colorCounterLabel(counter2Label, Color.GRAY);
                progress2.setValue(0);
                progress2.setString("Ready");
            }
        });
    }
    
    private void colorCounterLabel(JLabel lbl, Color bg) {
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setForeground(Color.WHITE);
    }
    
    public void showServed(String text) {
        SwingUtilities.invokeLater(() -> {
            servedModel.add(0, text); // Add to top
            if (servedModel.size() > 50) {
                servedModel.remove(50); // Keep only last 50
            }
        });
    }
    
    public void updateStatistics(int total, int senior, int normal, 
                                   int seniorWaiting, int normalWaiting, double avgWait) {
        SwingUtilities.invokeLater(() -> {
            String stats = String.format(
                "Total Served: %d  |  Senior: %d  |  Normal: %d  |  " +
                "Queue: Senior=%d, Normal=%d  |  Avg Wait: %.1fs",
                total, senior, normal, seniorWaiting, normalWaiting, avgWait
            );
            statsLabel.setText(stats);
        });
    }
}