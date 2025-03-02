/** Clasa pentru gestionarea utilizatorilor
 * @author Cozma-Ivan Carol
 * @version 2 Ianuarie 2025
 */

package com.project.second_hand_ecommerce_backend.api.controller.auth;

import com.project.second_hand_ecommerce_backend.api.model.AuthenticationBody;
import com.project.second_hand_ecommerce_backend.api.model.RegistrationBody;
import com.project.second_hand_ecommerce_backend.model.LocalUser;
import com.project.second_hand_ecommerce_backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")

public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        RegistrationBody registrationBody = new RegistrationBody();
        model.addAttribute("registrationBody", registrationBody);
        return "auth/register/index";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationBody registrationBody, Model model) {
        boolean hasErrors = false;

        if (registrationBody.getUsername() == null || registrationBody.getUsername().isEmpty()) {
            model.addAttribute("usernameError", "Numele de utilizator este obligatoriu");
            hasErrors = true;
        } else if (registrationBody.getUsername().length() < 3 || registrationBody.getUsername().length() > 20) {
            model.addAttribute("usernameError", "Numele de utilizator trebuie să aibă între 3 și 20 de caractere");
            hasErrors = true;
        } else if (userService.usernameExists(registrationBody)) {
            model.addAttribute("usernameError", "Numele de utilizator există deja");
            hasErrors = true;
        }

        if (registrationBody.getPassword() == null || registrationBody.getPassword().isEmpty()) {
            model.addAttribute("passwordError", "Parola este obligatorie");
            hasErrors = true;
        } else if (registrationBody.getPassword().length() < 8) {
            model.addAttribute("passwordError", "Parola trebuie să aibă cel puțin 8 caractere");
            hasErrors = true;
        } else if (!containsUpperCase(registrationBody.getPassword())) {
            model.addAttribute("passwordError", "Parola trebuie să conțină cel puțin o literă mare");
            hasErrors = true;
        } else if (!containsLowerCase(registrationBody.getPassword())) {
            model.addAttribute("passwordError", "Parola trebuie să conțină cel puțin o literă mică");
            hasErrors = true;
        } else if (!containsDigit(registrationBody.getPassword())) {
            model.addAttribute("passwordError", "Parola trebuie să conțină cel puțin o cifră");
            hasErrors = true;
        } else if (!containsSpecialCharacter(registrationBody.getPassword())) {
            model.addAttribute("passwordError", "Parola trebuie să conțină cel puțin un caracter special (!@#$%^&*)");
            hasErrors = true;
        }

        if (registrationBody.getEmail() == null || registrationBody.getEmail().isEmpty()) {
            model.addAttribute("emailError", "Email-ul este obligatoriu");
            hasErrors = true;
        } else if (!isValidEmail(registrationBody.getEmail())) {
            model.addAttribute("emailError", "Email-ul trebuie să fie valid");
            hasErrors = true;
        } else if (userService.emailExists(registrationBody)) {
            model.addAttribute("emailError", "Email-ul există deja");
            hasErrors = true;
        }

        if (registrationBody.getFirstName() == null || registrationBody.getFirstName().isEmpty()) {
            model.addAttribute("firstNameError", "Prenumele este obligatoriu");
            hasErrors = true;
        }

        if (registrationBody.getLastName() == null || registrationBody.getLastName().isEmpty()) {
            model.addAttribute("lastNameError", "Numele de familie este obligatoriu");
            hasErrors = true;
        }

        if (hasErrors) {
            return "auth/register/index";
        } else {
            userService.registerUser(registrationBody);
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authenticationBody", new AuthenticationBody());
        return "auth/login/index";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute AuthenticationBody authenticationBody, Model model, HttpSession session) {

        LocalUser user =  userService.loginUser(authenticationBody);

        if(user.getId() != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/products/" + user.getId();
        }
        else
        {
            model.addAttribute("error", "Email or password is incorrect");
            return "auth/login/index";
        }


    }

    private boolean containsUpperCase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsLowerCase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDigit(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialCharacter(String str) {
        String specialCharacters = "!@#$%^&*";
        for (char c : str.toCharArray()) {
            if (specialCharacters.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
