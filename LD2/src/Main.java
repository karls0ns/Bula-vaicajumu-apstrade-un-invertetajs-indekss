import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Main {

	public static void main(String[] args) throws IOException{
		
		String fileName, saveName, Shablon; 
		List<String> query;
		List<String> wildcard = new ArrayList<String>();
		List<String> lines = new ArrayList<String>();
		List<Integer> resAnd, resOr;
		int nr, nr2;
		boolean Input1 = true;
		boolean Input2 = true;
		boolean Input3 = true;
		boolean Input4 = true;
		boolean Exit = false;
		//Data input from keyboard
		Scanner ScanInput = new Scanner(System.in);
		
		//Header
		System.out.println("LD2");

		//Reading Data file for terms and postings
		System.out.println("Ievadiet faila vârdu, kurð satur dokumenta identifikatorus: ");
		fileName = ScanInput.nextLine();
		saveName="";
		while (Input1){		
			try {
			    //Reading data from text file of identifier and documents
				lines = Files.readAllLines(Path.of(fileName));			
				InvertedIndex.Create(lines);
				WildCard.CreateBiGramIndex();
				Input1 = false;		  
			}
			catch (IOException e) {			
				System.out.println("Fails ar ðâdu nosaukumu netika atrasts!");
				System.out.println("Atkârtoti ievadi, faila vârdu, kurð satur dokumenta identifikatorus: ");
				System.out.println("Vai ievadi Exit, lai izietu.");
				fileName = ScanInput.nextLine();			
				if (fileName.contentEquals("Exit")) {
					Input1 = false;
					Exit = true;
				}
			}
		}//END Reading Data file for terms and postings
		
		
		//Reading data file for queries
		if (!Exit) {
			System.out.println("Ievadiet faila vârdu, kurð satur vaicâjumus: ");				
			fileName = ScanInput.nextLine();
		}
		while (Input2){	
			if (Exit) {
				Input2 = false;
				break;
			}
			try {
			    //Reading data from text file of identifier and documents
				query = Files.readAllLines(Path.of(fileName));		
				Input2 = false;
				
				System.out.println("Ievadiet faila vârdu, kurð satur aizstâjçj vaicâjuma terminus: ");
			    fileName = ScanInput.nextLine();
				while (Input4) {
					try {
						//Reading data from text file of Wildcard Query
						wildcard = Files.readAllLines(Path.of(fileName));	
						Input4 = false;					
					}
					catch (IOException e) {			
						System.out.println("Fails ar ðâdu nosaukumu netika atrasts!");
						System.out.println("Atkârtoti ievadi, kurð satur aizstâjçj vaicâjuma terminus: ");
						System.out.println("Vai ievadi Exit, lai izietu.");
						fileName = ScanInput.nextLine();			
						if (fileName.contentEquals("Exit")) {
							Input4 = false;
							Input2 = false;
							Exit = true;
						}
					}
				}
				
				while (Input3) {
					if (Exit) {
						Input3 = false;
						break;
					}
					try {
						//Reading data from text file of identifier and documents
						System.out.println("Ievadiet faila vârdu, kurâ tiks veikta datu saglabâðana: ");				
						saveName = ScanInput.nextLine();		
						Input3 = false;				
						File myObj = new File(saveName);
						myObj.exists();
					}
					catch (Exception e) {			
						System.out.println("Fails ar ðâdu nosaukumu neizdevâs izveidot!");
						System.out.println("Atkârtoti ievadi, faila vârdu, kurâ tiks veikta datu saglabâðana: ");
						System.out.println("Vai ievadi Exit, lai izietu.");
						saveName = ScanInput.nextLine();			
						if (saveName.contentEquals("Exit")) {
							Input3 = false;
							Exit = true;
						}
					}
				}
				
				FileWriter myWriter = new FileWriter(saveName);			      
			    myWriter.close();
			    
			    for (nr=0; nr<query.size();nr++) {
			    	
			    	myWriter = new FileWriter(saveName,true);
			    	myWriter.append("\n"+"GetPostings"+"\n");
			    	myWriter.close();			
			    	BoolQuery.GetPostings(query.get(nr), saveName);				
				
			    	myWriter = new FileWriter(saveName,true);
					myWriter.append("\n"+"QueryAnd" +"\n");				
					myWriter.close();		
					resAnd = BoolQuery.QueryAnd(query.get(nr), saveName);					
				
					myWriter = new FileWriter(saveName,true);
					myWriter.append("\n"+"TD-IDF"+"\n");				
					myWriter.close();				
					TD_IDF.TF_IDF(lines, resAnd, query.get(nr), saveName);
				
					myWriter = new FileWriter(saveName,true);
					myWriter.append("\n"+"QueryOr" +"\n");				
					myWriter.close();
					resOr = BoolQuery.QueryOr(query.get(nr), saveName);					
				
					myWriter = new FileWriter(saveName,true);
					myWriter.append("\n"+"TD-IDF" +"\n");				
					myWriter.close();				
					TD_IDF.TF_IDF(lines, resOr, query.get(nr), saveName);
			    }
			    
				   for (nr2=0;nr2<wildcard.size();nr2++) {
					   
					   Shablon = wildcard.get(nr2);			   
					   myWriter = new FileWriter(saveName,true);
					   myWriter.append("\n"+"WildCard" +"\n");				
					   myWriter.close();
					   WildCard.WildCardQuery(Shablon, saveName);
				   }
			}
			catch (IOException e) {			
				if (Exit) break;
				System.out.println("Fails ar ðâdu nosaukumu netika atrasts!");
				System.out.println("Atkârtoti ievadi, faila vârdu, kurð satur vaicâjumus: ");
				System.out.println("Vai ievadi Exit, lai izietu.");
				fileName = ScanInput.nextLine();			
				if (fileName.contentEquals("Exit")) {
					Input2 = false;
					Exit = true;
				}
			}
		}//END Reading data file for queries
		
		if (!Exit) {
			System.out.println("Datu ieraksts failâ "+ saveName.toUpperCase() +" ir veikts veiksmîgi!");
		}
		else{
			System.out.println("Programmas darbîba pârtraukta!");
		}

		//Closing input from keyboard
		ScanInput.close();	  
	}			
}