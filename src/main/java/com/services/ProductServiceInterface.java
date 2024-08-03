package com.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.entities.Product;

public interface ProductServiceInterface 
{
	public List<Product> getAllProducts(Pageable pageable);
	
	public Optional<Product> getProductById(Integer id);
	
	public Product createProduct(Product product);
	
	public Product updateProduct(Integer id, Product productDetails);
	
	public void deleteProduct(Integer id);
}