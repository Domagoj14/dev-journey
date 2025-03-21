import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
public class ChangeCurrencyTest {
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
        WebElement currencyButton = driver.findElement(By.xpath("//*[@id=\"b2indexPage\"]/div[2]/div/div/header/nav[1]/div[2]/span[1]/button/span"));
        currencyButton.click();
        Thread.sleep(800);
        WebElement newCurrency = driver.findElement(By.xpath("//*[@id=\"b2indexPage\"]/div[22]/div/div/div/div/div[2]/div/div[2]/div/div/ul[1]/li[1]/button/div/div[1]"));
        newCurrency.click();
        Thread.sleep(2000);
        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
        Thread.sleep(800);

        WebElement searchArea = driver.findElement(By.xpath("//*[@id=\":re:\"]"));
        searchArea.sendKeys("Jahorina");

        driver.findElement(By.xpath("//*[@id=\"indexsearch\"]/div[2]/div/form/div[1]/div[2]")).click();
        Thread.sleep(200);
        driver.findElement(By.xpath("//*[@id=\"calendar-searchboxdatepicker\"]/div/div[1]/div/div[2]/table/tbody/tr[4]/td[7]/span/span")).click();
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

        WebElement priceInUSD = driver.findElement(By.xpath("//*[@id=\"bodyconstraint-inner\"]/div[2]/div/div[2]/div[3]/div[2]/div[2]/div[3]/div[3]/div[1]/div[2]/div/div[2]/div[2]/div/div[1]/span"));
        Assert.assertTrue(priceInUSD.getText().contains("US$"));
        System.out.print(priceInUSD.getText());
        Thread.sleep(2000);
    }
}