package PA1;

import java.io.*;
import java.util.*;
import java.lang.Thread;

public class MasterThread extends Thread {

    private final List<String> lines = new ArrayList<>(); // List to hold lines from the txt file
    private final String vulnerabilityPattern = "V04K4B63CL5BK0B"; // Pattern suggested

    // Initial conditions given by Dr. Goodarzi
    private int worker_number = 2;
    public int Count = 0;
    private double Avg = 0;
    private double approximate_avg = 0;

    // Constructor
    public MasterThread(String fileName) {
        // im going to use buffer because its more efficient for reading files than the classic Scanner library
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    // Using override to implement the run method of the MasterThread class 
    // (thats because the built-in thread class already has a run method, so we need to override it to implement our own functionality)
    @Override
    public void run() {
    	try {
    		int index = 0;

    		while (index < lines.size()) {

    			Worker[] workers = new Worker[worker_number]; // List to hold worker threads for the current batch
    			// Worker Thread code to run the Levenshtein Algorithm on the current batch of lines:
    			
    			//First if statement for if the worker count is larger than the count of the remaining logs
    			if (index+worker_number >= lines.size()) {
    				for (int i = 0; i < lines.size()-index; i++) {
    					workers[i] = new Worker(lines.get(i+index),vulnerabilityPattern, this);
        				//System.out.println(lines.get(i));
        				workers[i].start();
    				}
    				for (int i = 0; i < lines.size()-index; i++ ) {
        				workers[i].join();
        			}
    			}
    			else {
    				for (int i = 0; i < worker_number; i++ ) {
    					workers[i] = new Worker(lines.get(i+index),vulnerabilityPattern, this);
    					//System.out.println(lines.get(i));
    					workers[i].start();
    					
    				}
    				
    				for (int i = 0; i < worker_number; i++ ) {
        				workers[i].join();
        			}
    			}
    			
    			//This function removes the number of workers from the semaphore at once
    			//The master thread may only continue when all the workers are finished
    			semaphoreDefinition.signalMaster.Subtract(worker_number);
    			
    			//Wait for workers to finish
    			semaphoreDefinition.signalMaster.P();

    			// Update averages, we need to increment workers by 2 when approx_Avg - Avg > 0.2
    			index = index + worker_number;
    			//System.out.println("Detected vulnerabilities: " + Count);
    			approximate_avg = (double) Count / lines.size();

    			if (Math.abs(approximate_avg - Avg) > 0.2) {
    				try {
    					Thread.sleep(2000);
    				} catch (InterruptedException ignored) {}
    				
    				Avg = approximate_avg;
    				worker_number += 2;

    				System.out.println("Increasing workers to: " + worker_number);
    		}
    			//Release the lock again so that the semaphore is back at 1
    			semaphoreDefinition.signalMaster.V();
        }
    		
    	} 
    		catch (InterruptedException e) {
        	e.printStackTrace();
        }
        
        // print the count
        System.out.println("Detected vulnerabilities: " + Count);
}

    // synchronized as required
    public synchronized void incrementCount() {
        Count++;
    }
}
