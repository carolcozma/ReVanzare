/** Clasa pentru gestionarea produselor
 * @author Cozma-Ivan Carol
 * @version 2 Ianuarie 2025
 */

package com.project.second_hand_ecommerce_backend.service;

import com.project.second_hand_ecommerce_backend.api.model.ProductBody;
import com.project.second_hand_ecommerce_backend.model.LocalUser;
import com.project.second_hand_ecommerce_backend.model.Product;
import com.project.second_hand_ecommerce_backend.model.dao.LocalUserDAO;
import com.project.second_hand_ecommerce_backend.model.dao.ProductDAO;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductDAO productDAO;
    private final LocalUserDAO localUserDAO;

    public ProductService(ProductDAO productDAO,
                          LocalUserDAO localUserDAO) {
        this.productDAO = productDAO;
        this.localUserDAO = localUserDAO;
    }

    public void registerProduct(ProductBody productBody, LocalUser user, String imagePath)
    {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);

        Product product = new Product();
        product.setName(productBody.getName());
        product.setDescription(productBody.getDescription());
        product.setPrice(productBody.getPrice());
        product.setDate(date);
        product.setUserID(user);
        product.setImagePath(imagePath);
        productDAO.save(product);
    }


    public Product getProductById(long id){
        Optional<Product> optionalProduct = productDAO.findById(id);
        Product product = optionalProduct.orElse(new  Product());

        return product;
    }

    public List<Product> searchProduct(String query) {
        //return productDAO.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(query,query);
        //return productDAO.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrderByDateDesc(query,query);
        return productDAO.findByNameContainsIgnoreCaseOrderByDateDesc(query);
    }

    public List<Product> searchMyProducts(String query, LocalUser user)
    {
        //return productDAO.findByUserID_IdAndNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(user.getId(), query, query);
        //return productDAO.findByUserID_IdAndNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrderByDateDesc(user.getId(), query, query);
        return productDAO.findByUserID_IdAndNameContainsIgnoreCaseOrderByDateDesc(user.getId(), query);
    }

    public void deleteProduct(long productID) {
        productDAO.deleteById(productID);
    }

    public Iterable<Product> getAllProducts(){
        return productDAO.findAll();
    }

    public List<Product> getMyProducts(LocalUser user) {
        return productDAO.findByUserID_Id(user.getId());
    }



}
