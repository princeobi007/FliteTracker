package com.addidas.FliteTrakr.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Airport {
    final private String airportCode;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((airportCode == null) ? 0 : airportCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Airport other = (Airport) obj;
        if (airportCode == null) {
            return other.airportCode == null;
        } else return airportCode.equalsIgnoreCase(other.airportCode);
    }

    @Override
    public String toString() {
        return airportCode;
    }
}
