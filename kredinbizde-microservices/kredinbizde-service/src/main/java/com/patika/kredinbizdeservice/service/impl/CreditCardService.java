package com.patika.kredinbizdeservice.service.impl;

import com.patika.kredinbizdeservice.controller.model.CreditCardDto;
import com.patika.kredinbizdeservice.controller.model.IdRequestDto;
import com.patika.kredinbizdeservice.exceptions.BusinessException;
import com.patika.kredinbizdeservice.mapper.ModelMapper;
import com.patika.kredinbizdeservice.model.Bank;
import com.patika.kredinbizdeservice.model.Campaign;
import com.patika.kredinbizdeservice.model.CreditCard;
import com.patika.kredinbizdeservice.repository.CampaignRepository;
import com.patika.kredinbizdeservice.repository.CreditCardRepository;
import com.patika.kredinbizdeservice.service.ICreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardService implements ICreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final CampaignRepository campaignRepository;
    private final BankService bankService;

    private final ModelMapper modelMapper = ModelMapper.INSTANCE;

    @Override
    public CreditCard save(CreditCardDto dto) {
        Bank bank = bankService.getBankById(dto.getBank().getId());
        CreditCard creditCard = modelMapper.toCreditCard(dto);
        creditCard.setBank(bank);
        return creditCardRepository.save(creditCard);
    }

    @Override
    public List<CreditCard> getAll() {
        return creditCardRepository.getAll();
    }

    @Override
    public CreditCard getById(Long id) {
        Optional<CreditCard> creditCardOpt = creditCardRepository.findById(id);

        return creditCardOpt.orElseThrow(() -> new BusinessException("Credit card not found by given id"));
    }

    @Override
    public CreditCard assignCampaignsToCreditCard(CreditCard creditCard, IdRequestDto request) {
        List<Campaign> assignedCampaigns = new ArrayList<>();
        List<Long> ids = request.getIds();
        for (Long campaignId : ids) {
            Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
            campaignOpt.ifPresentOrElse(
                    assignedCampaigns::add,
                    () -> {
                        throw new BusinessException("Campaign not found by given id:");
                    }
            );
        }
        creditCard.setCampaigns(assignedCampaigns);
        return creditCard;
    }
}



