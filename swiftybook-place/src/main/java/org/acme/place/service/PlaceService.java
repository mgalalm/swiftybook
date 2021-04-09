package org.acme.place.service;

import lombok.extern.slf4j.Slf4j;
import org.acme.commons.dto.PlaceDto;
import org.acme.place.domain.Place;
import org.acme.place.domain.enums.PlaceStatus;
import org.acme.place.repository.CategoryRepository;
import org.acme.place.repository.PlaceRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class PlaceService {

    @Inject
    PlaceRepository PlaceRepository;
    @Inject
    CategoryRepository categoryRepository;

    @Inject
    CategoryRepository bookingRepository;

    public static PlaceDto mapToDto(Place place) {
        return new PlaceDto(
                place.getId(),
                place.getTitle(),
                place.getDescription(),
                place.getStatus().name(),
                place.getPrice(),
                place.getAvailableFrom(),
                place.getAvailableTo(),
                AddressService.mapToDto(place.getAddress()),
                place.getReviews().stream().map(ReviewService::mapToDto).collect(Collectors.toSet()),
                place.getCategory().getId(),
                place.getImageUrl()
        );
    }

    public List<PlaceDto> findAll() {
        log.debug("Request to get all Places");
        return this.PlaceRepository.findAll()
                .stream()
                .map(PlaceService::mapToDto)
                .collect(Collectors.toList());
    }

    public PlaceDto findById(Long id) {
        log.debug("Request to get Place : {}", id);
        return this.PlaceRepository.findById(id)
                .map(PlaceService::mapToDto)
                .orElse(null);
    }

    public PlaceDto create(PlaceDto placeDTo) {
        log.debug("Request to create Place : {}", placeDTo);

        return mapToDto(this.PlaceRepository.save(
                new Place(
                        placeDTo.getTitle(),
                        placeDTo.getDescription(),
                        placeDTo.getPrice(),
                        PlaceStatus.valueOf(placeDTo.getStatus()),
                        placeDTo.getAvailable_from(),
                        placeDTo.getAvailable_to(),
                        placeDTo.getImage_url(),
                        AddressService.createFromDto(placeDTo.getAddress()),
                        Collections.emptySet(),
                        categoryRepository.findById(placeDTo.getCategoryId()).orElse(null)
                )));
    }

    public void delete(Long id) {
        log.debug("Request to delete Place : {}", id);
        this.PlaceRepository.deleteById(id);
    }

    public List<PlaceDto> findByCategoryId(Long id) {
        return this.PlaceRepository.findByCategoryId(id).stream()
                .map(PlaceService::mapToDto)
                .collect(Collectors.toList());
    }

    public Long countAll() {
        return this.PlaceRepository.count();
    }

    public Long countByCategoryId(Long id) {
        return this.PlaceRepository.countAllByCategoryId(id);
    }
}
