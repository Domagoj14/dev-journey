import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
public class BookingLoginTest {
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
        Thread.sleep(2500);
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"b2indexPage\"]/div[2]/div/div/header/nav[1]/div[2]/div"));
        loginButton.click();
        Thread.sleep(1500);
        WebElement emailArea = driver.findElement(By.name("username"));
        emailArea.sendKeys("seleniumsourceproject@gmail.com");
        Thread.sleep(1600);
        emailArea.submit();
        Thread.sleep(1000);
        WebElement passwordArea = driver.findElement(By.name("password"));
        passwordArea.sendKeys("Seleniumsource1");
        Thread.sleep(1800);
        passwordArea.submit();
        Thread.sleep(2000);
        WebElement accountName = driver.findElement(By.xpath("//*[@id=\"b2indexPage\"]/div[3]/div/header/nav[1]/div[2]/div/span/button/span/div/div[2]/div[1]"));
        Assert.assertEquals(accountName.getText(), "Your account");
        System.out.print(accountName.getText());

        WebElement logout1 = driver.findElement(By.xpath("//*[@id=\"b2indexPage\"]/div[3]/div/header/nav[1]/div[2]/div/span/button/span/div/div[2]/div[1]"));
        logout1.click();
        Thread.sleep(1000);
        WebElement logout2 = driver.findElement(By.xpath("//*[@id=\":rc:\"]/div/div/div/div/ul/li[7]/button/div/div[2]/span"));
        logout2.click();
        Thread.sleep(2000);
    }
}
