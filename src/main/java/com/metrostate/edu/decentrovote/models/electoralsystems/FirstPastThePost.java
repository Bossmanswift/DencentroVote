package com.metrostate.edu.decentrovote.models.electoralsystems;

import com.metrostate.edu.decentrovote.exceptions.TieException;
import com.metrostate.edu.decentrovote.models.electoralsystems.interfaces.IQuotaSystem;
import com.metrostate.edu.decentrovote.models.vote.AbstractGenericChoice;
import com.metrostate.edu.decentrovote.models.vote.Ballot;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class FirstPastThePost extends AbstractElectoralSystem {
    protected IQuotaSystem<FirstPastThePost> quotaSystem;
    private final int quota;
    private final Map<AbstractGenericChoice<?>, Integer> tallyMap;

    public FirstPastThePost(SingleWinnerSystem singleWinnerSystem,
                            Collection<? extends AbstractGenericChoice<?>> choices,
                            int quota,
                            String electionDescription,
                            int electionId) {
        super(singleWinnerSystem, choices, electionDescription, electionId);
        this.quota = quota;
        tallyMap = new DualHashBidiMap<>();
    }

    private boolean quotaAchieved() {
        return quotaSystem.quotaAchieved(this);
    }

    public String getQuotaStringValue() {
        return String.valueOf(quota);
    }

    public int getQuota() {
        return quota;
    }

    @Override
    public void tallyAndDetermineWinner () {
        if (quota > 0) {
            if (!quotaAchieved()) {
                throw new RuntimeException("Quota Not Achieved!");
            }
        }
        setWinner(getWinningChoice().getChoiceName());
        stopSession();
    }

    public int winnerVoteCount() {
        AbstractGenericChoice<?> interimWinner = getWinningChoice();
        return getTallyMap().get(interimWinner);
    }

    private AbstractGenericChoice<?> getWinningChoice () {
        AbstractGenericChoice<?> winningChoice = getTallyMap().getKey(Collections.max(getTallyMap().values()));
        int winnerBallotCount = getTallyMap().get(winningChoice);
        getTallyMap().remove(winningChoice);

        Set<AbstractGenericChoice<?>> ties = getTallyMap().entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(winnerBallotCount))
                .map(Map.Entry::getKey).collect(Collectors.toSet());

        if (!ties.isEmpty()) {
            throw new TieException("Tie in Results");
        }
        return winningChoice;
    }

    private DualHashBidiMap<AbstractGenericChoice<?>, Integer> getTallyMap () {
        if (!tallyMap.isEmpty()) {
            return (DualHashBidiMap<AbstractGenericChoice<?>, Integer>) tallyMap;
        }
        ballots.forEach(ballot -> incrementBallotCount(ballot, tallyMap));
        return (DualHashBidiMap<AbstractGenericChoice<?>, Integer>) tallyMap;
    }

    private void incrementBallotCount (Ballot ballot, Map<AbstractGenericChoice<?>, Integer> tallyMap) {
        if (tallyMap.containsKey(ballot.getChoice())) {
            int currentCount = tallyMap.get(ballot.getChoice());
            tallyMap.put(ballot.getChoice(), currentCount + 1);
        } else {
            tallyMap.put(ballot.getChoice(), 1);
        }
    }
}
