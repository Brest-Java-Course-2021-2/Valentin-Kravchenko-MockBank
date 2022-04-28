package com.epam.brest.webapp.controller;

import com.epam.brest.model.BankAccountDto;
import com.epam.brest.model.BankAccountFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import com.epam.brest.webapp.util.ControllerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.epam.brest.webapp.constant.ControllerConstant.*;

@Controller
@RequestMapping("/accounts")
public class BankAccountDtoController {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoController.class);

    private final BankAccountDtoService bankAccountDtoService;
    private final Validator validator;

    @Value("${account.filter.error}")
    private String filterErrorMessage;

    public BankAccountDtoController(BankAccountDtoService bankAccountDtoServiceRest,
                                    Validator bankAccountFilterDtoValidator) {
        this.bankAccountDtoService = bankAccountDtoServiceRest;
        this.validator = bankAccountFilterDtoValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public String accounts(Model model) {
        LOGGER.debug("accountsGET(/accounts)");
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards();
        model.addAttribute(ACCOUNTS, accounts);
        LOGGER.debug("accountsGET(model={})", model);
        return ACCOUNTS;
    }

    @PostMapping
    public String accounts(@Valid @ModelAttribute(FILTER) BankAccountFilterDto bankAccountFilterDto,
                           BindingResult bindingResult,
                           Model model) {
        LOGGER.debug("accounts(/accounts, method=POST, bankAccountFilterDto={})", bankAccountFilterDto);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("accounts(/accounts, method=POST, errorFields={})", ControllerUtils.extractErrorFields(bindingResult));
            return ACCOUNTS;
        }
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        LOGGER.debug("accounts(/accounts, method=POST, accounts={})", accounts);
        if (accounts.isEmpty()) {
            model.addAttribute(ERROR, filterErrorMessage);
        }
        model.addAttribute(ACCOUNTS, accounts);
        LOGGER.debug("accounts(/accounts, method=POST, model={})", model);
        return ACCOUNTS;
    }

}
