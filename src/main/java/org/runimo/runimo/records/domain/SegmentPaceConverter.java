package org.runimo.runimo.records.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.runimo.runimo.records.service.usecases.dtos.SegmentPace;

import java.util.List;

public class SegmentPaceConverter implements AttributeConverter<List<SegmentPace>, String>
{
  private ObjectMapper objectMapper = new ObjectMapper();
  @Override
  public String convertToDatabaseColumn(List<SegmentPace> segmentPaces) {
    try {
      return objectMapper.writeValueAsString(segmentPaces);
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert segmentPaces to JSON", e);
    }
  }

  @Override
  public List<SegmentPace> convertToEntityAttribute(String s) {
    TypeReference<List<SegmentPace>> typeRef = new TypeReference<List<SegmentPace>>() {};
    try {
      if(s == null || s.isEmpty()) {
        return List.of();
      }
      return objectMapper.readValue(s, typeRef);
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert JSON to segmentPaces", e);
    }
  }
}
