package com.pir.wicket.web.pages;

import com.pir.wicket.web.components.*;

/**
 * Created by pritesh on 12/11/13.
 */
public class GeneralPage extends BasePage {

    public GeneralPage() {

        add(new HeaderIncludes("headerIncludes"));
        add(new FooterIncludes("footerIncludes"));
        add(new Footer("footer"));

        if (isUserLoggedIn()){
            add(new LoggedInMenu("menu",getLoggedInUser().getName(),getLoggedInUser().getId(),getLoggedInUser().isVerified()));
        } else {
            add(new NonLoggedInMenu("menu"));
        }

        add(new UserVoicePanel("userVoicePnl"));
        add(new TawkPanel("tawkPnl"));
    }
}
