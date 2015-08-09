import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyAnalysisTable {

	public static final int KEYS_TO_TRY = 3;
	public static final int ALEN = 26;
	public static final String CHFREQ = "ETAONRISHDLFCMUGYPWBVKJXQZ";
	public static final char[] CHAR_CHFREQ = CHFREQ.toCharArray();
	
	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final char[] CHAR_ALPHABET = ALPHABET.toCharArray();
	
	public FrequencyAnalysisTable() throws IOException{
		
		String text = readFile("testFiles/cAliceby1.txt", Charset.defaultCharset());
		text = text.toUpperCase();  //make it upper case
		
		for(int i = 1; i <= KEYS_TO_TRY; ++i){ //split cypher text into i subtexts
			
			StringBuilder [] subtextStringBuilders = new StringBuilder [i];
			int subTextLength = subtextStringBuilders.length;
			
			//Initialise the stringBuilder array
			for(int stringBuilderIndex = 0; stringBuilderIndex < subTextLength; ++stringBuilderIndex){
				subtextStringBuilders[stringBuilderIndex] = new StringBuilder();
			}
						
			for(int index = 0; index < text.length(); ++index){  //go through text
				char c = text.charAt(index);
				subtextStringBuilders[index % i].append(c);  //get the char at this index
			}
			
			//Change all the string builders back into Strings
			String [] subTextStrings = new String [i];
			
			//Transfer them into a String array
			for(int s = 0; s < subtextStringBuilders.length; ++s){
				subTextStrings[s] = subtextStringBuilders[s].toString();
				//System.out.println(subTextStrings[s]);
			}
			
			//Do frequency analysis
			//for each character in the substring, get its count and store it  -- move 
			
			for(int substring = 0; substring < subTextStrings.length; ++substring){  //for each substring
				final int [] frequencyTable = new int [ALEN];
				
				String currentSubtext = subTextStrings[substring];					//get the next substring
				for(int index = 0; index < currentSubtext.length(); ++index){ //go through subtext chars
					char c = currentSubtext.charAt(index);					//get the current char
					if(isLetter(c)){								//if its alphabetic	
						frequencyTable[c-'A']++;
					}
				}
				

				//Print the frequencies to check if they're correct
//				for(int j = 0; j < frequencyTable.length; ++j){
					//System.out.println(CHAR_ALPHABET[j] + " " + frequencyOf(frequencyTable, CHAR_ALPHABET[j]));	
//				}
				
//				System.out.println("-------------");
				
				//Sort them with custom comparator
				List<String> charsAsList = Arrays.asList(ALPHABET.split(""));
				Collections.sort(charsAsList, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						char char1= o1.charAt(0);
						char char2 = o2.charAt(0);
						
						return frequencyOf(frequencyTable, char2) - frequencyOf(frequencyTable, char1);
					}
				});
				
				// Convert from list to array
				char[] sortedChars = new char[ALEN];			
				for(int n=0;n<ALEN;n++) {
					sortedChars[n] = charsAsList.get(n).charAt(0);
				}
				
				//Print the frequencies to check if they're correct
//				for(int j = 0; j < frequencyTable.length; ++j){
//					System.out.println(sortedChars[j] + " " + frequencyOf(frequencyTable, sortedChars[j]));	
//				}
				
				//now map our most frequent characters to the English languages most frequent characters
				Map <Character,Character> characterMap = new HashMap<Character,Character>();
				for(int n = 0; n< ALEN; ++n){
					characterMap.put(sortedChars[n], CHAR_CHFREQ[n]);
				}
				
				//Turn it into a character table
				char [] currentSubTextArray = currentSubtext.toCharArray();
//					System.out.println(substring);
				
				for(int n = 0; n < currentSubTextArray.length; ++n){  //for the length of this substring
					char temp = currentSubTextArray[n]; 
					if(isLetter(temp)){
						currentSubTextArray[n] = characterMap.get(temp);   //turn it back into english						
					}
				}
				subTextStrings[substring] = String.valueOf(currentSubTextArray);  //put it back into the array
			}
			
			//now merge the substrings back together
			
			StringBuilder sb = new StringBuilder();
			
			for(int index = 0; index < subTextStrings[0].length(); ++index){  //for the number of chars in the text array. 1t substring will always have most characters
				int numSubstrings = subTextStrings.length;
				
				for(int k = 0; k < numSubstrings; ++k){
					
					if(index < subTextStrings[k].length()){
						sb.append(subTextStrings[k].charAt(index));  //get the kth substring, and get the char at the correct position. keep appending.
					}
					
					
				}
			}
			
//			System.out.println("[][][][][][][][[][");
			System.out.println(sb.toString().substring(0, 1000));
//			System.out.println("[][][][][][][][[][");
			
//			for(int index = 0; index < text.length(); ++index){  //go through text
//				char c = text.charAt(index);
//				subtextStringBuilders[index % i].append(c);  //get the char at this index
//			}
			
			
			//Print the substring
//			for(String s : subTextStrings){
//				System.out.println(s);
//			}
			
			//merge the substring
			
			
			
			System.out.println("///////////////////////////");
		}
	}

	private boolean isLetter(char c) {
		return 'A' <= c && c <= 'Z';
	}

	protected int frequencyOf(int[] frequencyTable, char char1) {
		return frequencyTable[char1 - 'A'];
	}

	static String readFile(String path, Charset encoding) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void main(String[] args) throws IOException {
		new FrequencyAnalysisTable();
	}

}
