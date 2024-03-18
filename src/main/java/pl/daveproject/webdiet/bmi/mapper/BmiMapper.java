package pl.daveproject.webdiet.bmi.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.webdiet.bmi.model.Bmi;
import pl.daveproject.webdiet.bmi.model.BmiDto;
import pl.daveproject.webdiet.bmi.model.BmiRequest;

import java.util.List;

@Mapper
public interface BmiMapper {

    BmiDto entityToDto(Bmi bmi);

    List<BmiDto> entitiesToDtoList(List<Bmi> bmiList);

    Bmi dtoToEntity(BmiDto dto);

    BmiDto requestToDto(BmiRequest request);
}
