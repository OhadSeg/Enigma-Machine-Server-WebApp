package servlets;

import components.Battlefield;
import enigmaDtos.CandidateDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.*;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "ShowCandidatesRefresherServlet", urlPatterns = {"/showCandidatesRefresher"})
public class ShowCandidatesRefresherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        BlockingQueue<CandidateDTO> candidateDTOS;
        if(userManager.getAgent(userNameFromSession)!= null){
            Agent agent = userManager.getAgent(userNameFromSession);
            candidateDTOS = agent.getCandidateQueue();
        }
        else if(userManager.getAlly(userNameFromSession)!= null){
            Ally ally = userManager.getAlly(userNameFromSession);
            candidateDTOS = ally.getCandidateQueue();
        }
        else{
            Uboat uboat = userManager.getUboat(userNameFromSession);
            candidateDTOS = uboat.getCandidateQueue();
        }

        ArrayList<CandidateDTO> candidatesList = new ArrayList<>();
        int amountOfCandidates = candidateDTOS.size();
        for (int i = 0; i < amountOfCandidates; i++) {
            candidatesList.add(candidateDTOS.remove());
        }
        response.getWriter().println(GSON_INSTANCE.toJson(candidatesList));
    }
}
