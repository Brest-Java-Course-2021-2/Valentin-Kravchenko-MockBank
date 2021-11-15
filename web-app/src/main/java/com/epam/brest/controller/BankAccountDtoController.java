package com.epam.brest.controller;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.service.BankAccountDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/accounts")
public class BankAccountDtoController {

    private final BankAccountDtoService bankAccountDtoService;

    public BankAccountDtoController(BankAccountDtoService bankAccountDtoService) {
        this.bankAccountDtoService = bankAccountDtoService;
    }

    @GetMapping()
    public String accounts(Model model) {
        List<BankAccountDto> accountDtos = bankAccountDtoService.getAllWithTotalCards();
        model.addAttribute("accountDtos", accountDtos);
        return "accounts";
    }

}
