package pageobjects;

public class ProductsPage {
    private int id;
    private String name;
    private String description;
    private int price;
    private int stock;
    private int categoryId;

    public ProductsPage(){}

    public ProductsPage(int id, String name, String description, int price, int stock, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
    }
    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }

    public int getCategoryId() { return categoryId; }

    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}
