package tests.Reviews;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.APIHelper;
import tests.BaseTest;

public class ReviewsTest extends BaseTest {
    @Test
    public void writeAReviewForThePurchasedProductSuccessfully() {
        APIHelper apiHelper = new APIHelper();
        int reviewId = 3;
        int userId = 1;
        int purchasedProductId = 1;
        double rating = 3.5;
        String comment = "Good but expensive'";
        Assert.assertTrue(apiHelper.isProductPurchased(purchasedProductId));
        apiHelper.writeAReviewSuccessfully(reviewId, userId, purchasedProductId, rating, comment);

    }
}
