package com.pedrolazari.dscatalog.services;

import com.pedrolazari.dscatalog.dto.ProductDTO;
import com.pedrolazari.dscatalog.entities.Product;
import com.pedrolazari.dscatalog.repositories.ProductRepository;
import com.pedrolazari.dscatalog.services.exceptions.DataBaseException;
import com.pedrolazari.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository ProductRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> products = ProductRepository.findAll(pageRequest);
        return products.map( Product -> new ProductDTO(Product));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> productOptional = ProductRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insertProduct(ProductDTO ProductDTO) {
        Product Product = new Product();
        //Product.setName(ProductDTO.getName());
        Product = ProductRepository.save(Product);
        return new ProductDTO(Product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO ProductDTO) {
        try{
            Product Product = ProductRepository.getOne(id);
            //Product.setName(ProductDTO.getName());
            Product = ProductRepository.save(Product);
            return new ProductDTO(Product);
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
}
