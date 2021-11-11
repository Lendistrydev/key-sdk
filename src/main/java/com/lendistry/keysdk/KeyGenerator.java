package com.lendistry.keysdk;

import com.nimbusds.jose.jca.JCAContext;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {

  private final JCAContext context;

  protected KeyGenerator(JCAContext context) {
    this.context = context;
  }

  public KeyPair generateRSA4096() throws NoSuchAlgorithmException {
    return generateRSA(4096);
  }

  public KeyPair generateRSA2048() throws NoSuchAlgorithmException {
    return generateRSA(2048);
  }

  public KeyPair generateRSA(int keyLength) throws NoSuchAlgorithmException {
    return generate(KeyAlgorithm.RSA, keyLength);
  }

  public KeyPair generate(KeyAlgorithm keyAlgorithm, int keyLength)
      throws NoSuchAlgorithmException {
    KeyPairGenerator generator =
        context.getProvider() != null
            ? KeyPairGenerator.getInstance(keyAlgorithm.getValue(), context.getProvider())
            : KeyPairGenerator.getInstance(keyAlgorithm.getValue());
    KeyPairGenerator.getInstance("RSA");
    generator.initialize(keyLength, context.getSecureRandom());
    return generator.generateKeyPair();
  }
}
