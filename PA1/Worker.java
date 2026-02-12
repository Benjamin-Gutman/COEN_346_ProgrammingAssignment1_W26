package PA1;

class semaphoreDefinition{
	public static Semaphore increment = new Semaphore(1);
}

public class Worker extends Thread{
	
	private String log;
	private String vulnerabilityPattern;
	private MasterThread master;
	private static LevenshteinDistance lev = new LevenshteinDistance();
	
	public Worker() {
		log = null;
		vulnerabilityPattern = null;
		master = null;
	}
	
	public Worker(String log, String vulnerabilityPattern, MasterThread master) {
		this.log = log;
		this.vulnerabilityPattern = vulnerabilityPattern;
		this.master = master;
	}
	
	
	public static boolean checkVulnerability(String log, int startIndex) {
		if (startIndex+vulnerabilityPattern.length()-1 >= log.length()) {
			return false;
		}
		lev.calculate(log.substring(startIndex, startIndex+vulnerabilityPattern.length()-1), vulnerabilityPattern);
		if (!lev.isAcceptable_change()) {
			return(checkVulnerability(log, startIndex+1));
		}
		else {
			return true;
		}
	}
	
	public void run() {
		boolean vulnerable = checkVulnerability(log, 0 );
		if(vulnerable) {
			semaphoreDefinition.increment.P(); //This waits for the key to enter critical section
			master.incrementCount();
			semaphoreDefinition.increment.V(); // This releases the key
		}
		
	}
}