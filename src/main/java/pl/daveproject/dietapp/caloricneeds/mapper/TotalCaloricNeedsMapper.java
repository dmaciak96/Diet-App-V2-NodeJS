package pl.daveproject.dietapp.caloricneeds.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.dietapp.caloricneeds.model.TotalCaloricNeeds;
import pl.daveproject.dietapp.caloricneeds.model.TotalCaloricNeedsDto;
import pl.daveproject.dietapp.caloricneeds.model.TotalCaloricNeedsRequest;

import java.util.List;


@Mapper
public interface TotalCaloricNeedsMapper {

    TotalCaloricNeedsDto entityToDto(TotalCaloricNeeds totalCaloricNeeds);

    List<TotalCaloricNeedsDto> entitiesToDtoList(List<TotalCaloricNeeds> totalCaloricNeedsList);

    TotalCaloricNeeds dtoToEntity(TotalCaloricNeedsDto dto);

    TotalCaloricNeedsDto requestToDto(TotalCaloricNeedsRequest request);
}
