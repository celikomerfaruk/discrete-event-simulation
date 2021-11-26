import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

public class project2main {

	public static void main(String[] args)throws FileNotFoundException {
		
		Scanner reader = new Scanner(new File(args[0]));
		reader.useLocale(Locale.US);
		PrintStream writer = new PrintStream(new File(args[1]));
		Operations operations = new Operations();
		
		//Reading data and allocating it to the convenient data structure.
		int numOfP = reader.nextInt();
		reader.nextLine();
		for (int i = 0; i < numOfP; i++) {
			operations.addPlayer((new Player(reader.nextInt(), reader.nextInt())));;
			reader.nextLine();
		}
		int numOfArrivals = reader.nextInt();
		reader.nextLine();
		for (int i = 0; i < numOfArrivals; i++) {
			String type = reader.next();
			int id = reader.nextInt();
			double arriveTime = reader.nextDouble();
				operations.addArrival(new Arrival(type, id, arriveTime, reader.nextDouble()));
			
			
			reader.nextLine();
		}
		int numOfPhysio= reader.nextInt();
		
		for(int i= 0 ; i<numOfPhysio;i++) {
			operations.addPhysio(new Physiotherapist(i, reader.nextDouble()));
		}
		reader.nextLine();
		operations.setAvailableCoach(reader.nextInt());
		operations.setAvailableMasseur(reader.nextInt());
		
		//necessary actions are taken
		operations.Operate();
		
		//printing output to file
		writer.print(operations.generateOutput());
		
		reader.close();
		writer.close();
	}

}
