package servlets;

import components.Enigma;
import constants.Constants;
import enigmaDtos.FileDetailsDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "ResetConfigurationServlet", urlPatterns = {"/resetConfiguration"})
public class ResetConfigurationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        synchronized (this) {
            String uboatName = (String) request.getSession().getAttribute(Constants.USERNAME);
            Enigma enigma = userManager.getEnigmaFromUboat(uboatName);
            enigma.reset();
            FileDetailsDTO fileDetailsDTO = new FileDetailsDTO();
            fileDetailsDTO = enigma.getConfigurationAsDTO();
            response.getWriter().println(GSON_INSTANCE.toJson(fileDetailsDTO));
        }
    }
}
