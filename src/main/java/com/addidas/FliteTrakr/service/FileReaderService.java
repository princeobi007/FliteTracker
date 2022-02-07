package com.addidas.FliteTrakr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


@Slf4j
@Service
public class FileReaderService {

    /**
     * Takes filename as input
     * @param input
     * @return String read from input
     */
    public String readFileFromInput(String input){
        StringBuilder builder = new StringBuilder("");
        File file = new File(input);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                log.info(st);
                builder.append(st);
            }

        } catch (IOException e) {
            log.error("Error reading file",e);
        }
        return builder.toString();
    }
}
