package com.metrostate.edu.decentrovote.models.electoralsystems;

import com.metrostate.edu.decentrovote.models.electoralsystems.interfaces.IElectoralSystem;
import com.metrostate.edu.decentrovote.models.vote.AbstractGenericChoice;
import com.metrostate.edu.decentrovote.models.vote.Ballot;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractElectoralSystem implements IElectoralSystem {
    private final SingleWinnerSystem singleWinnerSystem;
    private boolean isInSession;
    private String winner;
    private final Collection<? extends AbstractGenericChoice<?>> choices;
    protected Collection<Ballot> ballots;
    private final String electionDescription;
    private final Integer electionId;

    protected final Logger logger = LogManager.getLogger(getClass());

    public AbstractElectoralSystem(SingleWinnerSystem singleWinnerSystem, Collection<? extends AbstractGenericChoice<?>> choices, String electionDescription, Integer electionId) {
        this.singleWinnerSystem = singleWinnerSystem;
        this.electionDescription = electionDescription;
        this.electionId = electionId;
        ballots = new ArrayList<>();
        this.choices = choices;
        isInSession = true;
    }

    public SingleWinnerSystem getSingleWinnerSystem() {
        return singleWinnerSystem;
    }

    /*public void setSingleWinnerSystem(SingleWinnerSystem singleWinnerSystem) {
        this.singleWinnerSystem = singleWinnerSystem;
    }*/

    protected void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner () {
        return winner;
    }

    public void stopSession () {
        isInSession = false;
    }

    public boolean isInSession () {
        return isInSession;
    }

    public Collection<? extends AbstractGenericChoice<?>> getChoices() {
        return choices;
    }

    public String getElectionDescription() {
        return electionDescription;
    }

    public abstract void tallyAndDetermineWinner ();

    public Collection<Ballot> getBallots () {
        return ballots;
    }

    public void addToBallot(Ballot ballot) {
        ballots.add(ballot);
    }

    protected void clearBallots() {
        ballots.clear();
    }

    public enum SingleWinnerSystem {
        FIRST_PAST_THE_POST_VOTING_SYSTEM("First Past The Post Voting System (FPTP)"),
        SIMPLE_PLURALITY ("Single-Member/Simple Plurality"),
        SIMPLE_MAJORITY("Simple Majority"),
        SUPER_MAJORITY("Super Majority"),
        APPROVAL_VOTING_SYSTEM("Approval Voting System"),
        TWO_ROUND_VOTING_SYSTEM("Two Round Voting System"),
        CONTINGENT_VOTE_SYSTEM("Contingent Voting System"),
        EXHAUSTIVE_BALLOT("Exhaustive Ballot Voting System"),
        INSTANT_RUN_OFF_VOTING("Instant Run-Off Voting System");

        String votingSystemName;

        SingleWinnerSystem(String votingSystemName) {
            this.votingSystemName = votingSystemName;
        }

        public String getVotingSystemName () {
            return votingSystemName;
        }
    }
}
