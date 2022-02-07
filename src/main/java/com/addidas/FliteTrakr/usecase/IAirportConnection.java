package com.addidas.FliteTrakr.usecase;

import java.util.List;

public interface IAirportConnection {

    int priceForConnection(String connectionString);

    String cheapestConnection(String source, String destination);

    int connectionsWithMaximumStop(String source, String destination, int stops);

    List<String>  connectionsBelowCost(String source, String destination, int cost);

}
