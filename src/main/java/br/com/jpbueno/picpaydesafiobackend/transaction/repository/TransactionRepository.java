package br.com.jpbueno.picpaydesafiobackend.transaction.repository;

import br.com.jpbueno.picpaydesafiobackend.transaction.Transaction;
import org.springframework.data.repository.ListCrudRepository;

public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {
    /*
    ListCrudRepository - trabalha com listas e não com iterables
    (dificil fazer conversões de iterable pra lista e lista pra iterable)
    */
}
