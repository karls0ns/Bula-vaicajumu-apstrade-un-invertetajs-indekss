import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TD_IDF {

	private static FileWriter myWriter;

	static List<Integer> TF_IDF(List<String> Files, List<Integer> Postings, String TermList, String fileName) throws IOException{
		
		char dl = ' ';
		List<Integer> Result = new ArrayList<Integer>(); 
		List<Integer> Res;
		int i, j, l, m, n, k, p, r;
		int DocCount;
		int DocId = 0;
		List<String> Terms;
		List<String> DocSplit = new ArrayList<String>();
		List<Integer> TermSplit;
		String term,postVect;
		int[][] counter;
		double [] td_idfArray;
		double td_idf, tf, idf, t1, idf1;
		int doc, DocWordCount;
		Map<Integer, Double> DocTf_IDF = new HashMap<Integer, Double>();
		String FileExemplar, TermExemplar;
		
		myWriter = new FileWriter(fileName, true);		
		Terms = InvertedIndex.splitStrings(TermList, dl);								
		for (i=0; i<Terms.size(); i++) {								
			term = Terms.get(i).toString();				
			if (i == Terms.size()-1) {
				myWriter.append(term+"\n");
				}
			else {
				myWriter.append(term+" ");
				}
		}		
		myWriter.append("Result:");
		if (Postings.size()<1) {			
			myWriter.append(" "+"empty\n");			
		}
		else {
			

			//Counter stores data to calculate td-df in 1d there is terms
			//in 2d there is 
			//0 -term count in current document,
			//1 - total count of terms in current document,
			//2 - count of document which contains term
			
			//Calculating term count in document
			counter = new int[Terms.size()][2];
			
			// Calculating total count of documents
			DocCount = Files.size();
			
			//Chooses Document for calculating TD-IDF
			
			for (p=0; p<Postings.size(); p++) {			
				doc = Postings.get(p);
				j = 0;			
				//Finding Current Document
				while (doc != DocId) {				
					FileExemplar = Files.get(j);
					DocSplit = InvertedIndex.splitStrings(FileExemplar, dl);
					DocId = Integer.parseInt(DocSplit.get(0));
					j++;
				}
				//Counting how many words current document has
				DocWordCount = DocSplit.size() - 1;
				//Choosing current term
				for (m=0; m<Terms.size();m++) {	
					term = Terms.get(m);			
					//Counting how many term t current document has
					if(DocSplit.contains(term)) {							
						for (l=0; l < DocSplit.size();l++) {					
							TermExemplar = DocSplit.get(l);
							if (TermExemplar.contentEquals(term)) {
								counter[m][0] = counter[m][0]+1;
							}
						}		
					}			
					else {
						counter[m][0]=0;
					}
					//Counting how many documents has current term	
					TermSplit = InvertedIndex.Dictionary.get(term);
					counter[m][1] = TermSplit.size();			
				}			
			
				td_idfArray = new double[Terms.size()];
			
				//Calculating TF-IDF for each term
				for (n=0;n<Terms.size();n++) {
					//TF for a term	
					t1 = counter[n][0];
					tf = t1/DocWordCount;
					//IDf for term
					idf1 = counter[n][1];
					idf = DocCount/idf1;
					//TF-IDF for term			
					td_idfArray[n] = tf*idf;			
				}			
			
				//Calculating TF-IDF for current document
				td_idf = 0;
				for (k=0;k<td_idfArray.length;k++) {
					td_idf = td_idf +  td_idfArray[k]; 
				}
			
				DocTf_IDF.put(doc,td_idf);			

				
		
			
			}		
			
			//Sorting based by TD-IFD values in descending order
			// Create a list from elements of HashMap 
	        List<Map.Entry<Integer, Double> > listss = new LinkedList<Map.Entry<Integer, Double> >(DocTf_IDF.entrySet());	
	        //Sorting function
	        Collections.sort(listss, new Comparator<Map.Entry<Integer, Double> >() { 
	            public int compare(Map.Entry<Integer, Double> o1,  
	                               Map.Entry<Integer, Double> o2) 
	            { 
	                return (o2.getValue()).compareTo(o1.getValue()); 
	            } 
	        });
			
			
	        // put data from sorted list to HashMap  
	        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>(); 
	        for (Map.Entry<Integer, Double> aa : listss) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        }
			
	        Res = new ArrayList<Integer>(temp.keySet());
	        Result = Res;
	        
			for (r=0; r<Res.size(); r++) {
				postVect = " "+Res.get(r).toString();
				myWriter.append(postVect);
			}
			myWriter.append("\n");
		}
		myWriter.close();
		return Result;
	}
}