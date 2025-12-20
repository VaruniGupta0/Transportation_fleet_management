package com.fleet.simulator;

import com.fleet.exceptions.InvalidOperationException;
import com.fleet.vehicles.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FleetSimulatorGUI extends JFrame {
    private Highway highway;
    private List<VehicleThread> vehicleThreads = new ArrayList<>();
    private JLabel totalDistanceLabel;
    private JCheckBox safeModeBox;
    private JPanel vehicleContainerPanel; // Holds the vehicle cards

    public FleetSimulatorGUI() {// Initialize GUI components
        
        highway = new Highway();// Shared highway instance
        setTitle("Fleet Highway Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        
        setLayout(new BorderLayout(10, 10)); 
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10)); 

       
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Fleet Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 23));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton startBtn = new JButton(" Start Simulation");
        JButton pauseBtn = new JButton("Pause");
        JButton resumeBtn = new JButton("Resume");
        JButton stopBtn = new JButton("Stop");

        safeModeBox = new JCheckBox("Race Condition Fix (Synchronized)");
        safeModeBox.setFont(new Font("SansSerif", Font.BOLD, 12));
        safeModeBox.setForeground(new Color(5, 1, 74));

        buttonBar.add(startBtn);
        buttonBar.add(pauseBtn);
        buttonBar.add(resumeBtn);
        buttonBar.add(stopBtn);
        buttonBar.add(new JSeparator(SwingConstants.VERTICAL));
        buttonBar.add(safeModeBox);

        topPanel.add(buttonBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);


        vehicleContainerPanel = new JPanel();
        vehicleContainerPanel.setLayout(new BoxLayout(vehicleContainerPanel, BoxLayout.Y_AXIS));

        
        JScrollPane scrollPane = new JScrollPane(vehicleContainerPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 

        add(scrollPane, BorderLayout.CENTER);


        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(230, 230, 250)); 
        bottomPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        totalDistanceLabel = new JLabel("TOTAL FLEET DISTANCE: 0 km");
        totalDistanceLabel.setFont(new Font("Consolas", Font.BOLD, 27));
        totalDistanceLabel.setForeground(Color.BLACK);

        bottomPanel.add(totalDistanceLabel);
        add(bottomPanel, BorderLayout.SOUTH);


      
        initializeVehicles();// initialize vehicle cards and threads


        
        Timer guiTimer = new Timer(50, e -> {
            totalDistanceLabel.setText(String.format("TOTAL FLEET DISTANCE: %,d km", highway.getTotalDistance()));
            highway.setSynchronizedMode(safeModeBox.isSelected());
        });
        guiTimer.start();

        startBtn.addActionListener(e -> {
            highway.reset();
            vehicleThreads.forEach(VehicleThread::startSimulation);
        });

        pauseBtn.addActionListener(e -> vehicleThreads.forEach(vt -> vt.setPaused(true)));
        resumeBtn.addActionListener(e -> vehicleThreads.forEach(vt -> vt.setPaused(false)));
        stopBtn.addActionListener(e -> vehicleThreads.forEach(VehicleThread::stopSimulation));
    }

    private void initializeVehicles() {
        try {
            
            addVehicleCard(new Car("C101", "mAruti Suzuki", 280, 4, 90, 3));
            addVehicleCard(new Truck("T101", "Volvo DF6", 100, 6, 100, 5000));
            addVehicleCard(new Bus("B101", "M Tourist", 90, 6, 60, 45, 200));
            addVehicleCard(new Car("C102", "BMW", 130, 4, 80, 2));
            addVehicleCard(new Truck("T102", "SToyota", 95, 8, 100, 6000));
           
        } catch (InvalidOperationException e) {
            e.printStackTrace();
        }
    }

   
    private void addVehicleCard(Vehicle v) {
        
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 5, 5, 5), 
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                        v.getId() + " - " + v.getModel() + " (" + v.getClass().getSimpleName() + ")") 
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90)); 
        card.setPreferredSize(new Dimension(800, 90));

        JLabel statusLabel = new JLabel("READY", SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(100, 50));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.LIGHT_GRAY);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        card.add(statusLabel, BorderLayout.WEST);

        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JLabel mileageLabel = new JLabel("Distance: 0.00 km");
        mileageLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));

        
        JProgressBar fuelBar = new JProgressBar(0, 100);
        fuelBar.setValue(100);
        fuelBar.setStringPainted(true);
        fuelBar.setForeground(new Color(4, 166, 4));

        statsPanel.add(mileageLabel);
        statsPanel.add(fuelBar);
        card.add(statsPanel, BorderLayout.CENTER);

        
        JButton refuelBtn = new JButton("Refuel");
        refuelBtn.setBackground(new Color(5, 1, 74));
        card.add(refuelBtn, BorderLayout.EAST);

      

        VehicleThread vt = new VehicleThread(v, highway, statusLabel, fuelBar, mileageLabel);// Vehicle thread

        Timer statusColorTimer = new Timer(200, e -> {
            String txt = statusLabel.getText();
            if (txt.contains("Running")) statusLabel.setBackground(new Color(61, 178, 114));
            else if (txt.contains("Paused")) statusLabel.setBackground(Color.ORANGE);
            else if (txt.contains("FUEL")) statusLabel.setBackground(Color.RED);
            else statusLabel.setBackground(Color.GRAY);
        });
        statusColorTimer.start();

        vehicleThreads.add(vt);
        refuelBtn.addActionListener(e -> vt.refuel());

        vehicleContainerPanel.add(card);
    }

    public static void main(String[] args) {//run the GUI
        SwingUtilities.invokeLater(() -> new FleetSimulatorGUI().setVisible(true));
    }
}