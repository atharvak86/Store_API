package com.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.entities.Category;
import com.exceptions.ProdNotFoundException;
import com.repositories.CategoryRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class CategoryService implements CategoryServiceInterface
{

    @Autowired
    private CategoryRepository crepo;
    
    @PersistenceContext
    private EntityManager em;

    public List<Category> getAllCategories(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int startRow = pageNumber * pageSize + 1;
        int endRow = startRow + pageSize - 1;

        String sql = "SELECT * FROM ( " +
                     "    SELECT c.*, ROWNUM rnum " +
                     "    FROM ( " +
                     "        SELECT * FROM category ORDER BY id " +
                     "    ) c " +
                     "    WHERE ROWNUM <= :endRow " +
                     ") WHERE rnum >= :startRow";

        Query query = em.createNativeQuery(sql, Category.class);
        query.setParameter("startRow", startRow);
        query.setParameter("endRow", endRow);

        List<Category> categories = query.getResultList();

        // Get the total count of records
        String countSql = "SELECT COUNT(*) FROM category";
        Query countQuery = em.createNativeQuery(countSql);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        return query.getResultList();
    
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        return crepo.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return crepo.save(category);
    }

    @Override
    public Category updateCategory(Integer id, Category categoryDetails) {
        Category category = crepo.findById(id).orElseThrow(() -> new ProdNotFoundException("Category not found"));
        category.setName(categoryDetails.getName());
        return crepo.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {
    	crepo.deleteById(id);
    }
}