package tests.Products;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.APIHelper;
import tests.BaseTest;

import java.util.List;
import java.util.Map;

public class ProductsTest extends BaseTest {
    @Test
    public void searchProductsWithCategoryNamedElectronicsSuccessfully() throws Exception {
        APIHelper apiHelper = new APIHelper();
        String categoryName = "Electronics";
        int cateId = apiHelper.getCategoryIdByName(categoryName);
        System.out.println("The Electronics has the ID: " + cateId);
        apiHelper.printMatchedProductList(cateId);
        Assert.assertTrue(apiHelper.isCategoryNameExist(categoryName), "The category name is not existed");
        List<Map<String, Object>> products = apiHelper.getProductsByCategoryId(cateId);
        Assert.assertFalse(products.isEmpty(), "No products found in Electronics category");
    }
}
