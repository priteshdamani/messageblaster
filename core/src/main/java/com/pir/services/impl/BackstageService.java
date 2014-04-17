package com.pir.services.impl;

import com.pir.dao.IMessageDao;
import com.pir.dao.IMessageRecordDao;
import com.pir.dao.IUserDao;
import com.pir.domain.Group;
import com.pir.domain.Message;
import com.pir.domain.MessageRecord;
import com.pir.domain.User;
import com.pir.exceptions.TemplatingException;
import com.pir.modules.IMessageSendingModule;
import com.pir.modules.IMessagingProcessingModule;
import com.pir.services.IBackstageService;
import com.pir.util.AppClock;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("backstageService")
public class BackstageService implements IBackstageService {

    private final Logger logger = LoggerFactory.getLogger(BackstageService.class);

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IMessageDao messageDao;

    @Autowired
    private IMessageRecordDao messageRecordDao;

    @Autowired
    private IMessagingProcessingModule messageProcessingModule;

    @Autowired
    private IMessageSendingModule messageSendingModule;

    @Override
    @Transactional(readOnly = false)
    public void processUnsentMessages(int pageSize) {
        List<Message> messagesToSend = messageDao.unSentMessagesGet(pageSize);
        for(Message message : messagesToSend){
            Group group = message.getCreatedFor();
            User from = message.getCreatedBy();
            String subject = message.getSubject();
            String body = message.getBody();

            Map varMap = new HashMap();
            varMap.put("group",group);
            varMap.put("subject",message.getSubject());
            varMap.put("body",message.getBody());

            try {
                subject = messageProcessingModule.processSubjectTemplate(IMessagingProcessingModule.EMAIL_MSG, varMap);
            } catch (TemplatingException e) {
                logger.error("Error processing subject template EMAIl_MSG for message id "+message.getId());
                //throw ?
            }

            try {
                body = messageProcessingModule.processTemplate(IMessagingProcessingModule.EMAIL_MSG, varMap);
            } catch (TemplatingException e) {
                logger.error("Error processing template EMAIl_MSG for message id "+message.getId());
                //throw ?
            }

            String htmlFreeSubject = Jsoup.parse(message.getSubject()).text();
            String htmlFreeBody = Jsoup.parse(message.getBody()).text();

            List<User> usersToSend = userDao.searchUsersForGroup(group.getId());
            for(User user : usersToSend){
                if (user.isReceivingEmail(group.getId()) && user.getEmail() != null){
                    MessageRecord record = new MessageRecord(MessageRecord.MessageType.EMAIL, MessageRecord.MessagePriority.HIGH, IMessagingProcessingModule.CAMPAIGN_MESSAGING,body,subject,user.getEmail(),message, from.getEmail(), from.getName());
                    messageRecordDao.save(record);
                }
                if (user.isReceivingTxt(group.getId()) && user.getCellCarrier() != null && user.getMobileNumber() != null){
                    MessageRecord record = new MessageRecord(MessageRecord.MessageType.EMAIL_SMS, MessageRecord.MessagePriority.HIGH, IMessagingProcessingModule.CAMPAIGN_MESSAGING, group.getName() + " : " + htmlFreeSubject + " : " + htmlFreeBody, null, user.smsEmailGet(), message, from.getEmail(), from.getName());
                    messageRecordDao.save(record);
                }
            }
            message.setProcessedDate(AppClock.now());
            messageDao.save(message);
        }
    }

    @Override
    public void sendUnsentMessages(int pageSize) {
        messageProcessingModule.sendMessageRecords(pageSize);
    }


}
