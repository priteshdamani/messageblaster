package com.pir.services;

/**
 * Created by pritesh on 12/14/13.
 */
public interface IBackstageService {
    void processUnsentMessages(int pageSize);

    void sendUnsentMessages(int pageSize);
}
