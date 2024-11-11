package tests;

import common.TestConfig;
import helpers.JsonHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import pageobjects.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BaseTest {
    protected WebDriver driver;
    protected PageGenerator pageGenerator;
    protected static HashMap<String, String> urls;
    protected static String BASE_URL;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        System.setProperty("webdriver.chrome.driver", (System.getProperty("user.dir") + "/src/main/java/drivers/chromedriver.exe"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);

        pageGenerator = new PageGenerator(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    @BeforeSuite
    public void beforeSuiteMethod(){
        TestConfig.initEnvironment();
        urls = TestConfig.urls;
        BASE_URL = urls.get("base_url");
    }
    @DataProvider(name = "userData")
    public Object[][] getUserData() throws IOException {
        List<UsersPage> testData = JsonHelper.getUserData("./src/test/resources/testdata/users.json");
        Object[][] userData = new Object[testData.size()][4];

        for (int i = 0; i < testData.size(); i++) {
            userData[i][0] = testData.get(i).getId();
            userData[i][1] = testData.get(i).getUsername();
            userData[i][2] = testData.get(i).getEmail();
            userData[i][3] = testData.get(i).getPassword();
        }

        return userData;
    }
    @DataProvider(name = "categoryData")
    public Object[][] getCategoryData() throws IOException {
        List<CategoriesPage> testData = JsonHelper.getCategoryData("./src/test/resources/testdata/categories.json");
        Object[][] categoryData = new Object[testData.size()][2];

        for (int i = 0; i < testData.size(); i++) {
            categoryData[i][0] = testData.get(i).getId();
            categoryData[i][1] = testData.get(i).getName();
        }

        return categoryData;
    }
    @DataProvider(name = "productData")
    public Object[][] getProductData() throws IOException {
        List<ProductsPage> testData = JsonHelper.getProductData("./src/test/resources/testdata/products.json");
        Object[][] productData = new Object[testData.size()][6];

        for (int i = 0; i < testData.size(); i++) {
            productData[i][0] = testData.get(i).getId();
            productData[i][1] = testData.get(i).getName();
            productData[i][2] = testData.get(i).getDescription();
            productData[i][3] = testData.get(i).getPrice();
            productData[i][4] = testData.get(i).getStock();
            productData[i][5] = testData.get(i).getCategoryId();
        }

        return productData;
    }
    @DataProvider(name = "orderData")
    public Object[][] getOrderData() throws IOException {
        List<OrdersPage> testData = JsonHelper.getOrderData("./src/test/resources/testdata/orders.json");
        Object[][] orderData = new Object[testData.size()][4];

        for (int i = 0; i < testData.size(); i++) {
            orderData[i][0] = testData.get(i).getId();
            orderData[i][1] = testData.get(i).getUserId();
            orderData[i][2] = testData.get(i).getStatus();
            orderData[i][3] = testData.get(i).getItems();
        }

        return orderData;
    }
    @DataProvider(name = "reviewData")
    public Object[][] getReviewData() throws IOException {
        List<ReviewsPage> testData = JsonHelper.getReviewData("./src/test/resources/testdata/reviews.json");
        Object[][] reviewData = new Object[testData.size()][5];

        for (int i = 0; i < testData.size(); i++) {
            reviewData[i][0] = testData.get(i).getId();
            reviewData[i][1] = testData.get(i).getUserId();
            reviewData[i][2] = testData.get(i).getProductId();
            reviewData[i][3] = testData.get(i).getRating();
            reviewData[i][4] = testData.get(i).getComment();
        }

        return reviewData;
    }
}
