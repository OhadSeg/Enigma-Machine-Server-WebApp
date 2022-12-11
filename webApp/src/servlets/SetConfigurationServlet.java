package servlets;

import com.google.gson.Gson;
import components.Enigma;
import constants.Constants;
import enigmaDtos.FileDetailsDTO;
import exceptions.MachineDetailsFromUserException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import operation.Tests;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Scanner;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "SetConfigurationServlet", urlPatterns = {"/setConfiguration"})
public class SetConfigurationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = scanner.hasNext() ? scanner.next() : "";
        FileDetailsDTO fileDetailsDTO = new Gson().fromJson(reqBodyAsString, FileDetailsDTO.class);
        synchronized (this) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String uboatName = (String) request.getSession().getAttribute(Constants.USERNAME);
            Enigma enigma = userManager.getEnigmaFromUboat(uboatName);
            Tests test = userManager.getTest();
            try {
                test.runMachineDetailsTests(enigma, fileDetailsDTO);
            } catch (MachineDetailsFromUserException e) {
                throw new IOException(e.getMessage());
            }
            enigma.setEnigmaMachine(fileDetailsDTO);
            fileDetailsDTO.setNotchesPositionInUse(enigma.getAllRotorsNotches());
            response.getWriter().println(GSON_INSTANCE.toJson(fileDetailsDTO));
        }
    }
}
