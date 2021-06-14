package com.integration.poc.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ObjectToStringConverter implements AttributeConverter<Object, String> {

  @Override
  public String convertToDatabaseColumn(Object attribute) {
    return String.valueOf(attribute);
  }

  @Override
  public Object convertToEntityAttribute(String dbData) {
    return dbData;
  }


}
