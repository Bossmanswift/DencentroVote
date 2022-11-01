package com.metrostate.edu.decentrovote.repositories;

import com.metrostate.edu.decentrovote.models.entities.ChoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<ChoiceEntity, Integer> {
    @Query(value = "Select * from Choice c where c.electionId = :electionId",nativeQuery = true)
    public List<ChoiceEntity> findByElectionId(Integer electionId);
}
