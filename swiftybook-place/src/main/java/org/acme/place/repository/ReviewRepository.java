package org.acme.place.repository;

import org.acme.place.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select p.reviews from Place p where p.id = ?1")
    List<Review> findReviewsByPlaceId(Long id);
}
