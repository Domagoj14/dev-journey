import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.Set;


public class ChangeSearchTest {
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
        searchArea.sendKeys("Istria");

        driver.findElement(By.xpath("//*[@id=\"indexsearch\"]/div[2]/div/form/div[1]/div[2]")).click();
        Thread.sleep(200);

        WebElement nextMonthButton = driver.findElement(By.xpath("//*[@id=\"calendar-searchboxdatepicker\"]/div/div[1]/button"));
        for(int i=0;i<8;i++) {
            nextMonthButton.click();
            Thread.sleep(350);
        }
        driver.findElement(By.xpath("//*[@id=\"calendar-searchboxdatepicker\"]/div/div[1]/div/div[2]/table/tbody/tr[2]/td[1]/span/span")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[@id=\"calendar-searchboxdatepicker\"]/div/div[1]/div/div[2]/table/tbody/tr[2]/td[4]/span/span")).click();
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
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,700)", "");
        Thread.sleep(1000);
        WebElement appartment = driver.findElement(By.xpath("//*[@id=\"bodyconstraint-inner\"]/div[2]/div/div[2]/div[3]/div[2]/div[4]/div[3]/div[5]/div[1]/div[2]/div/div[2]/div[2]/div/div[2]/a"));
        appartment.click();
        Thread.sleep(3000);
        String currentHandle= driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for(String actual: handles)
        {
            if(!actual.equalsIgnoreCase(currentHandle))
            {
                driver.switchTo().window(actual);
            }
        }
        Thread.sleep(1000);
        js.executeScript("window.scrollBy(0,1720)", "");
        Thread.sleep(1000);
        WebElement currentDate = driver.findElement(By.xpath("//*[@id=\"hp_availability_style_changes\"]/div[3]/div/div/form/div[1]/div[1]/div/div/button[2]/span"));
        String currDate = currentDate.getText();
        currentDate.click();
        Thread.sleep(500);


        driver.findElement(By.xpath("//*[@id=\"hp_availability_style_changes\"]/div[3]/div/div/form/div[1]/div[1]/div/div[2]/div/div/div[1]/div/div[1]/table/tbody/tr[4]/td[1]/span")).click();
        Thread.sleep(400);
        driver.findElement(By.xpath("//*[@id=\"hp_availability_style_changes\"]/div[3]/div/div/form/div[1]/div[1]/div/div[2]/div/div/div[1]/div/div[1]/table/tbody/tr[4]/td[4]/span")).click();
        Thread.sleep(400);

        driver.findElement(By.xpath("//*[@id=\"hp_availability_style_changes\"]/div[3]/div/div/form/div[1]/div[2]/div/button")).click();
        Thread.sleep(800);
        driver.findElement(By.xpath("//*[@id=\"hp_availability_style_changes\"]/div[3]/div/div/form/div[1]/div[3]/button")).click();
        WebElement newDate = driver.findElement(By.xpath("//*[@id=\"hp_availability_style_changes\"]/div[3]/div/div/form/div[1]/div[1]/div/div/button[1]/span"));
        js.executeScript("window.scrollBy(0,820)", "");
        Thread.sleep(700);
        Assert.assertNotEquals(currDate, newDate.getText());
        System.out.print(currDate);
        System.out.print(newDate.getText());
    }
}
