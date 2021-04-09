package org.acme.place.repository;

import org.acme.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByCategoryId(Long categoryId);

    Long countAllByCategoryId(Long categoryId);

    @Query("select p from Place p JOIN p.reviews r WHERE r.id = ?1")
    Place findPlaceByReviewId(Long reviewId);

    void deleteAllByCategoryId(Long id);

    List<Place> findAllByCategoryId(Long id);
}
