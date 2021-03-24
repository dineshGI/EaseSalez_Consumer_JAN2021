package com.gieasesales.Model;

import java.io.Serializable;

public class Products implements Serializable {
    String Productcode;


    String Productid;
    String ProductName;
    String ProductTypeDesc;
    String UOM;
    String IsAllowCustomerReview;

    public String getProductImg() {
        return ProductImg;
    }

    public void setProductImg(String productImg) {
        ProductImg = productImg;
    }

    String ProductImg;

    public String getIsAttributeAdded() {
        return IsAttributeAdded;
    }

    public void setIsAttributeAdded(String isAttributeAdded) {
        IsAttributeAdded = isAttributeAdded;
    }

    String IsAttributeAdded;

    public int getRatingAverage() {
        return RatingAverage;
    }

    public void setRatingAverage(int ratingAverage) {
        RatingAverage = ratingAverage;
    }

    int RatingAverage;

    double SellinPrice;
    double DiscountValue;

    public String getDisPlayPriceText() {
        return DisPlayPriceText;
    }

    public void setDisPlayPriceText(String disPlayPriceText) {
        DisPlayPriceText = disPlayPriceText;
    }

    String DisPlayPriceText;

    public double getMrp() {
        return Mrp;
    }

    public void setMrp(double mrp) {
        Mrp = mrp;
    }

    double Mrp;

    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    private int mQuantity;

    public Products() {
    }

    public Products(String Productcode, String Productid, String ProductName, String ProductTypeDesc, String UOM, String IsAllowCustomerReview, double SellinPrice, double DiscountValue, double Mrp, String ProductImg, String IsAttributeAdded, int RatingAverage, String DisPlayPriceText) {
        this.Productcode = Productcode;
        this.Productid = Productid;
        this.ProductName = ProductName;
        this.ProductTypeDesc = ProductTypeDesc;
        this.UOM = UOM;
        this.IsAllowCustomerReview = IsAllowCustomerReview;
        this.SellinPrice = SellinPrice;
        this.DiscountValue = DiscountValue;
        this.Mrp = Mrp;
        this.ProductImg = ProductImg;
        this.IsAttributeAdded = IsAttributeAdded;
        this.mQuantity = 0;
        this.RatingAverage = RatingAverage;
        this.DisPlayPriceText = DisPlayPriceText;
    }

    public String getProductid() {
        return Productid;
    }

    public void setProductid(String productid) {
        Productid = productid;
    }

    public String getProductcode() {
        return Productcode;
    }

    public void setProductcode(String productcode) {
        Productcode = productcode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductTypeDesc() {
        return ProductTypeDesc;
    }

    public void setProductTypeDesc(String productTypeDesc) {
        ProductTypeDesc = productTypeDesc;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getIsAllowCustomerReview() {
        return IsAllowCustomerReview;
    }

    public void setIsAllowCustomerReview(String isAllowCustomerReview) {
        IsAllowCustomerReview = isAllowCustomerReview;
    }

    public double getSellinPrice() {
        return SellinPrice;
    }

    public void setSellinPrice(double sellinPrice) {
        SellinPrice = sellinPrice;
    }

    public double getDiscountValue() {
        return DiscountValue;
    }

    public void setDiscountValue(double discountValue) {
        DiscountValue = discountValue;
    }

    public void addToQuantity() {
        this.mQuantity += 1;
    }

    public void removeFromQuantity() {
        if (this.mQuantity > 0) {
            this.mQuantity -= 1;
        }
    }
}
