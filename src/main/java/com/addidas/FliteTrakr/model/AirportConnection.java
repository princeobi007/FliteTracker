package com.addidas.FliteTrakr.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AirportConnection {
    private final Airport source;
    private final Airport destination;
    private final int cost;

    @Override
    public String toString() {
        return source + "-" + destination;
    }
}
