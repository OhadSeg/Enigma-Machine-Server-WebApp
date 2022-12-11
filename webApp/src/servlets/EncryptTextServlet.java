package servlets;

import components.Enigma;
import constants.Constants;
import enigmaDtos.FileDetailsDTO;
import exceptions.UserInputException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import operation.Tests;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.*;

@WebServlet(name = "EncryptTextServlet", urlPatterns = {"/encryptText"})
public class EncryptTextServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String textToCode = request.getParameter(TEXT_TO_CODE);
        String textToCodeAfterUpper = textToCode.toUpperCase();
        FileDetailsDTO fileDetailsDTO = null;

        synchronized (this) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String uboatName = (String) request.getSession().getAttribute(Constants.USERNAME);
            Enigma enigma = userManager.getEnigmaFromUboat(uboatName);
            Tests tests = userManager.getTest();
            if(tests.checkIfTextToCodeFromUserInDictionary(textToCode, enigma) == false) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println("The entered text does not in the dictionary");
            }
            else {
                enigma.execute(textToCodeAfterUpper);
                fileDetailsDTO = enigma.getConfigurationAsDTO();
                response.getWriter().println(GSON_INSTANCE.toJson(fileDetailsDTO));
            }
        }
    }
}
