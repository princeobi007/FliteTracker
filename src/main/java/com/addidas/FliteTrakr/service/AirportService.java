package com.addidas.FliteTrakr.service;

import com.addidas.FliteTrakr.model.Airport;
import com.addidas.FliteTrakr.model.AirportConnection;
import com.addidas.FliteTrakr.model.AirportConnectionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AirportService {

    /**
     * This method takes in an input of , delimited connections and returns a List of AirportConnections
     * @param connectionString
     * @return List<AirportConnection>
     */
    public AirportConnectionWrapper buildAirportConnections(String connectionString){
        if(!StringUtils.hasText(connectionString)){
            log.error("Connection String is empty");
            throw  new RuntimeException("Connection String is empty");
        }
        //Split the list of connections to individual connection in an Arrays
        String [] connections = StringUtils.delimitedListToStringArray(connectionString,",","Connection:");

        if(connections.length < 1){
            log.error("Connections not found");
            throw new RuntimeException("Connections not found");
        }

        //create a set to hold unique airports
        Set<Airport> airportSet = new HashSet<>();
        //create a list to hold airport connection
        List<AirportConnection> airportConnectionList = new ArrayList<>();

        //for each connection in array process to get source, destination and cost
        for(String connection: connections){

            //split an individual connection to source airport, destination airport, and cost
            String [] connectionParams = StringUtils.delimitedListToStringArray(connection,"-"," ");

            if(connectionParams.length != 3){
                log.error("Invalid Connection params {}",connection);
            }

            Airport sourceAirport = new Airport(connectionParams[0].trim());
            airportSet.add(sourceAirport);
            Airport destinationAirport = new Airport(connectionParams[1].trim());
            airportSet.add(destinationAirport);


            //create a new AirportConnection Object
            AirportConnection airportConnection = new AirportConnection(sourceAirport,
                    destinationAirport,validateCost(connectionParams[2]));

            //Add airport connection to list
            airportConnectionList.add(airportConnection);
        }
        log.info("Connection list Size: {}",airportConnectionList.size());



        List<Airport>airportList = new ArrayList<>(airportSet);
        log.info("Airport list size: {}",airportList.size());

        for(Airport airport: airportList){
            log.info("AirportCode: {}",airport.getAirportCode());
        }

        return new AirportConnectionWrapper(airportList,airportConnectionList);
    }

    /**
     * validates a string cost is a number
     * @param cost
     * @return Integer cost
     */
    private int validateCost(String cost){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(cost);
        if(matcher.matches()){
            return Integer.parseInt(cost);
        }
        throw new RuntimeException("cost is not an Integer");
    }

}
