package com.lendistry.keysdk;

public enum KeyAlgorithm {
  DIFFIE_HELLMAN("DiffieHellman"),
  DSA("DSA"),
  RSA("RSA");

  private final String value;

  KeyAlgorithm(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
