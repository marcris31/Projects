package org.example.Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Queue implements Runnable {

    private BlockingQueue<Client> queue; //stores the clients in queue
    private int waitingTime; //total waiting time in the queue

    public Queue() {
        queue = new ArrayBlockingQueue<Client>(100);
        this.waitingTime = 0;
    }

    @Override
    public void run() {
        while(true) {
            if (!queue.isEmpty()) {
                try {
                    int serviceTime = queue.peek().getServiceTime(); //st for first client
                    for (int i = 0; i < serviceTime; i++) {
                        Thread.sleep(1000);
                        if(this.waitingTime > 0) { //if there are still clients waiting
                            this.waitingTime--; //decrease wt by 1
                        }
                    }
                    queue.take(); //remove client from queue
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addClient(Client c) {
        queue.add(c); //add at the end of the queue
        this.waitingTime += c.getServiceTime(); //increase waiting time
    }

    public int getNoClients() {
        return queue.size();
    }

    public String toString() {
        String output = "";
        for(int i = 0; i < queue.size(); i++) {
            output += "(I) ";
        }
        return output;
    }

    public BlockingQueue<Client> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<Client> queue) {
        this.queue = queue;
    }

    public int getWaitTime() {
        return waitingTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitingTime = waitTime;
    }
}

