package com.example.awesomespringtransactions.repository;

import com.example.awesomespringtransactions.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {

}
