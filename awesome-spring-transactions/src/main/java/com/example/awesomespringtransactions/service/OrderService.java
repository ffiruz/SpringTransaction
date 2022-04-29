package com.example.awesomespringtransactions.service;


import java.io.FileNotFoundException;

import com.example.awesomespringtransactions.model.Order;
import com.example.awesomespringtransactions.model.Payment;
import com.example.awesomespringtransactions.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

   @Transactional(propagation = Propagation.REQUIRED,rollbackFor = FileNotFoundException.class)
    public void placeOrder(Order order) throws FileNotFoundException {
        this.orderRepository.save(order);
        log.info("======> Order Id: {}", order.getId());
        Payment payment = Payment.builder()
            .price(order.getTotalPrice())
            .orderId(order.getId())
            .userId(order.getUserId())
            .build();
        this.paymentService.pay(payment);
    }

    /*
    placeOrder da @Tramsactional tanımladık.pay metodunda da @Transactional tanımladık.
    Ardından da pay metodunda bir exception fırlaatık-> throw new FileNotFoundException("asdfasd");
    Exception fırlattığı için roll back olnası gerekir ve db ye kayıt yapmaması gerekir.Oysaki db ye kayıt yaptı ve rollback olmadı.
    Bunun sebebi :Eğer atılan exception checked Exception ise annotation seviyesinde rollBackFor ile tanımlama yapmak gerekir.Eğer unCheckedException ise böyle bir tanımlama yapmamıza gerek yok.
    CheckeExceptionlar rollbacki tatiklemez.Ancak UnChecked exceptionlar tetikler ve tanımlamasak da rolback yaparlar.



     */
}
