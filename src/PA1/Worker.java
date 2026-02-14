package PA1;


//This class solely exists to define the semaphores used throughout the code
class semaphoreDefinition{
	public static Semaphore increment = new Semaphore(1);
	public static Semaphore signalMaster = new Semaphore(1);
}


//Being the worker class definition
public class Worker extends Thread{
	
	
	//Define private variables
	private String log;
	private String vulnerabilityPattern;
	private MasterThread master;
	private LevenshteinDistance lev = new LevenshteinDistance(); //This line makes an object from the Levenshtein class
	
	// *****One change was made to the LevenshteinDistance class provided by the teacher to fix an error***********
	
	
	//Class constructors
	public Worker() {
		log = null;
		vulnerabilityPattern = null;
		master = null;
	}
	
	public Worker(String log,String vulnerabilityPattern, MasterThread master) {
		this.log = log;
		this.vulnerabilityPattern = vulnerabilityPattern;
		this.master = master;
	}
	
	
	//This function implements the levenshtein class to each index of a substring to determine if the
	//log has a segment matching the vulnerability pattern
	public boolean checkVulnerability(String log) throws InterruptedException {
		//The for loop must check to see if the next substring will end outside the length of the log
		//Thus the reason for the +vulnerabilityLength() in the for loop
		for (int i = 0; i+vulnerabilityPattern.length() < log.length(); i++) {
			lev.Calculate(log.substring(i, i+vulnerabilityPattern.length()), vulnerabilityPattern);
			//If the variable is true, return the variable immediately, no need to continue the loop
			if (lev.acceptable_change) {
				return true;
			}
		}
		return false;
	}
	
	//Main run block for the thread
	public void run() {
		try {
			//
			boolean vulnerable = checkVulnerability(log);
			if(vulnerable) {
				semaphoreDefinition.increment.P(); //This waits for the key to enter critical section
				master.incrementCount(); // increments the value of count
				semaphoreDefinition.increment.V(); // This releases the key

			}
			//Release one of the locks for the master thread to restart
			semaphoreDefinition.signalMaster.V();
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}
}