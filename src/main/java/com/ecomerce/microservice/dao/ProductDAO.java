package com.ecomerce.microservice.dao;

import java.util.List;

import com.ecomerce.microservice.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer>{
    Product findById(int id);
    List<Product> findByPrixGreaterThan(int prixLimit);
}