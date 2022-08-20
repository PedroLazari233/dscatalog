package com.pedrolazari.dscatalog.services;

import com.pedrolazari.dscatalog.dto.CategoryDTO;
import com.pedrolazari.dscatalog.entities.Category;
import com.pedrolazari.dscatalog.repositories.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable){
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map( category -> new CategoryDTO(category));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insertCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        try{
            Category category = categoryRepository.getOne(id);
            category.setName(categoryDTO.getName());
            category = categoryRepository.save(category);
            return new CategoryDTO(category);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void deleteCategory(Long id) {
        try{
            categoryRepository.deleteById(id);
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
