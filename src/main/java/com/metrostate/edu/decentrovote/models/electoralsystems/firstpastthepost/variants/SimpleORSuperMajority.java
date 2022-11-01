package com.metrostate.edu.decentrovote.models.electoralsystems.firstpastthepost.variants;

import com.metrostate.edu.decentrovote.models.electoralsystems.firstpastthepost.QuotaSystemForFPTPImpl;
import com.metrostate.edu.decentrovote.models.electoralsystems.FirstPastThePost;
import com.metrostate.edu.decentrovote.models.vote.AbstractGenericChoice;

import java.util.Collection;

public class SimpleORSuperMajority extends FirstPastThePost {
    public SimpleORSuperMajority(SingleWinnerSystem singleWinnerSystem,
                                 Collection<? extends AbstractGenericChoice<?>> choices,
                                 int quota,
                                 String electionDescription, int electionId) {
        super(singleWinnerSystem, choices, quota, electionDescription, electionId);
        quotaSystem = new QuotaSystemForFPTPImpl();
    }

    @Override
    public String getName() {
        return getSingleWinnerSystem().getVotingSystemName();
    }
}
