package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Ally;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "AllyReadyServlet", urlPatterns = {"/allyReady"})
public class AllyReadyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        int taskSizeFromParameter = Integer.parseInt(request.getParameter("tasksize"));
        String allyNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Ally ally = userManager.getAlly(allyNameFromSession);
        if(ally.getAmountOfAgents() == 0){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().println("You can't be ready before agents have joined");
        }
        ally.setTaskSize(taskSizeFromParameter);
        ally.setReady(true);
    }
}
