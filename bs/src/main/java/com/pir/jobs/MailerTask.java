package com.pir.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pritesh on 12/13/13.
 */
public class MailerTask extends AbstractBaseTask{

    private final Logger logger = LoggerFactory.getLogger(MailerTask.class);

    @Override
    public void doRun() {
        backstageService.sendUnsentMessages(10);
    }
}
