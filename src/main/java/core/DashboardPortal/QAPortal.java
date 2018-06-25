package core.DashboardPortal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class QAPortal {

	public static void main(String[] args) {
		WebDriver driver = null;
		try {
			System.setProperty("webdriver.chrome.driver","/Frameworks/GALE-Alchemy-AutomationTests/GALE-Alchemy-AutomationTests/src/main/resources/Drivers/chromedriver");
			driver = new ChromeDriver();
			driver.get("file:///Users/deepaktiwari/eclipse-workspace/QA_DashBoard/DashBoardReports/QADashBoardReports.html");
		}catch(Exception e) {
			driver.close();
			driver.quit();
		}
	}
}
