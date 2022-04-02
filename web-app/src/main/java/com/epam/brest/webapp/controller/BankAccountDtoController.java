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
    private String filterError;

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
        LOGGER.debug("accountsPOST(/accounts, bankAccountFilterDto={})", bankAccountFilterDto);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("accountsPOST(/accounts, errorFields={})", ControllerUtils.extractErrorFields(bindingResult));
            return ACCOUNTS;
        }
        List<BankAccountDto> accounts = bankAccountDtoService.getAllWithTotalCards(bankAccountFilterDto);
        if (accounts.isEmpty()) {
            LOGGER.warn("accountsPOST(/accounts, accounts={})", accounts);
            model.addAttribute(ERROR, filterError);
        }
        model.addAttribute(ACCOUNTS, accounts);
        LOGGER.debug("accountsPOST(model={})", model);
        return ACCOUNTS;
    }

}
