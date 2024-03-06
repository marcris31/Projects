package org.example.BusinessLogic.Strategy;

import org.example.Model.Client;
import org.example.Model.Queue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeStrategy implements Strategy{
    @Override
    public void addClient(List<Queue> queueList, Client c) {
        queueList.get(0).addClient(c); //adds client to the first queue in the list
        Collections.sort(queueList, new SortByWaitingTime()); //sorts list by the waiting time of each queue
    }

    public static class SortByWaitingTime implements Comparator<Queue> {
        @Override
        public int compare(Queue q1, Queue q2) {
            return q1.getWaitTime() - q2.getWaitTime();
        }
    }
}

