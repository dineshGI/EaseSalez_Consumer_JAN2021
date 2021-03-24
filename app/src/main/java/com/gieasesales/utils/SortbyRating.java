package com.gieasesales.utils;

import com.gieasesales.Model.Products;

import java.util.Comparator;

public class SortbyRating implements Comparator<Products> {
    public int compare(Products a, Products b) {
        return (int) (a.getSellinPrice() - b.getSellinPrice());
    }
}
