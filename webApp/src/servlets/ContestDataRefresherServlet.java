package servlets;

import components.Battlefield;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.BattlefieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Map;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "ContestDataServlet", urlPatterns = {"/contestDataRefresher"})
public class ContestDataRefresherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());


        synchronized (this){
            Map<String, Battlefield> battlefieldMap = battlefieldManager.getBattlefieldMap();
            response.getWriter().println(GSON_INSTANCE.toJson(battlefieldMap));
        }
    }
}
