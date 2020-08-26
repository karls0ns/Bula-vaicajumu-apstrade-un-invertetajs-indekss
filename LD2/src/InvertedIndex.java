import java.util.*;

public class InvertedIndex {

	static Map<String, List<Integer>> Dictionary = new HashMap<String, List<Integer>>();
	
	//Creates the Dictionary and postings
	static void Create(List<String> List){
		
		char dl = ' ';
		String str, term;
		Vector<String> split;
		int docId, i, j;
		List<Integer> myList;		
					
		//Travels to next row in List	
		for (j=0; j!=List.size(); j++) {
		
			str = List.get(j);
			split = splitStrings(str, dl);
		
			docId = Integer.parseInt(split.get(0).toString());	
			//Travels to next element in list
			for (i=1; i !=split.size(); i++) {
			
			
				term = split.get(i).toString();		 
			
				if (Dictionary.containsKey(term)){
								
					myList = Dictionary.get(term);
					if (!myList.contains(docId)) {
						myList.add(docId);
						Collections.sort(myList);				        
					}
				} 
				else {
				
					Dictionary.put(term, new ArrayList<Integer>());         
					myList = Dictionary.get(term);
					myList.add(docId);
					Collections.sort(myList);
		        
				}
			}// END travels to next element in list
		}// END travels to next row in List	
	          
	    //Test output     
		//System.out.println(Dictionary);
	          
	    //for (String name: Dictionary.keySet()){
	    //	String key = name.toString();
	    //    String value = Dictionary.get(name).toString();  
	    //    System.out.println(key + " = " + value);  
	   // }      	          
	}
	
	//Splits Vector in it strings Based by delimiter		
	static Vector<String> splitStrings(String str, char dl){ 
		
		//Initialization of working variable
		String word = "";   
		boolean tab = true;
		boolean first = false;
		
		//Adding delimiter to the end of string
		str = str + dl;
		if (str.contains("\t")) {			
			tab = false;
			first = true;
		}
		
		int l = str.length();
		

		//Cycle to split string into substrings based on delimiter and splits first element apart
		Vector<String> substr_list = new Vector<String>(); 
			for (int i = 0; i < l; i++) { 
				
				//Splitting first element apart
				if (!tab && first) {					
					if (str.charAt(i) == 9) {
						tab = true;						
						if ((int) word.length() != 0){ 
							substr_list.add(word);
							first = false;							
						}
						word = "";
					}
					else {
					word = word + str.charAt(i);						
					}	
				}// End Splitting first element apart
				// searches for next delimiter to use as spliting point
				else if (str.charAt(i) != dl) { 
						word = word + str.charAt(i);							
					}  
				else{
					//Adds substring to list					
					if ((int) word.length() != 0){ 
						substr_list.add(word); 						
					} 					
					word = ""; 					 
				}
			}
		// return as the list of strings
	    return substr_list; 
    }	
}