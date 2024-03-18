package pl.daveproject.webdiet.caloricneeds.service;


import pl.daveproject.webdiet.caloricneeds.model.TotalCaloricNeedsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TotalCaloricNeedsService {

    List<TotalCaloricNeedsDto> findAll();

    Optional<TotalCaloricNeedsDto> findById(UUID id);

    List<TotalCaloricNeedsDto> findByDate(LocalDate from, LocalDate to);

    TotalCaloricNeedsDto save(TotalCaloricNeedsDto totalCaloricNeedsDto);

    void delete(TotalCaloricNeedsDto totalCaloricNeedsDto);

    Optional<TotalCaloricNeedsDto> findLatest();
}
