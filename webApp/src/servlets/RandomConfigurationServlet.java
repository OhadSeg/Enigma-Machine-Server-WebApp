package servlets;

import components.Enigma;
import constants.Constants;
import enigmaDtos.FileDetailsDTO;
import enigmaDtos.TestedMachineDetailsDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.RomanNumbers;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "RandomConfigurationServlet", urlPatterns = {"/randomConfiguration"})
public class RandomConfigurationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        FileDetailsDTO randomDetailsDTO = new FileDetailsDTO();
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String uboatName = (String) request.getSession().getAttribute(Constants.USERNAME);
        Enigma enigma = userManager.getEnigmaFromUboat(uboatName);

        randomDetailsDTO.setChosenValidInUseRotors(randomInUseRotors(enigma.getAmountOfAllRotors(), enigma.getAmountOfRotorsInUse()));
        randomDetailsDTO.setChosenValidStartPosRotors(randomStartingPos(enigma.getAmountOfRotorsInUse(), enigma.getAbc()));
        randomDetailsDTO.setChosenValidReflector(randomReflectorInUse(enigma.getAmountOfAllReflectors()));

        synchronized (this) {
            enigma.setEnigmaMachine(randomDetailsDTO);
            randomDetailsDTO.setNotchesPositionInUse(enigma.getAllRotorsNotches());
            response.getWriter().println(GSON_INSTANCE.toJson(randomDetailsDTO));
        }
    }

    private ArrayList<Integer> randomInUseRotors(int amountOfAllRotors, int amountOfRotorsInUse) {
        ArrayList<Integer> randomInUseToReturn = new ArrayList<>();
        ArrayList<Integer> randomFrom = new ArrayList<>();
        for (int i = 1; i <= amountOfAllRotors; i++) {
            randomFrom.add(i);
        }
        Collections.shuffle(randomFrom);
        for (int i = 0; i < amountOfRotorsInUse; i++) {
            randomInUseToReturn.add(randomFrom.get(i));
        }
        return randomInUseToReturn;
    }

    private ArrayList<Character> randomStartingPos(int amountOfRotorsInUse, String abcStr) {
        ArrayList<Character> startingPosToReturn = new ArrayList<>();
        ArrayList<Character> randomFrom = settingAbcToRandom(abcStr);
        for (int i = 0; i < amountOfRotorsInUse; i++) {
            startingPosToReturn.add(randomFrom.get(i));
        }
        return startingPosToReturn;
    }

    private ArrayList<Character> settingAbcToRandom(String abcStr) {
        ArrayList<Character> randomFrom = new ArrayList<>();
        for (int i = 0; i < abcStr.length(); i++) {
            randomFrom.add(abcStr.charAt(i));
        }
        Collections.shuffle(randomFrom);
        return randomFrom;
    }

    private RomanNumbers randomReflectorInUse(int amountOfAllReflectors) {
        Random rnd = new Random();
        int randInt = rnd.nextInt(amountOfAllReflectors);
        return RomanNumbers.values()[randInt];
    }
}
