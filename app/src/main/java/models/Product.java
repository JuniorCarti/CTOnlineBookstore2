package models;

import java.util.Objects;

public class Product {
    private String id;
    private String category;
    private String title;
    private String description; // Optional for Exercise Books
    private double price;
    private String imageUrl;
    private String pages; // Only for Exercise Books
    private int stockQuantity; // Only for Stationery
    private int cartonQuantity; // Only for Exercise Books
    private int cartonBooks; // Only for Exercise Books
    private int squareRuledBooks; // Only for Exercise Books
    private int singleRuledBooks; // Only for Exercise Books
    private float rating;
    private int peopleRated;

    // ✅ Default constructor (needed for Firebase)
    public Product() {}

    // ✅ Constructor for Exercise Books
    public Product(String id, String category, String title, double price, String imageUrl,
                   String pages, int cartonQuantity, int cartonBooks, int squareRuledBooks, int singleRuledBooks) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pages = pages;
        this.cartonQuantity = cartonQuantity;
        this.cartonBooks = cartonBooks;
        this.squareRuledBooks = squareRuledBooks;
        this.singleRuledBooks = singleRuledBooks;
    }

    // ✅ Constructor for Stationery & other categories
    public Product(String id, String category, String title, double price, String imageUrl, int stockQuantity) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
    }

    // ✅ Getter methods (required for SearchActivity & RecyclerView Adapter)

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description; // If not needed, you can return an empty string
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPages() {
        return pages;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getCartonQuantity() {
        return cartonQuantity;
    }

    public int getCartonBooks() {
        return cartonBooks;
    }

    public int getSquareRuledBooks() {
        return squareRuledBooks;
    }

    public int getSingleRuledBooks() {
        return singleRuledBooks;
    }

    public float getRating() {
        return rating;
    }

    public int getPeopleRated() {
        return peopleRated;
    }

    // Override equals() for DiffUtil in RecyclerView
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                Float.compare(product.rating, rating) == 0 &&
                peopleRated == product.peopleRated &&
                stockQuantity == product.stockQuantity &&
                cartonQuantity == product.cartonQuantity &&
                cartonBooks == product.cartonBooks &&
                squareRuledBooks == product.squareRuledBooks &&
                singleRuledBooks == product.singleRuledBooks &&
                Objects.equals(id, product.id) &&
                Objects.equals(category, product.category) &&
                Objects.equals(title, product.title) &&
                Objects.equals(description, product.description) &&
                Objects.equals(imageUrl, product.imageUrl) &&
                Objects.equals(pages, product.pages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, title, description, price, imageUrl, pages, stockQuantity,
                cartonQuantity, cartonBooks, squareRuledBooks, singleRuledBooks, rating, peopleRated);
    }
}
