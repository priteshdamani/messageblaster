package com.pir.dao;

import com.pir.domain.Message;

import java.util.List;

/**
 * Created by pritesh on 12/8/13.
 */
public interface IMessageDao extends IBaseDao<Message> {

    public List<Message> messagesGet(Long groupId, int pageSize);

    public List<Message> unSentMessagesGet(int pageSize);
}
