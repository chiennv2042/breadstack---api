package pageobjects;

import browserstack.shaded.org.jsoup.select.CombiningEvaluator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import helpers.JsonHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.EnvironmentData;

import java.io.IOException;
import java.util.*;

public class APIHelper {
    public static String URL_BASE;
    public static String URL_CREATE_NEW_USER;
    public static String URL_GET_ALL_USER;
    public static String URL_GET_ALL_PRODUCTS;
    public static String URL_GET_ALL_CATEGORIES;
    public static String URL_PLACE_AN_ORDER;
    public static String URL_WRITE_A_REVIEW;
    static HashMap<String, String> urls;
    private static final ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
    private RequestSpecification requestSpecMultipart;
    private RequestSpecification requestSpecJson;
    private String workingDir = System.getProperty("user.dir");
    JsonHelper jsonHelper = new JsonHelper();
    public APIHelper() {
        defineEnvironment();
    }
    public void defineEnvironment() {
        EnvironmentData environmentData = new EnvironmentData();
        urls = environmentData.urls;
        URL_BASE = urls.get("base_url");

        requestSpecMultipart = new RequestSpecBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "multipart/form-data")
                .build();
        requestSpecJson = new RequestSpecBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        URL_CREATE_NEW_USER = URL_BASE + urls.get("create_user_url");
        URL_GET_ALL_USER = URL_BASE + urls.get("create_user_url");
        URL_GET_ALL_PRODUCTS = URL_BASE + urls.get("get_all_products_url");
        URL_GET_ALL_CATEGORIES = URL_BASE + urls.get("get_all_categories_url");
        URL_PLACE_AN_ORDER = URL_BASE + urls.get("place_an_order_url");
        URL_WRITE_A_REVIEW = URL_BASE + urls.get("write_a_review_url");
    }
    public void createNewUser(int id, String username, String email, String password) {
        List<UsersPage> users = getListUsers();
        if (!validateUniqueIds(users, id)) {
            System.out.println("Error: Duplicate id found. User creation failed.");
            return;
        }
        RestAssured.baseURI = URL_CREATE_NEW_USER;

        UsersPage newUser = new UsersPage(id, username, email, password);
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(newUser)
                .post();
        if (response.getStatusCode() == 201) {
            System.out.println("User created successfully!");
        } else {
            System.out.println("Failed to create user. Status code: " + response.getStatusCode());
        }
    }
    public int getNumberOfUser(){
        Response response = RestAssured.get(URL_GET_ALL_USER);
        if (response.getStatusCode() != 200) {
            System.out.println("Error: Received status code " + response.getStatusCode());
            return 0;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<UsersPage> users = objectMapper.readValue(response.getBody().asString(), new TypeReference<List<UsersPage>>() {});
            return users.size();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public boolean validateUniqueIds(List<UsersPage> users, int newId) {
        Set<Integer> idSet = new HashSet<>();
        for (UsersPage user : users) {
            if (!idSet.add(user.getId())) {
                System.out.println("Duplicate id found: " + user.getId());
                return false;
            }
        }
        if (!idSet.add(newId)) {
            System.out.println("Duplicate id found: " + newId);
            return false;
        }
        return true;
    }
    public boolean validateUserIdExisted(List<UsersPage> users, int userId) {
        for (UsersPage user : users) {
            if (user.getId() == userId) {
                return true;
            }
        }
        return false;
    }
    public boolean validateProductIdExisted(List<ProductsPage> products, int productId) {
        for (ProductsPage product : products) {
            if (product.getId() == productId) {
                return true;
            }
        }
        return false;
    }

    public List<UsersPage> getListUsers(){
        Response response = RestAssured.get(URL_GET_ALL_USER);
        ObjectMapper objectMapper = new ObjectMapper();
        List<UsersPage> users;
        try {
            users = objectMapper.readValue(response.getBody().asString(), new TypeReference<List<UsersPage>>() {});
            return users;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Response getUserInfoById (int userId){
        String URL_SPECIFIC_USER = URL_GET_ALL_USER + "/" + userId;
        Response response = RestAssured.get(URL_SPECIFIC_USER);
        return response;
    }
    public Response getOrderInfoById (int orderId){
        String URL_SPECIFIC_ORDER = URL_PLACE_AN_ORDER + "/" + orderId;
        Response response = RestAssured.get(URL_SPECIFIC_ORDER);
        return response;
    }

    public int getCategoryIdByName(String categoryName){
        Response response = RestAssured.get(URL_GET_ALL_CATEGORIES);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map<String, Object>> categories = objectMapper.readValue(response.asString(), new TypeReference<List<Map<String, Object>>>() {});

            for (Map<String, Object> category : categories) {
                if (categoryName.equals(category.get("name"))) {
                    return (Integer) category.get("id");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean isCategoryNameExist(String categoryName){
        return getCategoryIdByName(categoryName) != -1;
    }
    public List<Map<String, Object>> getProductsByCategoryId(int cateId) throws Exception {
        Response response = RestAssured.get(URL_GET_ALL_PRODUCTS);
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to fetch products. Status Code: " + response.getStatusCode());
        }
        JsonPath jsonPath = response.jsonPath();

        String filterExpression = String.format("findAll { it.categoryId == %d }", cateId);

        List<Map<String, Object>> products = jsonPath.getList(filterExpression);

        return products;
    }
    public void printMatchedProductList(int cateId) throws Exception {
        List<Map<String, Object>> products = getProductsByCategoryId(cateId);

        for (Map<String, Object> product : products) {
            String formatProductList = writer.writeValueAsString(product);
            System.out.println(formatProductList);
        }
    }
    public int getProductIdByProductList(String categoryName) throws Exception {
        int cateId = getCategoryIdByName(categoryName);
        List<Map<String, Object>> productList = getProductsByCategoryId(cateId);
        for (Map<String, Object> product : productList) {
            if (cateId == (Integer)(product.get("categoryId"))) {
                return (Integer) product.get("id");
            }
        }
        return -1;
    }
    public void placeAnOrderWithSpecificProductId(int id, int userId, OrdersPage.Status status, int productId, int quantity) {
        try {
            //check orderId is not duplicate
            List<OrdersPage> orders = getListOrders();
            if (!isOrderIdDuplicate(orders, id)) {
                throw new IllegalArgumentException("Order ID is duplicate. Place order failed!");
            }
            //check userId is valid
            List<UsersPage> users = getListUsers();
            if (!validateUserIdExisted(users, userId)) {
                throw new IllegalArgumentException("User ID is not invalid. Place order failed!");
            }
            RestAssured.baseURI = URL_PLACE_AN_ORDER;

            Object items = addProductToOrderById(productId, quantity);
            OrdersPage newOrder = new OrdersPage(id, userId, status, items);

            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .body(newOrder)
                    .post();

            if (response.getStatusCode() == 201) {
                System.out.println("Ordered successfully!");
            } else {
                System.out.println("Failed to place order. Status code: " + response.getStatusCode());
            }
        } catch (IllegalArgumentException e) {
            // Handle validation exceptions (e.g., duplicate ID or invalid userId)
            System.out.println("Order validation failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while placing the order: " + e.getMessage());
        }
    }
    public void printNewOrder(int orderId) {
        List<OrdersPage> orders = getListOrders();
        boolean orderFound = false;
        for (OrdersPage order : orders) {
            if(order.getId() == orderId){
                orderFound = true;
                String formatProductList;
                try {
                    formatProductList = writer.writeValueAsString(order);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(formatProductList);
            }
        }
        if (!orderFound) {
            System.out.println("The order ID " + orderId + " is invalid!");
        };
    }

    private boolean isOrderIdDuplicate(List<OrdersPage> orders, int newId) {
        Set<Integer> idSet = new HashSet<>();

        for (OrdersPage order : orders) {
            if (!idSet.add(order.getId())) {
                System.out.println("Duplicate order id found within the existing orders: " + order.getId());
            }
        }
        if (!idSet.add(newId)) {
            System.out.println("Duplicate id found: " + newId);
            return false;
        }

        return true;
    }
    private boolean isReviewIdDuplicate(List<ReviewsPage> reviews, int newId) {
        Set<Integer> idSet = new HashSet<>();

        for (ReviewsPage review : reviews) {
            if (!idSet.add(review.getId())) {
                System.out.println("Duplicate review id found within the existing orders: " + review.getId());
            }
        }
        if (!idSet.add(newId)) {
            System.out.println("Duplicate id found: " + newId);
            return false;
        }

        return true;
    }

    public List<OrdersPage> getListOrders(){
        Response response = RestAssured.get(URL_PLACE_AN_ORDER);
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrdersPage> orders;
        try {
            orders = objectMapper.readValue(response.getBody().asString(), new TypeReference<List<OrdersPage>>() {});
            return orders;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object addProductToOrderById(int productId, int quantity){
        Map<String, Object> item = new HashMap<>();
        item.put("productId", productId);
        item.put("quantity", quantity);
        return item;
    }
    public int getNumberOfOrder(){
        Response response = RestAssured.get(URL_PLACE_AN_ORDER);
        if (response.getStatusCode() != 200) {
            System.out.println("Error: Received status code " + response.getStatusCode());
            return 0;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<OrdersPage> orders = objectMapper.readValue(response.getBody().asString(), new TypeReference<List<OrdersPage>>() {});
            return orders.size();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public List<ProductsPage> getAllThePurchasedProduct() {
        List<ProductsPage> products = getListProduct();
        List<OrdersPage> orders = getListOrders();
        Map<Integer, ProductsPage> productMap = new HashMap<>();

        for (ProductsPage product : products) {
            productMap.put(product.getId(), product);
        }

        List<ProductsPage> purchasedProducts = new ArrayList<>();

        for (OrdersPage order : orders) {
            if (order.getStatus() == OrdersPage.Status.shipped) {
                int productId = order.getId();
                ProductsPage product = productMap.get(productId);

                if (product != null) {
                    purchasedProducts.add(product);
                }
            }
        }
        return purchasedProducts;
    }

    public boolean isProductPurchased(int productId){
        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be greater than zero.");
        }
        List<ProductsPage> purchasedProducts = getAllThePurchasedProduct();
        if (purchasedProducts == null || purchasedProducts.isEmpty()) {
            throw new IllegalStateException("Failed to retrieve purchased products or no products were purchased.");
        }
        for(ProductsPage product: purchasedProducts){
            if(product.getId() == productId){
                return true;
            }
        }
        return false;
    }

    private List<ProductsPage> getListProduct() {
        Response response = RestAssured.get(URL_GET_ALL_PRODUCTS);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductsPage> products;
        try {
            products = objectMapper.readValue(response.getBody().asString(), new TypeReference<List<ProductsPage>>() {});
            return products;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void writeAReviewSuccessfully(int id, int userId, int productId, double rating, String comment){
        try {
            //check reviewId is not duplicate
            List<ReviewsPage> reviews = getListReviews();
            if (!isReviewIdDuplicate(reviews, id)) {
                throw new IllegalArgumentException("Review ID is duplicate. Write review failed!");
            }
            //check userId is valid
            List<UsersPage> users = getListUsers();
            if (!validateUserIdExisted(users, userId)) {
                throw new IllegalArgumentException("User ID is not invalid. Write review failed!");
            }
            //check productId is valid
            List<ProductsPage> products = getListProduct();
            if (!validateProductIdExisted(products, productId)) {
                throw new IllegalArgumentException("Product ID is not invalid. Write review failed!");
            }

            RestAssured.baseURI = URL_WRITE_A_REVIEW;

            ReviewsPage newReview = new ReviewsPage(id, userId, productId, rating, comment);

            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .body(newReview)
                    .post();

            if (response.getStatusCode() == 201) {
                System.out.println("Reviewed successfully!");
            } else {
                System.out.println("Failed to write review. Status code: " + response.getStatusCode());
            }
        } catch (IllegalArgumentException e) {
            // Handle validation exceptions (e.g., duplicate ID or invalid userId)
            System.out.println("Review validation failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while writing the review: " + e.getMessage());
        }
    }

    private List<ReviewsPage> getListReviews() {
        Response response = RestAssured.get(URL_WRITE_A_REVIEW);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ReviewsPage> reviews;
        try {
            reviews = objectMapper.readValue(response.getBody().asString(), new TypeReference<List<ReviewsPage>>() {});
            return reviews;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void checkTheAvailableProduct(int productId, int quality) {
        List<ProductsPage> products = getListProduct();

        for (ProductsPage product : products) {
            if (product.getId() == productId) {
                if (quality > product.getStock()) {
                    throw new IllegalArgumentException("Requested quantity " + quality + " exceeds available stock of " + product.getStock() + " for product: " + product.getName());
                } else {
                    System.out.println("Product " + product.getName() + " is available with requested quantity: " + quality);
                }
                return;
            }
        }
        throw new NoSuchElementException("Product with ID " + productId + " not found in the product list.");
    }
}
