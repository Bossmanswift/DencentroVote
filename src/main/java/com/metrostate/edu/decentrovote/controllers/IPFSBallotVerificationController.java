package com.metrostate.edu.decentrovote.controllers;

import com.metrostate.edu.decentrovote.models.entities.VoteRecordEntity;
import com.metrostate.edu.decentrovote.models.security.TwelveWordMnemonicSentence;
import com.metrostate.edu.decentrovote.services.interfaces.IPFSService;
import com.metrostate.edu.decentrovote.utils.AESUtility;
import com.metrostate.edu.decentrovote.utils.MnemonicCodeVerificationService;
import com.metrostate.edu.decentrovote.utils.crypto.MnemonicCodeGeneratorUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import static com.metrostate.edu.decentrovote.security.WebSecurityConfig.SECURITY_CONFIG_NAME;
import static com.metrostate.edu.decentrovote.utils.crypto.MnemonicCodeGeneratorUtil.getConcatenatedString;

@RestController
@Tag(name = "IPFS Ballot Verification REST API", description = "Use to get a verification of your ballot with IPFS")
public class IPFSBallotVerificationController {
    private final IPFSService ipfsService;
    private final MnemonicCodeVerificationService mnemonicCodeVerificationService;

    @Autowired
    public IPFSBallotVerificationController(IPFSService ipfsService,
                                            MnemonicCodeVerificationService mnemonicCodeVerificationService) {
        this.ipfsService = ipfsService;
        this.mnemonicCodeVerificationService = mnemonicCodeVerificationService;
    }

    @PostMapping("/get-ipfs-ballot")
    @PreAuthorize("hasRole('VOTER')")
    @SecurityRequirement(name = SECURITY_CONFIG_NAME)
    public VoteRecordEntity getBallotFromIPFS(@Parameter(hidden = true) @AuthenticationPrincipal User currentUser,
                                              @RequestBody TwelveWordMnemonicSentence mnemonicSentence,
                                              @RequestParam String ipfsContentIdentifierHash) {
        boolean incorrectHash = mnemonicCodeVerificationService.hasInCorrectMnemonicCodeForVoter(currentUser, mnemonicSentence);

        if (incorrectHash) {
            throw new BadRequestException("Incorrect Content Identifier Hash");
        }

        byte [] encryptedBallot = ipfsService.retrieveFromIPFS(ipfsContentIdentifierHash);
        VoteRecordEntity decryptedBallot;
        String decryptedConcatenatedMnemonic = getConcatenatedString(mnemonicSentence.getWordList(), currentUser.getPassword());
        String mnemonicCodePassphrase = mnemonicSentence.getWordOne().concat(mnemonicSentence.getWordTwo());
        byte [] seed = MnemonicCodeGeneratorUtil.toSeed(mnemonicSentence.getWordList(), mnemonicCodePassphrase);
        try {
            decryptedBallot = (VoteRecordEntity) AESUtility.decrypt(encryptedBallot, decryptedConcatenatedMnemonic, seed);
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
        return decryptedBallot;
    }
}
