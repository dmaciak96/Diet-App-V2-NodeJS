package pl.daveproject.dietapp.bmi.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.dietapp.bmi.model.Bmi;
import pl.daveproject.dietapp.bmi.model.BmiDto;
import pl.daveproject.dietapp.bmi.model.BmiRequest;

import java.util.List;

@Mapper
public interface BmiMapper {

    BmiDto entityToDto(Bmi bmi);

    List<BmiDto> entitiesToDtoList(List<Bmi> bmiList);

    Bmi dtoToEntity(BmiDto dto);

    BmiDto requestToDto(BmiRequest request);
}
