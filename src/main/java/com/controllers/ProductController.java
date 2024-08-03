package com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entities.Product;
import com.exceptions.ProdNotFoundException;
import com.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController 
{
	@Autowired
	ProductService pservice;
	
	
//	@GetMapping
//	public List<Product> getAllProducts()
//	{
//	    return pservice.getAllProducts();
//	}
	
	@GetMapping
    public List<Product> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page, 
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        return pservice.getAllProducts(pageable);
    }

	
	@GetMapping("/{id}")
	public Product getProductById(@PathVariable Integer id)
	{
		return pservice.getProductById(id).orElseThrow( () -> new ProdNotFoundException("Product Not Found"));
	}
	
	@PostMapping
	public Product createProduct(@RequestBody Product products) {
	    return pservice.createProduct(products);
	}
	
	@PostMapping("/save")
	public List<Product> saveProducts(@RequestBody List<Product> products) {
	    return pservice.saveProducts(products);
	}

	
	@PutMapping("/{id}")
	public Product updateProduct(@PathVariable Integer id, @RequestBody Product pDetails)
	{		
		return pservice.updateProduct(id, pDetails);
	}
	
	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable Integer id)
	{
		pservice.deleteProduct(id);
	}
}