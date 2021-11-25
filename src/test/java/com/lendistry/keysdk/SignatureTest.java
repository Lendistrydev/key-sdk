package com.lendistry.keysdk;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SignatureTest {

  private static KeySdk keySdk;
  private static KeyPair keyPair;

  @BeforeAll
  static void setUp() throws NoSuchAlgorithmException {
    keySdk = new KeySdk();
    keyPair = keySdk.getKeyGenerator().generateRSA2048();
  }

  @Test
  public void sign_whenSignByteArray_shouldVerifyItCorrectly() throws KeySdkException {
    byte[] message = "merchant lendistry".getBytes(StandardCharsets.UTF_8);

    byte[] signature = keySdk.sign(message, keyPair.getPrivate());

    boolean ok = keySdk.verify(message, signature, keyPair.getPublic());
    assertTrue(ok);
  }

  @Test
  public void sign_whenSignWithExpiration_shouldVerifyItCorrectly() throws KeySdkException {
    Date dateInFuture = new Date(new Date().getTime() + 10000);
    String message = "merchant lendistry";

    String signature = keySdk.sign(message, dateInFuture, keyPair.getPrivate());

    boolean ok = keySdk.verify(message, dateInFuture, signature, keyPair.getPublic());
    assertTrue(ok);
  }

  @Test
  public void sign_whenSignWithExpiredDate_shouldFail() {
    Date dateInPast = new Date(new Date().getTime() - 10000);
    String message = "merchant lendistry";

    assertThrows(
        KeySdkException.class, () -> keySdk.sign(message, dateInPast, keyPair.getPrivate()));
  }

  @Test
  public void sign_whenSignPlainText_shouldVerifyItCorrectly() throws KeySdkException {
    String message = "merchant lendistry";

    String signature = keySdk.sign(message, keyPair.getPrivate());

    boolean ok = keySdk.verify(message, signature, keyPair.getPublic());
    assertTrue(ok);
  }

  @Test
  public void sign_whenSignJson_shouldVerifyItCorrectly() throws KeySdkException {
    String exp = KeySdk.dateToIsoInUtc(new Date());

    String message =
        String.format(
            "{\"message\": \"\"merchant lendistry\"\", \"exp\": \"%s\", \"int\"%d}", exp, 5);

    String signature = keySdk.sign(message, keyPair.getPrivate());

    boolean ok = keySdk.verify(message, signature, keyPair.getPublic());
    assertTrue(ok);
  }

  @Test
  public void sign_whenSigningIsNotCorrect_shouldFail() throws KeySdkException {
    String message = "merchant lendistry";

    final String signature =
        keySdk.sign(message, keyPair.getPrivate()) + "a"; // make signature incorrect

    assertThrows(
        KeySdkException.class, () -> keySdk.verify(message, signature, keyPair.getPublic()));
  }
}
