package com.pedrolazari.dscatalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrolazari.dscatalog.dto.ProductDTO;
import com.pedrolazari.dscatalog.services.ProductService;
import com.pedrolazari.dscatalog.services.exceptions.DataBaseException;
import com.pedrolazari.dscatalog.services.exceptions.ResourceNotFoundException;
import com.pedrolazari.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long productExistingId;
    private Long productNonExistingId;
    private Long productDependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception{

        productExistingId = 1L;
        productNonExistingId = 1000L;
        productDependentId = 5L;

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(productService.findAllPaged(any())).thenReturn(page);

        when(productService.findById(eq(productExistingId))).thenReturn(productDTO);
        when(productService.findById(eq(productNonExistingId))).thenThrow(ResourceNotFoundException.class);

        when(productService.updateProduct(eq(productExistingId), any())).thenReturn(productDTO);
        when(productService.updateProduct(eq(productNonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        when(productService.insertProduct(any())).thenReturn(productDTO);

        doNothing().when(productService).deleteProduct(productExistingId);
        doThrow(ResourceNotFoundException.class).when(productService).deleteProduct(productNonExistingId);
        doThrow(DataBaseException.class).when(productService).deleteProduct(productDependentId);
    }

    @Test
    public void deleteProductShouldReturnBadRequestWhenIdIsDependent() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(delete("/products/{id}", productDependentId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(delete("/products/{id}", productNonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteProductShouldReturnNothingWhenIdExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(delete("/products/{id}", productExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void insertProductShouldReturnProductDTOCreated() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products").content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", productExistingId).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", productNonExistingId).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", productNonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", productExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findAllPagedShouldReturnPage() throws Exception{

        ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }
}
