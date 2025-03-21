import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;


public class OpenCheapestAppartment {
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
        Thread.sleep(300);
        driver.findElement(By.xpath("//*[@id=\"L2AGLb\"]/div")).click();
        Thread.sleep(1500);

        driver.navigate().to("https://www.booking.com/");
        Thread.sleep(1500);
        driver.findElement(By.xpath("//*[@id=\"onetrust-accept-btn-handler\"]")).click();
        Thread.sleep(1000);
        WebElement searchArea = driver.findElement(By.xpath("//*[@id=\":re:\"]"));
        searchArea.sendKeys("Jahorina");

        driver.findElement(By.xpath("//*[@id=\"indexsearch\"]/div[2]/div/form/div[1]/div[2]")).click();
        Thread.sleep(200);
        driver.findElement(By.xpath("//*[@id=\"calendar-searchboxdatepicker\"]/div/div[1]/div/div[2]/table/tbody/tr[4]/td[1]/span/span")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[@id=\"calendar-searchboxdatepicker\"]/div/div[1]/div/div[2]/table/tbody/tr[5]/td[4]/span/span")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[@id=\"indexsearch\"]/div[2]/div/form/div[1]/div[3]/div/button/span[1]")).click();
        Thread.sleep(200);
        WebElement personIncrement = driver.findElement(By.xpath("//*[@id=\":rf:\"]/div/div[1]/div[2]/button[2]/span"));
        personIncrement.click(); Thread.sleep(200);
        personIncrement.click(); Thread.sleep(200);
        personIncrement.click(); Thread.sleep(600);

        driver.findElement(By.xpath("//*[@id=\"indexsearch\"]/div[2]/div/form/div[1]/div[4]/button/span")).click();
        Thread.sleep(3500);

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
        Thread.sleep(400);

        driver.findElement(By.xpath("//*[@id=\"bodyconstraint-inner\"]/div[2]/div/div[2]/div[3]/div[2]/div[2]/div[1]/span")).click();
        Thread.sleep(1000);
        List<WebElement> options= driver.findElements(By.className("ac7953442b"));
        options.get(2).click();
        Thread.sleep(1000);
        List<WebElement> apartments= driver.findElements(By.className("c066246e13"));
        apartments.get(0).click();
        Thread.sleep(6000);

        String currentHandle= driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for(String actual: handles)
        {
            if(!actual.equalsIgnoreCase(currentHandle))
            {
                driver.switchTo().window(actual);
            }
        }
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for(int i=0;i<3;i++){
            js.executeScript("window.scrollBy(0,450)", "");
            Thread.sleep(300);
        }
        for(int i=0;i<3;i++){
            js.executeScript("window.scrollBy(0,-450)", "");
            Thread.sleep(300);
        }
        Thread.sleep(1000);
    }
}
