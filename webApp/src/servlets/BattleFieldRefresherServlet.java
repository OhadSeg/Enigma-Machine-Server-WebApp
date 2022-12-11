package servlets;
import components.Battlefield;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Agent;
import users.Ally;
import users.BattlefieldManager;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.GSON_INSTANCE;


@WebServlet(name = "BattleFieldRefresherServlet", urlPatterns = {"/battleFieldRefresher"})
public class BattleFieldRefresherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String entityNameFromSession = SessionUtils.getUsername(request);
        Ally ally = userManager.getAlly(entityNameFromSession);
        if (ally == null) {
            Agent agent = userManager.getAgent(entityNameFromSession);
            String allyName = agent.getAllyName();
            ally = userManager.getAlly(allyName);
        }
        synchronized (this) {
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            Battlefield battlefield = battlefieldManager.getBattlefield(ally.getBattlefieldAssignedTo());
            response.getWriter().println(GSON_INSTANCE.toJson(battlefield));
        }
    }
}
