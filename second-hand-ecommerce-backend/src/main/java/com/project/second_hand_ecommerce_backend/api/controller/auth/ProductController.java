/** Clasa pentru gestionarea produselor
 * @author Cozma-Ivan Carol
 * @version 2 Ianuarie 2025
 */

package com.project.second_hand_ecommerce_backend.api.controller.auth;

import com.project.second_hand_ecommerce_backend.api.model.ProductBody;
import com.project.second_hand_ecommerce_backend.model.LocalUser;
import com.project.second_hand_ecommerce_backend.model.Product;
import com.project.second_hand_ecommerce_backend.service.ProductService;
import com.project.second_hand_ecommerce_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;
    public LocalUser currUser;
    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/registerProduct")
    public String showCreateProductForm(Model model) {
        ProductBody productBody = new ProductBody();
        model.addAttribute("productBody", productBody);
        return "products/registerProduct/index";
    }

    @PostMapping("/registerProduct")
    public String registerProduct(HttpServletRequest request, @ModelAttribute ProductBody productBody, Model model, @RequestParam("image") MultipartFile image) throws IOException {
        LocalUser currUser = (LocalUser) request.getSession().getAttribute("currUser");

        if (currUser == null) {
            return "redirect:/auth/login";
        }

        boolean hasErrors = false;

        if (productBody.getName() == null || productBody.getName().isEmpty()) {
            model.addAttribute("nameError", "Numele produsului este obligatoriu");
            hasErrors = true;
        } else if (productBody.getName().length() < 3 || productBody.getName().length() > 50) {
            model.addAttribute("nameError", "Numele produsului trebuie să aibă între 3 și 50 de caractere");
            hasErrors = true;
        }

        if (productBody.getDescription() == null || productBody.getDescription().isEmpty()) {
            model.addAttribute("descriptionError", "Descrierea produsului este obligatorie");
            hasErrors = true;
        } else if (productBody.getDescription().length() < 10 || productBody.getDescription().length() > 500) {
            model.addAttribute("descriptionError", "Descrierea produsului trebuie să aibă între 10 și 500 de caractere");
            hasErrors = true;
        }

        if (productBody.getPrice() <= 0) {
            model.addAttribute("priceError", "Prețul produsului trebuie să fie mai mare decât 0");
            hasErrors = true;
        }

        if (image == null || image.isEmpty()) {
            model.addAttribute("imageError", "Imaginea produsului este obligatorie");
            hasErrors = true;
        } else if (!image.getContentType().startsWith("image/")) {
            model.addAttribute("imageError", "Fișierul încărcat trebuie să fie o imagine");
            hasErrors = true;
        }

        if (hasErrors) {
            return "products/registerProduct/index";
        }

        String imagePath = saveImage(image);
        productService.registerProduct(productBody, currUser, imagePath);
        return "redirect:/products/" + currUser.getId();
    }

    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpServletRequest request) {

        LocalUser currUser = (LocalUser) request.getSession().getAttribute("currUser");

        productService.deleteProduct(id);

        return "redirect:/products/" + currUser.getId();
    }

    @GetMapping({"{id}", "/{id}"})
    public String getAllProducts(@PathVariable int id, HttpServletRequest request, Model model) {
        Iterable<Product> allProductsIterable = productService.getAllProducts();
        LocalUser currUser = userService.getUserById(id);

        request.getSession().setAttribute("currUser", currUser);

        Iterable<Product> myProductsIterable = productService.getMyProducts(currUser);

        List<Product> allProducts = StreamSupport.stream(allProductsIterable.spliterator(), false)
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .collect(Collectors.toList());

        List<Product> myProducts = StreamSupport.stream(myProductsIterable.spliterator(), false)
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .collect(Collectors.toList());

        model.addAttribute("myProducts", myProducts);
        model.addAttribute("allProducts", allProducts);
        System.out.println(allProducts);
        return "products/index";
    }

    @GetMapping("/editProduct/{id}")
    public String showEditProductForm(@PathVariable int id, Model model, HttpServletRequest request) {
        Product product = productService.getProductById(id);
        request.getSession().setAttribute("currProd", product);
        if (product != null) {

            ProductBody productBody = new ProductBody();
            productBody.setName(product.getName());
            productBody.setDescription(product.getDescription());
            productBody.setPrice(product.getPrice());
            productBody.setImagePath(product.getImagePath());
            model.addAttribute("productBody", productBody);
            return "products/editProduct/index";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("/updateProduct")
    public String updateProduct(HttpServletRequest request, @ModelAttribute ProductBody productBody, Model model, @RequestParam("image") MultipartFile image) throws IOException {
        LocalUser currUser = (LocalUser) request.getSession().getAttribute("currUser");
        Product product = (Product) request.getSession().getAttribute("currProd");
        if (currUser == null) {
            return "redirect:/auth/login";
        }

        boolean hasErrors = false;

        if (productBody.getName() == null || productBody.getName().isEmpty()) {
            model.addAttribute("nameError", "Product name is required");
            hasErrors = true;
        } else if (productBody.getName().length() < 3 || productBody.getName().length() > 50) {
            model.addAttribute("nameError", "Product name must be between 3 and 50 characters");
            hasErrors = true;
        }

        if (productBody.getDescription() == null || productBody.getDescription().isEmpty()) {
            model.addAttribute("descriptionError", "Product description is required");
            hasErrors = true;
        } else if (productBody.getDescription().length() < 10 || productBody.getDescription().length() > 500) {
            model.addAttribute("descriptionError", "Product description must be between 10 and 500 characters");
            hasErrors = true;
        }

        if (productBody.getPrice() <= 0) {
            model.addAttribute("priceError", "Product price must be greater than 0");
            hasErrors = true;
        }

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            if (!image.getContentType().startsWith("image/")) {
                model.addAttribute("imageError", "Uploaded file must be an image");
                hasErrors = true;
            } else {
                imagePath = saveImage(image);
            }
        }

        if (hasErrors) {
            return "products/editProduct/index";
        }
        productService.registerProduct(productBody, currUser, imagePath);
        productService.deleteProduct(product.getId());
        return "redirect:/products/" + currUser.getId();
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model, HttpServletRequest request) {
        LocalUser currUser = (LocalUser) request.getSession().getAttribute("currUser");

        if (currUser == null) {
            return "redirect:/auth/login";
        }

        List<Product> filteredProducts = productService.searchProduct(query);
        List<Product> myFilteredProducts = productService.searchMyProducts(query, currUser);

        model.addAttribute("myProducts", myFilteredProducts);
        model.addAttribute("allProducts", filteredProducts);
        return "products/index";
    }

    private String saveImage(MultipartFile image) throws IOException {
        Path uploadDir = Paths.get("src/main/resources/static/images");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalFilename = image.getOriginalFilename();
        Path path = uploadDir.resolve(originalFilename);

        Files.write(path, image.getBytes());

        return "/images/" + originalFilename;
    }
}
