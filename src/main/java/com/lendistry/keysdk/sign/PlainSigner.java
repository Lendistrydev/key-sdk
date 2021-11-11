package com.lendistry.keysdk.sign;

import com.lendistry.keysdk.KeySdkException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jca.JCAContext;
import com.nimbusds.jose.util.Base64URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

public class PlainSigner implements Signer {

  private final JCAContext context;

  public PlainSigner(JCAContext context) {
    this.context = context;
  }

  @Override
  public byte[] sign(SigningAlg signatureAlg, byte[] message, PrivateKey privateKey)
      throws KeySdkException {
    try {
      Signature signature = SignatureFactory.create(signatureAlg, context.getProvider());
      signature.initSign(privateKey);
      signature.update(message);
      return signature.sign();
    } catch (JOSEException | SignatureException | InvalidKeyException e) {
      throw new KeySdkException("Failed to sign message: " + Arrays.toString(message));
    }
  }

  @Override
  public String sign(SigningAlg signatureAlg, String message, PrivateKey privateKey)
      throws KeySdkException {
    try {
      Signature signature = SignatureFactory.create(signatureAlg, context.getProvider());
      signature.initSign(privateKey);
      signature.update(message.getBytes(StandardCharsets.UTF_8));
      return Base64URL.encode(signature.sign()).toString();
    } catch (JOSEException | SignatureException | InvalidKeyException e) {
      throw new KeySdkException("Failed to sign message: " + message);
    }
  }

  @Override
  public boolean verify(
      SigningAlg signatureAlg, String message, String signature, PublicKey publicKey)
      throws KeySdkException {
    try {
      Signature publicSignature = SignatureFactory.create(signatureAlg, context.getProvider());
      publicSignature.initVerify(publicKey);
      publicSignature.update(message.getBytes(StandardCharsets.UTF_8));
      return publicSignature.verify(Base64URL.from(signature).decode());
    } catch (JOSEException | SignatureException | InvalidKeyException e) {
      throw new KeySdkException("Failed to verify message: " + message);
    }
  }

  @Override
  public boolean verify(
      SigningAlg signatureAlg, byte[] message, byte[] signature, PublicKey publicKey)
      throws KeySdkException {
    try {
      Signature publicSignature = SignatureFactory.create(signatureAlg, context.getProvider());
      publicSignature.initVerify(publicKey);
      publicSignature.update(message);
      return publicSignature.verify(signature);
    } catch (JOSEException | SignatureException | InvalidKeyException e) {
      throw new KeySdkException("Failed to verify message: " + message);
    }
  }
}
