package com.murgan.ecommerce.repository;

import com.murgan.ecommerce.domain.Rating;
import com.murgan.ecommerce.domain.Product;
import com.murgan.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProduct(Product product);
    Optional<Rating> findByProductAndUser(Product product, User user);
}
