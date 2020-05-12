package com.hanu.ims.model.domain;

import java.util.Date;

public class Batch implements Comparable {
    private final int id;
    private String sku;
    private int importQuantity; // need to change DB to support this
    private int quantity;
    private Date importDate;
    private long importPrice;
    private long retailPrice;
    private Status status;
    private String productName;
    private Category category;

    /**
     * Full constructor for database mapping
     *
     * @param id
     * @param sku
     * @param quantity
     * @param importDate
     * @param retailPrice
     */
    public Batch(int id, String sku, int quantity, Date importDate, long importPrice, long retailPrice) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
        this.importDate = importDate;
        this.importPrice = importPrice;
        this.retailPrice = retailPrice;
    }

    public Batch(int id, String sku, int importQuantity, int quantity,
                 Date importDate, long importPrice, long retailPrice, String productName) {
        this.id = id;
        this.sku = sku;
        this.importQuantity = importQuantity;
        this.quantity = quantity;
        this.importDate = importDate;
        this.importPrice = importPrice;
        this.retailPrice = retailPrice;
        this.productName = productName;
    }

    /**
     * Constructor for Batch objects that are pending to be added to database
     *
     * @param sku
     * @param quantity
     * @param importDate
     * @param retailPrice
     */
    public Batch(String sku, int quantity, Date importDate, long importPrice, long retailPrice) {
        this(0, sku, quantity, importDate, importPrice, retailPrice);
        this.importQuantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getImportDate() {
        return importDate;
    }

    public long getRetailPrice() {
        return retailPrice;
    }

    public long getImportPrice() {
        return importPrice;
    }

    public int getImportQuantity() {
        return importQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public Category getCategory() {
        return category;
    }

    public Status getStatus() {
        return status;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public void setRetailPrice(long retailPrice) {
        this.retailPrice = retailPrice;
    }

    public void setImportPrice(long importPrice) {
        this.importPrice = importPrice;
    }

    public void setImportQuantity(int importQuantity) {
        this.importQuantity = importQuantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Batch batch = (Batch) o;
        return id == batch.id &&
                quantity == batch.quantity &&
                retailPrice == batch.retailPrice &&
                sku.equals(batch.sku) &&
                importDate.equals(batch.importDate);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Batch{");
        sb.append("id=").append(id);
        sb.append(", sku='").append(sku).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", importDate=").append(importDate);
        sb.append(", retailPrice=").append(retailPrice);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Batch)) {
            throw new ClassCastException();
        } else {
            Batch batch = (Batch) o;
            return this.importDate.compareTo(batch.importDate);
        }
    }

    public enum Status {
        IMPORTED, ORDERED, OUT_OF_STOCK, EXPIRED
    }
}
