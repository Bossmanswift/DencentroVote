package com.metrostate.edu.decentrovote.utils;

import com.metrostate.edu.decentrovote.models.security.TwelveWordMnemonicSentence;
import com.metrostate.edu.decentrovote.models.security.UserEntity;
import com.metrostate.edu.decentrovote.services.interfaces.IUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.metrostate.edu.decentrovote.utils.crypto.MnemonicCodeGeneratorUtil.getConcatenatedString;

@Service
public class MnemonicCodeVerificationService {
    private final IUserService userService;

    public MnemonicCodeVerificationService(IUserService userService) {
        this.userService = userService;
    }

    public boolean hasInCorrectMnemonicCodeForVoter(User currentUser, TwelveWordMnemonicSentence mnemonicSentence) {
        String decryptedConcatenatedMnemonic = getConcatenatedString(mnemonicSentence.getWordList(), currentUser.getPassword());
        UserEntity userEntity = userService.findByUsername(currentUser.getUsername());
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return !bcrypt.matches(decryptedConcatenatedMnemonic, userEntity.getEncryptedMnemonicCode());
    }

}
