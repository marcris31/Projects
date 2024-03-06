package org.example.View;

import org.example.BusinessLogic.SelectionPolicy;
import org.example.Model.Queue;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class View extends JFrame {
    private JLabel ctLabel;
    private JTextArea clientsTA;
    private JLabel timeLabel;
    private JLabel arrivalTimeLabel;
    private JTextField timeTextField;
    private JTextField minArrivalTimeTextField;
    private JTextField maxArrivalTimeTextField;
    private JLabel stL;
    private JTextField minstTF;
    private JLabel jcomp10;
    private JTextField maxstTF;
    private JLabel jcomp12;
    private JLabel nrQueuesL;
    private JLabel nrClientsL;
    private JTextField nrClientsTF;
    private JTextField nrQueuesTF;
    private JComboBox strategyCB;
    private JLabel queue1L;
    private JLabel queue2L;
    private JLabel queue3L;
    private JLabel queue4L;
    private JButton simulateBTN;
    private ArrayList<JLabel> queueLabelList;

    public View() {
        //construct components
        ctLabel = new JLabel ("Current Time:");
        clientsTA = new JTextArea ("Clients: ");
        timeLabel = new JLabel ("Time:");
        arrivalTimeLabel = new JLabel ("Arrival Time Interval:");
        timeTextField = new JTextField (5);
        minArrivalTimeTextField = new JTextField (5);
        maxArrivalTimeTextField = new JTextField (5);
        stL = new JLabel ("Service Time Interval:");
        minstTF = new JTextField (5);
        jcomp10 = new JLabel ("-");
        maxstTF = new JTextField (5);
        jcomp12 = new JLabel ("-");
        nrQueuesL = new JLabel ("Number Of Queues:");
        nrClientsL = new JLabel ("Number Of Clients:");
        nrClientsTF = new JTextField (5);
        nrQueuesTF = new JTextField (5);
        strategyCB = new JComboBox (SelectionPolicy.values());
        queue1L = new JLabel ("Queue I:");
        queue2L = new JLabel ("Queue II:");
        queue3L = new JLabel ("Queue III:");
        queue4L = new JLabel ("Queue IV:");
        simulateBTN = new JButton("SIMULATE");

        //adjust size and set layout
        this.setPreferredSize (new Dimension (944, 630));
        this.setLayout (null);

        queue1L.setEnabled (false);
        queue2L.setEnabled (false);
        queue3L.setEnabled (false);
        queue4L.setEnabled (false);
        queueLabelList = new ArrayList<>(Arrays.asList(queue1L, queue2L, queue3L, queue4L));

        //add components
        this.add (ctLabel);
        this.add (clientsTA);
        this.add (timeLabel);
        this.add (arrivalTimeLabel);
        this.add (timeTextField);
        this.add (minArrivalTimeTextField);
        this.add (maxArrivalTimeTextField);
        this.add (stL);
        this.add (minstTF);
        this.add (jcomp10);
        this.add (maxstTF);
        this.add (jcomp12);
        this.add (nrQueuesL);
        this.add (nrClientsL);
        this.add (nrClientsTF);
        this.add (nrQueuesTF);
        this.add (strategyCB);
        this.add (queue1L);
        this.add (queue2L);
        this.add (queue3L);
        this.add (queue4L);
        this.add (simulateBTN);

        ctLabel.setFont(ctLabel.getFont().deriveFont(20.0f));
        clientsTA.setFont(clientsTA.getFont().deriveFont(clientsTA.getFont().getStyle() | Font.BOLD));
        clientsTA.setFont(clientsTA.getFont().deriveFont(15.0f));
        timeLabel.setFont(timeLabel.getFont().deriveFont(14.0f));
        stL.setFont(stL.getFont().deriveFont(13.0f));
        arrivalTimeLabel.setFont(arrivalTimeLabel.getFont().deriveFont(13.0f));
        nrClientsL.setFont(nrClientsL.getFont().deriveFont(13.0f));
        nrQueuesL.setFont(nrQueuesL.getFont().deriveFont(13.0f));
        queue1L.setFont(queue1L.getFont().deriveFont(18.0f));
        queue2L.setFont(queue2L.getFont().deriveFont(18.0f));
        queue3L.setFont(queue3L.getFont().deriveFont(18.0f));
        queue4L.setFont(queue4L.getFont().deriveFont(18.0f));
        strategyCB.setFont(strategyCB.getFont().deriveFont(13.0f));
        simulateBTN.setFont(simulateBTN.getFont().deriveFont(15.0f));
        timeTextField.setFont(timeTextField.getFont().deriveFont(25.0f));
        minArrivalTimeTextField.setFont(minArrivalTimeTextField.getFont().deriveFont(25.0f));
        maxArrivalTimeTextField.setFont(maxArrivalTimeTextField.getFont().deriveFont(25.0f));
        minstTF.setFont(minstTF.getFont().deriveFont(25.0f));
        maxstTF.setFont(maxstTF.getFont().deriveFont(25.0f));
        nrClientsTF.setFont(nrClientsTF.getFont().deriveFont(25.0f));
        nrQueuesTF.setFont(nrQueuesTF.getFont().deriveFont(25.0f));


        //set component bounds (only needed by Absolute Positioning)
        this.ctLabel.setBounds (50, 50, 200, 50);
        this.clientsTA.setBounds (50, 125, 200, 190);
        this.timeLabel.setBounds (50, 425, 75, 50);
        this.arrivalTimeLabel.setBounds (205, 400, 150, 50);
        this.timeTextField.setBounds (95, 425, 50, 50);
        this.minArrivalTimeTextField.setBounds (350, 400, 50, 50);
        this.maxArrivalTimeTextField.setBounds (405, 400, 50, 50);
        this.stL.setBounds (200, 450, 150, 50);
        this.minstTF.setBounds (350, 450, 50, 50);
        this.jcomp10.setBounds (400, 400, 10, 50);
        this.maxstTF.setBounds (405, 450, 50, 50);
        this.jcomp12.setBounds (400, 450, 10, 50);
        this.nrQueuesL.setBounds (485, 400, 150, 50);
        this.nrClientsL.setBounds (490, 450, 150, 50);
        this.nrClientsTF.setBounds (615, 450, 50, 50);
        this.nrQueuesTF.setBounds (615, 400, 50, 50);
        this.strategyCB.setBounds (700, 425, 175, 50);
        this.queue1L.setBounds (375, 50, 1500, 50);
        this.queue2L.setBounds (375, 125, 1500, 50);
        this.queue3L.setBounds (375, 200, 1500, 50);
        this.queue4L.setBounds (375, 275, 1500, 50);
        this.simulateBTN.setBounds (375, 525, 120, 50);

        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible (true);
    }

    public void updateView(ArrayList<Queue> queueList, int currentTime, String clientListString) {
        int i = 0;
        ctLabel.setText("Time: " + currentTime);

        for(Queue q : queueList) {
            if(i < 4) {
                if (!queueLabelList.get(i).isEnabled()) {
                    queueLabelList.get(i).setEnabled(true);
                }
                int temp = i + 1;
                queueLabelList.get(i).setText("Queue " + temp + ": " + q.toString());
                i++;
            }
        }
        clientsTA.setText("Clients: " + clientListString);
    }

    public int getTimeLimit() {
        return Integer.parseInt(timeTextField.getText());
    }

    public int[] getServiceTimeInterval() {
        return new int[]{Integer.parseInt(minstTF.getText()), Integer.parseInt(maxstTF.getText())};
    }

    public int[] getArrivalTimeInterval() {
        return new int[]{Integer.parseInt(minArrivalTimeTextField.getText()), Integer.parseInt(maxArrivalTimeTextField.getText())};
    }

    public int getNoQueues() {
        return Integer.parseInt(nrQueuesTF.getText());
    }

    public int getNoClients() {
        return Integer.parseInt(nrClientsTF.getText());
    }

    public SelectionPolicy getSelectionPolicy() {
        return (SelectionPolicy) strategyCB.getSelectedItem();
    }

    public void addSimulateListener(ActionListener a) {
        simulateBTN.addActionListener(a);
    }
}

