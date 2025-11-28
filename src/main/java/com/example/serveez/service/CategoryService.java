package com.example.serveez.service;

import com.example.serveez.dto.request.CategoryRequest;
import com.example.serveez.dto.response.CategoryResponse;
import com.example.serveez.exception.BadRequestException;
import com.example.serveez.exception.ResourceNotFoundException;
import com.example.serveez.model.ServiceCategory;
import com.example.serveez.repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ServiceCategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Category with this name already exists");
        }

        ServiceCategory category = new ServiceCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIsActive(true);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getAllCategoriesAdmin() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse updateCategory(String id, CategoryRequest request) {
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUpdatedAt(LocalDateTime.now());

        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    public void deleteCategory(String id) {
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setIsActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    private CategoryResponse mapToResponse(ServiceCategory category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getIsActive(),
                category.getCreatedAt());
    }
}
