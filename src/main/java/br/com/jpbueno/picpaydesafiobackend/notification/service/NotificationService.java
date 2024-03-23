package br.com.jpbueno.picpaydesafiobackend.notification.service;

import br.com.jpbueno.picpaydesafiobackend.notification.kafka.NotificationProducer;
import br.com.jpbueno.picpaydesafiobackend.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    public void notify(Transaction transaction) {
        notificationProducer.sendNotification(transaction);
    }
}
