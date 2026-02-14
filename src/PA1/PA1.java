package PA1;

public class PA1{
	//Initialize the main function
	public static void main(String[] args) {
		
		//Set the filename, create the master thread, start the master thread
		String filename = "vm_1.txt";
		MasterThread master = new MasterThread(filename);
		master.start();
		
	}
	
}