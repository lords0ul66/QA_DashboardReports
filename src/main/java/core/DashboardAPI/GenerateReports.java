package core.DashboardAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.collect.Multimap;

import core.DashboardPortal.Constants;

public class GenerateReports {
	
	
	public static void GenerateTraceabilityMatrixReport(Multimap<String,String> ComponentStoryMap, Multimap<String,String> storyTestCaseMap, Multimap<String,String> storyDefectMap) throws IOException {
		Collection<String> lines = new ArrayList<String>();
		lines.add(Constants.STATIC_CONTENT1.replace("##ChartType##", "table"));
		lines.add("var data = new google.visualization.DataTable();");
		lines.add("data.addColumn('string', 'Component');\n" + 
				"        data.addColumn('string', 'Story ID');\n" + 
				"        data.addColumn('string', 'Test Case ID');\n" + 
				"        data.addColumn('string', 'Defect ID');\n" + 
				"        data.addColumn('string', 'Health Status');\n" + 
				"        data.addRows([\n" );
				
				Set keySet = new HashSet();
				keySet = ComponentStoryMap.keySet();
			    Iterator keyIterator = keySet.iterator();
			    while (keyIterator.hasNext() ) {
			        String key = (String) keyIterator.next();
			        List<String> stories = (List<String>) ComponentStoryMap.get(key);
			        for(String story: stories) {
			         String healthstatus = "Good";
			        	
			        List<String> testcases = (List<String>) storyTestCaseMap.get(story);
			        String testCases = String.join(",", testcases);
			        if(testcases.isEmpty()) {
			        	testCases = "";
			        	healthstatus = "Not Good";
			        }
			        List<String> defects = (List<String>) storyDefectMap.get(story);
			        String Defects = String.join(",", defects);
			        if(defects.isEmpty())
			        	Defects = "";
			        
			        lines.add("          ['" + key + "' , '" +  story + "' ,'" + testCases + "', '" + Defects + "', '" + healthstatus + "'],");  
			        }
	    }
			 			
			    lines.add("        ]);");
		lines.add("var table = new google.visualization.Table(document.getElementById('chart_div'));\n" + 
				"\n" + 
				"        table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});");
		
		
		lines.add(Constants.STATIC_CONTENT4);
		lines.add(" <div id=\"chart_div\"></div>");
		lines.add(Constants.STATIC_CONTENT5);
		File htmlTemplateFile = new File(System.getProperty("user.dir") + "/DashBoardReports/TraceabilityMatrixReport.html");
		FileUtils.writeLines(htmlTemplateFile, lines);
	}
	
	@Test
	public static void GenerateDefectBreakdownByReleaseReport() throws IOException {
		Collection<String> lines = new ArrayList<String>();
		lines.add(Constants.STATIC_CONTENT1.replace("##ChartType##", "corechart"));
		lines.add("var data = google.visualization.arrayToDataTable([\n" + 
				"         ['Release', 'P0', 'P1', 'P2', 'P3', 'P4'],\n" + 
				"         ['Sanity',    20,      12,         12,             32,           11],\n" + 
				"         ['Regression',    5,      23,        2,             22,          22],\n" + 
				"         ['Release 1',  10,      22,        44,             11,           39],\n" + 
				"         ['Release 2',  28,      33,        23,             16,           21],\n" + 
				"         ['Release 3',  44,      22,         21,             10,          36]\n" + 
				"      ]);");
		lines.add("var options = {\n" + 
				"      title : 'Defect breakdown by releases',\n" + 
				"      vAxis: {title: 'Defect Count'},\n" + 
				"      hAxis: {title: 'Releases'},\n" + 
				"      seriesType: 'bars',\n" + 
				"      series: {5: {type: 'line'}}\n" + 
				"    };");
		lines.add(Constants.STATIC_CONTENT2.replace("##ChartType##", "ComboChart"));
		lines.add(Constants.STATIC_CONTENT4);
		lines.add("<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>");
		lines.add(Constants.STATIC_CONTENT5);
		
		File htmlTemplateFile = new File(System.getProperty("user.dir") + "/DashBoardReports/DefectBreakdownByReleaseReport.html");
		FileUtils.writeLines(htmlTemplateFile, lines);
	}
	
	@Test
	public static void GenerateDefectBreakdownByComponentReport() throws IOException {
		Collection<String> lines = new ArrayList<String>();
		lines.add(Constants.STATIC_CONTENT1.replace("##ChartType##", "corechart"));
		lines.add("var data = google.visualization.arrayToDataTable(\n" + 
				"          [\n" + 
				"         ['Component', 'P0', 'P1', 'P2', 'P3', 'P4'],\n" + 
				"         ['Login',  20,      12,         12,             32,           11],\n" + 
				"         ['Inbox',  5,      23,        2,             22,          22],\n" + 
				"         ['Sent Items',  10,      22,        44,             11,           39],\n" + 
				"         ['Module 1',  28,      33,        23,             16,           21],\n" + 
				"         ['Module 2',  44,      22,         21,             10,          36]\n" + 
				"      ]);");
		
		lines.add("var options = {\n" + 
				"      title : 'Defect breakdown by component',\n" + 
				"      vAxis: {title: 'Defect Count'},\n" + 
				"      hAxis: {title: 'Components'},\n" + 
				"      seriesType: 'bars',\n" + 
				"      series: {5: {type: 'line'}}\n" + 
				"    };");
		lines.add(Constants.STATIC_CONTENT2.replace("##ChartType##", "ComboChart"));
		lines.add(Constants.STATIC_CONTENT4);
		lines.add("<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>");
		lines.add(Constants.STATIC_CONTENT5);
		
		File htmlTemplateFile = new File(System.getProperty("user.dir") + "/DashBoardReports/DefectBreakdownByComponentReport.html");
		FileUtils.writeLines(htmlTemplateFile, lines);
	}
	
	@Test
	public static void GenerateDefectPriorityPieChartReport() throws IOException {
		Collection<String> lines = new ArrayList<String>();
		lines.add(Constants.STATIC_CONTENT1.replace("##ChartType##", "corechart"));
		lines.add("var data = google.visualization.arrayToDataTable([\n" + 
				"  ['Defect', 'Priority'],\n" + 
				"  ['P0', 5],\n" + 
				"  ['P1', 5],\n" + 
				"  ['P2', 2],\n" + 
				"  ['P3', 3],\n" + 
				"  ['P4', 8]\n" + 
				"]);");
		lines.add("var options = {'title':'Defects grouped based on Priority', 'width':400, 'height':300};");
		lines.add(Constants.STATIC_CONTENT2.replace("##ChartType##", "PieChart"));
		lines.add(Constants.STATIC_CONTENT4);
		lines.add("<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>");
		lines.add(Constants.STATIC_CONTENT5);
		
		
		File htmlTemplateFile = new File(System.getProperty("user.dir") + "/DashBoardReports/DefectPriorityPieChartReport.html");
		FileUtils.writeLines(htmlTemplateFile, lines);
	}
	
	public static void GenerateTestCaseStatusSummaryReport() throws IOException {
		Collection<String> lines = new ArrayList<String>();
		
		
		File htmlTemplateFile = new File(System.getProperty("user.dir") + "/DashBoardReports/TestCaseStatusSummaryReport.html");
		FileUtils.writeLines(htmlTemplateFile, lines);
	}
	
	public static void GenerateTestCaseStatusReport() throws IOException {
		Collection<String> lines = new ArrayList<String>();
		lines.add("abcd");
		File htmlTemplateFile = new File(System.getProperty("user.dir") + "/DashBoardReports/TestCaseStatusReport.html");
		FileUtils.writeLines(htmlTemplateFile, lines);
	}
	
	
	
	public static void GenerateDefectStatusReport() throws IOException {
		Collection<String> lines = new ArrayList<String>();
		lines.add("abcd");
		File htmlTemplateFile = new File(System.getProperty("user.dir") + "/DashBoardReports/DefectStatusReport.html");
		FileUtils.writeLines(htmlTemplateFile, lines);
	}
	
	
	
	

}
