package PA1;

public class PA1{
	
	public static void main(String[] args) {
		String filename = "vm_1.txt";
		MasterThread master = new MasterThread(filename);
		master.start();
		
	}
	
}