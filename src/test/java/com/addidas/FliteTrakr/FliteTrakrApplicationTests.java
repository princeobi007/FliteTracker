package com.addidas.FliteTrakr;

import com.addidas.FliteTrakr.impl.AirportConnectionImpl;
import com.addidas.FliteTrakr.model.AirportConnection;
import com.addidas.FliteTrakr.model.AirportConnectionGraph;
import com.addidas.FliteTrakr.model.AirportConnectionWrapper;
import com.addidas.FliteTrakr.service.AirportService;
import com.addidas.FliteTrakr.service.FileReaderService;
import com.addidas.FliteTrakr.usecase.IAirportConnection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
class FliteTrakrApplicationTests {

	private final String inputString = "Connection: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23";

	@Autowired
	FileReaderService fileReaderService;

	@Autowired
	AirportService airportService;


	@Before
	public void loadFile(){
		FliteTrakrApplication.main(new String[]{"input.txt"});
	}

	@Test
	@DisplayName("Test file reader")
	public void testFileReader () throws Exception{
		String INPUT_TXT_FILE = "input.txt";
		String input = fileReaderService.readFileFromInput(INPUT_TXT_FILE);
		String inputString = "Connection: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23";
		Assertions.assertTrue(input.equalsIgnoreCase(inputString));
	}

	@Test
	@DisplayName("Test that Airport Service formats passed input")
	public void testAirportService()throws Exception{

		AirportConnectionWrapper connectionWrapper = airportService.buildAirportConnections(inputString);

		Assertions.assertNotNull(connectionWrapper);

		Assertions.assertNotNull(connectionWrapper.getAirportList());

		Assertions.assertNotNull(connectionWrapper.getAirportConnectionList());

		Assertions.assertEquals(4, connectionWrapper.getAirportList().size());

		//Instantiate connection graph
		AirportConnectionGraph connectionGraph = new AirportConnectionGraph(connectionWrapper.getAirportList());


		//Add connections to graph
		for(AirportConnection connection: connectionWrapper.getAirportConnectionList()){
			connectionGraph.addEdge(connection.getSource(), connection.getDestination(), connection.getCost());
		}

		IAirportConnection airportConnection = new AirportConnectionImpl(connectionGraph);

		Assertions.assertEquals(70, airportConnection.priceForConnection("NUE-FRA-LHR"));

		Assertions.assertEquals("NUE-FRA-AMS-60", airportConnection.cheapestConnection("NUE","AMS"));

		Assertions.assertEquals(1, airportConnection.connectionsWithMaximumStop("NUE","FRA",3));

		Assertions.assertEquals("NUE-FRA-LHR-70",airportConnection.connectionsBelowCost("NUE","LHR",170).get(0));

	}



}
