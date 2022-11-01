package com.metrostate.edu.decentrovote.models.electoralsystems.firstpastthepost.variants;

import com.metrostate.edu.decentrovote.models.electoralsystems.FirstPastThePost;
import com.metrostate.edu.decentrovote.models.vote.AbstractGenericChoice;

import java.util.Collection;

public class SingleMemberPlurality extends FirstPastThePost {
    public SingleMemberPlurality(SingleWinnerSystem singleWinnerSystem,
                                 Collection<AbstractGenericChoice<?>> choices,
                                 int quota,
                                 String electionDescription,
                                 int electionId) {
        super(singleWinnerSystem, choices, quota, electionDescription, electionId);
    }

    @Override
    public String getName() {
        return getSingleWinnerSystem().getVotingSystemName();
    }
}
