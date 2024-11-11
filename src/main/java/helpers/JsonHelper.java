package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import pageobjects.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonHelper {

    public static List<UsersPage> getUserData(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, UsersPage.class));
    }
    public static List<ProductsPage> getProductData(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, ProductsPage.class));
    }
    public static List<CategoriesPage> getCategoryData(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, CategoriesPage.class));
    }
    public static List<OrdersPage> getOrderData(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, OrdersPage.class));
    }
    public static List<ReviewsPage> getReviewData(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, ReviewsPage.class));
    }
}
