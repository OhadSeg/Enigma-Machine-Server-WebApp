package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Agent;
import users.Ally;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "GetAllyDetailsServlet", urlPatterns = {"/getAllyDetails"})
public class GetAllyDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String agentNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Agent agent = userManager.getAgent(agentNameFromSession);
        String allyName = agent.getAllyName();
        Ally ally = userManager.getAlly(allyName);
        int taskSize = ally.getTaskSize();

        synchronized (this) {
            ArrayList<String> toReturn = new ArrayList<>();
            toReturn.add(allyName);
            toReturn.add(taskSize + "");
            response.getWriter().println(GSON_INSTANCE.toJson(toReturn));
        }
    }
}
