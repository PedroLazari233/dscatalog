package com.pedrolazari.dscatalog.resources;

import com.pedrolazari.dscatalog.dto.ProductDTO;
import com.pedrolazari.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value="/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable)
    {
        Page<ProductDTO> productsDTO = productService.findAllPaged(pageable);
        return ResponseEntity.ok().body(productsDTO);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO ProductDTO = productService.findById(id);
        return ResponseEntity.ok().body(ProductDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insertProduct(@Valid @RequestBody ProductDTO productDTO){
        productDTO = productService.insertProduct(productDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(productDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(productDTO);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductDTO productDTO){
        productDTO = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok().body(productDTO);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
