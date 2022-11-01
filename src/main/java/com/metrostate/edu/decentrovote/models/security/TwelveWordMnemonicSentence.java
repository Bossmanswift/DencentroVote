package com.metrostate.edu.decentrovote.models.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metrostate.edu.decentrovote.models.security.interfaces.IMnemonicSentence;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwelveWordMnemonicSentence implements IMnemonicSentence {
    private String wordOne;
    private String wordTwo;
    private String wordThree;
    private String wordFour;
    private String wordFive;
    private String wordSix;
    private String wordSeven;
    private String wordEight;
    private String wordNine;
    private String wordTen;
    private String wordEleven;
    private String wordTwelve;

    private List<String> mWordList;

    public TwelveWordMnemonicSentence () {
    }

    public TwelveWordMnemonicSentence(List<String> mWordList) {
        if (mWordList.size() != 12) {
            throw new BadRequestException ("The word list is not 12");
        }
        this.mWordList = mWordList;
    }

    public String getWordOne() {
        return wordOne = mWordList.get(0);
    }

    public String getWordTwo() {
        return wordTwo = mWordList.get(1);
    }

    public String getWordThree() {
        return wordThree = mWordList.get(2);
    }

    public String getWordFour() {
        return wordFour = mWordList.get(3);
    }

    public String getWordFive() {
        return wordFive = mWordList.get(4);
    }

    public String getWordSix() {
        return wordSix = mWordList.get(5);
    }

    public String getWordSeven() {
        return wordSeven = mWordList.get(6);
    }

    public String getWordEight() {
        return wordEight = mWordList.get(7);
    }

    public String getWordNine() {
        return wordNine = mWordList.get(8);
    }

    public String getWordTen() {
        return wordTen = mWordList.get(9);
    }

    public String getWordEleven() {
        return wordEleven = mWordList.get(10);
    }

    public String getWordTwelve() {
        return wordTwelve = mWordList.get(11);
    }

    @Override
    @JsonIgnore
    public List<String> getWordList() {
        if (mWordList != null && !mWordList.isEmpty()) {
            if (mWordList.size() != 12) {
                throw new BadRequestException ("The word list is not 12");
            }
            return mWordList;
        }
        mWordList = new ArrayList<>();
        mWordList.addAll(Arrays.asList(wordOne,
                wordTwo,
                wordThree,
                wordFour,
                wordFive,
                wordSix,
                wordSeven,
                wordEight,
                wordNine,
                wordTen,
                wordEleven,
                wordTwelve));
        return mWordList;
    }
}
