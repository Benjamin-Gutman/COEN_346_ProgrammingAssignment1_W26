package PA1;

class semaphoreDefinition{
	public static Semaphore increment = new Semaphore(1);
	public static Semaphore signalMaster = new Semaphore(1);
}

public class Worker extends Thread{
	
	private String log;
	private String vulnerabilityPattern;
	private MasterThread master;
	private LevenshteinDistance lev = new LevenshteinDistance();
	
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
	
	
	public boolean checkVulnerability(String log) throws InterruptedException {
		for (int i = 0; i < log.length(); i++) {
			lev.Calculate(log.substring(i, i+vulnerabilityPattern.length()), vulnerabilityPattern);
			
			if (lev.acceptable_change) {
				return true;
			}
		}
		return false;
	}
	
	public void run() {
		try {
		boolean vulnerable = checkVulnerability(log);
		if(vulnerable) {
			semaphoreDefinition.increment.P(); //This waits for the key to enter critical section
			master.incrementCount();
			semaphoreDefinition.increment.V(); // This releases the key
		}
		semaphoreDefinition.signalMaster.V();
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}
}