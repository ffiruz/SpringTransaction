package com.example.awesomespringtransactions.service;


import java.io.FileNotFoundException;

import com.example.awesomespringtransactions.model.Payment;
import com.example.awesomespringtransactions.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /*
        @Transactional(propagation = Propagation.REQUIRED) -> Default: @Transactional

     */

    @Transactional(rollbackFor = FileNotFoundException.class)   //(propagation = Propagation.NEVER)
    public void pay(Payment payment) throws FileNotFoundException {
        this.paymentRepository.save(payment);
        log.info("======> Payment Id: {}", payment.getId());
       throw new FileNotFoundException("asdfasd");
    }

    //try -catch kullanarak exceptionı korursak , rollbacki engellemiş oluruz.Bu iyi bir kullanım değil.
    //Eğer try-catch illa kullanmak istiyorsa, catch kısmında ilgili veriyi failed etmemiz gerekebilir.
}
