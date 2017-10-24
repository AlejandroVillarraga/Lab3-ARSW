package edu.eci.arsw.highlandersim;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlFrame extends JFrame {

    private static final int DEFAULT_IMMORTAL_HEALTH = 100;
    private static final int DEFAULT_DAMAGE_VALUE = 10;

    
    
    private JPanel contentPane;
    private List<Immortal> immortals;    
    public static AtomicInteger threadCont= new AtomicInteger(0);
    private JTextArea output;
    private JTextField numOfImmortals;
    private final Object lock ="";
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ControlFrame frame = new ControlFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ControlFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 647, 248);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JToolBar toolBar = new JToolBar();
        contentPane.add(toolBar, BorderLayout.NORTH);

        final JButton btnStart = new JButton("Start");
        JButton btnPauseAndCheck = new JButton("Pause and check");
        JButton btnResume = new JButton("Resume");
        
        btnStart.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                immortals = setupInmortals();

                if (immortals != null) {
                    for (Immortal im : immortals) {
                        im.start();
                    }
                }

                btnStart.setEnabled(false);
                btnPauseAndCheck.setEnabled(true);
                btnResume.setEnabled(true);

            }
        });
        toolBar.add(btnStart);

        
        btnPauseAndCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                for (int i = 0; i < immortals.size(); i++) {
                    immortals.get(i).pause();
                }
                while(threadCont.intValue()<immortals.size()){
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(threadCont.get());
                }
                
                int sum = 0;
                for (Immortal im : immortals) {
                    sum += im.getHealth();
                }
                threadCont=new AtomicInteger(0);
                output.setText(immortals.toString() + ". Sum:" + sum);
                output.setText(""+sum);
            }
        });
        toolBar.add(btnPauseAndCheck);

        

        btnResume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < immortals.size(); i++) {
                    immortals.get(i).pauseNot();
                }
                synchronized(lock){
                    lock.notifyAll();
                }
            }
        });

        toolBar.add(btnResume);

        JLabel lblNumOfImmortals = new JLabel("num. of immortals:");
        toolBar.add(lblNumOfImmortals);

        numOfImmortals = new JTextField();
        numOfImmortals.setText("3");
        toolBar.add(numOfImmortals);
        numOfImmortals.setColumns(10);

        JButton btnStop = new JButton("STOP");
        
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < immortals.size(); i++) {
                    immortals.get(i).stopThreads();
                }
                btnStart.setEnabled(true);
                btnPauseAndCheck.setEnabled(false);
                btnResume.setEnabled(false);
            }
        });
        
        btnStop.setForeground(Color.RED);
        toolBar.add(btnStop);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        output = new JTextArea();
        output.setEditable(false);
        scrollPane.setViewportView(output);

    }

    public List<Immortal> setupInmortals() {

        try {
            int ni = Integer.parseInt(numOfImmortals.getText());

            List<Immortal> il = new LinkedList<Immortal>();

            for (int i = 0; i < ni; i++) {
                Immortal i1 = new Immortal("im" + i, il, DEFAULT_IMMORTAL_HEALTH, DEFAULT_DAMAGE_VALUE,lock,i);
                i1.pauseNot();
                il.add(i1);
            }
            return il;
        } catch (NumberFormatException e) {
            JOptionPane.showConfirmDialog(null, "Número inválido.");
            return null;
        }

    }

}
