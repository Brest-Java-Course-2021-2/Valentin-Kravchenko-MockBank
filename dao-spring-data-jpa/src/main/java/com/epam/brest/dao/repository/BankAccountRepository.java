package com.epam.brest.dao.repository;

import com.epam.brest.model.BankAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {

    Optional<BankAccount> findByNumber(String number);

}
