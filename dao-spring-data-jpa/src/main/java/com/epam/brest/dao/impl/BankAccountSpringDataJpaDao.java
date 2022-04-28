package com.epam.brest.dao.impl;

import com.epam.brest.dao.api.BankAccountDao;
import com.epam.brest.dao.repository.BankAccountRepository;
import com.epam.brest.model.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.toIntExact;

@Repository
public class BankAccountSpringDataJpaDao implements BankAccountDao {
    
    private final BankAccountRepository bankAccountRepository;

    public BankAccountSpringDataJpaDao(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<BankAccount> getAll() {
        return (List<BankAccount>) bankAccountRepository.findAll();
    }

    @Override
    public Optional<BankAccount> getById(Integer id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public Optional<BankAccount> getByNumber(String number) {
        return bankAccountRepository.findByNumber(number);
    }

    @Override
    public BankAccount create(BankAccount entity) {
        return bankAccountRepository.save(entity);
    }

    @Override
    public Integer update(BankAccount entity) {
        return null;
    }

    @Override
    public Integer delete(Integer id) {
        bankAccountRepository.deleteById(id);
        return id;
    }

    @Override
    public Integer count() {
        return toIntExact(bankAccountRepository.count());
    }

    @Override
    public boolean isAccountNumberExists(String number) {
        return false;
    }

}
