package com.lendistry.keysdk.enc;

import com.nimbusds.jose.EncryptionMethod;
import lombok.Getter;

public enum ContentAlg {
  A128CBC_HS256,
  A192CBC_HS384,
  A256CBC_HS512,

  A128GCM,
  A192GCM,
  A256GCM,

  XC20P;

  static { // use static block, init via constructor produce illegal forward reference
    A128CBC_HS256.encryptionMethod = EncryptionMethod.A128CBC_HS256;
    A192CBC_HS384.encryptionMethod = EncryptionMethod.A192CBC_HS384;
    A256CBC_HS512.encryptionMethod = EncryptionMethod.A256CBC_HS512;

    A128GCM.encryptionMethod = EncryptionMethod.A128GCM;
    A192GCM.encryptionMethod = EncryptionMethod.A192GCM;
    A256GCM.encryptionMethod = EncryptionMethod.A256GCM;

    XC20P.encryptionMethod = EncryptionMethod.XC20P;
  }

  @Getter private EncryptionMethod encryptionMethod;
}
