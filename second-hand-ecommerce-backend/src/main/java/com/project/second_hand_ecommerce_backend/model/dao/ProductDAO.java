package com.project.second_hand_ecommerce_backend.model.dao;

import com.project.second_hand_ecommerce_backend.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductDAO extends CrudRepository <Product, Long>{

    Optional<Product> findByIdNotNull();

    List<Product> findByUserID_Id(Long id);

    List<Product> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrderByDateDesc(String name, String description);


    List<Product> findByUserID_IdAndNameContainsIgnoreCaseOrderByDateDesc(Long id, String name);

    List<Product> findByNameContainsIgnoreCaseOrderByDateDesc(String name);


}
