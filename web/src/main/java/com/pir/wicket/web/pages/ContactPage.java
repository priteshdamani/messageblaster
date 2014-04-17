package com.pir.wicket.web.pages;

import org.apache.wicket.request.flow.RedirectToUrlException;

/**
 * Created by pritesh on 1/2/14.
 */
public class ContactPage extends BasePage {
    public ContactPage() {
        throw new RedirectToUrlException("http://groupnotifier.uservoice.com");
    }
}
