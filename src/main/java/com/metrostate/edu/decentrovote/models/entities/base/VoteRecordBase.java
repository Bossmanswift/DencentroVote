package com.metrostate.edu.decentrovote.models.entities.base;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The class {@link VoteRecordBase} defines the attributes for a Vote Record.
 */
@MappedSuperclass
public class VoteRecordBase implements Serializable {
    @Id
    String voteRecordId;
    String choiceName;
    String choiceDescription;
    String electionDescription;
    LocalDateTime createdDate;

    public String getVoteRecordId() {
        return voteRecordId;
    }

    public void setVoteRecordId(String ballotId) {
        this.voteRecordId = ballotId;
    }

    public String getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(String choiceName) {
        this.choiceName = choiceName;
    }

    public String getChoiceDescription() {
        return choiceDescription;
    }

    public void setChoiceDescription(String choiceDescription) {
        this.choiceDescription = choiceDescription;
    }

    public String getElectionDescription() {
        return electionDescription;
    }

    public void setElectionDescription(String electionDescription) {
        this.electionDescription = electionDescription;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
