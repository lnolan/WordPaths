package breakfast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class WordPairs {

	private static final String dictionaryFilePath = "/usr/share/dict/words";
	private static final int wordLength = 4;

	public static void main(String[] args) throws IOException{
		Date startTime = new Date();
		File dictFile = new File(dictionaryFilePath);
		List<String> words = getWords(dictFile);

		File pairs = new File(args[0]);
		File output = new File(args[1]);

		Date gotWordsTime = new Date();
		Graph graph = new Graph(words);
		Date builtGraphTime = new Date();

		List<String> wordPairs = FileUtils.readLines(pairs);

		StringBuilder result = new StringBuilder();
		Date startFindTime = new Date();
		for(String pair : wordPairs){
			String[] pairStrs = pair.split(("\t"));
			result.append(findPath(graph, pairStrs[0], pairStrs[1]) + "\n");
		}
		FileUtils.write(output, result.toString());
		Date endFindTime = new Date();

		System.out.println("Time to find all pairs and write file: " + (endFindTime.getTime() - startFindTime.getTime()) + " msec");
		System.out.println("Pairs found: " + found + ", pairs with words not found in dict " + notfound + ", paths not found " + pathnotfound);

		System.out.println("Time to read words: " + (gotWordsTime.getTime() - startTime.getTime()) + " msec");
		System.out.println("Time to build graph: " + (builtGraphTime.getTime() - gotWordsTime.getTime()) + " msec");
	}

	private static int notfound = 0;
	private static int found = 0;
	private static int pathnotfound = 0;
	private static String findPath(Graph graph, String startStr, String endStr){
		Node start = graph.findNode(startStr);
		Node end = graph.findNode(endStr);

		if(start == null || end == null){
			notfound++;
		}else{
			found++;

			List<String> path = graph.findPathFrom(start, end);

			if(path == null){
				pathnotfound++;
			}else{
				StringBuilder result = new StringBuilder();
				for(String s : path){
					result.append(s + " ->");
				}
				return result.toString().substring(0, result.length()-3);
			}
		}

		return "";
	}

	private static List<String> getWords(File f) throws IOException{
		List<String> lines = FileUtils.readLines(f);
		List<String> words = new ArrayList<String>();

		String last = "";
		for(String s : lines){
			String cur = s.trim();
			if(cur.length() == wordLength){
				cur = cur.toLowerCase();
				if(!cur.equals(last)){ // avoid duplicates due to lowercase-ing
					words.add(cur);
					last = cur;
				}
			}
		}
		return words;
	}
}
