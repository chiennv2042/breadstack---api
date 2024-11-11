package pageobjects;

public class ReviewsPage {
    private int id;
    private int userId;
    private int productId;
    private double rating;
    private String comment;
    public ReviewsPage() { }

    // Constructors, Getters, and Setters
    public ReviewsPage(int id, int userId, int productId, double rating, String comment) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public double getRating() { return rating; }
    public void setRating(double ratingValue) {
        if (ratingValue < 1.0 || ratingValue > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = ratingValue;
    }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

}

