package br.com.jpbueno.picpaydesafiobackend.wallet;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table(name = "WALLETS")
public record Wallet(
        @Id Long id,
        String fullname,
        Long cpf,
        String email,
        String password,
        int type,
        BigDecimal balance) {

    public Wallet debit(BigDecimal value) {
        return new Wallet(id, fullname, cpf, email, password, type, balance.subtract(value));
    }

    public Wallet credit(BigDecimal value) {
        return new Wallet(id, fullname, cpf, email, password, type, balance.add(value));
    }
}
