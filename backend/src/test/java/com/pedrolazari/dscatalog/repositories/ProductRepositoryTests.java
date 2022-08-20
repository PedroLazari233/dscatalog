package com.pedrolazari.dscatalog.repositories;

import com.pedrolazari.dscatalog.entities.Product;
import com.pedrolazari.dscatalog.services.exceptions.ResourceNotFoundException;
import com.pedrolazari.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private long nonExistingId;
    private long existingId;
    private  long countTotalProducts;

    @BeforeEach
    void  setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void findShouldReturnAProductOptionalWhenExistingId(){

        Optional<Product> productOptional = productRepository.findById(existingId);

        Assertions.assertTrue(productOptional.isPresent());
    }

    @Test
    public void findShouldThrowResourceNotFoundExceptionOptionalWhenNonExistingId(){

        Optional<Product> productOptional = productRepository.findById(nonExistingId);

        Assertions.assertTrue(productOptional.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        productRepository.deleteById(existingId);

        Optional<Product> product = productRepository.findById(existingId);
        Assertions.assertTrue(product.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(nonExistingId);
        });
    }
}
