package servlets;

import enigmaDtos.AgentDataDTO;
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
import java.util.Map;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "AgentsJoinedRefresherServlet", urlPatterns = {"/agentsJoinedRefresher"})
public class AgentsJoinedRefresherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String allyNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Ally ally = userManager.getAlly(allyNameFromSession);
        synchronized (this) {
            ArrayList<AgentDataDTO> agentDataDTOS = ally.getAllAgentsDataDto();
            response.getWriter().println(GSON_INSTANCE.toJson(agentDataDTOS));
        }
    }
}
