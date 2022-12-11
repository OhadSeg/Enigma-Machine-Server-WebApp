package servlets;

import components.Enigma;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Agent;
import users.Ally;
import users.Uboat;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "GetEnigmaFromUboatServlet", urlPatterns = {"/getEnigmaFromUboat"})
public class GetEnigmaFromUboatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String agentNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Agent agent = userManager.getAgent(agentNameFromSession);
        String allyName = agent.getAllyName();
        Ally ally = userManager.getAlly(allyName);
        String uboatName = ally.getUboatAssignedTo();
        Uboat uboat = userManager.getUboat(uboatName);
        Enigma enigma = uboat.getEnigma();
        response.getWriter().println(GSON_INSTANCE.toJson(enigma));
    }
}