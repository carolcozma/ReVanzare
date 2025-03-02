package com.project.second_hand_ecommerce_backend.model.dao;

import org.springframework.data.repository.CrudRepository;
import com.project.second_hand_ecommerce_backend.model.LocalUser;

import java.util.Optional;

public interface LocalUserDAO extends CrudRepository<LocalUser, Long>{


    Optional<LocalUser> findByUsernameIgnoreCase(String username);

    Optional<LocalUser> findByEmailIgnoreCase(String email);

    Optional<LocalUser> findByEmailAndPassword(String email, String password);

}
