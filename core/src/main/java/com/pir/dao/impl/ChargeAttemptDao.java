package com.pir.dao.impl;

import com.pir.dao.IChargeAttemptDao;
import com.pir.domain.ChargeAttempt;
import org.springframework.stereotype.Repository;

/**
 * Created by pritesh on 12/8/13.
 */

@Repository
public class ChargeAttemptDao extends BaseDaoImpl<ChargeAttempt> implements IChargeAttemptDao {
    public ChargeAttemptDao() {
        super(ChargeAttempt.class);
    }

}
