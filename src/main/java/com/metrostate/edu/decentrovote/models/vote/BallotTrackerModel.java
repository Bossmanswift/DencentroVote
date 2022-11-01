package com.metrostate.edu.decentrovote.models.vote;

import java.util.Set;

public class BallotTrackerModel {
    private final String ballotTrackerId;
    private final Ballot ballot;
    private final Set<String> ipfsCID;


    public BallotTrackerModel(String ballotTrackerId,
                              Ballot ballot, Set<String> ipfsCID) {
        this.ballotTrackerId = ballotTrackerId;
        this.ballot = ballot;
        this.ipfsCID = ipfsCID;
    }

    public String getBallotTrackerId() {
        return ballotTrackerId;
    }

    public Ballot getBallot() {
        return ballot;
    }

    public Set<String> getIpfsCID() {
        return ipfsCID;
    }
}
