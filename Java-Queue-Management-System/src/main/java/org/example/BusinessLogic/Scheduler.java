package org.example.BusinessLogic;

import org.example.BusinessLogic.Strategy.ClientsStrategy;
import org.example.BusinessLogic.Strategy.Strategy;
import org.example.BusinessLogic.Strategy.TimeStrategy;
import org.example.Model.Client;
import org.example.Model.Queue;

import java.util.ArrayList;

public class Scheduler {

    private ArrayList<Queue> queueList; //list of all queues managed
    private Strategy strategy; // used strategy

    public Scheduler(int maxQueues, SelectionPolicy policy) {
        queueList = new ArrayList<Queue>();
        for(int i = 0; i < maxQueues; i++) {
            Queue q = new Queue();
            Thread t = new Thread(q);
            t.start();
            this.queueList.add(q);
        }
        changeSelectionPolicy(policy);
    }

    //changes the strategy used
    public void changeSelectionPolicy(SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE) {
            this.strategy = new ClientsStrategy();
        } else if(policy == SelectionPolicy.SHORTEST_TIME) {
            this.strategy = new TimeStrategy();
        }
    }

    //adds a client to the queue using the strategy chosen
    public void assignClient(Client c) {
        strategy.addClient(this.queueList, c);
    }

    //average waiting time
    public double calculateAWT() {
        int waitingTime = 0;
        for(Queue q : queueList) {
            waitingTime += q.getWaitTime();
        }
        if(queueList.size() != 0) {
            return waitingTime / queueList.size();
        }
        else {
            return 0;
        }
    }

    public int getCurrentWaitingTime() {
        int waitingTime = 0;
        for(Queue q : queueList) {
            waitingTime += q.getWaitTime();
        }
        return waitingTime;
    }

    public ArrayList<Queue> getQueueList() {
        return this.queueList;
    }

    public String toString() {
        String output = "";
        int index = 1;
        for(Queue q : queueList) {
            output += "Queue " + index + ": ";
            for(Client c : q.getQueue()) {
                output += c.toString() + " ";
            }
            output += "\n";
            index++;
        }
        return output;
    }
}
