package pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.List;

public class WebAbstractPage {

    protected WebDriver driver;
    protected WebDriverWait waitExplicit;
    protected Actions action;
    protected JavascriptExecutor jsExecutor;
    protected WebElement element;
    protected By by;

    long longTimeout = 30;

    public WebAbstractPage(WebDriver driver) {
        this.driver = driver;
        this.waitExplicit = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        this.action = new Actions(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    public void clickToElement(String locator) {
        element = driver.findElement(By.xpath(locator));
        element.click();
    }

    public void waitToElementVisible(String locator) {
        By by = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void sendKeyToElement(String locator, String value) {
        element = driver.findElement(By.xpath(locator));
        waitToElementPresence(locator);
        element.clear();
        element.sendKeys(value);
    }

    public void waitToElementPresence(String locator) {
        by = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public String getTextElement(String locator) {
        element = driver.findElement(By.xpath(locator));
        return element.getText().trim();
    }
    /*
    * to handle an element contains child element(s)
    * select the number of the node to get by childNodes[n]
    * */
    public String getTextWithoutTheChildNode(String locator){
        element = driver.findElement(By.xpath(locator));
        String messageText = (String) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].childNodes[0].nodeValue.trim();", element);
        return messageText;

    }

    public boolean isElementDisplayed(String locator) {
        try {
            element = driver.findElement(By.xpath(locator));
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    public List<WebElement> getListElements(String locator) {
        return driver.findElements(By.xpath(locator));
    }

}

