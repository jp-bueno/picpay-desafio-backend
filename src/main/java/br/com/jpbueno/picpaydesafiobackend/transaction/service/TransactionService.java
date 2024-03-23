package br.com.jpbueno.picpaydesafiobackend.transaction.service;

import br.com.jpbueno.picpaydesafiobackend.authorization.service.AuthorizerService;
import br.com.jpbueno.picpaydesafiobackend.notification.service.NotificationService;
import br.com.jpbueno.picpaydesafiobackend.transaction.InvalidTransactionException;
import br.com.jpbueno.picpaydesafiobackend.transaction.Transaction;
import br.com.jpbueno.picpaydesafiobackend.transaction.repository.TransactionRepository;
import br.com.jpbueno.picpaydesafiobackend.wallet.Wallet;
import br.com.jpbueno.picpaydesafiobackend.wallet.WalletType;
import br.com.jpbueno.picpaydesafiobackend.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;


    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizerService authorizerService, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    // Se uma operação falhar, será feito o rollback das operações feitas em banco
    @Transactional
    public Transaction create(Transaction transaction) {
        // 1 - validar
        validate(transaction);

        // 2 - criar a transação
        var newTransaction = transactionRepository.save(transaction);

        // 3 - debitar da carteira
        var walletPayer = walletRepository.findById(transaction.payer()).get();
        var walletPayee = walletRepository.findById(transaction.payer()).get();
        walletRepository.save(walletPayer.debit(transaction.value()));
        walletRepository.save(walletPayee.credit(transaction.value()));

        // 4 - chamar serviços externos
        // authorize transaction
        authorizerService.authorize(transaction);

        // notificacao
        notificationService.notify(transaction);

        return newTransaction;
    }

    /*
     * - the payer has a common wallet
     * - the payer has enough balance
     * - the payer is not the payee
     * */
    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(payer -> isTransactionValid(transaction, payer)
                                ? transaction : null)
                        .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction)));

    }

    private static boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0 &&
                !payer.id().equals(transaction.payee());
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }
}
