package com.lendistry.keysdk.enc;

import com.lendistry.keysdk.KeySdkException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jca.JWEJCAContext;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

public class PlainEncryptor implements Encryptor {

  private static final String MESSAGE_CLAIM_NAME = "message";

  private final JWEJCAContext context;

  public PlainEncryptor(JWEJCAContext context) {
    this.context = context;
  }

  @Override
  public String encrypt(
      EncryptionAlg encryptionAlg, ContentAlg contentAlg, String message, PublicKey publicKey)
      throws KeySdkException {
    try {
      EncryptedJWT encryptedMessage =
          new EncryptedJWT(
              new JWEHeader(encryptionAlg.getJweAlgorithm(), contentAlg.getEncryptionMethod()),
              new JWTClaimsSet.Builder().claim(MESSAGE_CLAIM_NAME, message).build());

      encryptedMessage.encrypt(
          createEncrypter(encryptionAlg, new RSAKey.Builder((RSAPublicKey) publicKey).build()));
      return encryptedMessage.serialize();
    } catch (JOSEException e) {
      throw new KeySdkException("Failed to encrypt message: " + message);
    }
  }

  @Override
  public String decrypt(String encryptedMessage, PrivateKey privateKey) throws KeySdkException {
    try {
      EncryptedJWT encMessage = EncryptedJWT.parse(encryptedMessage);
      encMessage.decrypt(createDecrypter(encMessage.getHeader().getAlgorithm(), privateKey));
      return encMessage.getJWTClaimsSet().getStringClaim(MESSAGE_CLAIM_NAME);
    } catch (JOSEException | ParseException e) {
      throw new KeySdkException("Failed to decrypt message: " + encryptedMessage);
    }
  }

  private JWEEncrypter createEncrypter(EncryptionAlg encryptionAlg, RSAKey rsaKey)
      throws JOSEException, KeySdkException {
    if (JWEAlgorithm.Family.RSA.contains(encryptionAlg.getJweAlgorithm())) {
      return new RSAEncrypter(rsaKey.toRSAPublicKey()) {
        @Override
        public JWEJCAContext getJCAContext() {
          return context;
        }
      };
    }

    throw new KeySdkException("Unsupported algorithm: " + encryptionAlg);
  }

  private JWEDecrypter createDecrypter(JWEAlgorithm encryptionAlg, PrivateKey privateKey)
      throws KeySdkException {
    if (JWEAlgorithm.Family.RSA.contains(encryptionAlg)) {
      return new RSADecrypter(privateKey) {
        @Override
        public JWEJCAContext getJCAContext() {
          return context;
        }
      };
    }

    throw new KeySdkException("Unsupported algorithm: " + encryptionAlg);
  }
}
