package pl.daveproject.dietapp.bmi.service;

import pl.daveproject.dietapp.bmi.model.BmiDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BmiService {
    List<BmiDto> findAll();

    Optional<BmiDto> findById(UUID id);

    List<BmiDto> findByDate(LocalDate from, LocalDate to);

    BmiDto save(BmiDto bmiDto);

    void delete(BmiDto bmiDto);

    Optional<BmiDto> findLatest();
}
