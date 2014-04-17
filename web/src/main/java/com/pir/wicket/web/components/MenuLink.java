package com.pir.wicket.web.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.jsoup.Jsoup;

/**
 * Created by pritesh on 12/12/13.
 */
public class MenuLink extends Panel {
    public MenuLink(String id, Long groupId, String url, String icon, String menuName, String boxColor) {
        super(id);
        String title = Jsoup.parse(menuName).text();
        Label menuLbl = new Label("markup",
                "<div class='col-xs-4 col-sm-2' id='" + id + "'>" +
                        "    <div class='box-quick-link " + boxColor + "'>" +
                        "        <a href=\"" + url + "?groupId=" + groupId + "\" title=\"" + title + "\">" +
                        "            <div class='header'>" +
                        "                <div class='" + icon + "'></div>" +
                        "            </div>" +
                        "            <div class='content'>" + menuName + "</div>" +
                        "        </a>" +
                        "    </div>" +
                        "</div>");
        menuLbl.setEscapeModelStrings(false);
        add(menuLbl);
    }
}
