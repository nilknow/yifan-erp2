package com.nilknow.yifanerp2.repository.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

@Converter
public class JsonbConverter implements AttributeConverter<String, Object> {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public Object convertToDatabaseColumn(String objectValue) {
    try {
      PGobject out = new PGobject();
      out.setType("jsonb");
      out.setValue(objectValue);
      return out;
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to serialize to json field ", e);
    }
  }

  @Override
  public String convertToEntityAttribute(Object dataValue) {
    try {
      if (dataValue instanceof PGobject && "jsonb".equals(((PGobject) dataValue).getType())) {
        return mapper.reader().readValue(((PGobject) dataValue).getValue());
      }
      return mapper.writeValueAsString(dataValue);
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to deserialize to json field ", e);
    }
  }
}