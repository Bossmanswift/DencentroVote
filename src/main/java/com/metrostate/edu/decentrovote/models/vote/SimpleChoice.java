package com.metrostate.edu.decentrovote.models.vote;

import com.metrostate.edu.decentrovote.models.entities.ChoiceEntity;

import java.io.Serializable;
import java.util.Objects;

public class SimpleChoice extends AbstractGenericChoice<Integer> implements Comparable <SimpleChoice>, Serializable {

    private ChoiceEntity choiceEntity;

    public SimpleChoice () {
        super();
    }

    public SimpleChoice (ChoiceEntity choiceEntity) {
        super(choiceEntity.getChoiceName(), choiceEntity.getChoiceDescription());
        this.choiceEntity = choiceEntity;
    }

    @Override
    public Integer getChoiceID() {
        return choiceEntity.getChoiceId();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(choiceEntity.getChoiceName(), choiceEntity.getChoiceDescription());
        result = 31 * result + choiceEntity.getChoiceId().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        SimpleChoice otherSimpleChoice = (SimpleChoice) otherObject;
        return this.choiceEntity.getChoiceId().equals(otherSimpleChoice.getChoiceID());
    }

    @Override
    public int compareTo(SimpleChoice otherSimpleChoice) {
        return this.getChoiceName().compareTo(otherSimpleChoice.getChoiceName());
    }
}
