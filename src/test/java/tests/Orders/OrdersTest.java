package tests.Orders;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.APIHelper;
import pageobjects.OrdersPage;
import pageobjects.UsersPage;
import tests.BaseTest;

import static pageobjects.OrdersPage.Status.shipped;
import static pageobjects.OrdersPage.Status.shipping;
import static pageobjects.OrdersPage.Status.processing;

public class OrdersTest extends BaseTest {
    @Test
    public void placeAnOrderWithAProductInElectronicsSuccessfully() throws Exception {
        APIHelper apiHelper = new APIHelper();
        int numberOfOrdersBeforeAdd = apiHelper.getNumberOfOrder();
        String categoryName = "Electronics";
        int prodId = apiHelper.getProductIdByProductList(categoryName);
        int orderId = 16;
        int userId = 1;
        OrdersPage.Status status = processing;
        int quantity = 2;
        apiHelper.placeAnOrderWithSpecificProductId(orderId, userId, status, prodId, quantity);
        int numberOfOrdersAfterAdd = apiHelper.getNumberOfOrder();
        Assert.assertEquals(numberOfOrdersAfterAdd, numberOfOrdersBeforeAdd + 1, "The order is not created");
        apiHelper.printNewOrder(orderId);
        Response response = apiHelper.getOrderInfoById(orderId);

        OrdersPage placedOrder = response.getBody().as(OrdersPage.class);
        Assert.assertEquals(placedOrder.getUserId(), userId, "UserID does not match!");
        Assert.assertEquals(placedOrder.getStatus(), status, "Status does not match!");
    }
}
