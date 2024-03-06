package org.example.BusinessLogic.Strategy;

import org.example.Model.Client;
import org.example.Model.Queue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClientsStrategy implements Strategy{
    @Override
    public void addClient(List<Queue> queueList, Client c) {
        queueList.get(0).addClient(c); //adds client to the first queue in the list
        Collections.sort(queueList, new SortByNoClients()); //sorts list by the number of clients in each queue
    }

    //compare queues by the number of clients in each queue
    public static class SortByNoClients implements Comparator<Queue> {
        @Override
        public int compare(Queue q1, Queue q2) {
            return q1.getNoClients() - q2.getNoClients();
        }
    }
}

