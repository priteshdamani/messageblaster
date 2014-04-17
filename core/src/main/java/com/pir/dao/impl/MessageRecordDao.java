package com.pir.dao.impl;

import com.pir.dao.IMessageRecordDao;
import com.pir.domain.MessageRecord;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pritesh on 12/8/13.
 */

@Repository
public class MessageRecordDao extends BaseDaoImpl<MessageRecord> implements IMessageRecordDao {
    public MessageRecordDao() {
        super(MessageRecord.class);
    }

    @Override
    public List<MessageRecord> unSentMessagesGet(int pageSize) {
        return findAllByCriteriaWithOrder(pageSize, Order.desc("priority"), Restrictions.and(Restrictions.isNull("sentDate"),Restrictions.isNull("errorDate")));
    }
}
