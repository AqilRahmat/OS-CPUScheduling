import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//Starting screen
class StartScreen {
    private JFrame startframe;
    private JButton startbutton;
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

    //starting frame initialization
    public void start() {
        startframe = new JFrame("Start Menu");
        startframe.setLayout(new GridLayout(4,2));
        startframe.setBounds(100, 100, 300, 200);
        startframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startframe.setResizable(false);
        InsertUserComponent();

        startframe.setVisible(true);
    }

    //function to insert the textfield, button and other component to get user input.
    private void InsertUserComponent() {
        //Number of process
        JLabel processlabel = new JLabel("Number of Processes :");
        processnum = new JComboBox<>();
        processnum.setEditable(false);
        for(int i = 3; i < 11; i++) {
            processnum.addItem(i+"");
        }

        //Calculation chooser
        JLabel calclabel = new JLabel("Calculation Method :");
        calcchosen = new JComboBox<>();
        calcchosen.setEditable(false);
        calcchosen.addItem("Round Robin (Q=3)");
        calcchosen.addItem("SRT");
        calcchosen.addItem("SJN");
        calcchosen.addItem("Preemptive Priority");
        calcchosen.addItem("Non-Preemptive Priority");

        //Priority
        JLabel prioritylabel = new JLabel("Priority :");
        priority = new JComboBox<>();
        priority.setEditable(false);
        priority.addItem("Have Priority");
        priority.addItem("No Priority");

        //start button
        startbutton = new JButton("Start");
        startbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startframe.setVisible(false);
                MainWindow main = new MainWindow();
            }
        });

        //add to frame
        startframe.add(processlabel);
        startframe.add(processnum);

        startframe.add(calclabel);
        startframe.add(calcchosen);

        startframe.add(prioritylabel);
        startframe.add(priority);

        startframe.add(new JLabel("")); //empty label to fill space
        startframe.add(startbutton);
    }
}

//class for new window when start button is clicked
class MainWindow {
    private JFrame mainframe;
    private StartScreen startscreen;
    private JComboBox<String> processnum, calcchosen, priority;
    static ArrayList<Integer> processes, burst, arrival, priorityvalue;

    //TODO: add boolean to set if user can enter burst time, arrival time, etc..
    //TODO: user can enter from left to right and top to bottom

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

        processnum = startscreen.getProcessnum();
        calcchosen = startscreen.getCalcchosen();
        priority = startscreen.getPriority();

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

            JButton reset = new JButton("Reset");
            reset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainframe.dispose();
                    MainWindow mainwindow = new MainWindow();
                }
            });

            mainframe.add(new JLabel("")); //empty space
            mainframe.add(new JLabel("")); //empty space
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
            mainframe.add(new JLabel("")); //empty space
            mainframe.add(reset);
            mainframe.add(calculate);
        }
        mainframe.setVisible(true);
    }
}

//Result window
class ResultWindow {
    private JFrame resultframe;

    public ResultWindow() {
        resultframe = new JFrame("Result");
        resultframe.setBounds(100, 100, 500, 300);
        resultframe.setResizable(true);

        for(int i = 0; i < MainWindow.getProcesseslist().size(); i++) {
            System.out.println(MainWindow.getProcesseslist().get(i));
            System.out.println(MainWindow.getPriorityvaluelist().get(i));
            System.out.println(MainWindow.getBurstlist().get(i));
            System.out.println(MainWindow.getArrivallist().get(i));
        }


        //TODO: Set gantt chart to display result (row based on the number of process, just use getprocesseslist.getsize)

        resultframe.setVisible(true);
    }
}

//Calculation classes
//TODO: Implement calculation class using inheritance, abstract calculate class and inherits by other calc method
//TODO: class to be overriden (Calculate)

public class test {
    public static void main(String[] args) {
        new StartScreen().start();
    }
}
