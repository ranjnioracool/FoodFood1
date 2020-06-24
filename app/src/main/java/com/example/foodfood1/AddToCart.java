package com.example.foodfood1;

public class AddToCart {
    String nameOfFood,rateOfFood,quantityOfFood;
    String amount;
    public AddToCart(){}

    public AddToCart(String nameOfFood, String rateOfFood, String quantityOfFood, String amount) {
        this.nameOfFood = nameOfFood;
        this.rateOfFood = rateOfFood;
        this.quantityOfFood = quantityOfFood;
        this.amount = amount;
    }

    public String getNameOfFood() {
        return nameOfFood;
    }

    public String getRateOfFood() {
        return rateOfFood;
    }

    public String getQuantityOfFood() {
        return quantityOfFood;
    }

    public String getAmount() {
        return amount;
    }
}
