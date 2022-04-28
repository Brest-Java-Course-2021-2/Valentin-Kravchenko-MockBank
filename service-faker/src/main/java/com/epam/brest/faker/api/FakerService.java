package com.epam.brest.faker.api;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface FakerService<T> {

   List<T> getFakeData(Optional<Integer> amount, Optional<Locale> locale);

   default List<T> getFakeData() {
      return getFakeData(Optional.empty(), Optional.empty());
   }

   default List<T> getFakeData(Optional<Integer> amount) {
      return getFakeData(amount, Optional.empty());
   }

}
