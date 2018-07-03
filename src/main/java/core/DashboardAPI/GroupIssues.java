package core.DashboardAPI;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class GroupIssues {

	private static final String jiraProjectID = "AUT" ;
	private static final String baseURI = "http://192.168.56.103:8080" ;
	static List<String> issueList = new ArrayList<String>();
	List<String> storyList = new ArrayList<String>();
	static Multimap<String, String> storyTestCaseMap = ArrayListMultimap.create();
	static Multimap<String, String> storyDefectMap = ArrayListMultimap.create();
	static List<String> storyCaseList = new ArrayList<String>();
	static List<String> testCaseList = new ArrayList<String>();
	static List<String> defectList = new ArrayList<String>();
	static HashSet<String> componentSet=new HashSet<String>();  
	static HashSet<String> labelSet=new HashSet<String>(); 
	static HashSet<String> prioritySet=new HashSet<String>();  
	
	Multimap<String,String> IssueLabelMap = ArrayListMultimap.create();
	HashMap<String,String> testCaseExecuteStatusMap = new HashMap<String,String>();
	HashMap<String,String> testCaseExecutePriorityMap = new HashMap<String,String>();
	
	static Multimap<String,String> ComponentStoryMap = ArrayListMultimap.create();
	static Multimap<String,String> ComponentTestCaseMap = ArrayListMultimap.create();
	static Multimap<String,String> componenDefectMap = ArrayListMultimap.create();
	static Multimap<String,String> componenPriorityDefectMap = ArrayListMultimap.create();
	
	static Multimap<String,String> labelStoryMap = ArrayListMultimap.create();
	static Multimap<String,String> labelTestCaseMap = ArrayListMultimap.create();
	static Multimap<String,String> labelDefectMap = ArrayListMultimap.create();
	static Multimap<String,String> labelPriorityDefectMap = ArrayListMultimap.create();
	
	Multimap<String,String> PriorityTestCaseMap = ArrayListMultimap.create();
	static Multimap<String,String> PriorityDefectMap = ArrayListMultimap.create();
	Multimap<String,String> ExecuteStatusTestCaseMap = ArrayListMultimap.create();
	Multimap<String,String> BugStatusIssueMap = ArrayListMultimap.create();
	HashSet<String> BugStatusSet=new HashSet<String>();
	
	static RequestSpecification requestSpec = null;
	static String[][] compArr;
	static String[][] labelArr;
	
	@Test
	public void MainEngine() throws IOException {
		
		RequestSpecBuilder builder = new RequestSpecBuilder();
		requestSpec = builder.build();
		requestSpec.auth().preemptive().basic("chrisgale2020@gmail.com", "chrisgayle2020");

		String response = given().spec(requestSpec).when().get(baseURI + "/rest/api/2/search?jql=project=" + jiraProjectID  + "&startAt=0&maxResults=100&fields=key").then().extract().response().asString();
		issueList = from(response).getList("issues.key");
		
		for(String issue:issueList) {
			response = given().spec(requestSpec).when().get(baseURI + "/rest/api/latest/issue/" + issue).then().extract().response().asString();
			
	        CreateComponentIssueMap(response, issue);
	        CreateLabelIssueMap(response, issue);
	        CreateStoryTestCaseMap(response, issue);
	        CreateDefectPriorityMap(response, issue);
	        
		}
		
		
//		  System.out.println(componentSet);
//        System.out.println(ComponentStoryMap);
//        System.out.println(componenDefectMap);
//        System.out.println(ComponentTestCaseMap);
//        System.out.println(labelSet);
//        System.out.println(labelStoryMap);
//        System.out.println(labelDefectMap);
//        System.out.println(labelTestCaseMap);
//        System.out.println(prioritySet);
//        System.out.println(PriorityDefectMap);
		
		System.out.println(componenPriorityDefectMap.toString());
		CreateComponentPriorityHashMap(componenPriorityDefectMap);
		CreateLabelPriorityHashMap(labelPriorityDefectMap);
		GenerateReports.GenerateTraceabilityMatrixReport(ComponentStoryMap, storyTestCaseMap, storyDefectMap);
        GenerateReports.GenerateDefectPriorityPieChartReport(PriorityDefectMap);
        GenerateReports.GenerateDefectBreakdownByComponentReport(compArr);
        GenerateReports.GenerateDefectBreakdownByReleaseReport(labelArr);
	}
	
	
	public static void CreateComponentPriorityHashMap(Multimap<String,String> componenPriorityDefectMap) {
		TreeMap<String, Integer> sortedMap = new TreeMap<>();
		HashMap<String,Integer> issueCountMap = new HashMap<String,Integer>();
		
		Set keySet = new HashSet();
		keySet = componenPriorityDefectMap.keySet();
	    Iterator keyIterator = keySet.iterator();
	    while (keyIterator.hasNext() ) {
	        String key = (String) keyIterator.next();
	        int IssueCount = componenPriorityDefectMap.get(key).size();
	        issueCountMap.put(key.replace("[", "").replace("]", ""), IssueCount);
	    }
	    sortedMap.putAll(issueCountMap);
	    int i=1;
	    int j=1;
	    compArr = new String[componentSet.size()+1][prioritySet.size()+1];
	    for(String component:componentSet) {
	    	j=1;
	    	for(String priority:prioritySet) {
	    		if(null!=issueCountMap.get(component+ "_" + priority)) 
	    			compArr[i][j] = Integer.toString(issueCountMap.get(component+ "_" + priority));
	    		else
	    			compArr[i][j] = "0";
	    		j++;
	    	}
	    	i++;
	    }
	    
	    int p=1;
	    for(String component:componentSet) {
	    	compArr[p][0] = component;
	    	p++;
	    }
	    
	    p=1;
	    for(String priority:prioritySet) {
	    	compArr[0][p] = priority;
	    	p++;
	    }
	    compArr[0][0] = "Component";
	    
	}
	
	
	public static void CreateLabelPriorityHashMap(Multimap<String,String> labelPriorityDefectMap) {
		TreeMap<String, Integer> sortedMap = new TreeMap<>();
		HashMap<String,Integer> issueCountMap = new HashMap<String,Integer>();
		
		Set keySet = new HashSet();
		keySet = labelPriorityDefectMap.keySet();
	    Iterator keyIterator = keySet.iterator();
	    while (keyIterator.hasNext() ) {
	        String key = (String) keyIterator.next();
	        int IssueCount = labelPriorityDefectMap.get(key).size();
	        issueCountMap.put(key.replace("[", "").replace("]", ""), IssueCount);
	    }
	    sortedMap.putAll(issueCountMap);
	    int i=1;
	    int j=1;
	    labelArr = new String[labelSet.size()+1][prioritySet.size()+1];
	    for(String label:labelSet) {
	    	j=1;
	    	for(String priority:prioritySet) {
	    		if(null!=issueCountMap.get(label+ "_" + priority)) 
	    			labelArr[i][j] = Integer.toString(issueCountMap.get(label+ "_" + priority));
	    		else
	    			labelArr[i][j] = "0";
	    		j++;
	    	}
	    	i++;
	    }
	    
	    int p=1;
	    for(String label:labelSet) {
	    	labelArr[p][0] = label;
	    	p++;
	    }
	    
	    p=1;
	    for(String priority:prioritySet) {
	    	labelArr[0][p] = priority;
	    	p++;
	    }
	    labelArr[0][0] = "Label";
	    
	}
			
	public static void CreateStoryTestCaseMap(String response, String issue) {
		List<String> subTaskList = from(response).getList("fields.subtasks.key");
		List<String> subTaskIssueTypeList = from(response).getList("fields.subtasks.fields.issuetype.name");
		
			for(int i=0;i<=subTaskList.size()-1;i++) {
				if(subTaskIssueTypeList.get(i).equalsIgnoreCase("Test Case Sub Task")) {
					storyTestCaseMap.put(issue, subTaskList.get(i));
				}
				else if (subTaskIssueTypeList.get(i).equalsIgnoreCase("Bug Sub Task"))
					storyDefectMap.put(issue, subTaskList.get(i));
			}
	}
	
	public static void CreateComponentIssueMap(String response, String issue) {
		List<String> components= from(response).getList("fields.components.name");
		
		String issueType = from(response).getString("fields.issuetype.name");
		String priority = from(response).getString("fields.priority.name");
		
		String Parentcomponent = null;
		if(components.isEmpty())
			components.add("None");
		
		for(String component:components) {
			
			componentSet.add(component);
			
			switch (issueType) {
        	
	        case "Story":
	        	storyCaseList.add(issue);
	        	ComponentStoryMap.put(component, issue);
	            break;
	        
	        case "Bug":
	        	defectList.add(issue);
	        	componenDefectMap.put(component, issue);
	        	componenPriorityDefectMap.put(component + "_" + priority, issue);
	            break;
	            
	        case "Bug Sub Task":
	        		defectList.add(issue);
	        		Parentcomponent = null;
	        		Parentcomponent = GetParentComponent(response);
	        		if(null!=Parentcomponent) {
	        			componenDefectMap.put(Parentcomponent, issue);
	        			componenPriorityDefectMap.put(Parentcomponent + "_" + priority, issue);
	        		}
	        		else {
	        			componenDefectMap.put(component, issue);
	        			componenPriorityDefectMap.put(component + "_" + priority, issue);
	        		}
	            break;
	        
	        case "Test Case Sub Task":
	        	testCaseList.add(issue);
	        	Parentcomponent = null;
	        	Parentcomponent = GetParentComponent(response);
        		if(null!=Parentcomponent)
        			ComponentTestCaseMap.put(Parentcomponent, issue);
        		else
        			ComponentTestCaseMap.put(component, issue);
	            break;
			}
		}
	}
	
	public static String GetParentComponent(String response) {
		String ParentKeyID = from(response).getString("fields.parent.key");
		String tempresponse  = given().spec(requestSpec).when().get(baseURI + "/rest/api/latest/issue/" + ParentKeyID + "?fields=components").then().extract().response().asString();
		if(null!=from(tempresponse).getString("fields.components.name"))
			return from(tempresponse).getString("fields.components.name");
		else 
			return null;
	}
	
	
	
	public static void CreateLabelIssueMap(String response, String issue) {
		List<String> labels= from(response).getList("fields.labels");
		String issueType = from(response).getString("fields.issuetype.name");
		String priority = from(response).getString("fields.priority.name");
		
		if(labels.isEmpty())
			labels.add("None");
		
		for(String label:labels) {
			
			labelSet.add(label);
			
			switch (issueType) {
        	
	        case "Story":
	        	labelStoryMap.put(label, issue);
	            break;
	        
	        case "Bug":
	        	labelDefectMap.put(label, issue);
	        	labelPriorityDefectMap.put(label + "_" + priority, issue);
	            break;
	        
	        case "Test Case Sub Task":
	        	labelTestCaseMap.put(label, issue);

	            break;
	        
	        case "Bug Sub Task":
	        	labelDefectMap.put(label, issue);
	        	labelPriorityDefectMap.put(label + "_" + priority, issue);
	            break;
			}
		}
	}
	
	public static void CreateDefectPriorityMap(String response, String issue) {
		
		String priority= from(response).getString("fields.priority.name");
		String issueType = from(response).getString("fields.issuetype.name");
		if(priority==null || priority.isEmpty())
			priority = "NoPrioritySet";
		
		prioritySet.add(priority);
		switch (issueType) {
			case "Bug":
        	case "Bug Sub Task":
        		PriorityDefectMap.put(priority, issue);
            break;
		}
	}
		
	

	
}
