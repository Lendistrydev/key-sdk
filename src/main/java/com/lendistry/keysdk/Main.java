package com.lendistry.keysdk;

import com.nimbusds.jose.util.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;

public class Main {

  public static void main(String[] args) throws IOException, KeySdkException {
    if (args.length < 2) {
      System.out.println(
          "To decrypt provide path to file as first argument and path to private key PEM as second argument");
      System.out.println("Example:");
      System.out.println(
          "~$ java -jar key-sdk-with-dependencies.jar encrypted-reply.txt private-key.pem");
      System.exit(0);
    }

    String encryptedFile = args[0];
    String privateKeyFile = args[1];

    KeyPair keyPair = KeyPairParser.parsePem(privateKeyFile);

    try (InputStream inputStream = new FileInputStream(encryptedFile)) {
      String encryptedMessage = IOUtils.readInputStreamToString(inputStream);
      KeySdk keySdk = new KeySdk();
      String decryptedMessage = keySdk.decrypt(encryptedMessage, keyPair.getPrivate());
      System.out.println("Decrypted message:");
      System.out.println(decryptedMessage);
    }
  }
}
