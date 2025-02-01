import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;
import java.util.LinkedList;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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

    //Start window
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

        //Choose number of process (3-10)
        JLabel processlabel = new JLabel("Number of Processes:");
        processnum = new JComboBox<>();
        for (int i = 3; i < 11; i++) {
            processnum.addItem(i + "");
        }

        //Choose calculation method
        JLabel calclabel = new JLabel("Calculation Method:");
        calcchosen = new JComboBox<>(new String[]{
                "SRT", "SJN", "Round Robin (Q=3)",  "Non-Preemptive Priority"
        });


        //Show priority type (with priority/no priority)
        JLabel prioritylabel = new JLabel("Priority:");
        priority = new JComboBox<>(new String[]{"Have Priority", "No Priority"});

        priority.setSelectedItem("No Priority");
        priority.setEnabled(false);



        //Set priority for each calculation method.
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


        //Button for guide
        JButton guidebutton = new JButton("Guide");
        guidebutton.addActionListener(e -> {
            GuideWindow guide = new GuideWindow();
            guide.guideframe.setVisible(true); // Ensure the guide window is visible
        });

        //Start button
        JButton startbutton = new JButton("Start");
        startbutton.addActionListener(e -> {
            startframe.setVisible(false);
            new MainWindow();
        });


        //Add all the process together
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
// class for new window when start button is clicked
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

    // getters for the process lists
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

    // main window setup
    public MainWindow() {
        mainframe = new JFrame("T12L Group 5 CPU Scheduling");
        mainframe.setBounds(100, 100, 500, 300);
        mainframe.setResizable(true);

        // reset lists
        processes = new ArrayList<>();
        burst = new ArrayList<>();
        arrival = new ArrayList<>();
        priorityvalue = new ArrayList<>();

        // initialize combo boxes
        processnum = StartScreen.getProcessnum();
        calcchosen = StartScreen.getCalcchosen();
        priority = StartScreen.getPriority();

        // default number of processes if not selected
        int process;
        if (processnum.getSelectedItem() != null) {
            process = Integer.parseInt(processnum.getSelectedItem().toString());
        } else process = 3;

        String prioritychosen = priority.getSelectedItem().toString();

        // check if priority option is selected
        if (prioritychosen.equals("Have Priority")) {
            mainframe.setLayout(new GridLayout(process + 2, 4));  // grid layout for processes with priority
            for (int i = 0; i < 4; i++) {
                // add label for each column
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

            // add fields for processes
            for (int i = 0; i < process; i++) {
                // process field
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(tf);
                tf.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        // do nothing on focus gain
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            // add process value when focus is lost
                            if (!tf.getText().trim().isEmpty()) {
                                int value = Integer.parseInt(tf.getText().trim());
                                processes.add(value);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input for process number: " + tf.getText());
                        }
                    }
                });

                // burst time field
                JTextField bursttime = new JTextField();
                bursttime.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(bursttime);
                bursttime.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            // add burst time value when focus is lost
                            if (!bursttime.getText().trim().isEmpty()) {
                                int value = Integer.parseInt(bursttime.getText().trim());
                                burst.add(value);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input for burst time: " + bursttime.getText());
                        }
                    }
                });

                // arrival time field
                JTextField arrivaltime = new JTextField();
                arrivaltime.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(arrivaltime);
                arrivaltime.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            // add arrival time value when focus is lost
                            if (!arrivaltime.getText().trim().isEmpty()) {
                                int value = Integer.parseInt(arrivaltime.getText().trim());
                                arrival.add(value);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input for arrival time: " + arrivaltime.getText());
                        }
                    }
                });

                // priority field
                JTextField priorityField = new JTextField();
                priorityField.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(priorityField);
                priorityField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            // add priority value when focus is lost
                            if (!priorityField.getText().trim().isEmpty()) {
                                int value = Integer.parseInt(priorityField.getText().trim());
                                priorityvalue.add(value);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input for priority: " + priorityField.getText());
                        }
                    }
                });
            }

            // calculate button
            JButton calculate = new JButton("Calculate");
            calculate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.setVisible(false);
                    ResultWindow resultwindow = new ResultWindow();  // open result window
                }
            });

            // reset button
            JButton reset = new JButton("Reset");
            reset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.dispose();  // dispose current window and open a new one
                    MainWindow mainwindow = new MainWindow();
                }
            });

            // guide button
            JButton guidebutton = new JButton("Guide");
            guidebutton.addActionListener(e -> new GuideWindow());

            mainframe.add(new JLabel(""));  // empty space
            mainframe.add(guidebutton);
            mainframe.add(reset);
            mainframe.add(calculate);
        } else {
            mainframe.setLayout(new GridLayout(process + 2, 3));  // grid layout without priority column
            for (int i = 0; i < 3; i++) {
                // add label for each column
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

            // add fields for processes
            for (int i = 0; i < process; i++) {
                // process field
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(tf);
                tf.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        // do nothing on focus gain
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            // add process value when focus is lost
                            if (!tf.getText().trim().isEmpty()) {
                                int value = Integer.parseInt(tf.getText().trim());
                                processes.add(value);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input for process number: " + tf.getText());
                        }
                    }
                });

                // burst time field
                JTextField bursttime = new JTextField();
                bursttime.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(bursttime);
                bursttime.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            // add burst time value when focus is lost
                            if (!bursttime.getText().trim().isEmpty()) {
                                int value = Integer.parseInt(bursttime.getText().trim());
                                burst.add(value);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input for burst time: " + bursttime.getText());
                        }
                    }
                });

                // arrival time field
                JTextField arrivaltime = new JTextField();
                arrivaltime.setHorizontalAlignment(JTextField.CENTER);
                mainframe.add(arrivaltime);
                arrivaltime.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            // add arrival time value when focus is lost
                            if (!arrivaltime.getText().trim().isEmpty()) {
                                int value = Integer.parseInt(arrivaltime.getText().trim());
                                arrival.add(value);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input for arrival time: " + arrivaltime.getText());
                        }
                    }
                });
            }

            // calculate button
            JButton calculate = new JButton("Calculate");
            calculate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.setVisible(false);
                    ResultWindow resultwindow = new ResultWindow();  // open result window
                }
            });

            // reset button
            JButton reset = new JButton("Reset");
            reset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.dispose();  // dispose current window and open a new one
                    MainWindow mainwindow = new MainWindow();
                }
            });

            // guide button
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
        mainframe.setVisible(true);  // show main frame
    }
}


// guide window to show how to use the program
class GuideWindow {
    public JFrame guideframe;

    public GuideWindow() {
        // create the guide window frame
        guideframe = new JFrame("Guide");
        guideframe.setAlwaysOnTop(true);  // keep window on top
        guideframe.setLayout(new BorderLayout());
        guideframe.setSize(500, 500);  // set window size
        guideframe.setResizable(true);  // allow resizing
        guideframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                guideframe.dispose();  // dispose of the window when closed
            }
        });

        // header label with program title
        JLabel headerLabel = new JLabel("Program Guideline", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));  // set font style and size
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // add margin
        guideframe.add(headerLabel, BorderLayout.NORTH);

        // content area for the guide
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

        guideText.setFont(new Font("Arial", Font.PLAIN, 14));  // set font style and size
        guideText.setEditable(false);  // make it read-only
        guideText.setLineWrap(true);  // wrap long lines
        guideText.setWrapStyleWord(true);  // wrap at word boundaries

        // add the text area inside a scroll pane for easy scrolling
        JScrollPane scrollPane = new JScrollPane(guideText);
        guideframe.add(scrollPane, BorderLayout.CENTER);  // add scroll pane to the center
        guideframe.setVisible(true);  // make the window visible
    }
}


        // Result window to display the calculated results
        class ResultWindow {

            private final JFrame resultframe;
            private final JTable resultTable;
            private final DefaultTableModel tableModel;
            private final JPanel ganttPanel;
            private static GanttPanel.Calculate Calculate;

            public ResultWindow() {
                // create the result window frame
                resultframe = new JFrame("Result");
                resultframe.setBounds(100, 100, 800, 600);  // set window position and size
                resultframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // close window when done
                resultframe.setResizable(true);  // allow resizing
                resultframe.setLayout(new BorderLayout());

                // create table model and table
                String[] columnNames = {"Process", "Burst Time", "Arrival Time", "Completion Time", "Turn Around Time", "Waiting Time"};
                tableModel = new DefaultTableModel(columnNames, 0);  // initialize with column names and no rows
                resultTable = new JTable(tableModel);  // create the table
                resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);  // auto resize columns
                resultTable.setFillsViewportHeight(true);  // fill the viewport with table content

                // add table inside a scroll pane
                JScrollPane scrollPane = new JScrollPane(resultTable);
                resultframe.add(scrollPane, BorderLayout.CENTER);

                // create and add Gantt chart panel at the top
                ganttPanel = new GanttPanel(resultframe.getWidth(), 3);  // set width and quantum
                resultframe.add(ganttPanel, BorderLayout.NORTH);

                loadResults();  // load and display results in the table
                resultframe.setVisible(true);  // make the window visible

                // Add listener to resize Gantt chart dynamically when window is resized
                resultframe.addComponentListener(new java.awt.event.ComponentAdapter() {
                    public void componentResized(java.awt.event.ComponentEvent evt) {
                        ganttPanel.repaint();  // repaint the chart on window resize
                    }
                });
            }

            // sort array lists together by the smallest to biggest number in the first list
            private static void sortTogether(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
                ArrayList<int[]> combinedList = new ArrayList<>();
                // combine the lists into a single list of arrays
                for (int i = 0; i < list1.size(); i++) {
                    combinedList.add(new int[]{list1.get(i), list2.get(i), list3.get(i)});
                }
                combinedList.sort(Comparator.comparingInt(a -> a[0]));  // sort by the first element (process number)

                // update original lists with sorted values
                for (int i = 0; i < combinedList.size(); i++) {
                    list1.set(i, combinedList.get(i)[0]);
                    list2.set(i, combinedList.get(i)[1]);
                    list3.set(i, combinedList.get(i)[2]);
                }
            }

            // calculate and display average turnaround time and waiting time
            private void calculateAverage() {
                int rowCount = tableModel.getRowCount();  // get number of rows in the table
                if (rowCount == 0) return;  // if there are no rows, exit

                double totalTurnAroundTime = 0;  // accumulator for turnaround time
                double totalWaitingTime = 0;  // accumulator for waiting time

                // iterate through all rows in the table to calculate averages
                for (int i = 0; i < rowCount; i++) {
                    Object tatValue = tableModel.getValueAt(i, 4);  // get turnaround time value
                    Object wtValue = tableModel.getValueAt(i, 5);  // get waiting time value

                    // skip rows with empty values
                    if (tatValue == null || tatValue.toString().trim().isEmpty()) continue;
                    if (wtValue == null || wtValue.toString().trim().isEmpty()) continue;

                    try {
                        // parse the values as doubles
                        double tat = Double.parseDouble(tatValue.toString());
                        double wt = Double.parseDouble(wtValue.toString());

                        totalTurnAroundTime += tat;  // accumulate turnaround time
                        totalWaitingTime += wt;  // accumulate waiting time
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing table values: " + e.getMessage());  // handle parsing error
                    }
                }

                if (rowCount > 0) {
                    // calculate and display averages
                    double avgTAT = totalTurnAroundTime / rowCount;
                    double avgWT = totalWaitingTime / rowCount;

                    JLabel avgLabel = new JLabel("Average Turnaround Time: " + String.format("%.2f", avgTAT) +
                            " | Average Waiting Time: " + String.format("%.2f", avgWT));
                    avgLabel.setFont(new Font("Arial", Font.BOLD, 14));  // set label style
                    avgLabel.setHorizontalAlignment(SwingConstants.CENTER);  // center align the text

                    resultframe.add(avgLabel, BorderLayout.SOUTH);  // add the label to the bottom of the window
                    resultframe.revalidate();  // revalidate the layout
                    resultframe.repaint();  // repaint the window to reflect changes
                }
            }


            private void loadResults() {
                // retrieve lists of processes, burst times, arrival times, and priority values from the main window
                ArrayList<Integer> processes = MainWindow.getProcesseslist();
                ArrayList<Integer> burst = MainWindow.getBurstlist();
                ArrayList<Integer> arrival = MainWindow.getArrivallist();
                ArrayList<Integer> priority = MainWindow.getPriorityvaluelist();

                // check for invalid input: empty lists or mismatched list sizes
                if (processes.isEmpty() || burst.isEmpty() || arrival.isEmpty() ||
                        processes.size() != burst.size() || processes.size() != arrival.size()) {
                    // if any issue is found, display an error message in the table
                    tableModel.addRow(new Object[]{"Error: Please enter valid input for all processes.", "", "", "", "", ""});
                    return;  // exit the method early to prevent further processing
                }

                try {
                    // sort the processes, burst times, and arrival times by arrival time in ascending order
                    sortTogether(processes, burst, arrival);

                    // get the selected calculation method from the start screen
                    String calcMethod = StartScreen.getCalcchosen().getSelectedItem().toString();
                    System.out.println("Calculation Method Selected: " + calcMethod);  // print selected method for debugging

                    // depending on the selected calculation method, call the appropriate calculation method
                    if (calcMethod.equals("SJN")) {
                        Calculate.calculateSJN(processes, burst, arrival, tableModel);  // shortest job next
                    } else if (calcMethod.equals("SRT")) {
                        Calculate.calculateSRT(processes, burst, arrival, tableModel);  // shortest remaining time
                    } else if (calcMethod.equals("Round Robin (Q=3)")) {
                        Calculate.calculateRR(processes, burst, arrival, tableModel);  // round robin with quantum of 3
                    } else if (calcMethod.equals("Non-Preemptive Priority")) {
                        Calculate.calculateNPP(processes, burst, arrival, priority, tableModel);  // non-preemptive priority scheduling
                    }

                    // debugging: print the content of the table to the console
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                            System.out.print(tableModel.getValueAt(i, j) + "\t");  // print each value in the row
                        }
                        System.out.println();  // newline for each row
                    }
                } catch (NumberFormatException e) {
                    // if there is an error parsing numbers, display an error message
                    tableModel.addRow(new Object[]{"Error: Invalid input format. Please enter valid numbers."});
                    System.err.println("Error parsing input: " + e.getMessage());  // print the error message
                }

                // repaint the gantt panel to reflect any updates
                ganttPanel.repaint();

                // calculate and display the averages (turnaround time and waiting time)
                calculateAverage();

                // make the result window visible
                resultframe.setVisible(true);
            }


            // Inner class to handle dynamic resizing of Gantt chart
            class GanttPanel extends JPanel {
                private int panelWidth, quantum;
                private ArrayList<Integer> processes, burst, arrival, priority;

                // constructor to set size and quantum, and fetch lists from MainWindow
                public GanttPanel(int width, int quantum) {
                    setPreferredSize(new Dimension(800, 100)); // set preferred size for panel
                    this.panelWidth = width;  // set width of panel
                    this.quantum = quantum;  // set quantum value
                    this.processes = MainWindow.getProcesseslist(); // get process list
                    this.burst = MainWindow.getBurstlist(); // get burst list
                    this.arrival = MainWindow.getArrivallist(); // get arrival list
                    this.priority = MainWindow.getPriorityvaluelist(); // get priority list
                }

                // method to draw gantt chart based on selected calculation method
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);  // call superclass method for component painting
                    String calcMethod = StartScreen.getCalcchosen().getSelectedItem().toString(); // get selected method

                    // check which calculation method is selected and call appropriate draw method
                    if (calcMethod.equals("Round Robin (Q=3)")) {
                        drawGanttChartRR(g, getWidth(), 3); // draw Round Robin gantt chart
                    } else if (calcMethod.equals("SJN")) {
                        drawGanttChartSJN(g, getWidth(), -1); // draw SJN gantt chart
                    } else if (calcMethod.equals("SRT")) {
                        drawGanttChartSRT(g, getWidth(), -1); // draw SRT gantt chart
                    } else if (calcMethod.equals("Non-Preemptive Priority")) {
                        drawGanttChartNPP(g, getWidth(), -1); // draw Non-Preemptive Priority gantt chart (no quantum)
                    }
                }


                // method to draw gantt chart for round robin scheduling
                private void drawGanttChartRR(Graphics g, int panelWidth, int quantum) {
                    // get process, burst, and arrival lists from MainWindow
                    ArrayList<Integer> processes = MainWindow.getProcesseslist();
                    ArrayList<Integer> burst = MainWindow.getBurstlist();
                    ArrayList<Integer> arrival = MainWindow.getArrivallist();

                    // return if processes list is empty or null
                    if (processes == null || processes.isEmpty()) return;

                    int n = processes.size();  // get number of processes
                    int[] remainingBurst = new int[n];  // array to track remaining burst times
                    int[] completionTime = new int[n];  // array to store completion times
                    ArrayList<Integer> ganttOrder = new ArrayList<>();  // list to track gantt order
                    ArrayList<Integer> timeStamps = new ArrayList<>();  // list to track time stamps

                    Queue<Integer> queue = new LinkedList<>();  // queue for round robin
                    boolean[] isCompleted = new boolean[n];  // array to track if process is completed

                    // initialize remaining burst times with the burst list
                    for (int i = 0; i < n; i++) {
                        remainingBurst[i] = burst.get(i);
                    }

                    int currentTime = 0;  // track the current time
                    int completedProcesses = 0;  // track completed processes
                    int index = 0;  // index for arrival sorting

                    // sort processes by arrival time
                    ArrayList<Integer> sortedIndexes = new ArrayList<>();
                    for (int i = 0; i < n; i++) sortedIndexes.add(i);
                    sortedIndexes.sort(Comparator.comparing(arrival::get));

                    // set current time to the first arrival time if greater than 0
                    if (arrival.get(sortedIndexes.get(0)) > 0) {
                        currentTime = arrival.get(sortedIndexes.get(0));
                    }

                    // add processes that arrive at or before current time
                    while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                        queue.add(sortedIndexes.get(index));
                        index++;
                    }

                    // main loop for round robin scheduling
                    while (completedProcesses < n) {
                        if (!queue.isEmpty()) {
                            int i = queue.poll();  // get process from queue

                            // execute process for a time slice or quantum
                            int executionTime = Math.min(quantum, remainingBurst[i]);
                            ganttOrder.add(processes.get(i));
                            timeStamps.add(currentTime + executionTime);

                            currentTime += executionTime;
                            remainingBurst[i] -= executionTime;

                            // check if process is completed
                            if (remainingBurst[i] == 0) {
                                completionTime[i] = currentTime;
                                isCompleted[i] = true;
                                completedProcesses++;
                            }

                            // add newly arrived processes to the queue
                            while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                                queue.add(sortedIndexes.get(index));
                                index++;
                            }

                            // re-add unfinished process to queue for round robin
                            if (!isCompleted[i]) queue.add(i);
                        } else {
                            // CPU idle: move to the next process arrival
                            if (index < n) {
                                currentTime = arrival.get(sortedIndexes.get(index));
                                queue.add(sortedIndexes.get(index));
                                index++;
                            }
                        }
                    }

                    // **Drawing Gantt Chart**
                    int xStart = 20;  // starting x position for drawing
                    int yStart = 40;  // starting y position for drawing
                    int height = 40;  // height of the gantt bar
                    int unitWidth = panelWidth / (currentTime + 1);  // width of each time unit

                    g.setFont(new Font("Arial", Font.BOLD, 12));  // set font for labels
                    g.drawString("Gantt Chart:", 10, 20);  // draw title

                    int time = 0;  // initialize time for drawing
                    for (int i = 0; i < ganttOrder.size(); i++) {
                        int width = (timeStamps.get(i) - time) * unitWidth;  // calculate width of gantt bar
                        g.setColor(Color.CYAN);  // set color for gantt bar
                        g.fillRect(xStart, yStart, width, height);  // draw gantt bar
                        g.setColor(Color.BLACK);  // set color for border
                        g.drawRect(xStart, yStart, width, height);  // draw border
                        g.drawString("P" + ganttOrder.get(i), xStart + width / 3, yStart + height / 2);  // process label
                        g.drawString(String.valueOf(time), xStart, yStart + height + 15);  // time label
                        time = timeStamps.get(i);  // update time
                        xStart += width;  // move x position
                    }
                    g.drawString(String.valueOf(time), xStart, yStart + height + 15);  // final time label
                }


                // method to draw gantt chart for Shortest Job Next (SJN) scheduling
                private void drawGanttChartSJN(Graphics g, int panelWidth, int quantum) {
                    // get process, burst, and arrival lists from MainWindow
                    ArrayList<Integer> processes = MainWindow.getProcesseslist();
                    ArrayList<Integer> burst = MainWindow.getBurstlist();
                    ArrayList<Integer> arrival = MainWindow.getArrivallist();

                    // return if processes list is empty or null
                    if (processes == null || processes.isEmpty()) return;

                    int n = processes.size();  // get number of processes
                    int[] process = new int[n];  // array to store process IDs
                    int[] bt = new int[n];  // array to store burst times
                    int[] at = new int[n];  // array to store arrival times

                    // copy input lists into arrays for easier handling
                    for (int i = 0; i < n; i++) {
                        process[i] = processes.get(i);
                        bt[i] = burst.get(i);
                        at[i] = arrival.get(i);
                    }

                    boolean[] isCompleted = new boolean[n];  // array to track completed processes
                    int currentTime = 0;  // track the current time
                    int completed = 0;  // track the number of completed processes

                    // store Gantt chart data
                    ArrayList<Integer> ganttOrder = new ArrayList<>();
                    ArrayList<Integer> timeStamps = new ArrayList<>();
                    timeStamps.add(0);  // start with initial timestamp

                    // main loop for SJN scheduling
                    while (completed < n) {
                        int shortest = -1;
                        int minBurst = Integer.MAX_VALUE;

                        // find the shortest job that is not completed and has arrived
                        for (int i = 0; i < n; i++) {
                            if (!isCompleted[i] && at[i] <= currentTime) {
                                // choose the shortest burst time, break ties by arrival time
                                if (bt[i] < minBurst || (bt[i] == minBurst && at[i] < at[shortest])) {
                                    minBurst = bt[i];
                                    shortest = i;
                                }
                            }
                        }

                        if (shortest != -1) {
                            // execute the shortest job
                            ganttOrder.add(process[shortest]);
                            currentTime += bt[shortest];
                            timeStamps.add(currentTime);

                            // mark the process as completed
                            isCompleted[shortest] = true;
                            completed++;
                        } else {
                            // if no process is available, increment the time
                            currentTime++;
                        }
                    }

                    // **Drawing Gantt Chart**
                    int xStart = 20;  // starting x position for drawing
                    int yStart = 40;  // starting y position for drawing
                    int height = 40;  // height of the gantt bar
                    int unitWidth = panelWidth / (currentTime + 1);  // width of each time unit

                    g.setFont(new Font("Arial", Font.BOLD, 12));  // set font for labels
                    g.drawString("Gantt Chart:", 10, 20);  // draw title

                    int time = timeStamps.get(0);  // initialize time for drawing
                    for (int i = 0; i < ganttOrder.size(); i++) {
                        // calculate width of the gantt bar based on timestamps
                        int width = (timeStamps.get(i + 1) - time) * unitWidth;
                        g.setColor(Color.CYAN);  // set color for gantt bar
                        g.fillRect(xStart, yStart, width, height);  // draw gantt bar
                        g.setColor(Color.BLACK);  // set color for border
                        g.drawRect(xStart, yStart, width, height);  // draw border
                        g.drawString("P" + ganttOrder.get(i), xStart + width / 3, yStart + height / 2);  // process label
                        g.drawString(String.valueOf(time), xStart, yStart + height + 15);  // time label
                        time = timeStamps.get(i + 1);  // update time
                        xStart += width;  // move x position
                    }
                    g.drawString(String.valueOf(time), xStart, yStart + height + 15);  // final time label
                }


                // method to draw gantt chart for Shortest Remaining Time (SRT) scheduling
                private void drawGanttChartSRT(Graphics g, int panelWidth, int quantum) {
                    // get process, burst, and arrival lists from MainWindow
                    ArrayList<Integer> processes = MainWindow.getProcesseslist();
                    ArrayList<Integer> burst = MainWindow.getBurstlist();
                    ArrayList<Integer> arrival = MainWindow.getArrivallist();

                    // return if processes list is empty or null
                    if (processes == null || processes.isEmpty()) return;

                    int n = processes.size();  // get number of processes
                    int[] remainingBurst = new int[n];  // array to store remaining burst times
                    int[] completionTime = new int[n];  // array to store completion times
                    int[] arrivalTime = new int[n];  // array to store arrival times

                    // store gantt chart data
                    ArrayList<Integer> ganttOrder = new ArrayList<>();
                    ArrayList<Integer> timeStamps = new ArrayList<>();

                    // initialize arrays for remaining burst and arrival times
                    for (int i = 0; i < n; i++) {
                        remainingBurst[i] = burst.get(i);
                        arrivalTime[i] = arrival.get(i);
                    }

                    int currentTime = 0;  // track the current time
                    int completedProcesses = 0;  // track the number of completed processes
                    boolean[] isCompleted = new boolean[n];  // array to track completed processes
                    int lastProcess = -1;  // variable to store last executed process

                    // main loop for SRT scheduling
                    while (completedProcesses < n) {
                        int shortest = -1;  // variable to store the index of the shortest process
                        int minRemainingTime = Integer.MAX_VALUE;  // variable to store the minimum remaining time

                        // find the process with the shortest remaining time that has arrived
                        for (int i = 0; i < n; i++) {
                            if (arrivalTime[i] <= currentTime && !isCompleted[i] && remainingBurst[i] < minRemainingTime) {
                                minRemainingTime = remainingBurst[i];
                                shortest = i;
                            }
                        }

                        // if no process is available, increment the time
                        if (shortest == -1) {
                            currentTime++;
                            continue;
                        }

                        // if the process has changed, add to gantt chart order and timestamps
                        if (shortest != lastProcess) {
                            ganttOrder.add(processes.get(shortest));
                            timeStamps.add(currentTime);
                            lastProcess = shortest;
                        }

                        // reduce the remaining burst time for the selected process
                        remainingBurst[shortest]--;
                        currentTime++;

                        // mark the process as completed when its remaining burst time reaches 0
                        if (remainingBurst[shortest] == 0) {
                            completionTime[shortest] = currentTime;
                            isCompleted[shortest] = true;
                            completedProcesses++;
                        }
                    }

                    timeStamps.add(currentTime);  // add the final timestamp to the list

                    // **Drawing the Gantt Chart**
                    int xStart = 20;  // starting x position for drawing
                    int yStart = 40;  // starting y position for drawing
                    int height = 40;  // height of the gantt bar
                    int unitWidth = panelWidth / (currentTime + 1);  // width of each time unit

                    g.setFont(new Font("Arial", Font.BOLD, 12));  // set font for labels
                    g.drawString("Gantt Chart:", 10, 20);  // draw title

                    // draw the gantt bars for each process
                    for (int i = 0; i < ganttOrder.size(); i++) {
                        // calculate width of the gantt bar based on timestamps
                        int width = (timeStamps.get(i + 1) - timeStamps.get(i)) * unitWidth;
                        g.setColor(Color.CYAN);  // set color for gantt bar
                        g.fillRect(xStart, yStart, width, height);  // draw gantt bar
                        g.setColor(Color.BLACK);  // set color for border
                        g.drawRect(xStart, yStart, width, height);  // draw border
                        g.drawString("P" + ganttOrder.get(i), xStart + width / 3, yStart + height / 2);  // process label
                        g.drawString(String.valueOf(timeStamps.get(i)), xStart, yStart + height + 15);  // time label
                        xStart += width;  // move x position
                    }
                    // draw final time label
                    g.drawString(String.valueOf(timeStamps.get(timeStamps.size() - 1)), xStart, yStart + height + 15);
                }


                // method to draw gantt chart for Non-Preemptive Priority (NPP) scheduling
                private void drawGanttChartNPP(Graphics g, int panelWidth, int quantum) {
                    // get process, burst, arrival, and priority lists from MainWindow
                    ArrayList<Integer> processes = MainWindow.getProcesseslist();
                    ArrayList<Integer> burst = MainWindow.getBurstlist();
                    ArrayList<Integer> arrival = MainWindow.getArrivallist();
                    ArrayList<Integer> priority = MainWindow.getPriorityvaluelist();

                    // return if processes list is empty or null
                    if (processes == null || processes.isEmpty()) return;

                    int n = processes.size();  // get number of processes
                    int[] remainingBurst = new int[n];  // array to store remaining burst times
                    int[] completionTime = new int[n];  // array to store completion times
                    int[] arrivalTime = new int[n];  // array to store arrival times
                    int[] pr = new int[n];  // array to store priority values

                    // store gantt chart data
                    ArrayList<Integer> ganttOrder = new ArrayList<>();
                    ArrayList<Integer> timeStamps = new ArrayList<>();

                    // initialize arrays for remaining burst, arrival times, and priorities
                    for (int i = 0; i < n; i++) {
                        remainingBurst[i] = burst.get(i);
                        arrivalTime[i] = arrival.get(i);
                        pr[i] = priority.get(i);
                    }

                    int currentTime = 0;  // track the current time
                    int completedProcesses = 0;  // track the number of completed processes
                    boolean[] isCompleted = new boolean[n];  // array to track completed processes

                    // main loop for Non-Preemptive Priority scheduling
                    while (completedProcesses < n) {
                        int highestPriorityIndex = -1;  // variable to store index of the process with the highest priority
                        int minPriority = Integer.MAX_VALUE;  // variable to store the minimum priority value

                        // find the process with the highest priority (lowest priority number) that has arrived
                        for (int i = 0; i < n; i++) {
                            if (!isCompleted[i] && arrivalTime[i] <= currentTime) {
                                if (pr[i] < minPriority ||
                                        (pr[i] == minPriority && highestPriorityIndex != -1 && arrivalTime[i] < arrivalTime[highestPriorityIndex])) {
                                    minPriority = pr[i];
                                    highestPriorityIndex = i;
                                }
                            }
                        }

                        // if no process is available, jump to the next arrival time
                        if (highestPriorityIndex == -1) {
                            int nextArrival = Integer.MAX_VALUE;
                            for (int i = 0; i < n; i++) {
                                if (!isCompleted[i]) {
                                    nextArrival = Math.min(nextArrival, arrivalTime[i]);
                                }
                            }
                            currentTime = nextArrival;
                        } else {
                            // if the process has changed, add it to the gantt chart order and timestamps
                            if (ganttOrder.isEmpty() || ganttOrder.get(ganttOrder.size() - 1) != processes.get(highestPriorityIndex)) {
                                ganttOrder.add(processes.get(highestPriorityIndex));
                                timeStamps.add(currentTime);
                            }

                            // add the burst time to the current time
                            currentTime += remainingBurst[highestPriorityIndex];
                            completionTime[highestPriorityIndex] = currentTime;
                            isCompleted[highestPriorityIndex] = true;  // mark process as completed
                            completedProcesses++;  // increment completed processes count
                        }
                    }

                    timeStamps.add(currentTime);  // add the final timestamp to the list

                    // **Drawing the Gantt Chart**
                    int xStart = 20;  // starting x position for drawing
                    int yStart = 40;  // starting y position for drawing
                    int height = 40;  // height of the gantt bar
                    int unitWidth = panelWidth / (currentTime + 1);  // width of each time unit

                    g.setFont(new Font("Arial", Font.BOLD, 12));  // set font for labels
                    g.drawString("Gantt Chart:", 10, 20);  // draw title

                    // draw the gantt bars for each process
                    for (int i = 0; i < ganttOrder.size(); i++) {
                        // calculate width of the gantt bar based on timestamps
                        int width = (timeStamps.get(i + 1) - timeStamps.get(i)) * unitWidth;
                        g.setColor(Color.CYAN);  // set color for gantt bar
                        g.fillRect(xStart, yStart, width, height);  // draw gantt bar
                        g.setColor(Color.BLACK);  // set color for border
                        g.drawRect(xStart, yStart, width, height);  // draw border
                        g.drawString("P" + ganttOrder.get(i), xStart + width / 3, yStart + height / 2);  // process label
                        g.drawString(String.valueOf(timeStamps.get(i)), xStart, yStart + height + 15);  // time label
                        xStart += width;  // move x position
                    }
                    // draw final time label
                    g.drawString(String.valueOf(timeStamps.get(timeStamps.size() - 1)), xStart, yStart + height + 15);
                }


                // calculation classes
                public static class Calculate {

                    public static void calculateSJN(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
                        int n = processes.size();
                        int[] process = new int[n];
                        int[] bt = new int[n];
                        int[] at = new int[n];
                        int[] ct = new int[n];  // completion time
                        int[] tat = new int[n]; // turnaround time
                        int[] wt = new int[n];  // waiting time

                        // copy input lists into arrays
                        for (int i = 0; i < n; i++) {
                            process[i] = processes.get(i);
                            bt[i] = burst.get(i);
                            at[i] = arrival.get(i);
                        }

                        boolean[] isCompleted = new boolean[n]; // tracks completed processes
                        int currentTime = 0, completed = 0;

                        // store gantt chart data
                        ArrayList<Integer> ganttOrder = new ArrayList<>();
                        ArrayList<Integer> timeStamps = new ArrayList<>();

                        while (completed < n) {
                            int shortest = -1;
                            int minBurst = Integer.MAX_VALUE;

                            // find the shortest available job
                            for (int i = 0; i < n; i++) {
                                if (!isCompleted[i] && at[i] <= currentTime) {
                                    if (bt[i] < minBurst || (bt[i] == minBurst && at[i] < at[shortest])) {
                                        minBurst = bt[i];
                                        shortest = i;
                                    }
                                }
                            }

                            if (shortest == -1) {
                                // no available process → cpu remains idle
                                currentTime++;
                            } else {
                                // execute the shortest job
                                currentTime += bt[shortest];  // move time forward
                                ct[shortest] = currentTime;   // completion time
                                tat[shortest] = ct[shortest] - at[shortest]; // turnaround time
                                wt[shortest] = tat[shortest] - bt[shortest]; // waiting time
                                isCompleted[shortest] = true;
                                completed++;

                                // store gantt chart data
                                ganttOrder.add(process[shortest]);
                                timeStamps.add(currentTime);
                            }
                        }

                        // update the table with calculated values
                        tableModel.setRowCount(0);
                        for (int i = 0; i < n; i++) {
                            tableModel.addRow(new Object[]{process[i], bt[i], at[i], ct[i], tat[i], wt[i]});
                        }
                    }


                    public static void calculateSRT(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
                        int n = processes.size();
                        int[] remainingBurst = new int[n];
                        int[] completionTime = new int[n];
                        int[] turnaroundTime = new int[n];
                        int[] waitingTime = new int[n];
                        int[] arrivalTime = new int[n];
                        boolean[] isCompleted = new boolean[n];

                        // copy arrival and burst times into arrays
                        for (int i = 0; i < n; i++) {
                            remainingBurst[i] = burst.get(i);
                            arrivalTime[i] = arrival.get(i);
                        }

                        int currentTime = 0, completedProcesses = 0;
                        // loop until all processes are completed
                        while (completedProcesses < n) {
                            int shortest = -1, minRemainingTime = Integer.MAX_VALUE;
                            // find the process with the shortest remaining burst time that is ready to execute
                            for (int i = 0; i < n; i++) {
                                if (arrivalTime[i] <= currentTime && !isCompleted[i] && remainingBurst[i] < minRemainingTime) {
                                    minRemainingTime = remainingBurst[i];
                                    shortest = i;
                                }
                            }

                            if (shortest == -1) {
                                // if no process is ready to execute, increment the time
                                currentTime++;
                                continue;
                            }

                            // reduce the remaining burst time of the selected process
                            remainingBurst[shortest]--;
                            currentTime++;

                            // if the process is completed, record its completion time
                            if (remainingBurst[shortest] == 0) {
                                completionTime[shortest] = currentTime;
                                isCompleted[shortest] = true;
                                completedProcesses++;
                            }
                        }

                        // calculate turnaround and waiting times for each process
                        for (int i = 0; i < n; i++) {
                            turnaroundTime[i] = completionTime[i] - arrivalTime[i];
                            waitingTime[i] = turnaroundTime[i] - burst.get(i);
                        }

                        // update the table with calculated values
                        tableModel.setRowCount(0);
                        for (int i = 0; i < n; i++) {
                            tableModel.addRow(new Object[]{processes.get(i), burst.get(i), arrival.get(i), completionTime[i], turnaroundTime[i], waitingTime[i]});
                        }
                    }


                    //round robin
                    public static void calculateRR(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival, DefaultTableModel tableModel) {
                        int n = processes.size();
                        int quantum = 3; // define the quantum time for round robin

                        // use double arrays for the calculations to support decimals
                        double[] remainingBurst = new double[n];
                        int[] completionTime = new int[n];
                        int[] turnAroundTime = new int[n];
                        int[] waitingTime = new int[n];

                        Queue<Integer> queue = new LinkedList<>(); // queue to store processes in round robin order
                        boolean[] isCompleted = new boolean[n]; // track which processes are completed
                        int currentTime = 0, completedProcesses = 0;

                        // copy burst times as doubles
                        for (int i = 0; i < n; i++) {
                            remainingBurst[i] = burst.get(i); // cast to double if needed (implicitly done here)
                        }

                        // sort processes by arrival time
                        ArrayList<Integer> sortedIndexes = new ArrayList<>();
                        for (int i = 0; i < n; i++) sortedIndexes.add(i);
                        sortedIndexes.sort(Comparator.comparing(arrival::get));

                        int index = 0;

                        // ensure time starts at the first process's arrival
                        if (arrival.get(sortedIndexes.get(0)) > 0) {
                            currentTime = arrival.get(sortedIndexes.get(0));
                        }

                        // add the first arriving processes to the queue
                        while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                            queue.add(sortedIndexes.get(index));
                            index++;
                        }

                        // round robin execution
                        while (completedProcesses < n) {
                            if (!queue.isEmpty()) {
                                int i = queue.poll(); // get the next process to execute

                                // use remainingBurst[i] directly as double
                                double executionTime = Math.min(quantum, remainingBurst[i]);
                                currentTime += executionTime; // advance current time
                                remainingBurst[i] -= executionTime; // reduce remaining burst time

                                if (remainingBurst[i] == 0) {
                                    // if process is completed, record its completion time
                                    completionTime[i] = currentTime;
                                    isCompleted[i] = true;
                                    completedProcesses++;
                                }

                                // add new arriving processes to the queue
                                while (index < n && arrival.get(sortedIndexes.get(index)) <= currentTime) {
                                    queue.add(sortedIndexes.get(index));
                                    index++;
                                }

                                // re-add unfinished process back to the queue
                                if (!isCompleted[i]) queue.add(i);
                            } else {
                                // if CPU is idle, move to the next available process arrival
                                if (index < n) {
                                    currentTime = arrival.get(sortedIndexes.get(index));
                                    queue.add(sortedIndexes.get(index));
                                    index++;
                                }
                            }
                        }

                        // compute turnaround time and waiting time for each process
                        for (int i = 0; i < n; i++) {
                            turnAroundTime[i] = completionTime[i] - arrival.get(i); // turnaround time = completion time - arrival time
                            waitingTime[i] = turnAroundTime[i] - burst.get(i); // waiting time = turnaround time - burst time
                        }

                        // update the table with the calculated values
                        tableModel.setRowCount(0);
                        for (int i = 0; i < n; i++) {
                            // convert burst time to integer for display, but keep calculation in double
                            tableModel.addRow(new Object[]{
                                    processes.get(i), (int) remainingBurst[i], arrival.get(i), completionTime[i], turnAroundTime[i], waitingTime[i]
                            });
                        }
                    }


                    //Non-preemptive priority
                    public static void calculateNPP(ArrayList<Integer> processes, ArrayList<Integer> burst, ArrayList<Integer> arrival,
                                                    ArrayList<Integer> priority, DefaultTableModel tableModel) {
                        int n = processes.size();
                        int[] process = new int[n];
                        int[] bt = new int[n]; // burst time
                        int[] at = new int[n]; // arrival time
                        int[] pr = new int[n]; // priority
                        int[] ct = new int[n]; // completion time
                        int[] tat = new int[n]; // turnaround time
                        int[] wt = new int[n]; // waiting time

                        // copy input lists into arrays
                        for (int i = 0; i < n; i++) {
                            process[i] = processes.get(i);
                            bt[i] = burst.get(i);
                            at[i] = arrival.get(i);
                            pr[i] = priority.get(i);
                        }

                        boolean[] isCompleted = new boolean[n]; // tracks completed processes
                        int currentTime = 0, completed = 0;

                        // while all processes are not completed
                        while (completed < n) {
                            int highestPriorityIndex = -1;
                            int minPriority = Integer.MAX_VALUE;

                            // find the highest priority process that has arrived and is not yet completed
                            for (int i = 0; i < n; i++) {
                                if (!isCompleted[i] && at[i] <= currentTime) {
                                    if (pr[i] < minPriority ||
                                            (pr[i] == minPriority && highestPriorityIndex != -1 && at[i] < at[highestPriorityIndex])) {
                                        minPriority = pr[i];
                                        highestPriorityIndex = i;
                                    }
                                }
                            }

                            if (highestPriorityIndex == -1) {  // no process is ready
                                // move to the next earliest arrival time
                                int nextArrival = Integer.MAX_VALUE;
                                for (int i = 0; i < n; i++) {
                                    if (!isCompleted[i]) {
                                        nextArrival = Math.min(nextArrival, at[i]);
                                    }
                                }
                                currentTime = nextArrival;
                            } else {  // execute the chosen process
                                currentTime = Math.max(currentTime, at[highestPriorityIndex]) + bt[highestPriorityIndex];
                                ct[highestPriorityIndex] = currentTime; // completion time
                                tat[highestPriorityIndex] = ct[highestPriorityIndex] - at[highestPriorityIndex]; // turnaround time
                                wt[highestPriorityIndex] = tat[highestPriorityIndex] - bt[highestPriorityIndex]; // waiting time
                                isCompleted[highestPriorityIndex] = true;
                                completed++;
                            }
                        }

                        // sorting results based on process id before displaying
                        for (int i = 0; i < n - 1; i++) {
                            for (int j = 0; j < n - i - 1; j++) {
                                if (process[j] > process[j + 1]) {
                                    // swap all values accordingly
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

                        // display results in table
                        tableModel.setRowCount(0);
                        for (int i = 0; i < n; i++) {
                            tableModel.addRow(new Object[]{process[i], bt[i], at[i], ct[i], tat[i], wt[i]});
                        }

                        System.out.println("using calculatenpp() method");
                    }

                }
            }
        }
            public class test {
                public static void main(String[] args) {
                    new StartScreen().start();
                }
            }
