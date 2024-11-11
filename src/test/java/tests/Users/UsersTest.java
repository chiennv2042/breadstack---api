package tests.Users;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.APIHelper;
import pageobjects.UsersPage;
import tests.BaseTest;

public class UsersTest extends BaseTest {
    @Test(dataProvider = "userData")
    public void createAnUserSuccessfully(int id, String username, String email, String password){
        APIHelper apiHelper = new APIHelper();
        int numberOfUserBeforeAddNew = apiHelper.getNumberOfUser();

        apiHelper.createNewUser(id, username, email, password);
        int numberOfUserAfterAddNew = apiHelper.getNumberOfUser();
        Assert.assertEquals(numberOfUserAfterAddNew, numberOfUserBeforeAddNew + 1, "None of user is created");
        Response response = apiHelper.getUserInfoById(id);

        UsersPage createdUser = response.getBody().as(UsersPage.class);
        Assert.assertEquals(createdUser.getUsername(), username, "Username does not match!");
        Assert.assertEquals(createdUser.getEmail(), email, "Email does not match!");
        Assert.assertEquals(createdUser.getPassword(), password, "Password does not match!");
    }
}
