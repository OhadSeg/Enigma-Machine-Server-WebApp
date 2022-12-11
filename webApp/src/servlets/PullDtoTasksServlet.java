package servlets;

import com.google.gson.Gson;
import enigmaDtos.BruteForceConfigDTO;
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
import java.util.ArrayList;

@WebServlet(name = "PullDtoTasksServlet", urlPatterns = {"/pullDtoTasks"})
public class PullDtoTasksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int tasksPerPullAsParameter = Integer.parseInt(request.getParameter("tasksPerPull"));
        String agentNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Agent agent = userManager.getAgent(agentNameFromSession);
        String allyName = agent.getAllyName();
        Ally ally = userManager.getAlly(allyName);
        ArrayList<BruteForceConfigDTO> pulledDtoTasks = ally.pullTasksFromDM(tasksPerPullAsParameter);
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(pulledDtoTasks));
    }
}
