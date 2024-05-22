package Reports;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DemoTest {
	
	//@BeforeMethod
	public WebDriver beforemethod() {
		WebDriver driver;
		driver = new ChromeDriver();
		return driver;
	}
	
	@Test
	public void passTest1() {
		WebDriver driver = beforemethod();
		driver.get("https://mvnrepository.com/");
		Assert.assertTrue(driver.getCurrentUrl().contains("mvnrepository"));
		driver.quit();
	}
	
	@Test
	public void skipTest() {
		WebDriver driver = beforemethod();
		driver.get("https://www.udemy.com/");
		Assert.assertTrue(driver.getCurrentUrl().contains("udemy"));
		aftermethod(driver);
		throw new SkipException("Skipping this test");
	}
	
	@Test
	public void failTest() {
		WebDriver driver = beforemethod();
		driver.get("https://www.google.com/");
		aftermethod(driver);
		Assert.fail();
	}
	
	@Test
	public void passTest2() {
		WebDriver driver = beforemethod();
		driver.get("https://www.way2automation.com/");
		Assert.assertTrue(driver.getCurrentUrl().contains("way2automation"));
		aftermethod(driver);
	}
	
	//@AfterMethod
	public void aftermethod(WebDriver driver) {
		driver.quit();
	}
}
