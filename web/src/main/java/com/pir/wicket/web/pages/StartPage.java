package com.pir.wicket.web.pages;

import com.pir.wicket.web.components.TawkPanel;
import com.pir.wicket.web.components.UserVoicePanel;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/6/13
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartPage extends BasePage {
    public StartPage() {
        if (isUserLoggedIn()) {
            setResponsePage(ProfilePage.class);
        }
        add(new UserVoicePanel("userVoicePnl"));
        add(new TawkPanel("tawkPnl"));
    }
}
