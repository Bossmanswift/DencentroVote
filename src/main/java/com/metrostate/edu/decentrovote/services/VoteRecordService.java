package com.metrostate.edu.decentrovote.services;

import com.metrostate.edu.decentrovote.models.entities.VoteRecordEntity;
import com.metrostate.edu.decentrovote.models.security.TwelveWordMnemonicSentence;
import com.metrostate.edu.decentrovote.models.vote.Ballot;
import com.metrostate.edu.decentrovote.models.vote.BallotTrackerModel;
import com.metrostate.edu.decentrovote.repositories.VoteRecordRepository;
import com.metrostate.edu.decentrovote.services.interfaces.IPFSService;
import com.metrostate.edu.decentrovote.utils.AESUtility;
import com.metrostate.edu.decentrovote.utils.crypto.MnemonicCodeGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.metrostate.edu.decentrovote.utils.crypto.MnemonicCodeGeneratorUtil.getConcatenatedString;

@Service
public class VoteRecordService extends AbstractDataAccessService<VoteRecordEntity>{
    private final VoteRecordRepository voteRecordRepository;
    private final ElectionProtocolService electionProtocolService;
    private final IPFSService ipfsService;

    @Autowired
    public VoteRecordService(VoteRecordRepository voteRecordRepository,
                             ElectionProtocolService electionProtocolService,
                             IPFSService ipfsService) {
        this.voteRecordRepository = voteRecordRepository;
        this.electionProtocolService = electionProtocolService;
        this.ipfsService = ipfsService;
        this.ipfsService.initIPFS();
    }

    public List<VoteRecordEntity> getAllVoteRecords () {
        return this.voteRecordRepository.findAll();
    }

    public BallotTrackerModel createVoteRecord(User currentUser, TwelveWordMnemonicSentence mnemonicSentence, Ballot ballot) {
        String decryptedConcatenatedMnemonic = getConcatenatedString(mnemonicSentence.getWordList(), currentUser.getPassword()); // Todo: change to word twelve as opposed to password
        String decryptedVoterRecordId = getConcatenatedString(mnemonicSentence.getWordList(), ballot.getElectionDescription());
        if (voteAlreadyExists(decryptedVoterRecordId)) {
            throw new BadRequestException ("Voter Already Cast Their Vote");
        }

        String mnemonicCodePassphrase = mnemonicSentence.getWordOne().concat(mnemonicSentence.getWordTwo());
        String voteRecordId = MnemonicCodeGeneratorUtil.encodeMnemonicWordListWithPassPhrase(mnemonicSentence.getWordList(), mnemonicCodePassphrase);
        VoteRecordEntity voteRecordEntity = new VoteRecordEntity();
        voteRecordEntity.setVoteRecordId(voteRecordId);
        voteRecordEntity.setChoiceName(ballot.getChoice().getChoiceName());
        voteRecordEntity.setCreatedDate(LocalDateTime.now());
        voteRecordEntity.setChoiceDescription(ballot.getChoice().getChoiceDescription());
        voteRecordEntity.setElectionDescription(electionProtocolService.getElectionDescription());

        byte [] encryptedVoteRecord;
        byte [] seed = MnemonicCodeGeneratorUtil.toSeed(mnemonicSentence.getWordList(), mnemonicSentence.getWordOne().concat(mnemonicSentence.getWordTwo()));

        try {
            encryptedVoteRecord = AESUtility.encrypt(voteRecordEntity, decryptedConcatenatedMnemonic, seed);
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }

        Set<String> ipfsContentIdentifiers = (Set<String>) ipfsService.persistToIPFS(encryptedVoteRecord);

        voteRecordRepository.save(voteRecordEntity);

        return new BallotTrackerModel(voteRecordId, ballot, ipfsContentIdentifiers);
    }

    private boolean voteAlreadyExists(String voteRecordId) {
        List<VoteRecordEntity> voteRecords = voteRecordRepository.findAll();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return voteRecords
                .stream()
                .anyMatch(voteRecordEntity -> bCryptPasswordEncoder.matches(voteRecordId, voteRecordEntity.getVoteRecordId()));
    }

    @Override
    protected Class<VoteRecordEntity> getEntityClass() {
        return VoteRecordEntity.class;
    }
}
