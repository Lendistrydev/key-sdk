package com.lendistry.keysdk.enc;

import com.lendistry.keysdk.KeySdkException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface Encryptor {

  String encrypt(
      EncryptionAlg encryptionAlg, ContentAlg contentAlg, String message, PublicKey publicKey)
      throws KeySdkException;

  String decrypt(String encryptedMessage, PrivateKey privateKey) throws KeySdkException;
}
