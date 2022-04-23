package com.epam.brest.faker.api;

import com.epam.brest.model.BasicEntity;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface FakerService<T extends BasicEntity> {

   List<T> getFakeData(Optional<Integer> amount, Optional<Locale> locale);

}
