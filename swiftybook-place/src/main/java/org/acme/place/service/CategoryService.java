package org.acme.place.service;


import lombok.extern.slf4j.Slf4j;
import org.acme.commons.dto.CategoryDto;
import org.acme.commons.dto.PlaceDto;
import org.acme.place.domain.Category;
import org.acme.place.repository.CategoryRepository;
import org.acme.place.repository.PlaceRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;
    @Inject
    PlaceRepository placeRepository;

    public static CategoryDto mapToDto(Category category, Long placesCount) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                placesCount
        );
    }

    public List<CategoryDto> findAll() {
        log.debug("Request to get all Categories");
        return this.categoryRepository.findAll()
                .stream()
                .map(category -> mapToDto(category, placeRepository.countAllByCategoryId(category.getId())))
                .collect(Collectors.toList());
    }

    public CategoryDto findById(Long id) {
        log.debug("Request to get Category : {}", id);
        return this.categoryRepository.findById(id)
                .map(category -> mapToDto(category, placeRepository.countAllByCategoryId(category.getId())))
                .orElse(null);
    }

    public CategoryDto create(CategoryDto categoryDto) {
        log.debug("Request to create Category : {}", categoryDto);
        return mapToDto(this.categoryRepository
                .save(new Category(categoryDto.getName(), categoryDto.getDescription())), 0L);
    }

    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        log.debug("Deleting all places for the Category : {}", id);
        this.placeRepository.deleteAllByCategoryId(id);
        log.debug("Deleting Category : {}", id);
        this.categoryRepository.deleteById(id);
    }

    public List<PlaceDto> findPlacesByCategoryId(Long id) {
        return this.placeRepository.findAllByCategoryId(id)
                .stream()
                .map(PlaceService::mapToDto)
                .collect(Collectors.toList());
    }
}
