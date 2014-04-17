package com.pir.wicket.web.pages;

import org.apache.wicket.request.flow.RedirectToUrlException;

/**
 * Created by pritesh on 12/11/13.
 */
public class HelpPage extends GeneralPage {

    public HelpPage() {
        throw new RedirectToUrlException("http://groupnotifier.uservoice.com/knowledgebase");
    }
}
