package com.example.awesomespringtransactions;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

import com.example.awesomespringtransactions.model.Order;
import com.example.awesomespringtransactions.model.Payment;
import com.example.awesomespringtransactions.service.OrderService;
import com.example.awesomespringtransactions.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner transactionRunner( //Command Line ile çalıştırdığımızda bir , api ya bağlı kalmadan uygulamamız ayağa kalkar.
		OrderService orderService,
		PaymentService paymentService
	) throws FileNotFoundException {
		Payment payment = Payment.builder()
			.userId(1)
			.orderId(2)
			.price(BigDecimal.valueOf(23))
			.build();


		Order order = Order.builder()
			.productCode("sku1")
			.amount(3)
			.unitPrice(BigDecimal.valueOf(23.1))
			.userId(23)
			.build();


		/* PaymentService pay metodunda bir transactional başlatır.
		 [com.example.awesomespringtransactions.service.Paymen
		 tService.pay]: PROPAGATION_REQUIRES_NEW,ISOLATION_DEFAULT

		 1.-> pay metodu Support olarak belitildiğinde : transaction oluşmaz.Sadece save metodunun jpa transactionı vardır.
		 2.-> pay metodu Not_Support olarak belitildiğinde : transaction oluşmaz ve suspend durumu vardır..Sadece save metodunun jpa transactionı vardır.
		 3.-> pay metodu requıres_new olarak belitildiğinde : yeni bir transaction oluşur.Aynı zaamanda  save metodunun jpa transactionı var olan transactionı kullanır.
		 4.-> pay metodu never olarak belitildiğinde : ve direk olarak içine girdiğimiz için bir transaction oluşturmaz.Sadece save metodunda li jpa transaction oluşturur.Ama transaction lı bir metodun içinden çağırırsak , hatayı almış oluruz.Çünkü çağıramayız.
		 5.-> pay metodu mandotary olarak belitildiğinde : ve direk olarak içine girdiğimiz için bir exception atar.Çünkü mandatory i çağıran metod ve ya servisde transactional olmalı.
		 */
		//Ardından ======> Payment Id: 4 ile db ye kayıt atar.
		//Eğer bir CheckException atarsa ve rollback olmasını istiyorsak , pay metodunun tanımladnığı yerde rollbackFor = FileNotFoundException.class(veya başa tip) yazammız gerekir.UnChecked için öyle bi şeye gerek yok.
		//Eğer 	belli  runtime exceptionlarda , rollback yapılmasını istemiyorsak NoRollBackException kullanabiliriz. -> noRollbackFor = RuntimeException.class
		paymentService.pay(payment);

		//orderService.placeOrder(order);
		/*
		Note 1: placeOrder metodu içerisinde  "Propagation.REQUIRED" ile tanımlama yapılmıştır.Bunun içinde çağrılan "pay" metoduda "Propagation.REQUIRES_NEW" olduğu için yeni bir  transactionı başlatır.
		Note 2: placeOrder metodu içerisinde  "Propagation.REQUIRED" ile tanımlama yapılmıştır.Bunun içinde çağrılan "pay" metoduda "Propagation.REQUIRED" olduğu için "placeOrder" da oluşturulan transactionı kullanır.
		Note 3: placeOrder metodu içerisinde   "Propagation.SUPPORT" ile tanımlama yapılmıştır.Burada yeni bir trasactional oluşmaz.Pay metodunda  "Propagation.REQUIRED" olduğu için  yeni bir transactional başlatılır.
		Note 4: placeOrder metodu içerisinde   "Propagation.SUPPORT" ile tanımlama yapılmıştır.Burada yeni bir trasactional oluşmaz.Pay metodunda  "Propagation.SUPPORT" olduğu için  yeni bir transactional başlatılmaz.
		Note 5: placeOrder metodu içerisinde   "Propagation.REQUIRED" ile tanımlama yapılmıştır.Burada yeni bir trasactional OLUŞUR.Pay metodunda  "Propagation.SUPPORT" olduğu için  placeOrder daki transaction varsa kullanır yoksa yeni bir transaction başlatmaz.
		Note 6: placeOrder metodu içerisinde   "Propagation.REQUIRED" ile tanımlama yapılmıştır.Burada yeni bir trasactional oluşur.Pay metodunda  "Propagation.NOT_SUPPORTED" olduğu için  placeOrder daki transaction suspende alınır.Yeni transaction başlatmaz.
		Note 7: placeOrder metodu içerisinde   "Propagation.REQUIRED" ile tanımlama yapılmıştır.Burada yeni bir trasactional oluşur.Pay metodunda  "Propagation.REQUIRED_NEW" olduğu için  placeOrder daki transaction  suspende alınır ve yeni transaction başlatır.
		Note 8: placeOrder metodu içerisinde   "Propagation.REQUIRED" ile tanımlama yapılmıştır.Burada yeni bir trasactional oluşur.Pay metodunda  "Propagation.NEVER" olduğu için  ve  placeOrder da transactional olan bir yerden pay metodu çağrıldığı için exception fırlatır.
		Yani diyor ki:beni , transactionalı başlamış hherhangi bir parent çağıramaz.Ve order ve payment tablosuna kayıt olmayacak.Çünkü rollback olacak.
	    Note 9: placeOrder metodu içerisinde   "Propagation.REQUIRED" ile tanımlama yapılmıştır.Burada yeni bir trasactional oluşur.Pay metodunda  "Propagation.MANDATORY" olduğu için  placeOrder daki transactionı kullanır.
		 */



		return null;
	}
}
