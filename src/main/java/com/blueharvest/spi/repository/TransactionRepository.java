package com.blueharvest.spi.repository;

import com.blueharvest.spi.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
