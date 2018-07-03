package core.DashboardAPI;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

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
	
	
	public static void GenerateDefectBreakdownByReleaseReport(String[][] arr) throws IOException {
		
		Collection<String> lines = new ArrayList<String>();
		lines.add(Constants.STATIC_CONTENT1.replace("##ChartType##", "corechart"));
		String staticContent1 = "var data = google.visualization.arrayToDataTable([";
		lines.add(staticContent1);
		String dynamicContent = "[";
		for(int x=0;x<arr.length;x++) {
	    	for(int y=0;y<arr[x].length;y++) {
	    		
	    		try
	    		{
	    		     NumberFormat.getInstance().parse(arr[x][y]);
	    		     dynamicContent = dynamicContent + arr[x][y] + ",";
	    		}
	    		catch(ParseException e)
	    		{
	    			dynamicContent = dynamicContent + "'" + arr[x][y] + "',";
	    		}
	    		
	    			if(y==arr[x].length-1)
		    			dynamicContent = dynamicContent + "]\n";
	    	}
	    	if(x==arr.length-1)
	    		dynamicContent = dynamicContent;
	    	else
	    		dynamicContent = dynamicContent + "[";
	    }
		
		dynamicContent = dynamicContent.replaceAll(",]", "],");
		lines.add(dynamicContent + "]);");
				
		String staticContent3 = "		]);";
		lines.add("var options = {\n" + 
				"      title : 'Defect breakdown by Releases',\n" + 
				"	   is3D	 : true,\n"+
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
	
	
	public static void GenerateDefectBreakdownByComponentReport(String[][] arr) throws IOException {
		
		Collection<String> lines = new ArrayList<String>();
		lines.add(Constants.STATIC_CONTENT1.replace("##ChartType##", "corechart"));
		String staticContent1 = "var data = google.visualization.arrayToDataTable([";
		lines.add(staticContent1);
		String dynamicContent = "[";
		for(int x=0;x<arr.length;x++) {
	    	for(int y=0;y<arr[x].length;y++) {
	    		
	    		try
	    		{
	    		     NumberFormat.getInstance().parse(arr[x][y]);
	    		     dynamicContent = dynamicContent + arr[x][y] + ",";
	    		}
	    		catch(ParseException e)
	    		{
	    			dynamicContent = dynamicContent + "'" + arr[x][y] + "',";
	    		}
	    		
	    			if(y==arr[x].length-1)
		    			dynamicContent = dynamicContent + "]\n";
	    	}
	    	if(x==arr.length-1)
	    		dynamicContent = dynamicContent;
	    	else
	    		dynamicContent = dynamicContent + "[";
	    }
		
		dynamicContent = dynamicContent.replaceAll(",]", "],");
		lines.add(dynamicContent + "]);");
				
		String staticContent3 = "		]);";
		lines.add("var options = {\n" + 
				"      title : 'Defect breakdown by component',\n" + 
				"	   is3D	 : true,\n"+
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
	
	
	public static void GenerateDefectPriorityPieChartReport(Multimap<String,String> PriorityDefectMap) throws IOException {
		String dynamicContent= "";
		Collection<String> lines = new ArrayList<String>();
		lines.add(Constants.STATIC_CONTENT1.replace("##ChartType##", "corechart"));
		String staticContent1 = "var data = google.visualization.arrayToDataTable([\n" + 
		                        "  ['Defect', 'Priority'],\n";  
				Set keySet = new HashSet();
				keySet = PriorityDefectMap.keySet();
			    Iterator keyIterator = keySet.iterator();
			    while (keyIterator.hasNext() ) {
			        String priority = (String) keyIterator.next();
			        List<String> defects = (List<String>) PriorityDefectMap.get(priority);
			        dynamicContent = dynamicContent + "  ['" + priority + "' ," + defects.size() + "],\n";
			    }


		String staticContent2 = "]);";
		lines.add(staticContent1+dynamicContent+staticContent2);
		lines.add("var options = {'title':'Defects grouped based on Priority', is3D: true, 'width':600, 'height':600};");
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
