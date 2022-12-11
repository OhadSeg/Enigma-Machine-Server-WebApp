package servlets;

import components.Battlefield;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Ally;
import users.BattlefieldManager;
import users.Uboat;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;

@WebServlet(name = "AddAllyToBattlefieldServlet", urlPatterns = {"/addAllyToBattlefield"})
public class AddAllyToBattlefieldServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String allyNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());

        String battlefiledNameFromParameter = request.getParameter("battlefieldname");
        Battlefield battlefield = battlefieldManager.getBattlefield(battlefiledNameFromParameter);
        String uboatName = battlefield.getUboatName();
        synchronized (this) {
            if (battlefield.getAllies() <= battlefield.getAlliesJoined()) {
                //שגיאה, אי אפשר להוסיף עוד ally
            }
            Ally ally = userManager.getAlly(allyNameFromSession);
            ally.setUboatAssignedTo(uboatName);
            ally.setBattlefieldAssignedTo(battlefield.getBattleName());
            Uboat uboat = userManager.getUboat(uboatName);
            uboat.addAlly(allyNameFromSession, ally);
            battlefield.addAllyToCounter();
        }
    }
}
