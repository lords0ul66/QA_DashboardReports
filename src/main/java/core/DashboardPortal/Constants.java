package core.DashboardPortal;

public final class Constants {
	
	public static final String STATIC_CONTENT1 = "<html>\n" + 
			"  <head>\n" + 
			"    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" + 
			"    <script type=\"text/javascript\">\n" + 
			"      google.charts.load('current', {'packages':['##ChartType##']});\n" + 
			"      google.charts.setOnLoadCallback(drawVisualization);\n" + 
			"\n" + 
			"      function drawVisualization() {\n";
	
	public static final String STATIC_CONTENT2 = "var chart = new google.visualization.##ChartType##(document.getElementById('chart_div'));\n" + 
			"    chart.draw(data, options);";
	
	
	public static final String STATIC_CONTENT3 = "<!DOCTYPE html>\n" + 
			"<html lang=\"en\">\n" + 
			"  <head>\n" + 
			"    <meta charset=\"utf-8\">\n" + 
			"    <title>GALE QA DASHBOARD</title>\n" + 
			"    <link rel=\"stylesheet\" href=\"common.css\">\n" + 
			"  </head>\n" + 
			"  <body>\n" + 
			"   <table cellpadding=\"10\" style=\"width:100%\">\n" + 
			"    <div>";
	
	public static final String STATIC_CONTENT4 = "}\n" + 
			"    </script>\n" + 
			"  </head>\n" + 
			"  <body>";
	
	public static final String STATIC_CONTENT5 = "</body>\n" + 
			"</html>";
	
}
