package com.patika.kredinbizdeservice.service.impl;

import com.patika.kredinbizdeservice.client.dto.request.ApplicationRequest;
import com.patika.kredinbizdeservice.client.dto.response.ApplicationResponse;
import com.patika.kredinbizdeservice.controller.model.ApplicationDto;
import com.patika.kredinbizdeservice.enums.LoanType;
import com.patika.kredinbizdeservice.exceptions.BusinessException;
import com.patika.kredinbizdeservice.factory.BankServiceClient;
import com.patika.kredinbizdeservice.factory.BankServiceClientFactory;
import com.patika.kredinbizdeservice.mapper.ModelMapper;
import com.patika.kredinbizdeservice.model.Application;
import com.patika.kredinbizdeservice.model.CreditCard;
import com.patika.kredinbizdeservice.model.Loan;
import com.patika.kredinbizdeservice.model.User;
import com.patika.kredinbizdeservice.producer.NotificationProducer;
import com.patika.kredinbizdeservice.producer.dto.NotificationDTO;
import com.patika.kredinbizdeservice.producer.enums.NotificationType;
import com.patika.kredinbizdeservice.repository.ApplicationRepository;
import com.patika.kredinbizdeservice.service.IApplicationService;
import com.patika.kredinbizdeservice.service.ICreditCardService;
import com.patika.kredinbizdeservice.service.ILoanService;
import com.patika.kredinbizdeservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService implements IApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ICreditCardService creditCardService;
    private final ILoanService loanService;
    private final IUserService userService;
    private final BankServiceClientFactory bankServiceClientFactory;
    private final NotificationProducer notificationProducer;
    private final ModelMapper modelMapper = ModelMapper.INSTANCE;

    @Override
    public Application saveApplication(ApplicationDto applicationDto) {
        User user = userService.getById(applicationDto.getUser().getId());
        //User user = modelMapper.toUser(userDto);
        String bankName;
        Long productId;
        Application application;
        if (applicationDto.getProduct().getType().equals(LoanType.CREDIT_CARD)) {
            CreditCard creditCard = creditCardService.getById(applicationDto.getProduct().getId());
            productId = creditCard.getId();
            bankName = creditCard.getBank().getName();
            Application savedApplication1 = new Application(applicationDto.getId(), applicationDto.getLocalDateTime(), creditCard, user);
            application = applicationRepository.save(savedApplication1);
        } else {
            Loan loan = loanService.getLoanById(applicationDto.getProduct().getId());
            productId = loan.getId();
            bankName = loan.getBank().getName();
            Application savedApplication2 = new Application(applicationDto.getId(), applicationDto.getLocalDateTime(), loan, user);
            application = applicationRepository.save(savedApplication2);
        }

        log.info("Bank name: " + bankName);
        BankServiceClient bankServiceClient = bankServiceClientFactory.getBankServiceClient(bankName);
        log.info("Bank Service Client is " + bankServiceClient.toString());
        ApplicationResponse response = bankServiceClient.createApplication(ApplicationRequest.builder()
                .userId(user.getId())
                .productId(productId)
                .build());
        log.info("Application sent to bank. Current status is: " + (response != null ? response.getApplicationStatus() : ""));

        notificationProducer.sendNotification(NotificationDTO.builder()
                .to(user.getEmail())
                .notificationType(NotificationType.EMAIL)
                .message("Başvurunuz alınmıstır!")
                .build());

        return application;
    }

    @Override
    public List<ApplicationResponse> getApplicationsByUserId(Long userId) {
        BankServiceClient bankServiceClient = bankServiceClientFactory.getBankServiceClient("Garanti");
        log.info("Applications found for user");
        return bankServiceClient.getApplicationsByUserId(userId);
    }

    @Override
    public List<Application> getAllApplications(LoanType type) {
        if (type == null) {
            return applicationRepository.getAll();
        }
        return applicationRepository.getAll().stream().filter(f -> f.getProduct().getLoanType().equals(type)).collect(Collectors.toList());

    }

    @Override
    public Application getApplicationById(Long id) {
        Optional<Application> applicationOpt = applicationRepository.findById(id);
        return applicationOpt.orElseThrow(() -> new BusinessException("Application not found by given id"));
    }
}
