package com.metrostate.edu.decentrovote.models.entities;

import com.metrostate.edu.decentrovote.models.entities.base.VoteRecordBase;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "VOTERECORD")
public class VoteRecordEntity extends VoteRecordBase {
}
