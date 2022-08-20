package com.pedrolazari.dscatalog.services;

import com.pedrolazari.dscatalog.dto.CategoryDTO;
import com.pedrolazari.dscatalog.dto.ProductDTO;
import com.pedrolazari.dscatalog.entities.Category;
import com.pedrolazari.dscatalog.entities.Product;
import com.pedrolazari.dscatalog.repositories.CategoryRepository;
import com.pedrolazari.dscatalog.repositories.ProductRepository;
import com.pedrolazari.dscatalog.services.exceptions.DataBaseException;
import com.pedrolazari.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository ProductRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable){
        Page<Product> products = ProductRepository.findAll(pageable);
        return products.map( product -> new ProductDTO(product));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> productOptional = ProductRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insertProduct(ProductDTO productDTO) {
        Product product = new Product();
        copyDtoToEntity(productDTO, product);
        product = ProductRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        try{
            Product product = ProductRepository.getOne(id);
            copyDtoToEntity(productDTO, product);
            product = ProductRepository.save(product);
            return new ProductDTO(product);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void deleteProduct(Long id) {
        try{
            ProductRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e)
        {
            throw  new ResourceNotFoundException("Id not found " + id);
        }
        catch (DataIntegrityViolationException e)
        {
            throw  new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product product){

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setDate(productDTO.getDate());
        product.setImgUrl(productDTO.getImgUrl());
        product.setPrice(productDTO.getPrice());

        product.getCategories().clear();
        for (CategoryDTO catDTO : productDTO.getCategories()){
            Category category = categoryRepository.getOne(catDTO.getId());
            product.getCategories().add(category);
        }
    }
}
