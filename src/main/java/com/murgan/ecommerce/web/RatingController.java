package com.murgan.ecommerce.web;

import com.murgan.ecommerce.domain.Product;
import com.murgan.ecommerce.domain.Rating;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.service.ProductService;
import com.murgan.ecommerce.service.RatingService;
import com.murgan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getRatingsForProduct(@PathVariable Long productId) {
        Product product = productService.requireById(productId);
        return ResponseEntity.ok(ratingService.getRatingsForProduct(product));
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<Rating> addOrUpdateRating(@PathVariable Long productId,
                                                    @RequestParam int rating,
                                                    @RequestParam String description,
                                                    Principal principal) {
        Product product = productService.requireById(productId);
        User user = userService.requireByUsernameOrEmail(principal.getName());
        Rating saved = ratingService.addOrUpdateRating(product, user, rating, description);
        return ResponseEntity.ok(saved);
    }
}
