package pl.daveproject.dietapp.caloricneeds.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daveproject.dietapp.bmi.model.UnitSystem;
import pl.daveproject.dietapp.caloricneeds.mapper.TotalCaloricNeedsMapper;
import pl.daveproject.dietapp.caloricneeds.model.TotalCaloricNeedsDto;
import pl.daveproject.dietapp.caloricneeds.repository.TotalCaloricNeedsRepository;
import pl.daveproject.dietapp.security.service.UserService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotalCaloricNeedsServiceImpl implements TotalCaloricNeedsService {

    private final TotalCaloricNeedsRepository totalCaloricNeedsRepository;
    private final TotalCaloricNeedsMapper totalCaloricNeedsMapper;
    private final UserService userService;

    @Override
    public List<TotalCaloricNeedsDto> findAll() {
        var currentUser = userService.getCurrentUser();
        var totalCaloricNeedsList = totalCaloricNeedsRepository.findAllByApplicationUserId(currentUser.getId());
        log.debug("Mapping {} entities to dto", totalCaloricNeedsList.size());
        return totalCaloricNeedsMapper.entitiesToDtoList(totalCaloricNeedsList);
    }

    @Override
    public Optional<TotalCaloricNeedsDto> findById(UUID id) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find total caloric needs by id {} for user {}", id, currentUser.getEmail());
        var totalCaloricNeeds = totalCaloricNeedsRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);
        return totalCaloricNeeds.map(totalCaloricNeedsMapper::entityToDto);
    }

    @Override
    public List<TotalCaloricNeedsDto> findByDate(LocalDate from, LocalDate to) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find total caloric needs by for user {} between {} and {}", currentUser.getEmail(), from, to);
        var totalCaloricNeedsList = totalCaloricNeedsRepository.findAllByApplicationUserIdAndAddedDateIsBetween(
                currentUser.getId(), from, to);
        log.debug("Mapping {} entities to dto", totalCaloricNeedsList.size());
        return totalCaloricNeedsMapper.entitiesToDtoList(totalCaloricNeedsList);
    }

    @Override
    public TotalCaloricNeedsDto save(TotalCaloricNeedsDto totalCaloricNeedsDto) {
        var currentUser = userService.getCurrentUser();
        totalCaloricNeedsDto.setAddedDate(LocalDate.now());
        totalCaloricNeedsDto.setValue(calculateTotalCaloricNeeds(totalCaloricNeedsDto));
        var entityToSave = totalCaloricNeedsMapper.dtoToEntity(totalCaloricNeedsDto);
        entityToSave.setApplicationUser(currentUser);
        var savedEntity = totalCaloricNeedsRepository.save(entityToSave);
        log.debug("Saved new total caloric needs {} - {}", savedEntity.getId(), savedEntity.getValue());
        return totalCaloricNeedsMapper.entityToDto(savedEntity);
    }

    private double calculateTotalCaloricNeeds(TotalCaloricNeedsDto totalCaloricNeedsDto) {
        var bmr = calculateBMR(totalCaloricNeedsDto);
        return bmr * totalCaloricNeedsDto.getActivityLevel().getPal();
    }

    private double calculateBMR(TotalCaloricNeedsDto totalCaloricNeedsDto) {
        var weight = totalCaloricNeedsDto.getWeight();
        var height = totalCaloricNeedsDto.getHeight();
        if (totalCaloricNeedsDto.getUnit() == UnitSystem.IMPERIAL) {
            height *= 2.54;
            weight *= 0.45359237;
        }
        var gender = totalCaloricNeedsDto.getGender();
        return gender.getMainPrefix() + (gender.getWeightPrefix() * weight) + (gender.getHeightPrefix()
                * height) - (gender.getAgePrefix() * totalCaloricNeedsDto.getAge());
    }

    @Override
    public void delete(TotalCaloricNeedsDto totalCaloricNeedsDto) {
        var currentUser = userService.getCurrentUser();
        if (totalCaloricNeedsRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), totalCaloricNeedsDto.getId())
                .isPresent()) {
            log.debug("Deleting total caloric needs {} - {}", totalCaloricNeedsDto.getAddedDate(),
                    totalCaloricNeedsDto.getValue());
            totalCaloricNeedsRepository.deleteById(totalCaloricNeedsDto.getId());
        }
    }

    @Override
    public Optional<TotalCaloricNeedsDto> findLatest() {
        return findAll().stream()
                .max(Comparator.comparing(TotalCaloricNeedsDto::getAddedDate));
    }
}
