package br.com.jpbueno.picpaydesafiobackend.wallet.repository;

import br.com.jpbueno.picpaydesafiobackend.wallet.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
