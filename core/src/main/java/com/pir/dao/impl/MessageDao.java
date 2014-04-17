package com.pir.dao.impl;

import com.pir.dao.IMessageDao;
import com.pir.domain.Message;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pritesh on 12/8/13.
 */

@Repository
public class MessageDao extends BaseDaoImpl<Message> implements IMessageDao {
    public MessageDao() {
        super(Message.class);
    }

    @Override
    public List<Message> messagesGet(Long groupId, int pageSize) {
        return findAllByCriteriaWithOrder(pageSize, Order.desc("createdDate"),Restrictions.eq("createdFor.id",groupId));
    }

    @Override
    public List<Message> unSentMessagesGet(int pageSize) {
        return findAllByCriteriaWithOrder(pageSize, Order.asc("createdDate"), Restrictions.isNull("processedDate"));
    }
}
