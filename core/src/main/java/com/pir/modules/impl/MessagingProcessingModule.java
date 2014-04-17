package com.pir.modules.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pir.dao.IMessageRecordDao;
import com.pir.domain.Group;
import com.pir.domain.MessageRecord;
import com.pir.domain.User;
import com.pir.exceptions.TemplatingException;
import com.pir.modules.IMessageSendingModule;
import com.pir.modules.IMessagingProcessingModule;
import com.pir.util.AppClock;
import com.pir.util.ErrorUtils;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by pritesh on 12/14/13.
 */

@Component("messagingModule")
public class MessagingProcessingModule implements IMessagingProcessingModule {


    private final Logger logger = LoggerFactory.getLogger(MessagingProcessingModule.class);


    //limit the number of actual threads
    int poolSize = 10;
    private ExecutorService service = Executors.newFixedThreadPool(poolSize);

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private IMessageRecordDao messageRecordDao;

    @Autowired
    private IMessageSendingModule messageSendingModule;

    private Configuration freemarkerConfig;

    @PostConstruct
    public synchronized void init() {
        freemarkerConfig = new Configuration();
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/email/");
        freemarkerConfig.setLocalizedLookup(false);
    }


    public String processSubjectTemplate(String templateName, Map vars) throws TemplatingException {
        return processTemplate(templateName + "-subject", vars);
    }

    public String processTemplate(String templateName, Map vars) throws TemplatingException {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate(templateName + ".ftl"), vars);
        } catch (Exception ie) {
            throw new TemplatingException("Problem processing template: " + templateName, ie);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void sendMessageRecords(int pageSize) {
        List<MessageRecord> recordsToProcess = messageRecordDao.unSentMessagesGet(10);

        if (recordsToProcess.size() > 0) {
            Map<Long, Future<MessageRecord>> recordsMap = Maps.newHashMap();
            for (int i = 0; i < recordsToProcess.size(); i++) {
                final MessageRecord record = recordsToProcess.get(i);
                Future<MessageRecord> f = service.submit(new SendMessage(record));
                recordsMap.put(record.getId(), f);
            }

            final List<MessageRecord> processedRecords = Lists.newArrayList();
            final Map<Long, String> errorRecords = Maps.newHashMap();

            // wait for all tasks to complete before continuing
            for (Map.Entry<Long, Future<MessageRecord>> rf : recordsMap.entrySet()) {
                try {
                    processedRecords.add(rf.getValue().get());
                } catch (Exception e) {
                    logger.error("Error processing email record ", e);
                    errorRecords.put(rf.getKey(), ErrorUtils.getStackTraceString(e));
                }
            }
            logger.info("sent {} emails", recordsToProcess.size());

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    for (MessageRecord mr : processedRecords) {
                        messageRecordDao.save(mr);
                    }
                    for (Map.Entry<Long, String> error : errorRecords.entrySet()) {
                        MessageRecord mr = messageRecordDao.findById(error.getKey());
                        mr.setErrorDate(AppClock.now());
                        mr.setErrorString(error.getValue());
                        messageRecordDao.save(mr);
                    }
                }
            });
        }
    }

    @Override
    public MessageRecord sendVerificationEmail(User user) throws TemplatingException {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("user", user);
        String subject = processSubjectTemplate(IMessagingProcessingModule.EMAIL_VERIFY_USER_SIGNUP, vars);
        String body = processTemplate(IMessagingProcessingModule.EMAIL_VERIFY_USER_SIGNUP, vars);
        MessageRecord record = new MessageRecord(MessageRecord.MessageType.EMAIL, MessageRecord.MessagePriority.IMMEDIATE, IMessagingProcessingModule.CAMPAIGN_VERIFY_USER_SIGNUP, body, subject, user.getEmail(), null);
        return messageRecordDao.save(record);
    }

    @Override
    public MessageRecord sendGroupCreatedEmail(Group group, User user) throws TemplatingException {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("user", user);
        vars.put("group", group);
        String subject = processSubjectTemplate(IMessagingProcessingModule.EMAIL_GROUP_CREATED, vars);
        String body = processTemplate(IMessagingProcessingModule.EMAIL_GROUP_CREATED, vars);
        MessageRecord record = new MessageRecord(MessageRecord.MessageType.EMAIL, MessageRecord.MessagePriority.IMMEDIATE, IMessagingProcessingModule.CAMPAIGN_GROUP_CREATED, body, subject, user.getEmail(), null);
        return messageRecordDao.save(record);
    }

    @Override
    public MessageRecord sendPasswordResetEmail(User user) throws TemplatingException {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("user", user);
        String subject = processSubjectTemplate(IMessagingProcessingModule.EMAIL_RESET_PASSWORD, vars);
        String body = processTemplate(IMessagingProcessingModule.EMAIL_RESET_PASSWORD, vars);
        MessageRecord record = new MessageRecord(MessageRecord.MessageType.EMAIL, MessageRecord.MessagePriority.IMMEDIATE, IMessagingProcessingModule.CAMPAIGN_EMAIL_RESET_PASSWORD, body, subject, user.getEmail(), null);
        return messageRecordDao.save(record);
    }

    @Override
    public long totalMessageRecordsGet() {
        return messageRecordDao.count();
    }


    private class SendMessage implements Callable<MessageRecord> {
        private MessageRecord record;

        public SendMessage(MessageRecord record) {
            this.record = record;
        }

        @Transactional(readOnly = false)
        public MessageRecord call() {
            try {
                if (record.getType() == MessageRecord.MessageType.EMAIL) {
                    messageSendingModule.sendMail(record.getFromEmail(), record.getToEmail(), record.getSubject(), record.getBody(), record.getId(), record.getFromName());
                } else if (record.getType() == MessageRecord.MessageType.EMAIL_SMS) {
                    messageSendingModule.sendSmsMail(record.getFromEmail(), record.getToEmail(), record.getBody());
                } /*else if (record.getType() == MessageRecord.MessageType.SMS) {
                    twilioManager.sendSMS(record.getFromEmail(), record.getToEmail(), record.getBody());
                }*/
                record.setSentDate(AppClock.now());
            } catch (Exception e) {
                record.setErrorDate(AppClock.now());
                record.setErrorString(e.toString());
            }
            return record;
        }
    }


}
