package entity;

import java.util.Comparator;

public class Product implements Comparable<Product> {
    private String name;
    private float price;
    private String imagePath;

    public Product(String name, float price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    public int compareTo(Product compareProduct) {
        float compareQuantity = ((Product) compareProduct).getPrice();
        //ascending
        return (int) (this.price - compareQuantity);
    }

    public static Comparator<Product> ProductNameComparator = new Comparator<Product>() {
        public int compare(Product product1, Product product2) {
            String productName1 = product1.getName().toLowerCase();
            String productName2 = product2.getName().toLowerCase();
            //ascending
            return productName1.compareTo(productName2);
        }
    };
}
