package com.example.blogrestapi.repository;

import com.example.blogrestapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
