package com.metrostate.edu.decentrovote.services;

import com.metrostate.edu.decentrovote.models.entities.ChoiceEntity;
import com.metrostate.edu.decentrovote.repositories.ChoiceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChoiceService extends AbstractDataAccessService<ChoiceEntity> {
    public static final Logger logger = LogManager.getLogger(ChoiceService.class);

    private final ChoiceRepository choiceRepository;

    @Autowired
    public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    public List<ChoiceEntity> getAllChoices () {
        return this.choiceRepository.findAll();
    }

    public ChoiceEntity createChoice(ChoiceEntity choiceEntity) {
        if (choiceEntity.getCreatedDate() == null) {
            choiceEntity.setCreatedDate(LocalDateTime.now());
        }
        return this.choiceRepository.save(choiceEntity);
    }

    public ChoiceEntity findChoiceById(Integer choiceId) {
        return this.choiceRepository.findById(choiceId).orElse(null);
    }

    public void deleteChoice(ChoiceEntity choiceEntity) {
        this.choiceRepository.delete(choiceEntity);
    }

    public void deleteChoicesByElectionId (Integer electionId) {
        List<ChoiceEntity> choicesToBeDeleted = choiceRepository.findByElectionId(electionId);
        choiceRepository.deleteAllInBatch(choicesToBeDeleted);
    }

    public ChoiceEntity updateChoice(ChoiceEntity updatedChoice) {
        ChoiceEntity existingChoice = this.choiceRepository.findById(updatedChoice.getChoiceId()).orElse(null);
        if (existingChoice == null) {
            String errorMessage = "Choice not found in the database";
            logger.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        existingChoice.setChoiceName(updatedChoice.getChoiceName());
        existingChoice.setChoiceDescription(updatedChoice.getChoiceDescription());
        existingChoice.setElectionId(updatedChoice.getElectionId());
        return choiceRepository.save(existingChoice);
    }

    @Override
    protected Class<ChoiceEntity> getEntityClass() {
        return ChoiceEntity.class;
    }
}
