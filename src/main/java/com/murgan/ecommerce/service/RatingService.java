package com.murgan.ecommerce.service;

import com.murgan.ecommerce.domain.Product;
import com.murgan.ecommerce.domain.Rating;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getRatingsForProduct(Product product) {
        return ratingRepository.findByProduct(product);
    }

    public Optional<Rating> getRatingByProductAndUser(Product product, User user) {
        return ratingRepository.findByProductAndUser(product, user);
    }

    public Rating addOrUpdateRating(Product product, User user, int rating, String description) {
        Optional<Rating> existing = ratingRepository.findByProductAndUser(product, user);
        Rating r = existing.orElse(new Rating());
        r.setProduct(product);
        r.setUser(user);
        r.setRating(rating);
        r.setDescription(description);
        return ratingRepository.save(r);
    }
}
