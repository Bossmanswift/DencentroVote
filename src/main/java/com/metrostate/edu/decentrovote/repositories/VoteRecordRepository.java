package com.metrostate.edu.decentrovote.repositories;

import com.metrostate.edu.decentrovote.models.entities.VoteRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRecordRepository extends JpaRepository<VoteRecordEntity, String> {
}
