package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.AddressDto;
import at.fhtw.webshop.dto.PaymentMethodDto;
import at.fhtw.webshop.model.Address;
import at.fhtw.webshop.model.PaymentMethod;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.PaymentMethodRepository;
import at.fhtw.webshop.repository.UserRepository;
import at.fhtw.webshop.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentMethodService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodService.class);

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, UserRepository userRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
    }

    public List<PaymentMethodDto> getPaymentMethodsForUser(CustomUserDetails userDetails) {

        List<PaymentMethod> paymentMethodsList  = paymentMethodRepository.getPaymentMethodsByUserID(
                userRepository.findById(userDetails.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userDetails.getId()))
        );

        return paymentMethodsList.stream()
                .map(paymentMethod -> new PaymentMethodDto(
                        paymentMethod.getId(),
                        paymentMethod.getCardNumber(),
                        paymentMethod.getCardHolderName(),
                        paymentMethod.getExpiryDate(),
                        paymentMethod.getCvv()
                ))
                .toList();
    }

    public void addPaymentMethodToUser(PaymentMethodDto paymentMethodDto, CustomUserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user != null) {
            PaymentMethod paymentMethod = new PaymentMethod(paymentMethodDto);
            paymentMethod.setUserID(user);
            paymentMethodRepository.save(paymentMethod);
        } else {
            logger.warn("User with username {} not found", userDetails.getUsername());
        }
    }

    public void updatePaymentMethodForUser(Integer paymentMethodId, PaymentMethodDto paymentMethodDto, CustomUserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user != null) {
            Optional<PaymentMethod> paymentMethodOptional = paymentMethodRepository.findById(paymentMethodId);
            if (paymentMethodOptional.isPresent()) {
                PaymentMethod paymentMethod = paymentMethodOptional.get();
                if (Objects.equals(paymentMethod.getUserID().getId(), user.getId())) {
                    paymentMethod.setCardNumber(paymentMethodDto.getCardNumber());
                    paymentMethod.setExpiryDate(paymentMethodDto.getExpiryDate());
                    paymentMethod.setCvv(paymentMethodDto.getCvv());
                    paymentMethodRepository.save(paymentMethod);
                } else {
                    logger.warn("Payment method with ID {} does not belong to user {}", paymentMethodId, userDetails.getUsername());
                }
            } else {
                logger.warn("Payment method with ID {} not found", paymentMethodId);
            }
        } else {
            logger.warn("User with username {} not found", userDetails.getUsername());
        }
    }

    public void deletePaymentMethodForUser(Integer paymentMethodId, CustomUserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user != null) {
            Optional<PaymentMethod> paymentMethodOptional = paymentMethodRepository.findById(paymentMethodId);
            if (paymentMethodOptional.isPresent()) {
                PaymentMethod paymentMethod = paymentMethodOptional.get();
                if (paymentMethod.getUserID().getId() == user.getId()) {
                    paymentMethodRepository.delete(paymentMethod);
                } else {
                    logger.warn("Payment method with ID {} does not belong to user {}", paymentMethodId, userDetails.getUsername());
                }
            } else {
                logger.warn("Payment method with ID {} not found", paymentMethodId);
            }
        } else {
            logger.warn("User with username {} not found", userDetails.getUsername());
        }
    }
}