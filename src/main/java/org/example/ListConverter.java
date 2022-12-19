package org.example;

import com.beust.jcommander.IStringConverter;

import java.util.List;

public class ListConverter implements IStringConverter<List<String>> {
    @Override
    public List<String> convert(String params) {
        return List.of(params.split(","));
    }
}

