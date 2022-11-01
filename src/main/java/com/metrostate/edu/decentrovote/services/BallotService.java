package com.metrostate.edu.decentrovote.services;

import com.metrostate.edu.decentrovote.models.entities.ChoiceEntity;
import com.metrostate.edu.decentrovote.models.vote.Ballot;
import com.metrostate.edu.decentrovote.models.vote.SimpleChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BallotService {
    private final ChoiceService choiceService;
    private final ElectionProtocolService electionProtocolService;

    @Autowired
    public BallotService(ChoiceService choiceService, ElectionProtocolService electionProtocolService) {
        this.choiceService = choiceService;
        this.electionProtocolService = electionProtocolService;
    }

    public Ballot createBallot (String choiceName) {
        ChoiceEntity choiceEntity = choiceService.findByNaturalId(choiceName);
        SimpleChoice simpleChoice = new SimpleChoice(choiceEntity);
        return new Ballot(
                UUID.randomUUID().toString(),
                electionProtocolService.getVotingSystemName(),
                simpleChoice,
                electionProtocolService.getElectionDescription());
    }
}
