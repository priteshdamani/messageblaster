package com.pir.wicket.web.pages;

import com.pir.wicket.web.components.CustomSession;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 */
public class WicketApplication extends WebApplication implements ApplicationContextAware {

    // used in testing instead of getting from servletcontext when deployed in tomcat
    private ApplicationContext applicationContext;
    private Map<String, Class> pathMountPageMap = new HashMap<String, Class>();

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return StartPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new CustomSession(request);
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        // for @SpringBean
        getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));


        //prod settings
        getApplicationSettings().setInternalErrorPage(Error500Page.class);
        // these are the defaults. just here in case we want to tune later
        getStoreSettings().setMaxSizePerSession(Bytes.megabytes(15));
        getStoreSettings().setInmemoryCacheSize(40);
        getApplicationSettings().setAccessDeniedPage(LoginPage.class);
        getApplicationSettings().setPageExpiredErrorPage(LoginPage.class);
        getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);

        // stuff we might want/need to look at/tweak later
        getSecuritySettings();
        getApplicationSettings();
        getFrameworkSettings();
        getPageSettings();
        getMarkupSettings();
        getResourceSettings();
        getRequestLoggerSettings();
        getExceptionSettings();
        getJavaScriptLibrarySettings();

        // this creates a test user.
        pathMountPageMap.put("/SignUp", SignupPage.class);
        pathMountPageMap.put("/Login", LoginPage.class);
        pathMountPageMap.put("/ForgotPassword", ForgotPasswordPage.class);
        pathMountPageMap.put("/Dashboard", DashboardPage.class);
        pathMountPageMap.put("/Profile", ProfilePage.class);
        pathMountPageMap.put("/Settings", SettingsPage.class);
        pathMountPageMap.put("/GroupSettings", GroupSettingsPage.class);
        pathMountPageMap.put("/NotificationOptions", NotificationOptionsPage.class);
        pathMountPageMap.put("/PaymentOptions", PaymentOptionsPage.class);
        pathMountPageMap.put("/Members", MembersPage.class);
        pathMountPageMap.put("/Messages", MessagesPage.class);
        pathMountPageMap.put("/Error404", Error404Page.class);
        pathMountPageMap.put("/Error500", Error500Page.class);
        pathMountPageMap.put("/Start", StartPage.class);
        pathMountPageMap.put("/SubscribeToGroup", SubscribeToGroupPage.class);
        pathMountPageMap.put("/CreateNewGroup", CreateNewGroupPage.class);
        pathMountPageMap.put("/AddGroup", AddGroup.class);
        pathMountPageMap.put("/SignOut", SignOutPage.class);
        pathMountPageMap.put("/JoinGroup", JoinGroupPage.class);
        pathMountPageMap.put("/Group", GroupPage.class);
        pathMountPageMap.put("/Pay", PayPage.class);
        pathMountPageMap.put("/GenericMessage", GenericMessagePage.class);
        pathMountPageMap.put("/Help", HelpPage.class);
        pathMountPageMap.put("/VerifyEmail", VerifyEmailPage.class);
        pathMountPageMap.put("/ForgotPassword", ForgotPasswordPage.class);
        pathMountPageMap.put("/ChangePassword", ChangePasswordPage.class);
        pathMountPageMap.put("/Privacy", PrivacyPage.class);
        pathMountPageMap.put("/Terms", TermsPage.class);
        pathMountPageMap.put("/Pricing", PricingPage.class);
        pathMountPageMap.put("/Support", SupportPage.class);
        pathMountPageMap.put("/Contact", ContactPage.class);


        // mount all the pages
        for (Map.Entry<String, Class> entry : pathMountPageMap.entrySet()) {
            mountPage(entry.getKey(), entry.getValue());
        }


        // add your configuration here
    }


    @Override
    public RuntimeConfigurationType getConfigurationType() {
        return RuntimeConfigurationType.DEPLOYMENT;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
