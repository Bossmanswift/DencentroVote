package com.metrostate.edu.decentrovote.controllers;

import com.metrostate.edu.decentrovote.models.vote.Ballot;
import com.metrostate.edu.decentrovote.models.vote.SimpleChoice;
import com.metrostate.edu.decentrovote.services.BallotService;
import com.metrostate.edu.decentrovote.services.ElectionProtocolService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.metrostate.edu.decentrovote.security.WebSecurityConfig.SECURITY_CONFIG_NAME;

@RestController
@SecurityRequirement(name = SECURITY_CONFIG_NAME)
@Tag(name = "Election Protocol REST API", description = "Use to control the election operations")
public class ElectionProtocolController {
    private final ElectionProtocolService electionProtocolService;
    private final BallotService ballotService;

    @Autowired
    public ElectionProtocolController(ElectionProtocolService electionProtocolService, BallotService ballotService) {
        this.electionProtocolService = electionProtocolService;
        this.ballotService = ballotService;
    }

    @PostMapping("/start-simple-majority-protocol")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public void startSingleMajorityElection (@RequestParam String electionDescription, @RequestParam Integer electionId) {
        List<SimpleChoice> simpleChoices = new ArrayList<>();
        electionProtocolService.instantiateSimpleMajorityProtocol(simpleChoices, electionDescription, electionId);
    }

    @GetMapping("/get-ballots")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public Collection<Ballot> getBallots () {
        return electionProtocolService.getBallots();
    }

    @GetMapping("/get-election-session-status")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public Boolean getElectionSessionStatus () {
        return electionProtocolService.electoralSystemIsInSession();
    }

    @PostMapping("/stop-election")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public void stopElection () {
        electionProtocolService.stopElection();
    }

    @GetMapping("/tally")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public void tallyAndGetElectionResults () {
        electionProtocolService.tallyAndDetermineElectionWinner();
    }
}
