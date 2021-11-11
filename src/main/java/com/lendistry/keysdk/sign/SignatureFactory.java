package com.lendistry.keysdk.sign;

import com.lendistry.keysdk.KeySdkException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.impl.ECDSA;
import com.nimbusds.jose.crypto.impl.RSASSA;
import java.security.Provider;
import java.security.Signature;

public class SignatureFactory {

  private SignatureFactory() {}

  public static Signature create(SigningAlg signatureAlg) throws JOSEException, KeySdkException {
    return create(signatureAlg, null);
  }

  public static Signature create(SigningAlg signatureAlg, Provider provider)
      throws JOSEException, KeySdkException {
    if (JWSAlgorithm.Family.RSA.contains(signatureAlg.getJwsAlgorithm())) {
      return RSASSA.getSignerAndVerifier(signatureAlg.getJwsAlgorithm(), provider);
    }
    if (JWSAlgorithm.Family.EC.contains(signatureAlg.getJwsAlgorithm())) {
      return ECDSA.getSignerAndVerifier(signatureAlg.getJwsAlgorithm(), provider);
    }
    throw new KeySdkException("Unsupported algorithm: " + signatureAlg);
  }
}
