package com.example.connectpostgresql;

public class productData {
    private Integer id;
    private Integer productId;
    private String productName;
    private String size;
    private Integer stock;
    private Integer quantity;
    private Double price;
    private String image;

    public productData(Integer productId,String productName, String size, Integer stock, Double price,String image) {
        this.productId = productId;
        this.productName = productName;
        this.size = size;
        this.stock = stock;
        this.price = price;
        this.image = image;
    }

        public productData(Integer productId,String productName, Integer quantity, Double price,String image) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }


    public Integer getId() {
        return id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
