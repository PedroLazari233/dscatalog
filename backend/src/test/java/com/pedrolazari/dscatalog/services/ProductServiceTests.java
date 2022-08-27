package com.pedrolazari.dscatalog.services;

import com.pedrolazari.dscatalog.dto.ProductDTO;
import com.pedrolazari.dscatalog.entities.Category;
import com.pedrolazari.dscatalog.entities.Product;
import com.pedrolazari.dscatalog.repositories.CategoryRepository;
import com.pedrolazari.dscatalog.repositories.ProductRepository;
import com.pedrolazari.dscatalog.services.exceptions.DataBaseException;
import com.pedrolazari.dscatalog.services.exceptions.ResourceNotFoundException;
import com.pedrolazari.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private long productExistingId;
    private long productNonExistingId;
    private long productDependentId;
    private long categoryExistingId;
    private long categoryNonExistingId;
    private long categoryDependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() throws Exception{
        productExistingId = 1L;
        productNonExistingId = 1000L;
        productDependentId = 3L;

        categoryExistingId = 2L;
        categoryNonExistingId = 1234L;
        categoryDependentId = 5L;

        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));

        when(categoryRepository.getOne(categoryExistingId)).thenReturn(category);
        when(categoryRepository.getOne(categoryNonExistingId)).thenThrow(EntityNotFoundException.class);

        when(productRepository.getOne(productExistingId)).thenReturn(product);
        when(productRepository.getOne(productNonExistingId)).thenThrow(EntityNotFoundException.class);

        when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        when(productRepository.findById(productExistingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(productNonExistingId)).thenReturn(Optional.empty());

        doNothing().when(productRepository).deleteById(productExistingId);
        doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(productNonExistingId);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(productDependentId);

    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = productService.updateProduct(productNonExistingId, productDTO);
        });

        verify(productRepository, times(1)).getOne(productNonExistingId);
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists(){

        ProductDTO result = productService.updateProduct(productExistingId, productDTO);

        Assertions.assertNotNull(result);

        verify(productRepository, times(1)).getOne(productExistingId);
        verify(productRepository, times(1)).save(product);
    }
    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = productService.findById(productNonExistingId);
        });

        verify(productRepository, times(1)).findById(productNonExistingId);
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists(){

        ProductDTO result = productService.findById(productExistingId);

        Assertions.assertNotNull(result);

        verify(productRepository, times(1)).findById(productExistingId);
    }

    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = productService.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent(){

        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.deleteProduct(productDependentId);
        });

        verify(productRepository, times(1)).deleteById(productDependentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(productNonExistingId);
        });

        verify(productRepository, times(1)).deleteById(productNonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {
            productService.deleteProduct(productExistingId);
        });

        verify(productRepository, times(1)).deleteById(productExistingId);
    }
}
