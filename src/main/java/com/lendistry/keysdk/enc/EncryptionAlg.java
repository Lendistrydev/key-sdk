package com.lendistry.keysdk.enc;

import com.nimbusds.jose.JWEAlgorithm;
import lombok.Getter;

public enum EncryptionAlg {
  RSA_OAEP_256,
  RSA_OAEP_384,
  RSA_OAEP_512,

  A128KW,
  A192KW,
  A256KW,

  DIR,

  ECDH_ES,
  ECDH_ES_A128KW,
  ECDH_ES_A192KW,
  ECDH_ES_A256KW,
  ECDH_1PU,
  ECDH_1PU_A128KW,
  ECDH_1PU_A192KW,
  ECDH_1PU_A256KW,

  A128GCMKW,
  A192GCMKW,
  A256GCMKW,

  PBES2_HS256_A128KW,
  PBES2_HS384_A192KW,
  PBES2_HS512_A256KW;

  static { // use static block, init via constructor produce illegal forward reference
    RSA_OAEP_256.jweAlgorithm = JWEAlgorithm.RSA_OAEP_256;
    RSA_OAEP_384.jweAlgorithm = JWEAlgorithm.RSA_OAEP_384;
    RSA_OAEP_512.jweAlgorithm = JWEAlgorithm.RSA_OAEP_512;

    A128KW.jweAlgorithm = JWEAlgorithm.A128KW;
    A192KW.jweAlgorithm = JWEAlgorithm.A192KW;
    A256KW.jweAlgorithm = JWEAlgorithm.A256KW;

    DIR.jweAlgorithm = JWEAlgorithm.DIR;

    ECDH_ES.jweAlgorithm = JWEAlgorithm.ECDH_ES;
    ECDH_ES_A128KW.jweAlgorithm = JWEAlgorithm.ECDH_ES_A128KW;
    ECDH_ES_A192KW.jweAlgorithm = JWEAlgorithm.ECDH_ES_A192KW;
    ECDH_ES_A256KW.jweAlgorithm = JWEAlgorithm.ECDH_ES_A256KW;
    ECDH_1PU.jweAlgorithm = JWEAlgorithm.ECDH_1PU;
    ECDH_1PU_A128KW.jweAlgorithm = JWEAlgorithm.ECDH_1PU_A128KW;
    ECDH_1PU_A192KW.jweAlgorithm = JWEAlgorithm.ECDH_1PU_A192KW;
    ECDH_1PU_A256KW.jweAlgorithm = JWEAlgorithm.ECDH_1PU_A256KW;

    A128GCMKW.jweAlgorithm = JWEAlgorithm.A128GCMKW;
    A192GCMKW.jweAlgorithm = JWEAlgorithm.A192GCMKW;
    A256GCMKW.jweAlgorithm = JWEAlgorithm.A256GCMKW;

    PBES2_HS256_A128KW.jweAlgorithm = JWEAlgorithm.PBES2_HS256_A128KW;
    PBES2_HS384_A192KW.jweAlgorithm = JWEAlgorithm.PBES2_HS384_A192KW;
    PBES2_HS512_A256KW.jweAlgorithm = JWEAlgorithm.PBES2_HS512_A256KW;
  }

  @Getter private JWEAlgorithm jweAlgorithm;
}
