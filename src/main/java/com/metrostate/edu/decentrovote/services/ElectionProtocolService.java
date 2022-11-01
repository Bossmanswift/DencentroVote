package com.metrostate.edu.decentrovote.services;

import com.metrostate.edu.decentrovote.models.electoralsystems.AbstractElectoralSystem;
import com.metrostate.edu.decentrovote.models.electoralsystems.ElectionProtocolSingleton;
import com.metrostate.edu.decentrovote.models.electoralsystems.firstpastthepost.variants.SimpleORSuperMajority;
import com.metrostate.edu.decentrovote.models.electoralsystems.firstpastthepost.variants.SingleMemberPlurality;
import com.metrostate.edu.decentrovote.models.vote.AbstractGenericChoice;
import com.metrostate.edu.decentrovote.models.vote.Ballot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.Collection;

import static com.metrostate.edu.decentrovote.models.electoralsystems.AbstractElectoralSystem.SingleWinnerSystem.SIMPLE_MAJORITY;
import static com.metrostate.edu.decentrovote.models.electoralsystems.AbstractElectoralSystem.SingleWinnerSystem.SIMPLE_PLURALITY;
import static com.metrostate.edu.decentrovote.models.electoralsystems.AbstractElectoralSystem.SingleWinnerSystem.SUPER_MAJORITY;

@Service
public class ElectionProtocolService {

    private final ElectionProtocolSingleton electionProtocolSingleton;

    @Autowired
    public ElectionProtocolService() {
        this.electionProtocolSingleton = ElectionProtocolSingleton.getInstance();
    }

    public void instantiateSimpleMajorityProtocol (Collection<? extends AbstractGenericChoice<?>> choices, String electionDescription, int electionId) {
        if (electoralSystemIsInSession()) {
            throw new BadRequestException(String.format("%s is still in session", electionProtocolSingleton.getElectoralSystem()
                    .getSingleWinnerSystem()
                    .getVotingSystemName()));
        }
        SimpleORSuperMajority simpleORSuperMajorityProtocol = new SimpleORSuperMajority(SIMPLE_MAJORITY, choices, 0, electionDescription, electionId);
        setElectoralSystem(simpleORSuperMajorityProtocol);
    }

    public void instantiateSuperMajorityProtocol (Collection<AbstractGenericChoice<?>> choices, int quota, String electionDescription, int electionId) {
        SimpleORSuperMajority simpleORSuperMajorityProtocol = new SimpleORSuperMajority(SUPER_MAJORITY, choices, quota, electionDescription, electionId);
        setElectoralSystem(simpleORSuperMajorityProtocol);
    }

    public void instantiateSimpleMemberPlurality (Collection<AbstractGenericChoice<?>> choices, String electionDescription, int electionId) {
        SingleMemberPlurality singleMemberPlurality = new SingleMemberPlurality(SIMPLE_PLURALITY, choices, 0, electionDescription, electionId);
    }

    public String getElectionDescription () {
        if (electoralSystemIsNull()) {
            return electionProtocolSingleton.getElectoralSystem().getElectionDescription();
        }
        throw new BadRequestException("Election is not is session");
    }

    public String getVotingSystemName () {
        if (electoralSystemIsNull()) {
            return electionProtocolSingleton.getElectoralSystem().getSingleWinnerSystem().getVotingSystemName();
        }
        throw new BadRequestException("Election is not is session");
    }

    public void addBallot (Ballot ballot) {
        if (!electoralSystemIsInSession()) {
            throw new BadRequestException("Election is not is session");
        }
        electionProtocolSingleton.getElectoralSystem().addToBallot(ballot);
    }

    public Collection<Ballot> getBallots () {
        if (electoralSystemIsInSession()) {
            return electionProtocolSingleton.getElectoralSystem().getBallots();
        }
        // Todo: Update Logic not to check if election is still in session
        throw new BadRequestException("Election is not is session");
    }

    public void tallyAndDetermineElectionWinner () {
        electionProtocolSingleton.getElectoralSystem().tallyAndDetermineWinner();
    }

    public Collection<? extends AbstractGenericChoice<?>> getChoices () {
        Collection<? extends AbstractGenericChoice<?>> choicesToReturn = electionProtocolSingleton.getElectoralSystem().getChoices();
        if (electoralSystemIsNull() &&  choicesToReturn != null) {
            return choicesToReturn;
        }
        throw new BadRequestException("Either election protocol or choices have been initialized");
    }

    public String getWinner () {
        if (electoralSystemIsInSession()) {
            throw new BadRequestException("Election protocol is still in session");
        }
        return electionProtocolSingleton.getElectoralSystem().getWinner();
    }

    public void stopElection () {
        if (!electoralSystemIsInSession()) {
            throw new BadRequestException("No election protocol is in session");
        }
        electionProtocolSingleton.getElectoralSystem().stopSession();
    }

    private void setElectoralSystem (AbstractElectoralSystem electoralSystem) {
        electionProtocolSingleton.setElectoralSystem(electoralSystem);
    }

    private boolean electoralSystemIsNull () {
        return electionProtocolSingleton.getElectoralSystem() != null;
    }

    public boolean electoralSystemIsInSession () {
        return electoralSystemIsNull() && electionProtocolSingleton.getElectoralSystem().isInSession();
    }
}
