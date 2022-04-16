package com.epam.brest.faker.api;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface FakerService<T> {

   List<T> getFakeData(Optional<Integer> dataVolume, Optional<Locale> locale);

}
