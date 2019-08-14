package com.why.studio.auth.util;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public static <I, C> Collection<C> convertCollection(Collection<I> initialList, ConversionService conversionService,
                                                         Class<I> classFrom, Class<C> classTo) {
        if (initialList.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<C> convertedList = initialList.stream().map(e -> (C)conversionService.convert(
                e, TypeDescriptor.valueOf(classFrom), TypeDescriptor.valueOf(classTo)))
                .collect(Collectors.toSet());
        return convertedList;
    }

    public static <I, C> List<C> convertCollectionToList(Collection<I> initialList, ConversionService conversionService,
                                                         Class<I> classFrom, Class<C> classTo) {
        return new LinkedList<>(convertCollection(initialList, conversionService, classFrom, classTo));
    }

    public static UUID getUuid(String userId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserId должен быть в формате UUID v4");
        }
        return uuid;
    }
}
