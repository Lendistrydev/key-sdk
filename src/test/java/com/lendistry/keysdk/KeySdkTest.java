package com.lendistry.keysdk;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nimbusds.jose.jwk.JWK;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

public class KeySdkTest {

  @Test
  public void convertToJwk() throws NoSuchAlgorithmException {
    KeySdk sdk = new KeySdk();
    KeyPair keyPair = sdk.getKeyGenerator().generateRSA4096();

    JWK jwk = KeySdk.convertToJwk(keyPair);
    assertNotNull(jwk);

    JWK publicJwk = KeySdk.convertToJwk(keyPair.getPublic());
    assertNotNull(publicJwk);

    System.out.println("JWK: " + jwk);
    System.out.println("Public JWK: " + publicJwk);
    System.out.println("Public JWK: " + jwk.toPublicJWK());
  }
}
