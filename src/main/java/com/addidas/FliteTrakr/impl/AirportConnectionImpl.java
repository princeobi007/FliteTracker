package com.addidas.FliteTrakr.impl;

import com.addidas.FliteTrakr.model.Airport;
import com.addidas.FliteTrakr.model.AirportConnection;
import com.addidas.FliteTrakr.model.AirportConnectionGraph;
import com.addidas.FliteTrakr.usecase.IAirportConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirportConnectionImpl implements IAirportConnection {
    private final AirportConnectionGraph airportConnectionGraph;

    private final String CONNECTION_NOT_FOUND = "No such connection found!";

    @Override
    public int priceForConnection(String connectionString) {
        //Split connection into array
        String[] connectionArray = StringUtils.delimitedListToStringArray(connectionString, "-");
        int connectionSum = 0;
        int windowStart = 0;
        int windowElementSize = 2;
        for (int windowEnd = windowStart + 1; windowEnd < connectionArray.length; windowEnd++) {
            int cost = airportConnectionGraph.getConnectionCost(new Airport(connectionArray[windowStart])
                    , new Airport(connectionArray[windowEnd]));
            if (cost == 0) {
                log.error(CONNECTION_NOT_FOUND);
                return 0;
            }
            connectionSum += cost; // add the next element

            // slide the window, we don't need to slide if we've not hit the required window size of 'k'
            if (windowEnd >= windowElementSize - 1) {
                windowStart++; // slide the window ahead
            }
        }
        return connectionSum;
    }

    @Override
    public String cheapestConnection(String source, String destination) {

        Map<Integer, String> costPerRoute = transverseGraph(source, destination);
        int min = Integer.MAX_VALUE;

        if (costPerRoute.isEmpty()) {
            return CONNECTION_NOT_FOUND;
        }

        //iterate through path of maps to get minimum cost
        for (Map.Entry<Integer, String> entry : costPerRoute.entrySet()) {
            min = Math.min(min, entry.getKey());
        }

        return costPerRoute.get(min).concat("-").concat(String.valueOf(min));
    }



    @Override
    public int connectionsWithMaximumStop(String source, String destination, int stops) {

        Map<Integer,String> pathMap = transverseGraph(source, destination);
        int maxAirports = stops+2;
        int count = 0;
        for (Map.Entry<Integer, String> entry : pathMap.entrySet()) {
            String [] airportsTouched = StringUtils.delimitedListToStringArray(entry.getValue(),"-");
            log.info("Cost: {}, Airports touched: {}",entry.getKey(),airportsTouched);
            if(airportsTouched.length <= maxAirports){
                count++;
            }
        }

        return count;
    }

    @Override
    public List<String> connectionsBelowCost(String source, String destination, int cost) {
        Map<Integer,String> pathMap = transverseGraph(source, destination);
        List<String> routeList = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : pathMap.entrySet()) {
            if(entry.getKey() < cost){
                routeList.add(entry.getValue().concat("-").concat(String.valueOf(entry.getKey())));
            }
        }
        log.info("list size:{}",routeList.size());
        return routeList;
    }

    private Map<Integer, String> transverseGraph(String source, String destination){
        Map<Integer, String> costPerRoute = new HashMap<>();

        StringBuilder builder = new StringBuilder("");

        int sourceIndex = airportConnectionGraph.getAirportIndex(source);
        int destinationIndex = airportConnectionGraph.getAirportIndex(destination);
        int[][] connection = airportConnectionGraph.getAirportConnections();
        int currentCost = 0;
        builder.append(source);

        boolean sourceIndexReset = false;
        boolean isFirstPass = true;

        for (int i = 0; i < airportConnectionGraph.getAirportList().size(); ++i) {
            //if source index has been reset and !first iteration check if it's the destination and add
            if(sourceIndexReset && !isFirstPass){
                if(connection[sourceIndex][i-1] != 0){
                    builder.append("-").append(destination);
                    currentCost += connection[sourceIndex][destinationIndex];
                    costPerRoute.put(currentCost, builder.toString());
                    break;
                }
            }


            if (connection[sourceIndex][i] == 0) {
                if(i == destinationIndex){
                    isFirstPass = false;
                }
                continue;
            }

            //destination has been reached, add path to map
            if (i == destinationIndex) {
                builder.append("-").append(destination);
                currentCost += connection[sourceIndex][destinationIndex];
                costPerRoute.put(currentCost, builder.toString());
                break;
            }


            currentCost += connection[sourceIndex][i];
            sourceIndex = i;
            builder.append("-").append(airportConnectionGraph.getAirportCode(sourceIndex));
            i = 0;
            sourceIndexReset = true;

        }

        return costPerRoute;
    }
}
