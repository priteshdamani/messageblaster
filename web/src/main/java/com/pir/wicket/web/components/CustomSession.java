package com.pir.wicket.web.components;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */

public class CustomSession extends WebSession {

    // the user id of the logged in user
    private Long userId = null;
    private boolean admin = false;

    /**
     * Constructor. Note that {@link org.apache.wicket.request.cycle.RequestCycle} is not available until this constructor returns.
     *
     * @param request The current request
     */
    public CustomSession(Request request) {
        super(request);
    }

    public static CustomSession get() {
        return (CustomSession) Session.get();
    }

    public synchronized boolean isLoggedIn() {
        return userId != null;
    }

    public synchronized void logout() {
        userId = null;
        bind();
        invalidateNow();
    }

    public synchronized Long getUserId() {
        return userId;
    }

    public synchronized void setUserId(Long userId) {
        this.userId = userId;
        bind();
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
