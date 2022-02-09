package com.addidas.FliteTrakr;

import com.addidas.FliteTrakr.impl.AirportConnectionImpl;
import com.addidas.FliteTrakr.model.AirportConnection;
import com.addidas.FliteTrakr.model.AirportConnectionGraph;
import com.addidas.FliteTrakr.model.AirportConnectionWrapper;
import com.addidas.FliteTrakr.service.AirportService;
import com.addidas.FliteTrakr.service.FileReaderService;
import com.addidas.FliteTrakr.usecase.IAirportConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FliteTrakrApplication implements CommandLineRunner {

	@Autowired
	FileReaderService fileReaderService;
	@Autowired
	AirportService airportService;

	public static void main(String[] args) {
		SpringApplication.run(FliteTrakrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if(args.length == 0){
			log.error("Input file not found");
			return;
		}

		//Read .txt file containing connection information as String
		String connectionString = fileReaderService.readFileFromInput(args[0]);

		//Build unique list of airports and airport connection from connection string
		AirportConnectionWrapper airportConnectionWrapper= airportService.buildAirportConnections(connectionString);

		//Instantiate connection graph
		AirportConnectionGraph connectionGraph = new AirportConnectionGraph(airportConnectionWrapper.getAirportList());


		//Add connections to graph
		for(AirportConnection connection: airportConnectionWrapper.getAirportConnectionList()){
			connectionGraph.addEdge(connection.getSource(), connection.getDestination(), connection.getCost());
		}


		IAirportConnection airportConnection = new AirportConnectionImpl(connectionGraph);

		int NUE_FRA_LHR_COST = airportConnection.priceForConnection("NUE-FRA-LHR");

		if(NUE_FRA_LHR_COST > 0){
			log.info("cost of connection NUE-FRA-LHR? {}",NUE_FRA_LHR_COST);
		}

		int NUE_AMS_LHR_COST = airportConnection.priceForConnection("NUE-AMS-LHR");

		if(NUE_AMS_LHR_COST > 0){
			log.info("cost of connection NUE-AMS-LHR? {}",NUE_AMS_LHR_COST);
		}


		int NUE_FRA_LHR_NUE_COST = airportConnection.priceForConnection("NUE-FRA-LHR-NUE");

		if(NUE_FRA_LHR_NUE_COST > 0){
			log.info("cost of connection NUE-FRA-LHR-NUE? {}",airportConnection.priceForConnection("NUE-FRA-LHR-NUE"));
		}


		log.info("What is the cheapest connection from NUE to AMS? {}",
				airportConnection.cheapestConnection("NUE", "AMS"));

		log.info("What is the cheapest connection from AMS to FRA? {}",
				airportConnection.cheapestConnection("AMS", "FRA"));

		log.info("What is the cheapest connection from LHR to LHR? {}",
				airportConnection.cheapestConnection("LHR", "LHR"));

		log.info("How many different connections with maximum 3 stops exists between NUE and FRA? {}"
				,airportConnection.connectionsWithMaximumStop("NUE","FRA",3));

		log.info("How mand different connections with exactly 1 stop exists between LHR and AMS? {}"
				,airportConnection.connectionsWithMaximumStop("LHR","AMS",1));

		log.info("Find all connections from NUE to LHR below 170Euros! {}",airportConnection.connectionsBelowCost("NUE","LHR",170));


	}
}
