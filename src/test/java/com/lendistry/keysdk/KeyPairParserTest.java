package com.lendistry.keysdk;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class KeyPairParserTest {

  @Test
  public void parsePem_withValidPem_shouldParseSuccessfully() throws IOException {
    try (InputStream resourceAsStream =
        KeyPairParserTest.class.getClassLoader().getResourceAsStream("private.pem")) {
      KeyPair keyPair = KeyPairParser.parsePem(resourceAsStream);

      assertNotNull(keyPair);
      assertNotNull(keyPair.getPublic());
      assertNotNull(keyPair.getPrivate());
    }
  }

  @Test
  public void parsePem_withPathToValidPemFile_shouldParseSuccessfully() throws IOException {
    String path =
        Objects.requireNonNull(KeyPairParserTest.class.getClassLoader().getResource("private.pem"))
            .getFile();
    KeyPair keyPair = KeyPairParser.parsePem(path);

    assertNotNull(keyPair);
    assertNotNull(keyPair.getPublic());
    assertNotNull(keyPair.getPrivate());
  }

  @Test
  public void parsePem_withValidPublicPem_shouldParseSuccessfully() throws IOException {
    try (InputStream resourceAsStream =
                 KeyPairParserTest.class.getClassLoader().getResourceAsStream("lendistry-public-key.pem")) {
      PublicKey publicKey = KeyPairParser.parsePublicKeyPem(resourceAsStream);

      assertNotNull(publicKey);
    }
  }
}
