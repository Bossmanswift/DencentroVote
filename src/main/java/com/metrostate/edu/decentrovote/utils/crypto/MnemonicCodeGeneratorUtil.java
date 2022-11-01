package com.metrostate.edu.decentrovote.utils.crypto;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A MnemonicCode object may be used to convert between binary seed values and
 * lists of words per <a href="https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki">the BIP 39
 * specification</a>
 */
@Component
public final class MnemonicCodeGeneratorUtil {

    public static List<String> generateMnemonicCodeWordList() throws IOException, MnemonicException.MnemonicLengthException, MnemonicException.MnemonicWordException {
        List<String> randomlySelectedWords = new ArrayList<>();
        MnemonicCode mnemonicCode = new MnemonicCode();

        Random random = new Random();
        int randomWordIndex = random.nextInt(mnemonicCode.getWordList().size());
        while (randomlySelectedWords.size() != 12) {
            String randomString = mnemonicCode.getWordList().get(randomWordIndex);
            randomlySelectedWords.add(randomString);
            randomWordIndex = random.nextInt(mnemonicCode.getWordList().size());
        }
        return randomlySelectedWords;
    }

    // Used for IPFS Encoding the user's Secret Key
    public static byte [] toSeed(List<String> words, String passPhrase) {
        return MnemonicCode.toSeed(words, passPhrase);
    }

    public static String encodeMnemonicWordListWithPassPhrase(List<String> words, String passPhrase) {
        String mnemonicString = getConcatenatedString(words, passPhrase);
        return new BCryptPasswordEncoder().encode(mnemonicString);
    }

    @NotNull
    public static String getConcatenatedString(List<String> words, String passPhrase) {
        return words
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining()).concat(passPhrase);
    }
}
