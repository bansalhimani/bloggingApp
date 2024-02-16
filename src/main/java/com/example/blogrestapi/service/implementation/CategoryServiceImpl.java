package com.example.blogrestapi.service.implementation;

import com.example.blogrestapi.exception.ResourceNotFoundException;
import com.example.blogrestapi.model.Category;
import com.example.blogrestapi.payload.CategoryDto;
import com.example.blogrestapi.repository.CategoryRepository;
import com.example.blogrestapi.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found", "id", id));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories= categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category=categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found", "id", id));
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setId(id);
        Category updatedCategory=categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found", "id", id));
        categoryRepository.delete(category);
    }
}
