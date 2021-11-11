package com.lendistry.keysdk.sign;

import com.lendistry.keysdk.KeySdkException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface Signer {

  byte[] sign(SigningAlg signatureAlg, byte[] message, PrivateKey privateKey)
      throws KeySdkException;

  String sign(SigningAlg signatureAlg, String message, PrivateKey privateKey)
      throws KeySdkException;

  boolean verify(SigningAlg signatureAlg, String message, String signature, PublicKey publicKey)
      throws KeySdkException;

  boolean verify(SigningAlg signatureAlg, byte[] message, byte[] signature, PublicKey publicKey)
      throws KeySdkException;
}
