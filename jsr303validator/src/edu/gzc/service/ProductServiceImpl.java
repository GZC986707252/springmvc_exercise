package edu.gzc.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import edu.gzc.pojo.Product;

@Service
public class ProductServiceImpl implements ProductService {

	private static ArrayList<Product> products = new ArrayList<Product>();

	@Override
	public boolean add(Product product) {
		// TODO Auto-generated method stub
		products.add(product);
		return true;
	}

	@Override
	public ArrayList<Product> getProducts() {
		// TODO Auto-generated method stub
		return products;
	}

}
