package com.patika.kredinbizdeservice.service.impl;

import com.patika.kredinbizdeservice.controller.model.BankDto;
import com.patika.kredinbizdeservice.controller.model.IdRequestDto;
import com.patika.kredinbizdeservice.exceptions.BusinessException;
import com.patika.kredinbizdeservice.mapper.ModelMapper;
import com.patika.kredinbizdeservice.model.Bank;
import com.patika.kredinbizdeservice.model.CreditCard;
import com.patika.kredinbizdeservice.repository.BankRepository;
import com.patika.kredinbizdeservice.repository.CreditCardRepository;
import com.patika.kredinbizdeservice.service.IBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankService implements IBankService {
    private final BankRepository bankRepository;
    private final CreditCardRepository creditCardRepository;
    private final ModelMapper modelMapper = ModelMapper.INSTANCE;

    @Override
    public Bank saveBank(BankDto dto) {
        Bank bank = modelMapper.toBank(dto);
        return bankRepository.save(bank);
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankRepository.getAll();
    }

    @Override
    public Bank assignCreditCardsToBank(Bank bank, IdRequestDto request) {
        List<CreditCard> creditCards = new ArrayList<>();
        List<Long> ids = request.getIds();
        for (Long cardId : ids) {
            Optional<CreditCard> foundedCreditCard = creditCardRepository.findById(cardId);

            CreditCard creditCard = foundedCreditCard.orElseThrow(() -> new BusinessException("Credit card not found by given id"));
            creditCard.setBank(bank);
            creditCards.add(creditCard);
        }
        //bank.setCreditCards(creditCards);
        return bank;
    }

    @Override
    public Bank getBankById(Long id) {
        Optional<Bank> bankOpt = bankRepository.findById(id);
        return bankOpt.orElseThrow(() -> new BusinessException("Bank not found by given id"));
    }
}

