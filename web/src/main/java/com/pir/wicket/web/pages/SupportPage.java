package com.pir.wicket.web.pages;

import org.apache.wicket.request.flow.RedirectToUrlException;

/**
 * Created by pritesh on 1/2/14.
 */
public class SupportPage extends BasePage {
    public SupportPage() {
        throw new RedirectToUrlException("http://groupnotifier.uservoice.com");
    }
}
