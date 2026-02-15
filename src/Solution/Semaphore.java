package Solution;

//This code was taken from the Semaphore example shown in the tutorial 4 slides and executed in Lab2

public class Semaphore{
	private int value;
	public Semaphore() {
		value = 0;
	}
	public Semaphore(int value) {
		this.value = value;
	}
	
	public synchronized void P() {
		while (value <= 0 ) {
			try {
				wait(); // There is no waiting buffer since critical section is small (1 line java, 3 lines assembly)
			} catch (InterruptedException e) {
		 	}
		}
		value--;
	}
	
	public synchronized void V() {
		++value;
		notify();
	}
	
	
	//This is a personal addition to the class, this is to reduce the semaphore by a large amount so that
	//a number of processes may complete before another is ok to continue. 
	public synchronized void Subtract(int count) {
		while (value <= 0 ) {
			try {
				wait(); // There is no waiting buffer since critical section is small (1 line java, 3 lines assembly)
			} catch (InterruptedException e) {
		 	}
		}
		value = value - count;
	}
	
}
