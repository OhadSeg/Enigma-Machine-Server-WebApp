package servlets;

import enigmaDtos.AllyDataDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Ally;
import users.Uboat;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "ActiveTeamsRefresherServlet", urlPatterns = {"/activeTeamsDataRefresher"})
public class ActiveTeamsRefresherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String entityNameFromSession = SessionUtils.getUsername(request);
        Uboat uboat;
        if (userManager.getUboat(entityNameFromSession) != null) {
            uboat = userManager.getUboat(entityNameFromSession);
        } else {
            Ally ally = userManager.getAlly(entityNameFromSession);
            String uboatName = ally.getUboatAssignedTo();
            uboat = userManager.getUboat(uboatName);
        }
        synchronized (this) {
            ArrayList<AllyDataDTO> allyDataDTOS = uboat.getAllAlliesDataDto();
            response.getWriter().println(GSON_INSTANCE.toJson(allyDataDTOS));
        }
    }
}
