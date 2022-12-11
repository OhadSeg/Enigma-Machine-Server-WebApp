package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "GetAlliesNamesServlet", urlPatterns = {"/getAlliesNames"})
public class GetAlliesNamesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        ArrayList<String> alliesNames = userManager.getAlliesNames();
        response.getWriter().println(GSON_INSTANCE.toJson(alliesNames));
    }
}
