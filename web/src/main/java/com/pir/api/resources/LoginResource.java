package com.pir.api.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pir.api.dto.LoginDto;
import com.pir.api.dto.Response;
import com.pir.services.IBusinessService;
import com.pir.util.JsonViews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */

@Component("loginResource")
@Path("/login")
public class LoginResource extends AbstractBaseResource {

    private final Logger logger = LoggerFactory.getLogger(LoginResource.class);

    @Autowired
    private IBusinessService businessService;

    @POST
    @Consumes( { MediaType.APPLICATION_JSON })
    @Produces( { MediaType.APPLICATION_JSON })
    public String login(LoginDto loginDto,
                        @Context javax.servlet.http.HttpServletRequest request) {

        logger.debug("{} attempting to login", loginDto.getUsername());

        Long userId = -1L;//businessService.authenticate(loginDto.getUsername(), loginDto.getPassword());

        try {
            if (userId != null) {
                return OBJECT_MAPPER.writerWithView(JsonViews.API.class).writeValueAsString(new Response(Response.Status.OK, "User Logged in"));
            } else {
                return OBJECT_MAPPER.writeValueAsString(new Response(Response.Status.NOK, "Authentication failed."));
            }
        } catch (JsonProcessingException e) {
            logger.error("Login problem", e);
            return getErrorJsonString("Unfortunately there was a problem logging you in. We're working to fix it. Please try again later.");
        } catch (Exception e) {
            logger.error("Login problem", e);
            return getErrorJsonString("Unfortunately there was a problem logging you in. We're working to fix it. Please try again later.");
        }
    }


}
