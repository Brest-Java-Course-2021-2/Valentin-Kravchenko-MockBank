package com.epam.brest.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.service.BankAccountDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.epam.brest.constant.ControllerConstant.ACCOUNTS;

@Controller
@RequestMapping("/accounts")
public class BankAccountDtoController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoController.class);

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoController(BankAccountDtoService bankAccountDtoService) {
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @GetMapping()
    public String accounts(Model model) {
        LOGGER.debug("accounts(/accounts)");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        model.addAttribute(ACCOUNTS, accounts);
        LOGGER.debug("accounts(model={})", model);
        return ACCOUNTS;
    }

}
