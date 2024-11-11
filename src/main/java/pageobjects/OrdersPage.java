package pageobjects;

public class OrdersPage {
    private int id;
    private int userId;
    private Status status;
    private Object items;
    public enum Status {
        processing, shipping, shipped;
    }
    public OrdersPage() { }

    // Constructors, Getters, and Setters
    public OrdersPage(int id, int userId, Status status, Object items) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.items = items;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Object getItems() { return items; }
    public void setItems(Object items) { this.items = items; }
}
