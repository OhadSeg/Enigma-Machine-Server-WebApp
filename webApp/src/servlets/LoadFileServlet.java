package servlets;

import bruteForce.DecriptionManager;
import com.google.gson.Gson;
import components.Battlefield;
import components.Enigma;
import constants.Constants;
import enigmaDtos.FileDetailsDTO;
import exceptions.MachineDetailsFromFileException;
import exceptions.MachineDetailsFromUserException;
import generated.CTEEnigma;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import operation.Tests;
import users.BattlefieldManager;
import users.Uboat;
import users.UserManager;
import utils.ServletUtils;
import utils.UserType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "servlets.LoadFileServlet", urlPatterns = {"/loadFile"})

public class LoadFileServlet extends HttpServlet {

    private final static String XML_FILE_CONSTANT = "xmlfile";
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain");
        Part part = request.getPart(XML_FILE_CONSTANT);

        try {
            CTEEnigma enigma123 = desrializeFrom(part.getInputStream());
            Gson gson = new Gson();
            Enigma enigmaTest = new Enigma(enigma123);
            Tests tests = new Tests();
            tests.runEnigmaSetTests(enigmaTest);
            Enigma enigma = new Enigma(enigma123);
            DecriptionManager decriptionManager = new DecriptionManager(enigma123);
            Battlefield battlefield = new Battlefield(enigma123.getCTEBattlefield());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());

            FileDetailsDTO fileDetailsDTO = new FileDetailsDTO(enigma.getAmountOfAllReflectors(), enigma.getAbc(), enigma.getAmountOfAllRotors(),
                    enigma.getAmountOfRotorsInUse(), decriptionManager.getDictionary(), null, null,
                    null, enigma.getAllRotorsNotches());

            synchronized (this) {
                if (battlefieldManager.isBattlefieldExists(battlefield.getBattleName())) {
                    String errorMessage = "Battlefield " + battlefield.getBattleName() + " already exists. Please enter a different battlefield.";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//405
                    response.getOutputStream().print(errorMessage);
                } else {
                    battlefieldManager.addBattlefield(battlefield.getBattleName(), battlefield);
                    String uboatName = (String) request.getSession().getAttribute(Constants.USERNAME);
                    battlefield.setUboatName(uboatName);
                    Uboat uboat = new Uboat(enigma, battlefield.getBattleName());
                    userManager.addEntity(uboatName, uboat, UserType.UBOAT);
                    response.getWriter().println(gson.toJson(fileDetailsDTO));
                }
            }
        } catch (JAXBException e) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().println(e.getMessage());
        } catch (MachineDetailsFromFileException e) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().println(e.getMessage());
        }
    }

    private static CTEEnigma desrializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }
}
