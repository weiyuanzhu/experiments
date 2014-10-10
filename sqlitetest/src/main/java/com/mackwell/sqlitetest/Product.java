package com.mackwell.sqlitetest;

/**
 * Created by weiyuan zhu on 10/10/14.
 */
public class Product {
    private int _id;
    private String _productName;
    private int _quantity;

    public Product()
    {

    }

    public Product(int id, String productName, int quantity)
    {
        this._id = id;
        this._productName = productName;
        this._quantity = quantity;
    }

    public Product(String productName, int quantity)
    {
        this._productName = productName;
        this._quantity = quantity;
    }

    //getter and setters

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_productName() {
        return _productName;
    }

    public void set_productName(String _productName) {
        this._productName = _productName;
    }

    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }
}
