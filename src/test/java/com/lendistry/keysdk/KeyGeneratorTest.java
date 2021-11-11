package com.lendistry.keysdk;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

public class KeyGeneratorTest {

  @Test
  public void generateRsa_whenCalled_shouldProduceKeyPair() throws NoSuchAlgorithmException {
    KeyPair keyPair = new KeySdk().getKeyGenerator().generateRSA2048();

    assertNotNull(keyPair);
    assertNotNull(keyPair.getPrivate());
    assertNotNull(keyPair.getPublic());
  }
}
