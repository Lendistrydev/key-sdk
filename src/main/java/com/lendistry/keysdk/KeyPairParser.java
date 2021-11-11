package com.lendistry.keysdk;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class KeyPairParser {

  private KeyPairParser() {}

  public static KeyPair parsePem(String pathToPemFile) throws IOException {
    try (InputStream inputStream = new FileInputStream(pathToPemFile)) {
      return parsePem(inputStream);
    }
  }

  public static KeyPair parsePem(InputStream inputStream) throws IOException {
    try (InputStreamReader r = new InputStreamReader(inputStream)) {
      return parsePem(r);
    }
  }

  public static KeyPair parsePem(InputStreamReader inputStreamReader) throws IOException {
    KeySdk.installBouncyCastle();

    try (PEMParser pemParser = new PEMParser(inputStreamReader)) {
      PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
      return converter.getKeyPair(pemKeyPair);
    }
  }
}
