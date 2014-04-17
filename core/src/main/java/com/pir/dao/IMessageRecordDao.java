package com.pir.dao;

import com.pir.domain.MessageRecord;

import java.util.List;

/**
 * Created by pritesh on 12/8/13.
 */
public interface IMessageRecordDao extends IBaseDao<MessageRecord> {

    public List<MessageRecord> unSentMessagesGet(int pageSize);

}
