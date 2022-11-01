package com.metrostate.edu.decentrovote.controllers;

import com.metrostate.edu.decentrovote.models.entities.ChoiceEntity;
import com.metrostate.edu.decentrovote.services.ChoiceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.BadRequestException;
import java.util.List;

import static com.metrostate.edu.decentrovote.security.WebSecurityConfig.SECURITY_CONFIG_NAME;

@RestController
@SecurityRequirement(name = SECURITY_CONFIG_NAME)
@Tag(name = "Candidate REST API", description = "Use this API to perform allowed operations on a Candidate entity")
public class CandidateController {

    private final ChoiceService choiceService;

    @Autowired
    public CandidateController(ChoiceService choiceService) {
        this.choiceService = choiceService;
    }

    @PostMapping("/create-choice")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public ChoiceEntity createNewChoice(@RequestBody ChoiceEntity newChoice) {
        return choiceService.createChoice(newChoice);
    }

    @GetMapping("/choices")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public List<ChoiceEntity> getAllChoices () {
        return choiceService.getAllChoices();
    }

    @PutMapping("/update-choice")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public ChoiceEntity updateChoice (@RequestBody ChoiceEntity choiceEntity) {
        return choiceService.updateChoice(choiceEntity);
    }


    @DeleteMapping("delete/{choiceName}")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public void deleteChoiceByName(@PathVariable String choiceName) {
        ChoiceEntity choiceEntity = choiceService.findByNaturalId(choiceName);
        if (choiceEntity == null) {
            throw new BadRequestException(String.format("Choice %s not found", choiceName));
        }
        choiceService.deleteChoice(choiceEntity);
    }

    @DeleteMapping("delete-choices-by-election-id/{electionId}")
    @PreAuthorize("hasRole('ELECT_ADMIN')")
    public void deleteChoicesByElectionId(@PathVariable Integer electionId) {
        choiceService.deleteChoicesByElectionId(electionId);
    }
}
