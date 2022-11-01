package com.metrostate.edu.decentrovote.models.vote;

import com.metrostate.edu.decentrovote.models.vote.interfaces.IBallot;

import java.io.Serializable;

public class Ballot implements IBallot, Serializable {
    private String ballotUID;
    private String electoralSystem;
    private SimpleChoice choice;
    private String electionDescription;

    public Ballot(String ballotUID,
                  String votingSystem,
                  SimpleChoice choice,
                  String electionDescription) {
        this.ballotUID = ballotUID;
        this.electoralSystem = votingSystem;
        this.electionDescription = electionDescription;
        this.choice = choice;
    }

    public String getElectionDescription() {
        return electionDescription;
    }

    public String getBallotUID() {
        return ballotUID;
    }

    public void setBallotUID(String ballotUID) {
        this.ballotUID = ballotUID;
    }

    public String getElectoralSystem() {
        return electoralSystem;
    }

    public void setElectoralSystem(String electoralSystem) {
        this.electoralSystem = electoralSystem;
    }

    public void setElectionDescription(String electionDescription) {
        this.electionDescription = electionDescription;
    }

    public SimpleChoice getChoice() {
        return choice;
    }

    public void setChoice(SimpleChoice choice) {
        this.choice = choice;
    }
}
