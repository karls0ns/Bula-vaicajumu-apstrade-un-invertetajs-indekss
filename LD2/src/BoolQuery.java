import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BoolQuery {
	
	private static FileWriter myWriter;

	static ArrayList<List<Integer>> GetPostings(String List, String fileName) throws IOException{
		
		char dl = ' ';		
		Vector<String> split;	
		String term, postVect;
		List<Integer> Vect;
		ArrayList<List<Integer>> Result = new ArrayList<List<Integer>>();		
		split = InvertedIndex.splitStrings(List, dl);
	    int i,j; 		
		
		FileWriter myWriter = new FileWriter(fileName, true);		
		for (j=0; j<split.size();j++) {					
			term = split.get(j).toString();
			myWriter.append(term + "\n");
			myWriter.append("Postings:");
			if (InvertedIndex.Dictionary.containsKey(term)){			
				Vect = InvertedIndex.Dictionary.get(term);			
				Result.add(new ArrayList<Integer>(Vect));		
				
				for (i=0; i<Vect.size(); i++) {
					postVect = " "+Vect.get(i).toString();
					myWriter.append(postVect);
				}
			myWriter.append("\n");			
			}
			else {
				myWriter.append(" empty\n");
			}
		}	
	    myWriter.close();
	    return Result;
	}	

	static List<Integer> QueryAnd(String List, String fileName) throws IOException {
		char dl = ' ';		
		Vector<String> split;	
		String term, term1, postVect;
		List<Integer> Vect;
		List<Integer> Result = new ArrayList<Integer>();
		split = InvertedIndex.splitStrings(List, dl);
	    List<Integer> CompTo, Work;
	    List<Integer> Final = new ArrayList<Integer>();
		boolean NotFound = false;
		int temp;
	    
		int i,j,l,m; 		
		
		myWriter = new FileWriter(fileName, true);		
		
		//Works with query that contains only 1 term
		if (split.size() >=1 && split.size() < 2) {
							
			term = split.get(0).toString();
			myWriter.append(term + "\n");
			myWriter.append("Results:");
			if (InvertedIndex.Dictionary.containsKey(term)){			
				Vect = InvertedIndex.Dictionary.get(term);							
				for (i=0; i<Vect.size(); i++) {
					postVect = " "+Vect.get(i).toString();
					myWriter.append(postVect);
					Result.add(Vect.get(0));
				}
			myWriter.append("\n");			
			}			
			else {
				myWriter.append(" "+"empty\n");
			}
		}//END Works with query that contains only 1 term	   
		
		//Works with query that contains 2 or more terms
		if (split.size() >= 2) {
			for (j=0; j<split.size();j++) { 
				term = split.get(j).toString();				
				if (j == split.size()-1) {
					myWriter.append(term+"\n");
				}
				else {
					myWriter.append(term+" ");
				}				
				if (InvertedIndex.Dictionary.containsKey(term)){	
								
				}
				else {
					NotFound = true;
				}
			}						
			myWriter.append("Results:");			
			if (NotFound) {								
				myWriter.append(" "+"empty\n");				
			}
			else {
				term = split.get(0).toString();
				CompTo = InvertedIndex.Dictionary.get(term);
				for(m=1;m<split.size();m++){
					term1 = split.get(m).toString();		
					Work = InvertedIndex.Dictionary.get(term1);
					for (l=0; l<Work.size();l++) {
						temp = Work.get(l);
						if (CompTo.contains(temp)) Final.add(temp);				
					}
					
					CompTo = new ArrayList<Integer>();					
					CompTo.addAll(Final);
					Final.clear();
					
				}			
				Result = CompTo;
				if (Result.size()<1) {
					myWriter.append(" "+"empty");
				}
				else {
					for (i=0; i<Result.size(); i++) {
						postVect = " "+Result.get(i).toString();
						myWriter.append(postVect);				
					}
				}
				myWriter.append("\n");		
			}		
		}
		myWriter.close();	
		return Result;	
	}
	
	static List<Integer> QueryOr(String List, String fileName) throws IOException {
		char dl = ' ';		
		Vector<String> split;	
		String term, term1, postVect;
		List<Integer> Vect;
		List<Integer> Result = new ArrayList<Integer>();		
	    List<Integer> Work;
	    List<Integer> First = new ArrayList<Integer>();	    
		boolean Found = false;
		int temp;
		List<Integer> CompTo = new ArrayList<Integer>();
		int i,j,l,m;
		int n = 0;
		
		myWriter = new FileWriter(fileName, true);		
		split = InvertedIndex.splitStrings(List, dl);
		
		//Works with query that contains only 1 term
		if (split.size() >=1 && split.size() < 2) {
							
			term = split.get(0).toString();
			myWriter.append(term + "\n");
			myWriter.append("Results:");
			if (InvertedIndex.Dictionary.containsKey(term)){			
				Vect = InvertedIndex.Dictionary.get(term);							
				for (i=0; i<Vect.size(); i++) {
					postVect = " "+Vect.get(i).toString();
					myWriter.append(postVect);
					Result.add(Vect.get(0));
				}
			myWriter.append("\n");			
			}			
			else {
				myWriter.append(" "+"empty\n");
			}
		}//END Works with query that contains only 1 term
		
		//Works with query that contains 2 or more terms
		if (split.size() >= 2) {
			for (j=0; j<split.size();j++) { 
				term = split.get(j).toString();				
				if (j == split.size()-1) {
					myWriter.append(term+"\n");
				}
				else {
					myWriter.append(term+" ");
				}				
				if (InvertedIndex.Dictionary.containsKey(term)) Found = true;
			}						
			myWriter.append("Results:");			
			if (!Found) {								
				myWriter.append(" "+"empty\n");				
			}
			else {
				term = split.get(0).toString();	
				while (First.size()<1){
					if (InvertedIndex.Dictionary.containsKey(term)) {
						First = InvertedIndex.Dictionary.get(term);
						CompTo.addAll(First);
					}
					else {
						n = n+1;
						term = split.get(n).toString();
					}						
				}
				for(m=1+n;m<split.size();m++){							
					term1 = split.get(m).toString();
					if (InvertedIndex.Dictionary.containsKey(term1)){
						Work = InvertedIndex.Dictionary.get(term1);
						for (l=0; l<Work.size();l++) {
							temp = Work.get(l);
							if (!CompTo.contains(temp)) CompTo.add(temp);
						}
					}
				}
			Collections.sort(CompTo);
				Result = CompTo;
				if (Result.size()<1) {
					myWriter.append(" "+"empty");
				}
				else {
					for (i=0; i<Result.size(); i++) {
						postVect = " "+Result.get(i).toString();
						myWriter.append(postVect);				
					}
				}
			myWriter.append("\n");		
			}		
		}
		myWriter.close();	
		return Result;
	}
}