package servlets;

import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Agent;
import users.Ally;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.UserType;

import java.io.IOException;

@WebServlet(name = "AgentLogoutServlet", urlPatterns = {"/agentLogout"})
public class AgentLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String agentNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Agent agent = userManager.getAgent(agentNameFromSession);
        String allyName = agent.getAllyName();
        Ally ally = userManager.getAlly(allyName);

        synchronized (this) {
            ally.removeAgent(agentNameFromSession);
            userManager.removeEntity(agentNameFromSession, UserType.AGENT);
            userManager.removeUser(agentNameFromSession);
            request.getSession().removeAttribute(Constants.USERNAME);
            request.getSession().removeAttribute(Constants.USERTYPE);
        }
    }
}
