package core.DashboardAPI;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetSprintList {

	List<String> issueList = new ArrayList<String>();
	List<String> storyList = new ArrayList<String>();
	Multimap<String, String> storyTestCaseMap = ArrayListMultimap.create();
	Multimap<String, String> storyDefectMap = ArrayListMultimap.create();
	List<String> testCaseList = new ArrayList<String>();
	List<String> defectList = new ArrayList<String>();
	HashSet<String> componentSet=new HashSet<String>();  
	HashSet<String> labelSet=new HashSet<String>();  
	Multimap<String,String> IssueComponentMap = ArrayListMultimap.create();
	Multimap<String,String> IssueLabelMap = ArrayListMultimap.create();
	HashMap<String,String> testCaseExecuteStatusMap = new HashMap<String,String>();
	HashMap<String,String> testCaseExecutePriorityMap = new HashMap<String,String>();
	Multimap<String,String> ComponentTestCaseMap = ArrayListMultimap.create();
	Multimap<String,String> ComponentIssueMap = ArrayListMultimap.create();
	Multimap<String,String> LabelIssueMap = ArrayListMultimap.create();
	Multimap<String,String> PriorityTestCaseMap = ArrayListMultimap.create();
	Multimap<String,String> ExecuteStatusTestCaseMap = ArrayListMultimap.create();
	Multimap<String,String> BugStatusIssueMap = ArrayListMultimap.create();
	HashSet<String> BugStatusSet=new HashSet<String>();
	
	
	@Test
	public void Test2() {
		try {
			File file = new File("/Frameworks/GALE-Alchemy-AutomationTests/GALE-Alchemy-AutomationTests/src/test/resources/inputdata/jsonFiles/usercredentials.json");
			
			
			Header header = new Header("Authorization","Token a656b7c09cc87dcbb22e6d30e2316ca3f0f814ef25c9f6f602edc8b513d62b51");
			RequestSpecification requestSpec = given().relaxedHTTPSValidation().contentType(ContentType.JSON).body(file);
			Response response = requestSpec.when().post("https://alchemy-marketing-app-qa.gale.agency/api/auth/login/");
			//Response response = given().relaxedHTTPSValidation().spec(requestSpec).when().get("https://alchemy-marketing-app-qa.gale.agency/api/core/audience");
			String strResponse = response.then().extract().asString();
			System.out.println(from(strResponse).getString("token"));
		}
			catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	
	public void Test1() throws IOException {
		
		String response = given()
					.auth().preemptive().basic("chrisgale2020@gmail.com", "chrisgayle2020")
					.when().get("http://192.168.56.103:8080/rest/api/2/search?jql=project=\"AUT\"").then().extract().response().asString();
		
		GroupIssues(response);
		GroupIssuesIntoCategories();
		GroupTestCases();
		
		System.out.println("List of stories " + storyList);
		System.out.println("List of test cases " + testCaseList);
		System.out.println("List of defects " + defectList);
		System.out.println("Map of defect to a story " + storyDefectMap);
		System.out.println("Map of testcase to a story " + storyTestCaseMap);
		//System.out.println("List of components " + componentSet);
		//System.out.println("Map of issues to a components " + IssueComponentMap);
		//System.out.println("List of labels "+ labelSet);
		//System.out.println("Map of issues to a label " + IssueLabelMap);
		//System.out.println("Map of testcase to status " + testCaseExecuteStatusMap);
		//System.out.println("Map of testcase to priority " + testCaseExecutePriorityMap);
		System.out.println("Map of component to issue " + ComponentIssueMap);
		System.out.println("Map of priority to testcase " + PriorityTestCaseMap);
		System.out.println("Map of execution status to testcase " + ExecuteStatusTestCaseMap);
		System.out.println("Map of issues to a label " + LabelIssueMap);
		System.out.println("Defect Status List " + BugStatusSet);
		System.out.println("Map of issues to a Status " + BugStatusIssueMap);
		
		
		
  }
	
	public void GroupTestCases() {
		for(String testcase:testCaseList) {
			String response = given()
					.auth().preemptive().basic("chrisgale2020@gmail.com", "chrisgayle2020")
					.when().get("http://192.168.56.103:8080/rest/api/latest/issue/" + testcase).then().extract().response().asString();
			
			String TestCasePriority = from(response).getString("fields.priority.name");
			String TestCaseExecuteStatus = from(response).getString("fields.customfield_10201.value");
			testCaseExecuteStatusMap.put(testcase, TestCaseExecuteStatus);
			ExecuteStatusTestCaseMap.put(TestCaseExecuteStatus, testcase);
			testCaseExecutePriorityMap.put(testcase, TestCasePriority);
			PriorityTestCaseMap.put(TestCasePriority, testcase);
		}
	}
	
	public void GroupIssuesIntoCategories(){
		for(String issue:issueList) {
			String response = given()
					.auth().preemptive().basic("chrisgale2020@gmail.com", "chrisgayle2020")
					.when().get("http://192.168.56.103:8080/rest/api/latest/issue/" + issue).then().extract().response().asString();
			
			List<String> subTaskList = from(response).getList("fields.subtasks.key");
			List<String> subTaskIssueTypeList = from(response).getList("fields.subtasks.fields.issuetype.name");
			List<String> components= from(response).getList("fields.components.name");
			List<String> labels= from(response).getList("fields.labels");
			String defectStatus= from(response).getString("fields.status.name");
			
				for(int i=0;i<=subTaskList.size()-1;i++) {
					if(subTaskIssueTypeList.get(i).equalsIgnoreCase("Test Case Sub Task")) {
						storyTestCaseMap.put(issue, subTaskList.get(i));
					}
					else if (subTaskIssueTypeList.get(i).equalsIgnoreCase("Bug Sub Task"))
						storyDefectMap.put(issue, subTaskList.get(i));
				}
				
			
			for(String component:components) {
				if(component==null || component.isEmpty()) {
					component = "blank";
					componentSet.add(component);
				}
					
				else
					
				IssueComponentMap.put(issue, component);
				ComponentIssueMap.put(component, issue);
			}
			
			
			for(String label:labels) {
				if(null!=label)
					labelSet.add(label);
				IssueLabelMap.put(issue, label);
				LabelIssueMap.put(label, issue);
			}
			
			
			BugStatusSet.add(defectStatus);
			BugStatusIssueMap.put(defectStatus, issue);
		}
	}

	

	public void GroupIssues(String response){
		
		issueList = from(response).getList("issues.key");
		List<String> issueTypeList = from(response).getList("issues.fields.issuetype.name");
		
		for(int i=0;i<=issueTypeList.size()-1;i++) {
			if(issueTypeList.get(i).equalsIgnoreCase("story"))
				storyList.add(issueList.get(i).toString());
			else if(issueTypeList.get(i).equalsIgnoreCase("Test Case Sub Task"))
				testCaseList.add(issueList.get(i).toString());
			else if(issueTypeList.get(i).equalsIgnoreCase("Bug Sub Task"))
				defectList.add(issueList.get(i).toString());
		}
		
	}
	
	
	
	
	
}
