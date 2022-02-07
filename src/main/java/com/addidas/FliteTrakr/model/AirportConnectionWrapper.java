package com.addidas.FliteTrakr.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class AirportConnectionWrapper {

    private final List<Airport> airportList;
    private final List<AirportConnection> airportConnectionList;
}
