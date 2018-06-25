package core.DashboardAPI;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class GroupIssues {

	private static final String jiraProjectID = "AUT" ;
	static List<String> issueList = new ArrayList<String>();
	List<String> storyList = new ArrayList<String>();
	static Multimap<String, String> storyTestCaseMap = ArrayListMultimap.create();
	static Multimap<String, String> storyDefectMap = ArrayListMultimap.create();
	List<String> testCaseList = new ArrayList<String>();
	List<String> defectList = new ArrayList<String>();
	static HashSet<String> componentSet=new HashSet<String>();  
	static HashSet<String> labelSet=new HashSet<String>();  
	
	Multimap<String,String> IssueLabelMap = ArrayListMultimap.create();
	HashMap<String,String> testCaseExecuteStatusMap = new HashMap<String,String>();
	HashMap<String,String> testCaseExecutePriorityMap = new HashMap<String,String>();
	
	static Multimap<String,String> ComponentStoryMap = ArrayListMultimap.create();
	static Multimap<String,String> ComponentTestCaseMap = ArrayListMultimap.create();
	static Multimap<String,String> componenDefectMap = ArrayListMultimap.create();
	
	static Multimap<String,String> labelStoryMap = ArrayListMultimap.create();
	static Multimap<String,String> labelTestCaseMap = ArrayListMultimap.create();
	static Multimap<String,String> labelDefectMap = ArrayListMultimap.create();
	
	Multimap<String,String> PriorityTestCaseMap = ArrayListMultimap.create();
	Multimap<String,String> ExecuteStatusTestCaseMap = ArrayListMultimap.create();
	Multimap<String,String> BugStatusIssueMap = ArrayListMultimap.create();
	HashSet<String> BugStatusSet=new HashSet<String>();
	
	@Test
	public void MainEngine() throws IOException {
		
		String response = given()
				.auth().preemptive().basic("chrisgale2020@gmail.com", "chrisgayle2020")
				.when().get("http://192.168.56.103:8080/rest/api/2/search?jql=project=" + jiraProjectID  + "&startAt=0&maxResults=100&fields=key").then().extract().response().asString();
		
		issueList = from(response).getList("issues.key");
		
		for(String issue:issueList) {
			response = given()
					.auth().preemptive().basic("chrisgale2020@gmail.com", "chrisgayle2020")
					.when().get("http://192.168.56.103:8080/rest/api/latest/issue/" + issue).then().extract().response().asString();
			
			String issueType = from(response).getString("fields.issuetype.name");
			List<String> subTaskList = from(response).getList("fields.subtasks.key");
			List<String> subTaskIssueTypeList = from(response).getList("fields.subtasks.fields.issuetype.name");
			
			List<String> labels= from(response).getList("fields.labels");

	        CreateComponentIssueMap(response, issue);
	        CreateLabelIssueMap(response, issue);
	        CreateStoryTestCaseMap(response, issue);
	        
	        GenerateReports.GenerateTraceabilityMatrixReport(ComponentStoryMap, storyTestCaseMap, storyDefectMap);
		}
		
//		System.out.println(componentSet);
//        System.out.println(ComponentStoryMap);
//        System.out.println(componenDefectMap);
//        System.out.println(ComponentTestCaseMap);
//        System.out.println(labelSet);
//        System.out.println(labelStoryMap);
//        System.out.println(labelDefectMap);
//        System.out.println(labelTestCaseMap);
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
		
		for(String component:components) {
			if(component==null || component.isEmpty())
				component = "blank";
			
			componentSet.add(component);
			
			switch (issueType) {
        	
	        case "Story":
	        	ComponentStoryMap.put(component, issue);
	            break;
	        
	        case "Bug":
	        	componenDefectMap.put(component, issue);
	            break;
	        
	        case "Test Case Sub Task":
	        	ComponentTestCaseMap.put(component, issue);
	            break;
	        
	        case "Bug Sub Task":
	        	componenDefectMap.put(component, issue);
	            break;
	   
			}
			
		}
		
	}
	
	public static void CreateLabelIssueMap(String response, String issue) {
		List<String> labels= from(response).getList("fields.labels");
		String issueType = from(response).getString("fields.issuetype.name");
		
		for(String label:labels) {
			if(label==null || label.isEmpty())
				label = "blank";
			
			labelSet.add(label);
			switch (issueType) {
        	
	        case "Story":
	        	labelStoryMap.put(label, issue);
	            break;
	        
	        case "Bug":
	        	labelDefectMap.put(label, issue);
	            break;
	        
	        case "Test Case Sub Task":
	        	labelTestCaseMap.put(label, issue);
	            break;
	        
	        case "Bug Sub Task":
	        	labelDefectMap.put(label, issue);
	            break;
	   
			}
			
		}
		
	}
}
