import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


public class WildCard {
	
	static Map<String, List<String>> BiGramIndex = new HashMap<String, List<String>>();
	private static FileWriter myWriter;
	
	static void CreateBiGramIndex(){
		
		List<String> Termi = new ArrayList<String>();
		List<String> TempList;	
		String Terms="";
		String temp;
		int i, j;
		
		Termi.addAll(InvertedIndex.Dictionary.keySet());
		for (j=0;j<Termi.size();j++) {
			Terms = Termi.get(j);

			//Splits term in BiGrams and adds to index
			for (i=0;i<Terms.length()-1;i++){
				//For start of the term
				if (i == 0) {
					temp = "$"+Terms.substring(i,i+1);
					if (BiGramIndex.containsKey(temp)) {
						TempList = BiGramIndex.get(temp);
						if (!TempList.contains(Terms)) TempList.add(Terms);
						Collections.sort(TempList);
					}
					else {
						BiGramIndex.put(temp, new ArrayList<String>());         
						TempList = BiGramIndex.get(temp);
						TempList.add(Terms);
						Collections.sort(TempList);
					}				
				}//End For start of the term
				//For other parts of term
				temp = Terms.substring(i,i+2);
				if (BiGramIndex.containsKey(temp)) {
					TempList = BiGramIndex.get(temp);
					if (!TempList.contains(Terms)) TempList.add(Terms);
					Collections.sort(TempList);
				}
				else {
					BiGramIndex.put(temp, new ArrayList<String>());         
					TempList = BiGramIndex.get(temp);
					TempList.add(Terms);
					Collections.sort(TempList);
				}//End For other parts of term
				//For end of the term
				if (i == Terms.length()-2) {
					temp = Terms.substring(i+1,i+2)+"$";
					if (BiGramIndex.containsKey(temp)) {
						TempList = BiGramIndex.get(temp);
						if (!TempList.contains(Terms)) TempList.add(Terms);
						Collections.sort(TempList);
					}
					else {
						BiGramIndex.put(temp, new ArrayList<String>());         
						TempList = BiGramIndex.get(temp);
						TempList.add(Terms);
						Collections.sort(TempList);
					}								
				}//End For end of the term
			
			}
		}
	}

	static Map<String, List<String>> WildCardQuery(String Shablon, String fileName) throws IOException{
		
		Map<String, List<String>> Result = new HashMap<String, List<String>>();
		
		int i, j, k, m, l, r, n;
		int startIndex, endIndex;
		String temp, spliter, term, term1, tempFinal, tempShablon;
		List<String> WildCardList = new ArrayList<String>();


		List<String> WildCardListAnd = new ArrayList<String>();
		List<String> CompTo = new ArrayList<String>();

		List<String> Work, Split; 
		List<String> Final = new ArrayList<String>();
		List<String> Last = new ArrayList<String>();

		boolean NotFound = false;
		boolean Found = true;
		spliter = "*";
		
		myWriter = new FileWriter(fileName, true);
		myWriter.append(Shablon+"\n");
		myWriter.append("Results:"+"\n");
		
		//Transforming Shablon in Bi-Gram List
		if (Shablon.substring(0,1).contains(spliter) && Shablon.length()==1){
			myWriter.append("* is not a search term"+"\n");
		}
		else if (Shablon.length()==1) {
			WildCardList.add("$"+Shablon);
		}
		else if (Shablon.length()==2)  {
			if (Shablon.substring(0,1).contains(spliter) && Shablon.substring(1,2).contains(spliter)) {
				myWriter.append("* is not a search term"+"\n");
			}
			else if (Shablon.substring(0,1).contains(spliter)){
				temp = Shablon.substring(1,2)+  "$";
				WildCardList.add(temp);
			}
			else if (Shablon.substring(1,2).contains(spliter)) {
				temp = "$" + Shablon.substring(0,1);
				WildCardList.add(temp);
			}
			else {
				temp = "$" +Shablon.substring(0,1);
				WildCardList.add(temp);
				temp = Shablon.substring(0,2);
				WildCardList.add(temp);
				temp = Shablon.substring(1,2)+"$";
				WildCardList.add(temp);
			}
		}
		else {	
			//Removes all extra * symbols
			Shablon = Shablon.replaceAll(Pattern.quote(spliter)," ");
			Shablon = Shablon.replaceAll(" {2,}"," ");
			Shablon = Shablon.replaceAll(" ",spliter);
			//End Removes all extra * symbols
			temp = "";
			for (i=1;i<Shablon.length();i++){	
				if (Shablon.substring(i,i+1).contains(spliter)){
					if( i == 1) {
						temp = "$"+Shablon.substring(0,1);
						if (!WildCardList.contains(temp)) WildCardList.add(temp);
					}					
				}
				else if (!(Shablon.substring(i-1,i).contains(spliter))) {
					if (i == 1) {
						temp = "$"+ Shablon.substring(i-1,i);
						if (!WildCardList.contains(temp)) WildCardList.add(temp);
					}
					temp = Shablon.substring(i-1,i+1);
					if (!WildCardList.contains(temp)) WildCardList.add(temp);
					if (i == Shablon.length()-1) {
						temp = Shablon.substring(i,i+1)+"$";
						if (!WildCardList.contains(temp)) WildCardList.add(temp);
					}
				}
				else if (i == Shablon.length()-1) {
					temp = Shablon.substring(i,i+1)+"$";
					if (!WildCardList.contains(temp)) WildCardList.add(temp);
				}
			}
		}//Transforming Shablon in Bi-Gram List
		//IF Shablon only has 1 bi gram
		if (WildCardList.size() >=1 && WildCardList.size() < 2) {
			term = WildCardList.get(0);
			if (BiGramIndex.containsKey(term)){			
				WildCardListAnd.addAll(BiGramIndex.get(term));
			}
		}//End IF Shablon only has 1 bi gram
		//IF Shablon only has 2 or more bi grams
		if (WildCardList.size() >= 2) {
			for (r=0; r<WildCardList.size();r++) { 
				term = WildCardList.get(r);								
				if (BiGramIndex.containsKey(term)){
				}
				else {
					NotFound = true;
				}
			}								
			if (NotFound) {												
			}
			else {
				term = WildCardList.get(0);
				CompTo = BiGramIndex.get(term);
				for(m=1;m<WildCardList.size();m++){
					term1 = WildCardList.get(m);		
					Work = BiGramIndex.get(term1);
					for (l=0; l<Work.size();l++) {
						temp = Work.get(l);
						if (CompTo.contains(temp)) Final.add(temp);				
					}
					CompTo = new ArrayList<String>();					
					CompTo.addAll(Final);
					Final.clear();
				}			
				WildCardListAnd = CompTo;
			}
		}//END IF Shablon only has 2 or more bi grams
		Split = InvertedIndex.splitStrings(Shablon, '*');
		//False positive removal
		for(k=0;k<WildCardListAnd.size();k++) {
			Found = true;
			tempFinal = WildCardListAnd.get(k);
			for (j=0;j<Split.size();j++) {
				tempShablon = Split.get(j);
				if (!tempFinal.contains(tempShablon))  {
					Found = false;
					break;
				}
				startIndex = tempFinal.indexOf(tempShablon) + tempShablon.length();
				endIndex = tempFinal.length();
				if (j == 0) {
					tempFinal = tempFinal.substring(startIndex,endIndex);
				}
				if (j > 0) {
					if (startIndex < tempShablon.length()) {
						Found = false;
						break;
					}
					tempFinal = tempFinal.substring(startIndex,endIndex);
				}
			}
			if (Found) Last.add(WildCardListAnd.get(k));
		}//END False positive removal
		//Writing in file
		if (Last.size() == 0 ) {
			myWriter.append("empty");
		}
		myWriter.close();
		for (n=0; n<Last.size();n++) {
			BoolQuery.GetPostings(Last.get(n), fileName);
		}
		return Result;
	}
}