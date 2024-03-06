package org.example.BusinessLogic.Strategy;

import org.example.Model.Client;
import org.example.Model.Queue;

import java.util.List;

public interface Strategy {
    public void addClient(List<Queue> queueList, Client c);
}
