package com.metrostate.edu.decentrovote.models.vote;

import com.metrostate.edu.decentrovote.models.vote.interfaces.IChoice;

import java.io.Serializable;

public abstract class AbstractGenericChoice<ID> implements IChoice<ID>, Serializable {
    String choiceName;
    String choiceDescription;

    public AbstractGenericChoice (String choiceName, String choiceDescription) {
        this.choiceName = choiceName;
        this.choiceDescription = choiceDescription;
    }

    public AbstractGenericChoice() {

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
}
