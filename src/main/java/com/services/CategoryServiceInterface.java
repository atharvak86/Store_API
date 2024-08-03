package com.services;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.entities.Category;

public interface CategoryServiceInterface 
{
	public List<Category> getAllCategories(Pageable pageable);
	
	public Optional<Category> getCategoryById(Integer id);
	
	public Category createCategory(Category category);
	
	public Category updateCategory(Integer id, Category categoryDetails);
	
	public void deleteCategory(Integer id);
}