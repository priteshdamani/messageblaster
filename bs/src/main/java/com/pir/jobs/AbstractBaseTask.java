package com.pir.jobs;

import com.pir.services.IBackstageService;
import com.pir.services.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pritesh on 12/13/13.
 */
public abstract class AbstractBaseTask {

    @Autowired
    protected IBusinessService businessService;

    @Autowired
    protected IBackstageService backstageService;

    public abstract void doRun();

    public void runMe() {
        doRun();
    }
}
