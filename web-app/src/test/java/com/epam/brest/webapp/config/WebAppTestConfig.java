package com.epam.brest.webapp.config;

import com.epam.brest.service.api.BankAccountDtoService;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.api.CreditCardDtoService;
import com.epam.brest.service.api.CreditCardService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WebAppTestConfig {

    private final BankAccountService bankAccountServiceImpl;
    private final BankAccountDtoService bankAccountDtoServiceImpl;
    private final CreditCardService creditCardServiceImpl;
    private final CreditCardDtoService creditCardDtoServiceImpl;

    public WebAppTestConfig(BankAccountService bankAccountServiceImpl,
                            BankAccountDtoService bankAccountDtoServiceImpl,
                            CreditCardService creditCardServiceImpl,
                            CreditCardDtoService creditCardDtoServiceImpl) {
        this.bankAccountServiceImpl = bankAccountServiceImpl;
        this.bankAccountDtoServiceImpl = bankAccountDtoServiceImpl;
        this.creditCardServiceImpl = creditCardServiceImpl;
        this.creditCardDtoServiceImpl = creditCardDtoServiceImpl;
    }

    @Bean
    BankAccountService bankAccountServiceRest() {
        return bankAccountServiceImpl;
    }

    @Bean
    BankAccountDtoService bankAccountDtoServiceRest() {
        return bankAccountDtoServiceImpl;
    }

    @Bean
    CreditCardService creditCardServiceRest() {
        return creditCardServiceImpl;
    }

    @Bean
    CreditCardDtoService creditCardDtoServiceRest() {
        return creditCardDtoServiceImpl;
    }

}
