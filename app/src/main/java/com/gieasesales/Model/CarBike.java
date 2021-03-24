package com.gieasesales.Model;

import java.io.Serializable;

public class CarBike implements Serializable {

    String Productcode;

    public String getSellingPriceWords() {
        return SellingPriceWords;
    }

    public void setSellingPriceWords(String sellingPriceWords) {
        SellingPriceWords = sellingPriceWords;
    }

    String SellingPriceWords;

    public String getPOSLocation() {
        return POSLocation;
    }

    public void setPOSLocation(String POSLocation) {
        this.POSLocation = POSLocation;
    }

    String POSLocation;

    public String getPOSId() {
        return POSId;
    }

    public void setPOSId(String POSId) {
        this.POSId = POSId;
    }

    String POSId;

    public String getPOSName() {
        return POSName;
    }

    public void setPOSName(String POSName) {
        this.POSName = POSName;
    }

    String POSName;


    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    String City;

    public String getTransmission() {
        return Transmission;
    }

    public void setTransmission(String transmission) {
        Transmission = transmission;
    }

    String Transmission;

    public String getOwnership() {
        return Ownership;
    }

    public void setOwnership(String ownership) {
        Ownership = ownership;
    }

    String Ownership;

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String fuelType) {
        FuelType = fuelType;
    }

    String FuelType;

    public String getVehicleModel() {
        return VehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        VehicleModel = vehicleModel;
    }

    String VehicleModel;

    public String getKMDriven() {
        return KMDriven;
    }

    public void setKMDriven(String KMDriven) {
        this.KMDriven = KMDriven;
    }

    String KMDriven;

    public String getBodyType() {
        return BodyType;
    }

    public void setBodyType(String bodyType) {
        BodyType = bodyType;
    }

    String BodyType;

    public String getSaleType() {
        return SaleType;
    }

    public void setSaleType(String saleType) {
        SaleType = saleType;
    }

    String SaleType;

    public String getModelYear() {
        return ModelYear;
    }

    public void setModelYear(String modelYear) {
        ModelYear = modelYear;
    }

    String ModelYear;

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    String VehicleType;

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

    public CarBike() {
    }

    public CarBike(String Productcode, String Productid, String ProductName, String ProductTypeDesc, String UOM, String IsAllowCustomerReview, double SellinPrice, double DiscountValue, double Mrp, String ProductImg, String IsAttributeAdded, int RatingAverage, String DisPlayPriceText, String VehicleType, String ModelYear, String SaleType, String BodyType, String KMDriven, String VehicleModel, String FuelType, String Ownership, String Transmission, String City, String POSName,String POSId,String POSLocation,String SellingPriceWords) {
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
        this.VehicleType = VehicleType;
        this.ModelYear = ModelYear;
        this.SaleType = SaleType;
        this.BodyType = BodyType;
        this.KMDriven = KMDriven;
        this.VehicleModel = VehicleModel;
        this.FuelType = FuelType;
        this.Ownership = Ownership;
        this.Transmission = Transmission;
        this.City = City;
        this.POSName = POSName;
        this.POSId = POSId;
        this.POSLocation = POSLocation;
        this.SellingPriceWords = SellingPriceWords;
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
