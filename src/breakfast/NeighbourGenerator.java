package breakfast;

/**
 * Utility that generates all words with a levenshtein distance of 1 from the
 * input word.
 *
 */
public class NeighbourGenerator {

	public static String[] neighbourGenerator(String word){
		String[] neighbours = new String[25 * word.length()];
		int idx = 0;
		char[] cur = new char[word.length()];

		for(int i = 0; i < word.length(); i++){
			for(char c = 'a'; c <= 'z'; c++){
				word.getChars(0, word.length(), cur, 0);
				cur[i] = c;
				String result = String.copyValueOf(cur);
				if(!word.equals(result)){ // don't include the word itself
					neighbours[idx++] = result;
				}
			}
		}

		return neighbours;
	}
}

