package com.metrostate.edu.decentrovote.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.InternalServerErrorException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class AESUtility {
    public static final String AES_CBC_PKCS5PADDING_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String PBKDF_2_WITH_HMAC_SHA_256 = "PBKDF2WithHmacSHA256";
    public static final String AES = "AES";
    public static final int ITERATION_COUNT = 65536;
    public static final int KEY_SIZE = 256;
    public static final int IV_SIZE = 16;

    public static byte[] encrypt(Serializable serializableObject, String mnemonicCodeString, byte[] entropy) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte [] encryptedBytes;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Cipher cipher = computeSecretKey(mnemonicCodeString, entropy, ENCRYPT_MODE);

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);

        SealedObject sealedObject = new SealedObject(serializableObject, cipher);
        CipherOutputStream cipherOutputStream = new CipherOutputStream(bufferedOutputStream, cipher);
        ObjectOutputStream outputStream = new ObjectOutputStream(cipherOutputStream);
        outputStream.writeObject(sealedObject);
        outputStream.close();

        encryptedBytes = byteArrayOutputStream.toByteArray();
        return encryptedBytes;
    }

    public static Object decrypt(byte [] cipherBytes, String mnemonicCodeString, byte[] entropy) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IOException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        ByteArrayInputStream byteArrayOutputStream = new ByteArrayInputStream(cipherBytes);

        Cipher cipher = computeSecretKey(mnemonicCodeString, entropy, DECRYPT_MODE);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayOutputStream);

        CipherInputStream cipherInputStream = new CipherInputStream(new BufferedInputStream(bufferedInputStream), cipher);
        ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);


        SealedObject sealedObject = (SealedObject) inputStream.readObject();

        return sealedObject.getObject(cipher);
    }

    private static Cipher computeSecretKey(String mnemonicCodeString, byte[] entropy, int encryptMode) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        SecretKey key = generateSecretKeyFromString(mnemonicCodeString, entropy);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING_ALGORITHM);
        cipher.init(encryptMode, key, new IvParameterSpec(new byte[IV_SIZE]));
        return cipher;
    }

    private static SecretKey generateSecretKeyFromString(String mnemonicCodeString, byte[] entropy) {
        SecretKey secretKey;
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_256);
            KeySpec spec = new PBEKeySpec(mnemonicCodeString.toCharArray(), entropy, ITERATION_COUNT, KEY_SIZE);
            secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), AES);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InternalServerErrorException(e);
        }
        return secretKey;
    }
}
