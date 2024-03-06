package org.example.BusinessLogic;

import org.example.Model.Client;
import org.example.View.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SimulationManager implements Runnable{

    private int timeLimit; //max simulation time
    private int[] processingTimeInterval = new int[1];
    private int[] arrivalTimeInterval = new int[1];
    private int noQueues;
    private int noClients;
    private SelectionPolicy selectionPolicy;

    private Scheduler scheduler;
    private ArrayList<Client> clientList;
    private Controller controller;
    private View view;

    private double averageWaitingTime;
    private double averageServiceTime;
    private int peakHour; //higest waiting time

    public SimulationManager(int timeLimit, int[] arrivalTimeInterval, int[] processingTimeInterval, int noQueues, int noClients, SelectionPolicy selectionPolicy, Controller controller, View v) {
        this.timeLimit = timeLimit;
        this.arrivalTimeInterval = arrivalTimeInterval;
        this.processingTimeInterval = processingTimeInterval;
        this.noQueues = noQueues;
        this.noClients = noClients;
        this.selectionPolicy = selectionPolicy;

        this.scheduler = new Scheduler(noQueues, selectionPolicy);
        this.clientList = generateClients();
        this.averageServiceTime = calculateAST();
        this.controller = controller;
        this.view = v;
    }

    @Override
    public void run() {
        int currentTime = 0;
        int maxWaitingTime = 0;
        // reun condition
        while(currentTime < timeLimit) {
            ArrayList<Client> temp = new ArrayList<Client>();
            //assign clients for that time of arrival
            for(Client c : clientList) {
                if(c.getArrivalTime() == currentTime) {
                    scheduler.assignClient(c);
                    temp.add(c);
                }
            }
            clientList.removeAll(temp); //clients assigned are removed
            currentTime++;

            view.updateView(scheduler.getQueueList(), currentTime, clientListToStringView());
            averageWaitingTime += scheduler.calculateAWT();
            if(scheduler.getCurrentWaitingTime() > maxWaitingTime) {
                peakHour = currentTime;
                maxWaitingTime = scheduler.getCurrentWaitingTime();
            }
            controller.writeInLog(currentTime, clientListToStringLog(), scheduler.toString());
            try {
                Thread.sleep(1000); //1 sec pause to slow down
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        averageWaitingTime = averageWaitingTime / timeLimit;
        controller.writeInLog(averageWaitingTime, averageServiceTime, peakHour);
        controller.stopSimulation();
    }

    //random generating the clients
    public ArrayList<Client> generateClients() {
        ArrayList<Client> temp = new ArrayList<Client>();
        for(int i = 0; i < noClients; i++) {
            Client c = new Client(i, randomNumber(arrivalTimeInterval), randomNumber(processingTimeInterval));
            temp.add(c);
        }
        Collections.sort(temp, new SortClients());
        return temp;
    }

    //average service time
    public double calculateAST() {
        double serviceTime = 0;
        for (Client c : clientList) {
            serviceTime += c.getServiceTime();
        }
        return serviceTime / clientList.size();
    }

    public String clientListToStringView() {
        String output = "";
        for(Client c : clientList) {
            output += c.toString() + "\n";
        }
        return output;
    }

    public String clientListToStringLog() {
        String output = "";
        for(Client c : clientList) {
            output += c.toString() + " ";
        }
        return output;
    }

    public int randomNumber(int[] interval) {
        Random random = new Random();
        return (int) ((Math.random() * (interval[1] - interval[0])) + interval[0]);
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int[] getProcessingTimeInterval() {
        return processingTimeInterval;
    }

    public void setProcessingTimeInterval(int[] processingTimeInterval) {
        this.processingTimeInterval = processingTimeInterval;
    }

    public int[] getArrivalTimeInterval() {
        return arrivalTimeInterval;
    }

    public void setArrivalTimeInterval(int[] arrivalTimeInterval) {
        this.arrivalTimeInterval = arrivalTimeInterval;
    }

    public int getNoQueues() {
        return noQueues;
    }

    public void setNoQueues(int nrQueues) {
        this.noQueues = noQueues;
    }

    public int getNoClients() {
        return noClients;
    }

    public void setNoClients(int nrClients) {
        this.noClients = noClients;
    }

    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public static class SortClients implements Comparator<Client> {
        @Override
        public int compare(Client c1, Client c2) {
            return c1.getArrivalTime() - c2.getArrivalTime();
        }
    }
}

