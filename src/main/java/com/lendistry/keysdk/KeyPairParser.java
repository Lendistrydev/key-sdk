package com.lendistry.keysdk;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.PublicKey;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
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

  public static PublicKey parsePublicKeyPem(String pathToPemFile) throws IOException {
    try (InputStream inputStream = new FileInputStream(pathToPemFile)) {
      return parsePublicKeyPem(inputStream);
    }
  }

  public static PublicKey parsePublicKeyPem(InputStream inputStream) throws IOException {
    try (InputStreamReader r = new InputStreamReader(inputStream)) {
      return parsePublicKeyPem(r);
    }
  }

  public static PublicKey parsePublicKeyPem(InputStreamReader inputStreamReader) throws IOException {
    KeySdk.installBouncyCastle();

    try (PEMParser pemParser = new PEMParser(inputStreamReader)) {
      SubjectPublicKeyInfo parsed = (SubjectPublicKeyInfo) pemParser.readObject();
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
      return converter.getPublicKey(parsed);
    }
  }
}
