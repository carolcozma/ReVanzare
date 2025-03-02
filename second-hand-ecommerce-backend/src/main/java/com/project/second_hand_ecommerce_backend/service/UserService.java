/** Clasa pentru gestionarea utilizatorilor
 * @author Cozma-Ivan Carol
 * @version 2 Ianuarie 2025
 */

package com.project.second_hand_ecommerce_backend.service;


import com.project.second_hand_ecommerce_backend.api.model.AuthenticationBody;
import com.project.second_hand_ecommerce_backend.api.model.RegistrationBody;
import com.project.second_hand_ecommerce_backend.exception.UserAlreadyExistsException;
import com.project.second_hand_ecommerce_backend.model.LocalUser;
import com.project.second_hand_ecommerce_backend.model.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;

    public UserService(LocalUserDAO localUserDAO){
        this.localUserDAO = localUserDAO;
    }

    public boolean emailExists(RegistrationBody registrationBody) {
        return localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent();
    }
    public boolean usernameExists(RegistrationBody registrationBody) {
        return localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent();
    }
    public LocalUser registerUser(RegistrationBody registrationBody){

        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastname(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(registrationBody.getPassword());
        return localUserDAO.save(user);

    }

    public LocalUser loginUser(AuthenticationBody authenticationBody){
        Optional<LocalUser> optionalUser = localUserDAO.findByEmailAndPassword(authenticationBody.getEmail(), authenticationBody.getPassword());
        LocalUser user = optionalUser.orElse(new LocalUser());
        return user;
    }

    public LocalUser getUserById(long id){
        Optional<LocalUser> optionalUser = localUserDAO.findById(id);
        LocalUser user = optionalUser.orElse(new LocalUser());
        return user;
    }

}
