package com.pir.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pritesh on 12/13/13.
 */
public class MessageProcessingTask extends AbstractBaseTask{

    private final Logger logger = LoggerFactory.getLogger(MessageProcessingTask.class);

    @Override
    public void doRun() {
        backstageService.processUnsentMessages(10);
    }
}
