package org.acme.place.service;

import lombok.extern.slf4j.Slf4j;
import org.acme.commons.dto.ReviewDto;
import org.acme.place.domain.Review;
import org.acme.place.repository.PlaceRepository;
import org.acme.place.repository.ReviewRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class ReviewService {

    @Inject
    ReviewRepository reviewRepository;
    @Inject
    PlaceRepository PlaceRepository;

    public static ReviewDto mapToDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getTitle(),
                review.getDescription(),
                review.getRating()
        );
    }

    public List<ReviewDto> findReviewsByPlaceId(Long id) {
        log.debug("Request to get all Reviews");
        return this.reviewRepository.findReviewsByPlaceId(id)
                .stream()
                .map(ReviewService::mapToDto)
                .collect(Collectors.toList());
    }

    public ReviewDto findById(Long id) {
        log.debug("Request to get Review : {}", id);
        return this.reviewRepository.findById(id)
                .map(ReviewService::mapToDto)
                .orElse(null);
    }

    public ReviewDto create(ReviewDto reviewDto, Long PlaceId) {
        log.debug("Request to create Review : {} ofr the Place {}", reviewDto, PlaceId);

        var Place = this.PlaceRepository.findById(PlaceId)
                .orElseThrow(() -> new IllegalStateException("Place with ID:" + PlaceId + " was not found !"));

        var savedReview = this.reviewRepository.saveAndFlush(
                new Review(
                        reviewDto.getTitle(),
                        reviewDto.getDescription(),
                        reviewDto.getRating()
                )
        );

        Place.getReviews().add(savedReview);
        this.PlaceRepository.saveAndFlush(Place);

        return mapToDto(savedReview);
    }

    public void delete(Long reviewId) {
        log.debug("Request to delete Review : {}", reviewId);

        var review = this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Place with ID:" + reviewId + " was not found !"));

        var Place = this.PlaceRepository.findPlaceByReviewId(reviewId);

        Place.getReviews().remove(review);

        this.PlaceRepository.saveAndFlush(Place);
        this.reviewRepository.delete(review);
    }
}
