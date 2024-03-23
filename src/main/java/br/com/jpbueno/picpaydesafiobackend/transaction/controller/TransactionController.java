package br.com.jpbueno.picpaydesafiobackend.transaction.controller;

import br.com.jpbueno.picpaydesafiobackend.transaction.Transaction;
import br.com.jpbueno.picpaydesafiobackend.transaction.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.create(transaction);
    }

    @GetMapping
    public List<Transaction> list() {
        return transactionService.list();
    }
}
