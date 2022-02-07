package com.addidas.FliteTrakr.model;



import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Slf4j
@Component
public class AirportConnectionGraph {
    private final List<Airport> airportList;
    private final int[][] airportConnections;

    public AirportConnectionGraph(List<Airport> airportList){
        this.airportList = airportList;
        airportConnections = new int[airportList.size()][airportList.size()];
    }

    public void addEdge(Airport source, Airport destination, int connectionCost){
        int sourcePosition = getAirportIndex(source);
        int destinationPosition = getAirportIndex(destination);
        if (sourcePosition < airportList.size() && destinationPosition < airportList.size()){
           this.airportConnections[sourcePosition][destinationPosition]=connectionCost;
        }

    }

    public int getConnectionCost(Airport source, Airport destination){
        int sourcePosition = getAirportIndex(source);
        int destinationPosition = getAirportIndex(destination);
        if (sourcePosition < airportList.size() && destinationPosition < airportList.size()){
            if(source.equals(destination)){
                for(int i =0; i < airportList.size();i++){
                    if(this.airportConnections[sourcePosition][i] == 0){
                        continue;
                    }
                    return this.airportConnections[sourcePosition][i];
                }

            }
           return this.airportConnections[sourcePosition][destinationPosition];
        }
        return 0;
    }

    public int getAirportIndex(Airport airport){
        return this.airportList.indexOf(airport);
    }

    public int getAirportIndex(String code){
        return this.airportList.indexOf(new Airport(code.trim()));
    }

    public String getAirportCode(int index){
        return this.airportList.get(index).getAirportCode();
    }



}
