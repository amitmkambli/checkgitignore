package Reports;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

public class DemoParallelTest {
	//https://medium.com/@arunangshu.podder/parallel-execution-in-selenium-using-threadlocal-boosting-test-efficiency-dfb35b59dd03
	//https://www.linkedin.com/pulse/selenium-parallel-testing-using-java-threadlocal-testng-shargo
	//https://medium.com/@sidharth.shukla19/implement-multithreading-in-test-automation-selenium-3b87edfe6b39
	// Check playlist : https://www.testingminibytes.com/courses/design-advanced-selenium-automation-framework-with-real-time-dashboard/fixing-parallel-execution-issues-in-selenium--thank-you-thread-local
	protected ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();	
	
	//can be kept in base class and test class can extend base class
	@BeforeMethod(alwaysRun=true) 
	@Parameters("browser")
	public void beforemethod(String browser) {
		//WebDriver driver = new ChromeDriver();
		WebDriver driver = browserSetUp(System.getProperty("browser","chrome"));
		threadDriver.set(driver);
	}
	
	@AfterMethod(alwaysRun=true) 
	public void aftermethod() {
		threadDriver.get().quit();
		threadDriver.remove();
	}
	
	public static WebDriver browserSetUp(String browser) {
		WebDriver driver = null;
		switch (browser) {
		case "firefox":
			driver = new FirefoxDriver();
			break;
		case "chrome":
			driver = new ChromeDriver();
			break;
		default:
			break;
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		return driver;
	}

	@Test
	public void passTest1() {
		WebDriver driver = threadDriver.get();
		driver.get("https://mvnrepository.com/");
		Assert.assertTrue(driver.getCurrentUrl().contains("mvnrepository"));
		ExtentListeners.testReport.get().log(Status.INFO, "Test1: " + driver.getCurrentUrl());		
	}
	
	@Test
	public void skipTest() {
		WebDriver driver = threadDriver.get();
		driver.get("https://www.udemy.com/");
		Assert.assertTrue(driver.getCurrentUrl().contains("udemy"));
		ExtentListeners.testReport.get().log(Status.INFO, "skipTest: " + driver.getCurrentUrl());
	}
	
	@Test
	public void failTest() {
		WebDriver driver = threadDriver.get();
		driver.get("https://www.google.com/");
		Assert.assertTrue(driver.getCurrentUrl().contains("google"));
		ExtentListeners.testReport.get().log(Status.INFO, "failTest: " + driver.getCurrentUrl());
	}
	
	@Test
	public void passTest2() {
		WebDriver driver = threadDriver.get();
		driver.get("https://www.way2automation.com/");
		Assert.assertTrue(driver.getCurrentUrl().contains("way2automation"));
		ExtentListeners.testReport.get().log(Status.INFO, "passTest2: " + driver.getCurrentUrl());
	}
	
	
}
