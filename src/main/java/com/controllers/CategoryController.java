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

import com.entities.Category;
import com.exceptions.ProdNotFoundException;
import com.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController 
{
	@Autowired
	CategoryService cservice;
	
	@GetMapping
    public List<Category> getAllCategories(
    		@RequestParam(value = "page", defaultValue = "0") int page, 
    		@RequestParam(value = "size", defaultValue = "5") int size) 
	{
        Pageable pageable = PageRequest.of(page, size);
        return cservice.getAllCategories(pageable);
    }
	
	@PostMapping
	public Category createCategory(@RequestBody Category cat)
	{
		return cservice.createCategory(cat);
	}
	
	@GetMapping("/{id}")
	public Category getCategoryById(@PathVariable Integer id)
	{
		return cservice.getCategoryById(id).orElseThrow( () -> new ProdNotFoundException("Category Not Found"));
	}
	
	@PutMapping("/{id}")
	public Category updateCategory(@PathVariable Integer id, @RequestBody Category cDetails)
	{
		return cservice.updateCategory(id, cDetails);
	}
	
	@DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        cservice.deleteCategory(id);
    }
}