package edu.gzc.service;

import java.util.ArrayList;

import edu.gzc.pojo.Product;

public interface ProductService {
	boolean add(Product product);

	ArrayList<Product> getProducts();
}
