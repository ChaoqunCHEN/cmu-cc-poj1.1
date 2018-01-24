package com.poj1_1.filter;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Filter {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		// read from file
		BufferedReader brRecord = new BufferedReader(
		        new InputStreamReader(new FileInputStream("pageviews-20161109-000000"), StandardCharsets.UTF_8));
		BufferedReader brBlack = new BufferedReader(
		        new InputStreamReader(new FileInputStream("BlackList"), StandardCharsets.UTF_8));
		List<String> blackList = new ArrayList<>();
		String line;
		while((line = brBlack.readLine()) != null) {
			blackList.add(line);
		}
		brBlack.close();
		String[] blackSuffix = {".png", ".gif", ".jpg", ".jpeg", ".tiff", ".tif", ".xcf", ".mid", ".ogg", 
				".ogv", ".svg", ".djvu", ".oga", ".flac", ".opus", ".wav", ".webm", ".ico", ".txt"};
		Map<String, Integer> validRecord = new HashMap<>();
		while((line = brRecord.readLine()) != null) {
			//orgList.add(line.split("\\s+"))
			//pre-process Regex matching
//			if(!line.startsWith("en ") && !line.startsWith("en.m ")) continue;			
//			String[] record = line.split("\\s+");
			// match en|en.m
			String domain;
			String title;
			int count;
			Pattern pattern = Pattern.compile("(en\\.m|en)(?=.*+)");
			Matcher matcher = pattern.matcher(line);
			if (matcher.find())
			{
			    domain = matcher.group(1);
			}
			else continue;
			// match title;
			pattern = Pattern.compile("(en\\.m|en)\\s+([a-zA-Z].+)(?=\\s+[1-9]\\d*\\s+[0-9]\\d*)");
			matcher = pattern.matcher(line);
			if (matcher.find())
			{
			    title = matcher.group(2);
			}			
			else continue;
			// match count
			pattern = Pattern.compile("\\s+(.+)\\s+([1-9]\\d*\\s+[0-9]\\d*)");
			matcher = pattern.matcher(line);
			if (matcher.find())
			{
			    count = Integer.parseInt(matcher.group(2).split("\\s")[0]);
			}
			else continue;
			// decode
			title = PercentDecoder.decode(title);
			// first lowercase char
			if(Character.isLetter(title.charAt(0)) && Character.isLowerCase(title.charAt(0))) continue;
			// special page
			if(title.equals("404.php")  || title.equals("Main_Page") || title.equals("-")) continue;
			// disambiguation
			String lowerTitle = title.toLowerCase();
			if(lowerTitle.endsWith("_(disambiguation)")) continue;
			// suffix
			Boolean hasBlackSuffix = false;
			for(String s:blackSuffix) {
				if(lowerTitle.endsWith(s)) {
					hasBlackSuffix = true;
					break;
				}
			}
			if(hasBlackSuffix) continue;
			// blackList
			Boolean inBlackList = false;
			for(String s:blackList)
			{
				if(lowerTitle.startsWith(s)) {
					inBlackList = true;
					break;
				}
			}
			if(inBlackList) continue;
			// finish validation
			if(validRecord.containsKey(title)) {
				validRecord.put(title, validRecord.get(title) + count);
			}
			else validRecord.put(title, count);
		}
		brRecord.close();
		// output to file"output"
		try {
			PrintWriter printWriter = new PrintWriter(new File("output"), "UTF-8");
			for(Map.Entry<String, Integer> en:validRecord.entrySet()) {
				printWriter.println(en.getKey() + " " + en.getValue());
			}
		}catch(IOException e){
			System.out.println(e);
		}	
	}

}

