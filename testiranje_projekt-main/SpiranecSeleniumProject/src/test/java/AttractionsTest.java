import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AttractionsTest {
    public WebDriver driver;

    //----------------------Test Setup-----------------------------------
    @BeforeMethod
    public void setupTest() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.navigate().to("https://www.google.com/");
    }

    @AfterMethod
    public void teardownTest() {
        driver.quit();
    }

    @Test
    public void bookingLoginTest() throws InterruptedException {
        driver.manage().window().maximize();
        Thread.sleep(500);
        WebElement acceptLink = driver.findElement(By.xpath("//*[@id=\"L2AGLb\"]/div"));
        acceptLink.click();
        Thread.sleep(1500);
        driver.navigate().to("https://www.booking.com/");
        Thread.sleep(1500);
        driver.findElement(By.xpath("//*[@id=\"onetrust-accept-btn-handler\"]")).click();
        Thread.sleep(1000);

        WebElement attractions = driver.findElement(By.xpath("//*[@id=\"attractions\"]"));
        attractions.click();
        Thread.sleep(2500);
        WebElement searchArea = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[1]/div/div/div/div[3]/div/div/div/div[1]/form/div[1]/input"));
        searchArea.sendKeys("Dubrovnik");
        Thread.sleep(1400);
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[1]/div/div/div/div[3]/div/div/div/div[2]/ul[1]/li[2]/a/div[2]")).click();
        Thread.sleep(3500);
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div[1]/div[1]/div/div/div/span/label[3]/span")).click();
        Thread.sleep(2500);

        List<WebElement> myList= driver.findElements(By.className("css-zq8daf"));
        myList.get(1).click();
        Thread.sleep(3000);

    }
}