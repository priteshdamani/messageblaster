package com.pir.wicket.web.pages;

import com.pir.wicket.web.components.*;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/6/13
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlainPage extends BasePage {

    public PlainPage() {
        add(new HeaderIncludes("headerIncludes"));
        if (isUserLoggedIn()){
            add(new LoggedInMenu("menu",getLoggedInUser().getName(),getLoggedInUser().getId(),getLoggedInUser().isVerified()));
        } else {
            add(new NonLoggedInMenu("menu"));
        }
        add(new Footer("footer"));
        add(new FooterIncludes("footerIncludes"));
        add(new UserVoicePanel("userVoicePnl"));
        add(new TawkPanel("tawkPnl"));
    }

}
