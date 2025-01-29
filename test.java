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
                "SRT", "SJN", "Round Robin (Q=3)", "Preemptive Priority", "Non-Preemptive Priority"
        });

        JLabel prioritylabel = new JLabel("Priority:");
        priority = new JComboBox<>(new String[]{"Have Priority", "No Priority"});


        calcchosen.addActionListener(e -> {
            String selectedMethod = (String) calcchosen.getSelectedItem();
            priority.removeAllItems();
            switch (selectedMethod) {
                case "Round Robin (Q=3)":
                    priority.addItem("No Priority");
                    priority.setEnabled(false);
                    break;
                case "Preemptive Priority", "Non-Preemptive Priority":
                    priority.addItem("Have Priority");
                    priority.setEnabled(false);
                    break;
                default:
                    priority.addItem("Have Priority");
                    priority.addItem("No Priority");
                    priority.setEnabled(true);
                    break;
            }
        });

        JButton guidebutton = new JButton("Guide");
        guidebutton.addActionListener(e -> new GuideWindow());

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
    static ArrayList<Integer> processes, burst, arrival, priorityvalue;

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

        int process = Integer.parseInt(processnum.getSelectedItem().toString());
        String prioritychosen = priority.getSelectedItem().toString();

        if (prioritychosen.equals("Have Priority")) {
            mainframe.setLayout(new GridLayout(process + 2, 4));
            for(int i = 0; i < 4; i++) {
                //add label for each column
                switch(i) {
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
                        processes.add(Integer.parseInt(tf.getText()));
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {}
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
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
                    public void removeUpdate(DocumentEvent e) {}
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
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
                    public void removeUpdate(DocumentEvent e) {}
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
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
                    public void removeUpdate(DocumentEvent e) {}
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
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
            guidebutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    GuideWindow guide = new GuideWindow();
                }
            });

            mainframe.add(new JLabel("")); //empty space
            mainframe.add(guidebutton);
            mainframe.add(reset);
            mainframe.add(calculate);
        } else {
            mainframe.setLayout(new GridLayout(process + 2, 3));
            for(int i = 0; i < 3; i++) {
                //add label for each column
                switch(i) {
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
                    public void removeUpdate(DocumentEvent e) {}
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
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
                    public void removeUpdate(DocumentEvent e) {}
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
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
                    public void removeUpdate(DocumentEvent e) {}
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
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
    private final JFrame guideframe;

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
            - Set whether the calculation make use of priority value or not
            
            2. Main Window
            - Input the numbers in this order :
            TOP TO BOTTOM process, TOP TO BOTTOM burst time, TOP TO BOTTOM arrival time
            
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

        ArrayList<Integer> processes = MainWindow.getProcesseslist();
        ArrayList<Integer> burst = MainWindow.getBurstlist();
        ArrayList<Integer> arrival = MainWindow.getArrivallist();
        ArrayList<Integer> priority = MainWindow.getPriorityvaluelist();

        if (processes == null || burst == null || arrival == null || processes.isEmpty() || burst.isEmpty() || arrival.isEmpty() || processes.size() != burst.size() || processes.size() != arrival.size()) {
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
                }
                  else if (calcMethod.equals("Round Robin (Q=3)")) {
                    Calculate.calculateRR(processes, burst, arrival, tableModel);
                }
                  else if (calcMethod.equals("Preemptive Priority")) {
                    Calculate.calculatePP(processes, burst, arrival, priority, tableModel);
                }
                  else if (calcMethod.equals("Non-Preemptive Priority")) {
                    Calculate.calculateNPP(processes, burst, arrival, priority, tableModel);
                }

            } catch (NumberFormatException e) {
                tableModel.addRow(new Object[]{"Error: Invalid input format. Please enter valid numbers."});
            }
        }

        resultframe.setVisible(true);
    }

    // sort arraylist together by smallest to biggest number
    private static void sortTogether(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
        int n = list1.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list1.get(j) > list1.get(j + 1)) {
                    // swap elements in all three lists
                    int temp = list1.get(j);
                    list1.set(j, list1.get(j + 1));
                    list1.set(j + 1, temp);

                    temp = list2.get(j);
                    list2.set(j, list2.get(j + 1));
                    list2.set(j + 1, temp);

                    temp = list3.get(j);
                    list3.set(j, list3.get(j + 1));
                    list3.set(j + 1, temp);
                }
            }
        }
    }
}

//Calculation classes
//TODO: Implement calculation for each scheduling here
class Calculate {

    public static void calculateSJN(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
        int n = processes.size();
        int[] process = new int[n];
        int[] bt = new int[n];
        int[] at = new int[n];
        int[] ct = new int[n];
        int[] tat = new int[n];
        int[] wt = new int[n];

        for (int i = 0; i < n; i++) {
            process[i] = processes.get(i);
            bt[i] = burst.get(i);
            at[i] = arrival.get(i);
        }

        boolean[] isCompleted = new boolean[n]; // Tracks completed processes
        int currentTime = 0, completed = 0;

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
                currentTime++; // CPU idle
            } else {
                currentTime += bt[shortest];  // Move time forward
                ct[shortest] = currentTime;   // Completion Time
                tat[shortest] = ct[shortest] - at[shortest]; // Turnaround Time
                wt[shortest] = tat[shortest] - bt[shortest]; // Waiting Time
                isCompleted[shortest] = true;
                completed++;
            }
            System.out.println("Using calculateSJN() method");
        }

        // Display results
        tableModel.setRowCount(0);
        for (int i = 0; i < n; i++) {
            tableModel.addRow(new Object[]{process[i], bt[i], at[i], ct[i], tat[i], wt[i]});
        }
        System.out.println("Using calculateSJN() method");
    }

    public static void calculateSRT(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
        int n = processes.size();
        int[] process = new int[n];
        int[] bt = new int[n];
        int[] at = new int[n];
        int[] ct = new int[n];
        int[] tat = new int[n];
        int[] wt = new int[n];
        int[] rt = new int[n];

        for (int i = 0; i < n; i++) {
            process[i] = processes.get(i);
            bt[i] = burst.get(i);
            at[i] = arrival.get(i);
            rt[i] = bt[i];
        }

        int current = 0;
        int completed = 0;
        boolean idle = false; //flag

        while (completed != n) {
            int shortest = -1;
            int minRemaining = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (at[i] <= current && rt[i] > 0 && rt[i] < minRemaining) {
                    minRemaining = rt[i];
                    shortest = i;
                }
            }

            if (shortest == -1) {
                current++; // CPU idle, move to next time unit
                if (idle) { // check idle
                    throw new RuntimeException("Infinite loop detected: No more processes to execute.");
                }
                idle = true;
            } else {
                rt[shortest]--;
                current++;
                idle = false; // cpu busy

                if (rt[shortest] == 0) {
                    completed++;
                    ct[shortest] = current;
                }
            }
        }

        // calculate TAT and WT
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i];
            wt[i] = tat[i] - bt[i];
        }

        // sort by process ID for display
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (process[j] > process[j + 1]) {
                    // swap all related values
                    int temp = process[j]; process[j] = process[j + 1]; process[j + 1] = temp;
                    temp = bt[j]; bt[j] = bt[j + 1]; bt[j + 1] = temp;
                    temp = at[j]; at[j] = at[j + 1]; at[j + 1] = temp;
                    temp = ct[j]; ct[j] = ct[j + 1]; ct[j + 1] = temp;
                    temp = tat[j]; tat[j] = tat[j + 1]; tat[j + 1] = temp;
                    temp = wt[j]; wt[j] = wt[j + 1]; wt[j + 1] = temp;
                }
                System.out.println("Using calculateSRT() method");
            }
        }

        tableModel.setRowCount(0); // clear existing rows
        for (int i = 0; i < n; i++) {
            tableModel.addRow(new Object[]{process[i], bt[i], at[i], ct[i], tat[i], wt[i]});
        }
        System.out.println("Using calculateSRT() method");
    }

    //Round Robin (Q=3)
    public static void calculateRR(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
        int n = processes.size();
        int quantum = 3; // q = 3

        int[] remainingBurst = new int[n];
        for (int i = 0; i < n; i++) {
            remainingBurst[i] = burst.get(i);
        }

        int currentTime = 0;
        int completedProcesses = 0;
        int[] completionTime = new int[n];
        int[] turnAroundTime = new int[n];
        int[] waitingTime = new int[n];
        int[] processOrder = new int[n];
        for(int i = 0; i<n; i++){
            processOrder[i] = i;
        }

        while (completedProcesses < n) {
            boolean aProcessRan = false;
            for (int i = 0; i < n; i++) {
                if (arrival.get(i) <= currentTime && remainingBurst[i] > 0) {
                    aProcessRan = true;
                    if (remainingBurst[i] <= quantum) {
                        currentTime += remainingBurst[i];
                        completionTime[i] = currentTime;
                        remainingBurst[i] = 0;
                        completedProcesses++;
                    } else {
                        currentTime += quantum;
                        remainingBurst[i] -= quantum;
                    }
                }
            }
            if (!aProcessRan) {
                currentTime++;
            }
        }

        for (int i = 0; i < n; i++) {
            turnAroundTime[i] = completionTime[i] - arrival.get(i);
            waitingTime[i] = turnAroundTime[i] - burst.get(i);
        }

        tableModel.setRowCount(0);
        for (int i = 0; i < n; i++) {
            tableModel.addRow(new Object[]{processes.get(i), burst.get(i), arrival.get(i), completionTime[i], turnAroundTime[i], waitingTime[i]});
        }
        System.out.println("Using calculateRR() method");
    }


    //Preemptive priority
    public static void calculatePP(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, ArrayList<Integer> priority, DefaultTableModel tableModel) {
        int n = processes.size();
        int[] process = new int[n];
        int[] bt = new int[n];
        int[] at = new int[n];
        int[] ct = new int[n];
        int[] tat = new int[n];
        int[] wt = new int[n];
        int[] rt = new int[n];
        int[] pr = new int[n];

        // Copy input lists into arrays
        for (int i = 0; i < n; i++) {
            process[i] = processes.get(i);
            bt[i] = burst.get(i);
            at[i] = arrival.get(i);
            pr[i] = priority.get(i);
            rt[i] = bt[i];  // Remaining time initially set to burst time
        }

        int currentTime = 0, completed = 0;

        while (completed < n) {
            int highestPriority = -1;
            int minPriority = Integer.MAX_VALUE;

            // Find the highest priority process that is ready to execute
            for (int i = 0; i < n; i++) {
                if (at[i] <= currentTime && rt[i] > 0) {
                    if (pr[i] < minPriority || (pr[i] == minPriority && (highestPriority == -1 || at[i] < at[highestPriority]))) {
                        minPriority = pr[i];
                        highestPriority = i;
                    }
                }
            }

            if (highestPriority == -1) {
                // Jump to the next process arrival time instead of incrementing 1
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (rt[i] > 0 && at[i] > currentTime) {
                        nextArrival = Math.min(nextArrival, at[i]);
                    }
                }
                currentTime = (nextArrival == Integer.MAX_VALUE) ? currentTime + 1 : nextArrival;
            } else {
                rt[highestPriority]--;  // Reduce remaining time for the selected process
                currentTime++;

                if (rt[highestPriority] == 0) {
                    completed++;
                    ct[highestPriority] = currentTime; // Store completion time
                }
            }
        }

        // Calculate Turnaround Time (TAT) and Waiting Time (WT)
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i];  // Turnaround Time = Completion Time - Arrival Time
            wt[i] = tat[i] - bt[i];  // Waiting Time = Turnaround Time - Burst Time
        }

        // Sorting results based on process ID before displaying
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (process[j] > process[j + 1]) {
                    // Swap all values accordingly
                    int temp;

                    temp = process[j]; process[j] = process[j + 1]; process[j + 1] = temp;
                    temp = bt[j]; bt[j] = bt[j + 1]; bt[j + 1] = temp;
                    temp = at[j]; at[j] = at[j + 1]; at[j + 1] = temp;
                    temp = pr[j]; pr[j] = pr[j + 1]; pr[j + 1] = temp;
                    temp = ct[j]; ct[j] = ct[j + 1]; ct[j + 1] = temp;
                    temp = tat[j]; tat[j] = tat[j + 1]; tat[j + 1] = temp;
                    temp = wt[j]; wt[j] = wt[j + 1]; wt[j + 1] = temp;
                }
                System.out.println("Using calculatePP() method");
            }
        }

        // Display results in table
        tableModel.setRowCount(0);
        for (int i = 0; i < n; i++) {
            tableModel.addRow(new Object[]{process[i], bt[i], at[i], pr[i], ct[i], tat[i], wt[i]});
        }
        System.out.println("Using calculatePP() method");
    }

    //Non-preemptive priority
    public static void calculateNPP(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, ArrayList<Integer> priority, DefaultTableModel tableModel) {
        int n = processes.size();
        int[] process = new int[n];
        int[] bt = new int[n];
        int[] at = new int[n];
        int[] ct = new int[n];
        int[] tat = new int[n];
        int[] wt = new int[n];
        int[] pr = new int[n];

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
            int highestPriority = -1;
            int minPriority = Integer.MAX_VALUE;

            // Find the highest priority process that has arrived and is not yet completed
            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && at[i] <= currentTime) {
                    if (pr[i] < minPriority || (pr[i] == minPriority && (highestPriority == -1 || at[i] < at[highestPriority]))) {
                        minPriority = pr[i];
                        highestPriority = i;
                    }
                }
            }

            if (highestPriority == -1) {
                // Jump to the next earliest arrival time
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!isCompleted[i] && at[i] > currentTime) {
                        nextArrival = Math.min(nextArrival, at[i]);
                    }
                }
                currentTime = (nextArrival == Integer.MAX_VALUE) ? currentTime + 1 : nextArrival;
            } else {
                currentTime += bt[highestPriority]; // Execute the selected process fully
                ct[highestPriority] = currentTime; // Store completion time
                tat[highestPriority] = ct[highestPriority] - at[highestPriority]; // Turnaround Time
                wt[highestPriority] = tat[highestPriority] - bt[highestPriority]; // Waiting Time
                isCompleted[highestPriority] = true;
                completed++;
            }
        }

        // Sorting results by process ID for final display
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (process[j] > process[j + 1]) {
                    // Swap values in all arrays
                    int temp;
                    temp = process[j]; process[j] = process[j + 1]; process[j + 1] = temp;
                    temp = bt[j]; bt[j] = bt[j + 1]; bt[j + 1] = temp;
                    temp = at[j]; at[j] = at[j + 1]; at[j + 1] = temp;
                    temp = pr[j]; pr[j] = pr[j + 1]; pr[j + 1] = temp;
                    temp = ct[j]; ct[j] = ct[j + 1]; ct[j + 1] = temp;
                    temp = tat[j]; tat[j] = tat[j + 1]; tat[j + 1] = temp;
                    temp = wt[j]; wt[j] = wt[j + 1]; wt[j + 1] = temp;
                }
                System.out.println("Using calculateNPP() method");
            }
        }

        // Display results in table
        tableModel.setRowCount(0);
        for (int i = 0; i < n; i++) {
            tableModel.addRow(new Object[]{process[i], bt[i], at[i], pr[i], ct[i], tat[i], wt[i]});
        }
        System.out.println("Using calculateNPP() method");
    }

}
public class test {
    public static void main(String[] args) {
        new StartScreen().start();
    }
}
