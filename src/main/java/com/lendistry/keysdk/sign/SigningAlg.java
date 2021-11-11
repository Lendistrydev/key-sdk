package com.lendistry.keysdk.sign;

import com.nimbusds.jose.JWSAlgorithm;
import lombok.Getter;

public enum SigningAlg {
  HS256,
  HS384,
  HS512,

  RS256,
  RS384,
  RS512,

  ES256,
  ES256K,
  ES384,
  ES512,

  PS256,
  PS384,
  PS512,

  EdDSA;

  static { // use static block, init via constructor produce illegal forward reference
    HS256.jwsAlgorithm = JWSAlgorithm.HS256;
    HS384.jwsAlgorithm = JWSAlgorithm.HS384;
    HS512.jwsAlgorithm = JWSAlgorithm.HS512;

    RS256.jwsAlgorithm = JWSAlgorithm.RS256;
    RS384.jwsAlgorithm = JWSAlgorithm.RS384;
    RS512.jwsAlgorithm = JWSAlgorithm.RS512;

    ES256.jwsAlgorithm = JWSAlgorithm.ES256;
    ES256K.jwsAlgorithm = JWSAlgorithm.ES256K;
    ES384.jwsAlgorithm = JWSAlgorithm.ES384;
    ES512.jwsAlgorithm = JWSAlgorithm.ES512;

    PS256.jwsAlgorithm = JWSAlgorithm.PS256;
    PS384.jwsAlgorithm = JWSAlgorithm.PS384;
    PS512.jwsAlgorithm = JWSAlgorithm.PS512;

    EdDSA.jwsAlgorithm = JWSAlgorithm.EdDSA;
  }

  @Getter private JWSAlgorithm jwsAlgorithm;
}
