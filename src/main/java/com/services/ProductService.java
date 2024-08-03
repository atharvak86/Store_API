package com.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;
import com.entities.Category;
import com.entities.Product;
import com.exceptions.ProdNotFoundException;
import com.repositories.CategoryRepository;
import com.repositories.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class ProductService implements ProductServiceInterface
{
	@Autowired
	public ProductRepository prepo;
	@Autowired
	CategoryRepository crepo;
	@PersistenceContext
    private EntityManager em;

	public List<Product> getAllProducts(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int startRow = pageNumber * pageSize + 1;
        int endRow = startRow + pageSize - 1;

        String sql = "SELECT * FROM ( " +
                     "    SELECT p.*, ROWNUM rnum " +
                     "    FROM ( " +
                     "        SELECT * FROM products ORDER BY p_id " +
                     "    ) p " +
                     "    WHERE ROWNUM <= :endRow " +
                     ") WHERE rnum >= :startRow";

        Query query = em.createNativeQuery(sql, Product.class);
        query.setParameter("startRow", startRow);
        query.setParameter("endRow", endRow);

        List<Product> products = query.getResultList();

        // Get the total count of records
        String countSql = "SELECT COUNT(*) FROM products";
        Query countQuery = em.createNativeQuery(countSql);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        return products;
    }

	@Override
	public Optional<Product> getProductById(Integer id) {
		return prepo.findById(id);
	}

	@Override
	public Product createProduct(Product product) {
		Category category = crepo.findByName(product.getCategory().getName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(product.getCategory().getName());
                    return crepo.save(newCategory);
                });

        product.setCategory(category);
        return prepo.save(product);
	}
	
	
	public List<Product> saveProducts(List<Product> product) {
		for (Product prod : product) {
            Category category = crepo.findByName(prod.getCategory().getName())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(prod.getCategory().getName());
                        return crepo.save(newCategory);
                    });

            prod.setCategory(category);
        }
		return prepo.saveAll(product);
	}

	@Override
	public Product updateProduct(Integer id, Product pDetails) {
		Product product = prepo.findById(id).orElseThrow(() -> new ProdNotFoundException("Product not found"));
		product.setName(pDetails.getName());
		product.setDescription(pDetails.getDescription());
		product.setPrice(pDetails.getPrice());
		return prepo.save(product);
	}

	@Override
	public void deleteProduct(Integer id) {
		prepo.deleteById(id);		
	}

//	public List<Product> getAllProducts() {
//		return prepo.findAll();
//	}

}