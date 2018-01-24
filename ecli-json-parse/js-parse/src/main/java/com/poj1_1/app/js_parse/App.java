package com.poj1_1.app.js_parse;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException,UnsupportedEncodingException
    {
//        System.out.println( "Hello World!" );
	    BufferedReader br = new BufferedReader(
	        new InputStreamReader(new FileInputStream("api-result.json"), StandardCharsets.UTF_8));
	    Gson gson = new Gson();
	     Data data = gson.fromJson(br, Data.class);
	     ArrayList<String> domain = new ArrayList<String>();
	     for(Map.Entry<String, Map<String, String>> m:data.getQuery()
	    		 .getNamespaces().entrySet()) {
	    	 	if(!m.getKey().equals("0")) {
	    	 		domain.add(m.getValue().get("*"));
	    	 	}
	     }
	     PrintWriter out = new PrintWriter(
	    	        new OutputStreamWriter(System.out, "UTF-8"), true);
	     for(String s:domain) {
	    	 	
	    	 	out.println(s.replace(' ', '_').toLowerCase()+":");
	     }
//	     System.out.println(data.getQuery()
//	    		 .getNamespaces()
//	    		 .get("-2")
//	    		 .get("*"));
	}
}

class Data{
//	List<Query> queries = new ArrayList<Query>();
	String batchcomplete;
	Query query;
	Data(){}
	Query getQuery(){
		return query;
	}
	void set(Query q){
		query = q;
	}
}

class Query{
	String name;
	Map<String, Map<String, String>> namespaces;
	Map<String, Map<String, String>> getNamespaces(){
		return namespaces;
	}
	void set(String str, Map<String, String> m)
	{
		namespaces.put(str, m);
	}
}
