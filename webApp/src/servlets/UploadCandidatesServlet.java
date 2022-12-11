package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jmx.remote.internal.ArrayQueue;
import components.Battlefield;
import enigmaDtos.CandidateDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.*;
import utils.BattlefieldStatus;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

@WebServlet(name = "UploadCandidatesServlet", urlPatterns = {"/uploadCandidates"})
public class UploadCandidatesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = scanner.hasNext() ? scanner.next() : "";
        ArrayList<CandidateDTO> candidateList = new Gson().fromJson(reqBodyAsString, new TypeToken<ArrayList<CandidateDTO>>() {
        }.getType());
        String agentNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Agent agent = userManager.getAgent(agentNameFromSession);
        String allyName = agent.getAllyName();
        Ally ally = userManager.getAlly(allyName);
        String uboatName = ally.getUboatAssignedTo();
        Uboat uboat = userManager.getUboat(uboatName);
        String winningWord = uboat.getWinningWord();

        boolean isWinner = false;
        int amountOfCandidates = candidateList.size();
        for (int i = 0; i < amountOfCandidates; i++) {
            CandidateDTO candidate = candidateList.get(i);
            if (winningWord.equals(candidate.getCandidateWord())) {
                candidate.setAsWinningWord();
                isWinner = true;
            }
            uboat.addCandidate(candidate);
            agent.addCandidate(candidate);
            ally.addCandidate(candidate);
        }
        if (isWinner) {
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            String battlefieldName = uboat.getBattlefieldName();
            Battlefield battlefield = battlefieldManager.getBattlefield(battlefieldName);
            battlefield.setWinningAlly(allyName);
            battlefield.setStatus(BattlefieldStatus.OFFLINE);
        }
    }
}