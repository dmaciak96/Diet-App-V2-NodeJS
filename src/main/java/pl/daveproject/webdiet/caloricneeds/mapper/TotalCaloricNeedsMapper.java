package pl.daveproject.webdiet.caloricneeds.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.webdiet.caloricneeds.model.TotalCaloricNeeds;
import pl.daveproject.webdiet.caloricneeds.model.TotalCaloricNeedsDto;
import pl.daveproject.webdiet.caloricneeds.model.TotalCaloricNeedsRequest;

import java.util.List;


@Mapper
public interface TotalCaloricNeedsMapper {

    TotalCaloricNeedsDto entityToDto(TotalCaloricNeeds totalCaloricNeeds);

    List<TotalCaloricNeedsDto> entitiesToDtoList(List<TotalCaloricNeeds> totalCaloricNeedsList);

    TotalCaloricNeeds dtoToEntity(TotalCaloricNeedsDto dto);

    TotalCaloricNeedsDto requestToDto(TotalCaloricNeedsRequest request);
}
