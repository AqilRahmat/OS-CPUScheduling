import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;
import java.util.LinkedList;

//Starting screen
class StartScreen {
    private JFrame startframe;
    static JComboBox<String> processnum, calcchosen, priority;

    public static JComboBox<String> getCalcchosen() {
        return calcchosen;
    }

    public static JComboBox<String> getPriority() {
        return priority;
    }

    public static JComboBox<String> getProcessnum() {
        return processnum;
    }

    public void start() {
        GuideWindow guide = new GuideWindow();
        startframe = new JFrame("Start Menu");
        startframe.setLayout(new GridLayout(4, 2));
        startframe.setBounds(100, 100, 300, 200);
        startframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startframe.setResizable(false);
        insertUserComponent();
        startframe.setVisible(true);
    }

    private void insertUserComponent() {
        JLabel processlabel = new JLabel("Number of Processes:");
        processnum = new JComboBox<>();
        for (int i = 3; i < 11; i++) {
            processnum.addItem(i + "");
        }

        JLabel calclabel = new JLabel("Calculation Method:");
        calcchosen = new JComboBox<>(new String[]{
                "SRT", "SJN", "Round Robin (Q=3)",  "Non-Preemptive Priority"
        });

        JLabel prioritylabel = new JLabel("Priority:");
        priority = new JComboBox<>(new String[]{"Have Priority", "No Priority"});

        priority.setSelectedItem("No Priority");
        priority.setEnabled(false);



        calcchosen.addActionListener(e -> {
            String selectedMethod = (String) calcchosen.getSelectedItem();
            priority.removeAllItems();
            switch (selectedMethod) {
                case "Round Robin (Q=3)":
                    priority.addItem("No Priority");
                    priority.setEnabled(false);
                    break;
                case "Non-Preemptive Priority":
                    priority.addItem("Have Priority");
                    priority.setEnabled(false);
                    break;
                case "SRT":
                    priority.addItem("No Priority");
                    priority.setEnabled(false);
                    break;
                default:
                    priority.addItem("No Priority");
                    priority.setEnabled(false);
            }
        });

        JButton guidebutton = new JButton("Guide");
        guidebutton.addActionListener(e -> {
            GuideWindow guide = new GuideWindow();
            guide.guideframe.setVisible(true); // Ensure the guide window is visible
        });

        JButton startbutton = new JButton("Start");
        startbutton.addActionListener(e -> {
            startframe.setVisible(false);
            new MainWindow();
        });

        startframe.add(processlabel);
        startframe.add(processnum);
        startframe.add(calclabel);
        startframe.add(calcchosen);
        startframe.add(prioritylabel);
        startframe.add(priority);
        startframe.add(guidebutton);
        startframe.add(startbutton);
    }
}

//class for new window when start button is clicked
class MainWindow {
    private final JFrame mainframe;
    private StartScreen startscreen;
    private final JComboBox<String> processnum;
    private final JComboBox<String> calcchosen;
    private final JComboBox<String> priority;
    static ArrayList<Integer> processes = new ArrayList<>();
    static ArrayList<Integer> burst = new ArrayList<>();
    static ArrayList<Integer> arrival = new ArrayList<>();
    static ArrayList<Integer> priorityvalue = new ArrayList<>();

    public static ArrayList<Integer> getProcesseslist() {
        return processes;
    }

    public static ArrayList<Integer> getBurstlist() {
        return burst;
    }

    public static ArrayList<Integer> getArrivallist() {
        return arrival;
    }

    public static ArrayList<Integer> getPriorityvaluelist() {
        return priorityvalue;
    }

    public MainWindow() {
        mainframe = new JFrame("T12L Group 5 CPU Scheduling");
        mainframe.setBounds(100, 100, 500, 300);
        mainframe.setResizable(true);

        processes = new ArrayList<>();
        burst = new ArrayList<>();
        arrival = new ArrayList<>();
        priorityvalue = new ArrayList<>();

        processnum = StartScreen.getProcessnum();
        calcchosen = StartScreen.getCalcchosen();
        priority = StartScreen.getPriority();

        int process;
        if (processnum.getSelectedItem() != null) {
            process = Integer.parseInt(processnum.getSelectedItem().toString());
        } else process = 3;

        String prioritychosen = priority.getSelectedItem().toString();

        if (prioritychosen.equals("Have Priority")) {
            mainframe.setLayout(new GridLayout(process + 2, 4));
            for (int i = 0; i < 4; i++) {
                //add label for each column
                switch (i) {
                    case 0:
                        mainframe.add(new JLabel("Processes", SwingConstants.CENTER));
                        break;
                    case 1:
                        mainframe.add(new JLabel("Burst Time", SwingConstants.CENTER));
                        break;
                    case 2:
                        mainframe.add(new JLabel("Arrival Time", SwingConstants.CENTER));
                        break;
                    case 3:
                        mainframe.add(new JLabel("Priority", SwingConstants.CENTER));
                        break;
                    default:
                        return;
                }
            }

            for (int i = 0; i < process; i++) {
                //process
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(tf);
                tf.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            if (!tf.getText().isEmpty()) {
                                processes.add(Integer.parseInt(tf.getText()));
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input: " + tf.getText());
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        // Handle text removal
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        // Handle style changes
                    }
                });

                //burst time
                JTextField bursttime = new JTextField();
                bursttime.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(bursttime);
                bursttime.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            burst.add(Integer.parseInt(bursttime.getText()));
                        } catch (NumberFormatException ex) {
                            // Handle invalid input
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        // Handle text removal
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        // Handle style changes
                    }
                });

                //arrivaltime time
                JTextField arrivaltime = new JTextField();
                arrivaltime.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(arrivaltime);
                arrivaltime.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        arrival.add(Integer.parseInt(arrivaltime.getText()));
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                });

                //priority
                JTextField priority = new JTextField();
                priority.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(priority);
                priority.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        priorityvalue.add(Integer.parseInt(priority.getText()));
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                });
            }

            //calculate button
            JButton calculate = new JButton("Calculate");
            calculate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.setVisible(false);
                    ResultWindow resultwindow = new ResultWindow();
                }
            });

            //reset button
            JButton reset = new JButton("Reset");
            reset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.dispose();
                    MainWindow mainwindow = new MainWindow();
                }
            });

            //Guide button
            JButton guidebutton = new JButton("Guide");
            guidebutton.addActionListener(e -> new GuideWindow());

            mainframe.add(new JLabel("")); //empty space
            mainframe.add(guidebutton);
            mainframe.add(reset);
            mainframe.add(calculate);
        } else {
            mainframe.setLayout(new GridLayout(process + 2, 3));
            for (int i = 0; i < 3; i++) {
                //add label for each column
                switch (i) {
                    case 0:
                        mainframe.add(new JLabel("Processes", SwingConstants.CENTER));
                        break;
                    case 1:
                        mainframe.add(new JLabel("Burst Time", SwingConstants.CENTER));
                        break;
                    case 2:
                        mainframe.add(new JLabel("Arrival Time", SwingConstants.CENTER));
                        break;
                    default:
                        return;
                }
            }

            for (int i = 0; i < process; i++) {
                //process
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(tf);
                tf.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        processes.add(Integer.parseInt(tf.getText()));
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                });

                //burst time
                JTextField bursttime = new JTextField();
                bursttime.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(bursttime);
                bursttime.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        burst.add(Integer.parseInt(bursttime.getText()));
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                });

                //arrivaltime time
                JTextField arrivaltime = new JTextField();
                arrivaltime.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(arrivaltime);
                arrivaltime.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        arrival.add(Integer.parseInt(arrivaltime.getText()));
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                });
            }

            //calculate button
            JButton calculate = new JButton("Calculate");
            calculate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.setVisible(false);
                    ResultWindow resultwindow = new ResultWindow();
                }
            });
            JButton reset = new JButton("Reset");
            reset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.dispose();
                    MainWindow mainwindow = new MainWindow();
                }
            });

            //Guide button
            JButton guidebutton = new JButton("Guide");
            guidebutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    GuideWindow guide = new GuideWindow();
                }
            });

            mainframe.add(guidebutton);
            mainframe.add(reset);
            mainframe.add(calculate);
        }
        mainframe.setVisible(true);
    }
}

//Guide window to show how to use the program
class GuideWindow {
    public JFrame guideframe;

    public GuideWindow() {
        guideframe = new JFrame("Guide");
        guideframe.setAlwaysOnTop(true);
        guideframe.setLayout(new BorderLayout());
        guideframe.setSize(500,500);
        guideframe.setResizable(true);
        guideframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                guideframe.dispose();
            }
        });

        // Header label
        JLabel headerLabel = new JLabel("Program Guideline", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        guideframe.add(headerLabel, BorderLayout.NORTH);

        // Content area for the guide
        JTextArea guideText = new JTextArea();
        guideText.setText("""
            T12L CPU Scheduling Program Manual
            
            1. Main Menu
            - Set the number of processes that you want to calculate [3 - 10]
            - Set the calculation method 
            - You can see whether the calculation uses priority or not.
            
            2. Main Window
            - Input the numbers in this order :
            TOP TO BOTTOM process, TOP TO BOTTOM burst time, TOP TO BOTTOM arrival time
            - MAKE SURE to input the correct number. If not, reset before inputting the correct number.
            
            3. Result Window
            - Click the Result Button to make this window appear
            - it will take the inputs and calculate it, showing the results in the form of a gantt chart
            ----------------------------------------
            T (0) |
            ----------------------------------------
            P     |
            ----------------------------------------
            - It will also display the final table containing the Process | Arrival Time | Burst Time | Finish Time | TurnAround Time | Waiting Time
            
            4. Buttons inside the program
            - Guide Button -> this will show the guide window, you can use this if you need to see the guide
            - Reset Button -> this will empty all the field and you can enter a new value 
            """);

        guideText.setFont(new Font("Arial", Font.PLAIN, 14));
        guideText.setEditable(false); // Make it read-only
        guideText.setLineWrap(true); // Wrap long lines
        guideText.setWrapStyleWord(true);

        // Add the text area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(guideText);
        guideframe.add(scrollPane, BorderLayout.CENTER);
        guideframe.setVisible(true);
    }
}

//Result window
class ResultWindow {

    private final JFrame resultframe;
    private final JTable resultTable;
    private final DefaultTableModel tableModel;
    private final JPanel ganttPanel;
    private static GanttPanel.Calculate Calculate;

    public ResultWindow() {
        resultframe = new JFrame("Result");
        resultframe.setBounds(100, 100, 800, 600);
        resultframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultframe.setResizable(true);
        resultframe.setLayout(new BorderLayout());

        // create table model and table
        String[] columnNames = {"Process", "Burst Time", "Arrival Time", "Completion Time", "Turn Around Time", "Waiting Time"};
        tableModel = new DefaultTableModel(columnNames, 0); // 0 rows initially
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Resize columns to fit
        resultTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultframe.add(scrollPane, BorderLayout.CENTER);

        ganttPanel = new GanttPanel(resultframe.getWidth(), 3); // Width and quantum as parameters
        resultframe.add(ganttPanel, BorderLayout.NORTH);

        loadResults();
        resultframe.setVisible(true);

        // Add listener to resize Gantt chart dynamically
        resultframe.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ganttPanel.repaint();
            }
        });
    }

    // sort arraylist together by smallest to biggest number
    private static void sortTogether(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
        ArrayList<int[]> combinedList = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            combinedList.add(new int[]{list1.get(i), list2.get(i), list3.get(i)});
        }
        combinedList.sort(Comparator.comparingInt(a -> a[0]));

        for (int i = 0; i < combinedList.size(); i++) {
            list1.set(i, combinedList.get(i)[0]);
            list2.set(i, combinedList.get(i)[1]);
            list3.set(i, combinedList.get(i)[2]);
        }
    }

    private void calculateAverage() {
        int rowCount = tableModel.getRowCount();
        if (rowCount == 0) return;

        double totalTurnAroundTime = 0;
        double totalWaitingTime = 0;

        for (int i = 0; i < rowCount; i++) {
            totalTurnAroundTime += (int) tableModel.getValueAt(i, 4); // TAT is at column index 4
            totalWaitingTime += (int) tableModel.getValueAt(i, 5);  // WT is at column index 5
        }

        double avgTAT = totalTurnAroundTime / rowCount;
        double avgWT = totalWaitingTime / rowCount;

        JLabel avgLabel = new JLabel("Average Turnaround Time: " + String.format("%.2f", avgTAT) +
                " | Average Waiting Time: " + String.format("%.2f", avgWT));
        avgLabel.setFont(new Font("Arial", Font.BOLD, 14));
        avgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        resultframe.add(avgLabel, BorderLayout.SOUTH);
        resultframe.revalidate();
        resultframe.repaint();
    }

    private void loadResults() {
        ArrayList<Integer> processes = MainWindow.getProcesseslist();
        ArrayList<Integer> burst = MainWindow.getBurstlist();
        ArrayList<Integer> arrival = MainWindow.getArrivallist();
        ArrayList<Integer> priority = MainWindow.getPriorityvaluelist();

        if (processes.isEmpty() || burst.isEmpty() || arrival.isEmpty() ||
                processes.size() != burst.size() || processes.size() != arrival.size()) {
            tableModel.addRow(new Object[]{"Error: Please enter valid input for all processes.", "", "", "", "", ""});
        } else {
            try {
                // sort processes, burst times, and arrival times by arrival time (ascending)
                sortTogether(processes, burst, arrival);

                String calcMethod = StartScreen.getCalcchosen().getSelectedItem().toString();
                if (calcMethod.equals("SJN")) {
                    Calculate.calculateSJN(processes, burst, arrival, tableModel);
                } else if (calcMethod.equals("SRT")) {
                    Calculate.calculateSRT(processes, burst, arrival, tableModel);
                } else if (calcMethod.equals("Round Robin (Q=3)")) {
                    Calculate.calculateRR(processes, burst, arrival, tableModel);
                } else if (calcMethod.equals("Non-Preemptive Priority")) {
                    Calculate.calculateNPP(processes, burst, arrival, priority, tableModel);
                }
            } catch (NumberFormatException e) {
                tableModel.addRow(new Object[]{"Error: Invalid input format. Please enter valid numbers."});
            }
        }
        ganttPanel.repaint();
        calculateAverage();
        resultframe.setVisible(true);
    }

    // Inner class to handle dynamic resizing of Gantt chart
    class GanttPanel extends JPanel {
        private int panelWidth, quantum;
        private ArrayList<Integer> processes, burst, arrival, priority;

        public GanttPanel(int width, int quantum) {
            setPreferredSize(new Dimension(800, 100));
            this.panelWidth = width;
            this.quantum = quantum;
            this.processes = MainWindow.getProcesseslist();
            this.burst = MainWindow.getBurstlist();
            this.arrival = MainWindow.getArrivallist();
            this.priority = MainWindow.getPriorityvaluelist();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            String calcMethod = StartScreen.getCalcchosen().getSelectedItem().toString();

            if (calcMethod.equals("Round Robin (Q=3)")) {
                drawGanttChartRR(g, getWidth(), 3);
            } else if (calcMethod.equals("SJN")) {
                drawGanttChartSJN(g, getWidth(), -1);
            } else if (calcMethod.equals("SRT")) {
                drawGanttChartSRT(g, getWidth(), -1);
            } else if (calcMethod.equals("Non-Preemptive Priority")) {
                drawGanttChartNPP(g,getWidth(), -1);// No quantum for other calculations
            }
        }

        private void drawGanttChartRR(Graphics g, int panelWidth, int quantum) {
            ArrayList<Integer> processes = MainWindow.getProcesseslist();
            ArrayList<Integer> burst = MainWindow.getBurstlist();
            ArrayList<Integer> arrival = MainWindow.getArrivallist();

            if (processes == null || processes.isEmpty()) return;

            int n = processes.size();
            int[] remainingBurst = new int[n];
            int[] completionTime = new int[n];

            ArrayList<Integer> ganttOrder = new ArrayList<>();
            ArrayList<Integer> timeStamps = new ArrayList<>();

            Queue<Integer> queue = new LinkedList<>();
            boolean[] isCompleted = new boolean[n];

            for (int i = 0; i < n; i++) {
                remainingBurst[i] = burst.get(i);
            }

            int currentTime = 0;
            int completedProcesses = 0;
            int index = 0;

            // Sort processes by arrival time
            ArrayList<Integer> sortedIndexes = new ArrayList<>();
            for (int i = 0; i < n; i++) sortedIndexes.add(i);
            sortedIndexes.sort(Comparator.comparing(arrival::get));

            // Add first available process to the queue
            while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                queue.add(sortedIndexes.get(index));
                index++;
            }

            while (completedProcesses < n) {
                if (!queue.isEmpty()) {
                    int i = queue.poll(); // Get process

                    int executionTime;
                    if (quantum == -1) {  // For non-RR scheduling, execute full burst
                        executionTime = remainingBurst[i];
                    } else {  // For RR, execute within quantum
                        executionTime = Math.min(quantum, remainingBurst[i]);
                    }

                    currentTime += executionTime;
                    remainingBurst[i] -= executionTime;

                    // Store execution sequence for Gantt Chart
                    ganttOrder.add(processes.get(i));
                    timeStamps.add(currentTime);

                    // Check if process is completed
                    if (remainingBurst[i] == 0) {
                        completionTime[i] = currentTime;
                        isCompleted[i] = true;
                        completedProcesses++;
                    }

                    // Check for new arrivals and add to the queue
                    while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                        queue.add(sortedIndexes.get(index));
                        index++;
                    }

                    // If not completed and it's RR, re-add process to queue
                    if (quantum != -1 && !isCompleted[i]) queue.add(i);
                } else {
                    // If no process is available, CPU remains idle
                    currentTime++;
                }
            }

            int xStart = 20;
            int yStart = 40;
            int height = 40;
            int unitWidth = panelWidth / (currentTime + 1);  // Adjust width dynamically

            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Gantt Chart:", 10, 20);

            int time = 0;
            for (int i = 0; i < ganttOrder.size(); i++) {
                int width = (timeStamps.get(i) - time) * unitWidth;
                g.setColor(Color.CYAN);
                g.fillRect(xStart, yStart, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(xStart, yStart, width, height);
                g.drawString("P" + ganttOrder.get(i), xStart + width / 3, yStart + height / 2);
                g.drawString(String.valueOf(time), xStart, yStart + height + 15);
                time = timeStamps.get(i);
                xStart += width;
            }
            g.drawString(String.valueOf(time), xStart, yStart + height + 15);
        }

        private void drawGanttChartSJN(Graphics g, int panelWidth, int quantum) {
            ArrayList<Integer> processes = MainWindow.getProcesseslist();
            ArrayList<Integer> burst = MainWindow.getBurstlist();
            ArrayList<Integer> arrival = MainWindow.getArrivallist();

            if (processes == null || processes.isEmpty()) return;

            int n = processes.size();
            int[] process = new int[n];
            int[] bt = new int[n];
            int[] at = new int[n];

            // Copy input lists into arrays
            for (int i = 0; i < n; i++) {
                process[i] = processes.get(i);
                bt[i] = burst.get(i);
                at[i] = arrival.get(i);
            }

            boolean[] isCompleted = new boolean[n];
            int currentTime = 0, completed = 0;

            // Store Gantt Chart data
            ArrayList<Integer> ganttOrder = new ArrayList<>();
            ArrayList<Integer> timeStamps = new ArrayList<>();

            timeStamps.add(0); // Start with initial timestamp

            while (completed < n) {
                int shortest = -1;
                int minBurst = Integer.MAX_VALUE;

                // Find the shortest available job
                for (int i = 0; i < n; i++) {
                    if (!isCompleted[i] && at[i] <= currentTime) {
                        if (bt[i] < minBurst || (bt[i] == minBurst && at[i] < at[shortest])) {
                            minBurst = bt[i];
                            shortest = i;
                        }
                    }
                }

                if (shortest != -1) {
                    // Execute the shortest job
                    ganttOrder.add(process[shortest]);
                    currentTime += bt[shortest];
                    timeStamps.add(currentTime);

                    isCompleted[shortest] = true;
                    completed++;
                } else {
                    // If no process is available, increment time
                    currentTime++;
                }
            }

            // **Gantt Chart Drawing**
            int xStart = 20;
            int yStart = 40;
            int height = 40;
            int unitWidth = panelWidth / (currentTime + 1);

            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Gantt Chart:", 10, 20);

            int time = timeStamps.get(0);
            for (int i = 0; i < ganttOrder.size(); i++) {
                int width = (timeStamps.get(i + 1) - time) * unitWidth;
                g.setColor(Color.CYAN);
                g.fillRect(xStart, yStart, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(xStart, yStart, width, height);
                g.drawString("P" + ganttOrder.get(i), xStart + width / 3, yStart + height / 2);
                g.drawString(String.valueOf(time), xStart, yStart + height + 15);
                time = timeStamps.get(i + 1);
                xStart += width;
            }
            g.drawString(String.valueOf(time), xStart, yStart + height + 15);
        }




        private void drawGanttChartSRT(Graphics g, int panelWidth, int quantum) {
            ArrayList<Integer> processes = MainWindow.getProcesseslist();
            ArrayList<Integer> burst = MainWindow.getBurstlist();
            ArrayList<Integer> arrival = MainWindow.getArrivallist();

            if (processes == null || processes.isEmpty()) return;

            int n = processes.size();
            int[] remainingBurst = new int[n];
            int[] completionTime = new int[n];
            int[] arrivalTime = new int[n];

            ArrayList<Integer> ganttOrder = new ArrayList<>();
            ArrayList<Integer> timeStamps = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                remainingBurst[i] = burst.get(i);
                arrivalTime[i] = arrival.get(i);
            }

            int currentTime = 0;
            int completedProcesses = 0;
            boolean[] isCompleted = new boolean[n];
            int lastProcess = -1;

            while (completedProcesses < n) {
                int shortest = -1;
                int minRemainingTime = Integer.MAX_VALUE;

                for (int i = 0; i < n; i++) {
                    if (arrivalTime[i] <= currentTime && !isCompleted[i] && remainingBurst[i] < minRemainingTime) {
                        minRemainingTime = remainingBurst[i];
                        shortest = i;
                    }
                }

                if (shortest == -1) {
                    currentTime++;
                    continue;
                }

                if (shortest != lastProcess) {
                    ganttOrder.add(processes.get(shortest));
                    timeStamps.add(currentTime);
                    lastProcess = shortest;
                }

                remainingBurst[shortest]--;
                currentTime++;

                if (remainingBurst[shortest] == 0) {
                    completionTime[shortest] = currentTime;
                    isCompleted[shortest] = true;
                    completedProcesses++;
                }
            }

            timeStamps.add(currentTime);

            int xStart = 20;
            int yStart = 40;
            int height = 40;
            int unitWidth = panelWidth / (currentTime + 1);

            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Gantt Chart:", 10, 20);

            for (int i = 0; i < ganttOrder.size(); i++) {
                int width = (timeStamps.get(i + 1) - timeStamps.get(i)) * unitWidth;
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(xStart, yStart, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(xStart, yStart, width, height);
                g.drawString("P" + ganttOrder.get(i), xStart + width / 3, yStart + height / 2);
                g.drawString(String.valueOf(timeStamps.get(i)), xStart, yStart + height + 15);
                xStart += width;
            }
            g.drawString(String.valueOf(timeStamps.get(timeStamps.size() - 1)), xStart, yStart + height + 15);
        }


        private void drawGanttChartNPP(Graphics g, int panelWidth, int quantum) {
            ArrayList<Integer> processes = MainWindow.getProcesseslist();
            ArrayList<Integer> burst = MainWindow.getBurstlist();
            ArrayList<Integer> arrival = MainWindow.getArrivallist();

            if (processes == null || processes.isEmpty()) return;

            int n = processes.size();
            int[] remainingBurst = new int[n];
            int[] completionTime = new int[n];

            ArrayList<Integer> ganttOrder = new ArrayList<>();
            ArrayList<Integer> timeStamps = new ArrayList<>();

            Queue<Integer> queue = new LinkedList<>();
            boolean[] isCompleted = new boolean[n];

            for (int i = 0; i < n; i++) {
                remainingBurst[i] = burst.get(i);
            }

            int currentTime = 0;
            int completedProcesses = 0;
            int index = 0;

            // Sort processes by arrival time
            ArrayList<Integer> sortedIndexes = new ArrayList<>();
            for (int i = 0; i < n; i++) sortedIndexes.add(i);
            sortedIndexes.sort(Comparator.comparing(arrival::get));

            // Add first available process to the queue
            while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                queue.add(sortedIndexes.get(index));
                index++;
            }

            while (completedProcesses < n) {
                if (!queue.isEmpty()) {
                    int i = queue.poll(); // Get process

                    int executionTime;
                    if (quantum == -1) {  // For non-RR scheduling, execute full burst
                        executionTime = remainingBurst[i];
                    } else {  // For RR, execute within quantum
                        executionTime = Math.min(quantum, remainingBurst[i]);
                    }

                    currentTime += executionTime;
                    remainingBurst[i] -= executionTime;

                    // Store execution sequence for Gantt Chart
                    ganttOrder.add(processes.get(i));
                    timeStamps.add(currentTime);

                    // Check if process is completed
                    if (remainingBurst[i] == 0) {
                        completionTime[i] = currentTime;
                        isCompleted[i] = true;
                        completedProcesses++;
                    }

                    // Check for new arrivals and add to the queue
                    while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                        queue.add(sortedIndexes.get(index));
                        index++;
                    }

                    // If not completed and it's RR, re-add process to queue
                    if (quantum != -1 && !isCompleted[i]) queue.add(i);
                } else {
                    // If no process is available, CPU remains idle
                    currentTime++;
                }
            }

            int xStart = 20;
            int yStart = 40;
            int height = 40;
            int unitWidth = panelWidth / (currentTime + 1);  // Adjust width dynamically

            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Gantt Chart:", 10, 20);

            int time = 0;
            for (int i = 0; i < ganttOrder.size(); i++) {
                int width = (timeStamps.get(i) - time) * unitWidth;
                g.setColor(Color.CYAN);
                g.fillRect(xStart, yStart, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(xStart, yStart, width, height);
                g.drawString("P" + ganttOrder.get(i), xStart + width / 3, yStart + height / 2);
                g.drawString(String.valueOf(time), xStart, yStart + height + 15);
                time = timeStamps.get(i);
                xStart += width;
            }
            g.drawString(String.valueOf(time), xStart, yStart + height + 15);
        }


        //Calculation classes
//TODO: Implement calculation for each scheduling here
        public static class Calculate {

            public static void calculateSJN(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
                int n = processes.size();
                int[] process = new int[n];
                int[] bt = new int[n];
                int[] at = new int[n];
                int[] ct = new int[n];  // Completion time
                int[] tat = new int[n]; // Turnaround time
                int[] wt = new int[n];  // Waiting time

                // Copy input lists into arrays
                for (int i = 0; i < n; i++) {
                    process[i] = processes.get(i);
                    bt[i] = burst.get(i);
                    at[i] = arrival.get(i);
                }

                boolean[] isCompleted = new boolean[n]; // Tracks completed processes
                int currentTime = 0, completed = 0;

                // Store Gantt Chart data
                ArrayList<Integer> ganttOrder = new ArrayList<>();
                ArrayList<Integer> timeStamps = new ArrayList<>();

                while (completed < n) {
                    int shortest = -1;
                    int minBurst = Integer.MAX_VALUE;

                    // Find the shortest available job
                    for (int i = 0; i < n; i++) {
                        if (!isCompleted[i] && at[i] <= currentTime) {
                            if (bt[i] < minBurst || (bt[i] == minBurst && at[i] < at[shortest])) {
                                minBurst = bt[i];
                                shortest = i;
                            }
                        }
                    }

                    if (shortest == -1) {
                        // No available process → CPU remains idle
                        currentTime++;
                    } else {
                        // Execute the shortest job
                        currentTime += bt[shortest];  // Move time forward
                        ct[shortest] = currentTime;   // Completion Time
                        tat[shortest] = ct[shortest] - at[shortest]; // Turnaround Time
                        wt[shortest] = tat[shortest] - bt[shortest]; // Waiting Time
                        isCompleted[shortest] = true;
                        completed++;

                        // Store Gantt Chart Data
                        ganttOrder.add(process[shortest]);
                        timeStamps.add(currentTime);
                    }
                }

                // Update the table with calculated values
                tableModel.setRowCount(0);
                for (int i = 0; i < n; i++) {
                    tableModel.addRow(new Object[]{process[i], bt[i], at[i], ct[i], tat[i], wt[i]});
                }
                System.out.println("Using corrected calculateSJN() method");
            }

            public static void calculateSRT(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
                int n = processes.size();
                int[] remainingBurst = new int[n];
                int[] completionTime = new int[n];
                int[] turnaroundTime = new int[n];
                int[] waitingTime = new int[n];
                int[] arrivalTime = new int[n];
                boolean[] isCompleted = new boolean[n];

                for (int i = 0; i < n; i++) {
                    remainingBurst[i] = burst.get(i);
                    arrivalTime[i] = arrival.get(i);
                }

                int currentTime = 0, completedProcesses = 0;
                while (completedProcesses < n) {
                    int shortest = -1, minRemainingTime = Integer.MAX_VALUE;
                    for (int i = 0; i < n; i++) {
                        if (arrivalTime[i] <= currentTime && !isCompleted[i] && remainingBurst[i] < minRemainingTime) {
                            minRemainingTime = remainingBurst[i];
                            shortest = i;
                        }
                    }

                    if (shortest == -1) {
                        currentTime++;
                        continue;
                    }

                    remainingBurst[shortest]--;
                    currentTime++;

                    if (remainingBurst[shortest] == 0) {
                        completionTime[shortest] = currentTime;
                        isCompleted[shortest] = true;
                        completedProcesses++;
                    }
                }

                for (int i = 0; i < n; i++) {
                    turnaroundTime[i] = completionTime[i] - arrivalTime[i];
                    waitingTime[i] = turnaroundTime[i] - burst.get(i);
                }

                tableModel.setRowCount(0);
                for (int i = 0; i < n; i++) {
                    tableModel.addRow(new Object[]{processes.get(i), burst.get(i), arrival.get(i), completionTime[i], turnaroundTime[i], waitingTime[i]});
                }
                System.out.println("Using calculateSRT() method");
            }

            //Round Robin (Q=3)
            public static void calculateRR(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
                int n = processes.size();
                int quantum = 3; // Time quantum

                int[] remainingBurst = new int[n];
                int[] completionTime = new int[n];
                int[] turnAroundTime = new int[n];
                int[] waitingTime = new int[n];

                Queue<Integer> queue = new LinkedList<>();
                int currentTime = 0, completedProcesses = 0;
                boolean[] isCompleted = new boolean[n];

                // Copy burst times
                for (int i = 0; i < n; i++) remainingBurst[i] = burst.get(i);

                // Sort by Arrival Time
                ArrayList<Integer> sortedIndexes = new ArrayList<>();
                for (int i = 0; i < n; i++) sortedIndexes.add(i);
                sortedIndexes.sort(Comparator.comparing(arrival::get));

                int index = 0;
                while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                    queue.add(sortedIndexes.get(index));
                    index++;
                }

                // Round Robin Execution
                while (completedProcesses < n) {
                    if (!queue.isEmpty()) {
                        int i = queue.poll(); // Get process

                        if (remainingBurst[i] > quantum) {
                            currentTime += quantum;
                            remainingBurst[i] -= quantum;
                        } else {
                            currentTime += remainingBurst[i];
                            completionTime[i] = currentTime;
                            remainingBurst[i] = 0;
                            isCompleted[i] = true;
                            completedProcesses++;
                        }

                        // Check for newly arrived processes
                        while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                            queue.add(sortedIndexes.get(index));
                            index++;
                        }

                        // Re-add unfinished process
                        if (!isCompleted[i]) queue.add(i);
                    } else {
                        currentTime++; // Move forward if CPU is idle
                    }
                }

                // Compute Turnaround Time & Waiting Time
                for (int i = 0; i < n; i++) {
                    turnAroundTime[i] = completionTime[i] - arrival.get(i);
                    waitingTime[i] = turnAroundTime[i] - burst.get(i);
                }

                // Update Table
                tableModel.setRowCount(0);
                for (int i = 0; i < n; i++) {
                    tableModel.addRow(new Object[]{
                            processes.get(i), burst.get(i), arrival.get(i), completionTime[i], turnAroundTime[i], waitingTime[i]
                    });
                }

                System.out.println("Using calculateRR() method");
            }


            //Non-preemptive priority
            public static void calculateNPP(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival,
                                            ArrayList<Integer> priority, DefaultTableModel tableModel) {
                int n = processes.size();
                int[] process = new int[n];
                int[] bt = new int[n]; // Burst time
                int[] at = new int[n]; // Arrival time
                int[] pr = new int[n]; // Priority
                int[] ct = new int[n]; // Completion time
                int[] tat = new int[n]; // Turnaround time
                int[] wt = new int[n]; // Waiting time

                // Copy input lists into arrays
                for (int i = 0; i < n; i++) {
                    process[i] = processes.get(i);
                    bt[i] = burst.get(i);
                    at[i] = arrival.get(i);
                    pr[i] = priority.get(i);
                }

                boolean[] isCompleted = new boolean[n]; // Tracks completed processes
                int currentTime = 0, completed = 0;

                while (completed < n) {
                    int highestPriorityIndex = -1;
                    int minPriority = Integer.MAX_VALUE;

                    // Find the highest priority process that has arrived and is not yet completed
                    for (int i = 0; i < n; i++) {
                        if (!isCompleted[i] && at[i] <= currentTime) {
                            if (pr[i] < minPriority ||
                                    (pr[i] == minPriority && highestPriorityIndex != -1 && at[i] < at[highestPriorityIndex])) {
                                minPriority = pr[i];
                                highestPriorityIndex = i;
                            }
                        }
                    }

                    if (highestPriorityIndex == -1) {  // No process is ready
                        // Move to the next earliest arrival time
                        int nextArrival = Integer.MAX_VALUE;
                        for (int i = 0; i < n; i++) {
                            if (!isCompleted[i]) {
                                nextArrival = Math.min(nextArrival, at[i]);
                            }
                        }
                        currentTime = nextArrival;
                    } else {  // Execute the chosen process
                        currentTime = Math.max(currentTime, at[highestPriorityIndex]) + bt[highestPriorityIndex];
                        ct[highestPriorityIndex] = currentTime; // Completion Time
                        tat[highestPriorityIndex] = ct[highestPriorityIndex] - at[highestPriorityIndex]; // Turnaround Time
                        wt[highestPriorityIndex] = tat[highestPriorityIndex] - bt[highestPriorityIndex]; // Waiting Time
                        isCompleted[highestPriorityIndex] = true;
                        completed++;
                    }
                }

                // Sorting results based on process ID before displaying
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < n - i - 1; j++) {
                        if (process[j] > process[j + 1]) {
                            // Swap all values accordingly
                            int temp;

                            temp = process[j];
                            process[j] = process[j + 1];
                            process[j + 1] = temp;
                            temp = bt[j];
                            bt[j] = bt[j + 1];
                            bt[j + 1] = temp;
                            temp = at[j];
                            at[j] = at[j + 1];
                            at[j + 1] = temp;
                            temp = pr[j];
                            pr[j] = pr[j + 1];
                            pr[j + 1] = temp;
                            temp = ct[j];
                            ct[j] = ct[j + 1];
                            ct[j + 1] = temp;
                            temp = tat[j];
                            tat[j] = tat[j + 1];
                            tat[j + 1] = temp;
                            temp = wt[j];
                            wt[j] = wt[j + 1];
                            wt[j + 1] = temp;
                        }
                    }
                }

                // Display results in table
                tableModel.setRowCount(0);
                for (int i = 0; i < n; i++) {
                    tableModel.addRow(new Object[]{process[i], bt[i], at[i], ct[i], tat[i], wt[i]});
                }

                System.out.println("Using calculateNPP() method");
            }
        }
    }
}

public class test {
    public static void main(String[] args) {
        new StartScreen().start();
    }
}
