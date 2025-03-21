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

public class FlightTest {
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

        WebElement flights = driver.findElement(By.xpath("//*[@id=\"flights\"]"));
        flights.click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"basiclayout\"]/div/div/div[1]/div/div/div/div/div[2]/div[2]/div/div/div/div/div[1]/div/button[3]")).click();
        WebElement searchArea = driver.findElement(By.xpath("//*[@id=\":R2dmk5b9:\"]/div/div/div/div/div/div/div[1]/div/div/div/div/input"));
        Thread.sleep(400);
        searchArea.sendKeys("London");
        Thread.sleep(600);

        List<WebElement> suggestions = new ArrayList<>();
        for (int i=1;i<6;i++) {
            suggestions.add(driver.findElement(By.xpath("//*[@id=\"flights-searchbox_suggestions\"]/li["+i+"]")));
        }
        for (WebElement suggestion : suggestions) {
            System.out.println(suggestion.getText());
        }
        Thread.sleep(800);
        suggestions.get(3).click();
        Thread.sleep(800);
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"basiclayout\"]/div/div/div[1]/div/div/div/div/div[2]/div[2]/div/div/button"));
        searchButton.click();
        Thread.sleep(9000);
        driver.findElement(By.xpath("//*[@id=\"flight-card-0\"]/div/div/div[2]/div[2]/button")).click();
        Thread.sleep(5000);
    }
}