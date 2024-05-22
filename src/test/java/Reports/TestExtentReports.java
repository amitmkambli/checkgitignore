package Reports;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestExtentReports {
	
	public ExtentSparkReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;
	public WebDriver driver;
	
	@BeforeTest
	public void setReport() {
		//will create report where we mention path in the root directory of project
		htmlReporter = new ExtentSparkReporter("./reports/extent.html");
		
		 htmlReporter.config().setTheme(Theme.STANDARD);
	        htmlReporter.config().setDocumentTitle("Test Document Title");
	        htmlReporter.config().setEncoding("utf-8");
	        htmlReporter.config().setReportName("Test Report Name");
	        
	        extent = new ExtentReports();
	        extent.attachReporter(htmlReporter);
	        // we can configure additional info
	        extent.setSystemInfo("Automation Tester", "Rahul Arora");
	        extent.setSystemInfo("Organization", "Way2Automation");
	        extent.setSystemInfo("Build no", "W2A-1234");
	        
	        driver = new ChromeDriver();
		
	}
	
	@AfterTest
	public void endReport() {
		extent.flush();
		driver.quit();
	}
	
	@Test
	public void login() {
		test = extent.createTest("Login Test");
		System.out.println("Executing Login Test");
	}
	
	@Test
	public void doUserRegistration() {
		test = extent.createTest("User Registration Test");
		Assert.fail("User Registration Test failed..");
	}
	
	@Test
	public void isSkip() {
		test = extent.createTest("Skipped Test");
		throw new SkipException("Skipping the test");
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {
		if(result.getStatus() == ITestResult.FAILURE ) {
			String excepionMessage = Arrays.toString(result.getThrowable().getStackTrace());
			test.fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
					+ "</font>" + "</b >" + "</summary>" + excepionMessage.replaceAll(",", "<br>") + "</details>"
					+ " \n");
			
			try {

				//ExtentManager.captureScreenshot();
				
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

				Date d = new Date();
				//String screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
				String screenshotName = "errorscreenshot.jpg";
				//(source , target)
				FileUtils.copyFile(scrFile,new File(System.getProperty("user.dir") + "\\reports\\" + screenshotName));
				
				test.fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotName)
								.build());
			} catch (Exception e) {

			}
			
			String failureLog  = "Test Failed";   //check with result.getTestName();
			//String logText = "<b>" + "Test Case : " + methodName.toUpperCase() + "Failed.</b>";
			Markup m = MarkupHelper.createLabel(failureLog, ExtentColor.RED);
			test.log(Status.FAIL, m);
			
		} else if(result.getStatus() == ITestResult.SKIP ) {
			
			String methodName = result.getMethod().getMethodName();   //check with result.getTestName();
			String logText = "<b>" + "Test Case : " + methodName.toUpperCase() + "Skipped.</b>";
			Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
			test.skip(m);
			
		} else if(result.getStatus() == ITestResult.SUCCESS ) {
			String methodName = result.getMethod().getMethodName();   //check with result.getTestName();
			String logText = "<b>" + "Test Case : " + methodName.toUpperCase() + "Passed.</b>";
			Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
			test.pass(m);
			
		}
	}
}
