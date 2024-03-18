package pl.daveproject.webdiet.bmi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daveproject.webdiet.bmi.mapper.BmiMapper;
import pl.daveproject.webdiet.bmi.model.BmiDto;
import pl.daveproject.webdiet.bmi.model.BmiRate;
import pl.daveproject.webdiet.bmi.model.UnitSystem;
import pl.daveproject.webdiet.bmi.repository.BmiRepository;
import pl.daveproject.webdiet.security.service.UserService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BmiServiceImpl implements BmiService {

    private final BmiRepository bmiRepository;
    private final BmiMapper bmiMapper;
    private final UserService userService;

    @Override
    public List<BmiDto> findAll() {
        var bmiList = bmiRepository.findAllByApplicationUserId(userService.getCurrentUser().getId());
        log.debug("Mapping {} entities to dto", bmiList.size());
        return bmiMapper.entitiesToDtoList(bmiList);
    }

    @Override
    public Optional<BmiDto> findById(UUID id) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find BMI by id {} for user {}", id, currentUser.getEmail());
        var bmi = bmiRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);
        return bmi.map(bmiMapper::entityToDto);
    }

    @Override
    public List<BmiDto> findByDate(LocalDate from, LocalDate to) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find BMI by for user {} between {} and {}", currentUser.getEmail(), from, to);
        var bmiList = bmiRepository.findAllByApplicationUserIdAndAddedDateIsBetween(currentUser.getId(), from, to);
        log.debug("Mapping {} entities to dto", bmiList.size());
        return bmiMapper.entitiesToDtoList(bmiList);
    }

    @Override
    public BmiDto save(BmiDto bmiDto) {
        var currentUser = userService.getCurrentUser();
        bmiDto.setValue(calculateBmi(bmiDto));
        bmiDto.setRate(calculateBmiRate(bmiDto.getValue()));
        bmiDto.setAddedDate(LocalDate.now());
        var entityToSave = bmiMapper.dtoToEntity(bmiDto);
        entityToSave.setApplicationUser(currentUser);
        var savedEntity = bmiRepository.save(entityToSave);
        log.debug("Saved new BMI {} - {}", savedEntity.getId(), savedEntity.getValue());
        return bmiMapper.entityToDto(savedEntity);
    }

    private double calculateBmi(BmiDto bmiDto) {
        var weight = bmiDto.getWeight();
        var height = bmiDto.getHeight();
        if (bmiDto.getUnit() == UnitSystem.IMPERIAL) {
            height *= 0.0254;
            weight *= 0.45359237;
        }
        return weight / Math.pow(height, 2);
    }

    private BmiRate calculateBmiRate(double bmi) {
        if (bmi <= 18.5) {
            return BmiRate.UNDERWEIGHT;
        } else if (bmi > 18.5 && bmi <= 25.0) {
            return BmiRate.CORRECT_VALUE;
        } else if (bmi > 25.0 && bmi <= 30.0) {
            return BmiRate.OVERWEIGHT;
        } else if (bmi > 30.0 && bmi <= 35.0) {
            return BmiRate.FIRST_OBESITY_DEGREE;
        } else if (bmi > 35.0 && bmi <= 40.0) {
            return BmiRate.SECOND_OBESITY_DEGREE;
        } else {
            return BmiRate.THIRD_OBESITY_DEGREE;
        }
    }

    @Override
    public void delete(BmiDto bmiDto) {
        var currentUser = userService.getCurrentUser();
        if (bmiRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), bmiDto.getId()).isPresent()) {
            log.debug("Deleting BMI {} - {}", bmiDto.getAddedDate(), bmiDto.getValue());
            bmiRepository.deleteById(bmiDto.getId());
        }
    }

    @Override
    public Optional<BmiDto> findLatest() {
        return findAll().stream()
                .max(Comparator.comparing(BmiDto::getAddedDate));
    }
}
