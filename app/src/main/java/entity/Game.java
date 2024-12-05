package entity;

public class Game {
    private int id;
    private String name;
    private String description;
    private String publisher;
    private double price;

    // Constructor
    public Game(int id, String name, String description, String publisher, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.publisher = publisher;
        this.price = price;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
