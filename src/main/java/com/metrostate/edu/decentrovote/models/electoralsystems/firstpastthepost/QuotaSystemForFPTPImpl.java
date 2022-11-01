package com.metrostate.edu.decentrovote.models.electoralsystems.firstpastthepost;

import com.metrostate.edu.decentrovote.models.electoralsystems.FirstPastThePost;
import com.metrostate.edu.decentrovote.models.electoralsystems.interfaces.IQuotaSystem;

public class QuotaSystemForFPTPImpl implements IQuotaSystem<FirstPastThePost> {
    public QuotaSystemForFPTPImpl () {
    }
    @Override
    public boolean quotaAchieved(FirstPastThePost electoralSystem) {
        return electoralSystem.winnerVoteCount() > electoralSystem.getQuota();
    }
}
