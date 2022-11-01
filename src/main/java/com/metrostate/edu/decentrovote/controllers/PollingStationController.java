package com.metrostate.edu.decentrovote.controllers;

import com.metrostate.edu.decentrovote.models.entities.VoteRecordEntity;
import com.metrostate.edu.decentrovote.models.security.TwelveWordMnemonicSentence;
import com.metrostate.edu.decentrovote.models.vote.Ballot;
import com.metrostate.edu.decentrovote.models.vote.BallotTrackerModel;
import com.metrostate.edu.decentrovote.services.BallotService;
import com.metrostate.edu.decentrovote.services.ElectionProtocolService;
import com.metrostate.edu.decentrovote.services.VoteRecordService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.metrostate.edu.decentrovote.security.WebSecurityConfig.SECURITY_CONFIG_NAME;

@RestController
@Tag(name = "Polling Station REST API", description = "Controls polling operations such as the monitoring and verification election process")
public class PollingStationController {

    private final ElectionProtocolService electionProtocolService;
    private final BallotService ballotService;
    private final VoteRecordService voteRecordService;

    @Autowired
    public PollingStationController(ElectionProtocolService electionProtocolService, BallotService ballotService, VoteRecordService voteRecordService) {
        this.electionProtocolService = electionProtocolService;
        this.ballotService = ballotService;
        this.voteRecordService = voteRecordService;
    }

    @PostMapping("/add-ballot")
    @PreAuthorize("hasRole('VOTER')")
    @SecurityRequirement(name = SECURITY_CONFIG_NAME)
    public BallotTrackerModel addBallot (@Parameter(hidden = true) @AuthenticationPrincipal User currentUser,
                                         @RequestBody TwelveWordMnemonicSentence mnemonicSentence,
                                         @RequestParam String choiceName) {
        Ballot ballot = ballotService.createBallot(choiceName);
        BallotTrackerModel ballotTrackerModel = voteRecordService.createVoteRecord(currentUser, mnemonicSentence, ballot);
        electionProtocolService.addBallot(ballot);
        return ballotTrackerModel;
    }

    @GetMapping("/election-progress")
    public List<VoteRecordEntity> getAllVoteRecords() {
        return voteRecordService.getAllVoteRecords();
    }
}
