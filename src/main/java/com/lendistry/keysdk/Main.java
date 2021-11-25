package com.lendistry.keysdk;

import com.nimbusds.jose.util.IOUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PublicKey;
import picocli.CommandLine;

/** Create jar with dependencies: mvn clean compile assembly:single */
public class Main {

  @CommandLine.Option(
      names = {"-m", "--mode"},
      usageHelp = true,
      description = "Possible values: encrypt or decrypt")
  String mode;

  @CommandLine.Option(
      names = {"-f", "--file"},
      usageHelp = true,
      description = "File to process")
  String filePath;

  @CommandLine.Option(
      names = {"-private", "--private-key"},
      usageHelp = true,
      description = "Path to private key file (PEM)")
  String privateKeyPath;

  @CommandLine.Option(
      names = {"-public", "--public-key"},
      usageHelp = true,
      description = "Path to public key file (PEM)")
  String publicKeyPath;

  public static void main(String[] args) throws IOException, KeySdkException {
    Main main = new Main();

    CommandLine commandLine = new CommandLine(main);
    commandLine.parseArgs(args);

    if (args.length < 2 || !main.isValid()) {
      commandLine.usage(System.out);
      System.out.println("Example:");
      System.out.println(
          "~$ java -jar key-sdk-with-dependencies.jar --mode decrypt -f encrypted-reply.txt -private private-key.pem");
      System.exit(0);
      return;
    }

    if (main.isDecrypt()) {
      main.decrypt();
      return;
    }

    if (main.isEncrypt()) {
      main.encrypt();
    }
  }

  private void encrypt() throws IOException, KeySdkException {
    PublicKey publicKey = KeyPairParser.parsePublicKeyPem(publicKeyPath);
    KeySdk keySdk = new KeySdk();

    try (InputStream inputStream = new FileInputStream(filePath)) {
      String input = IOUtils.readInputStreamToString(inputStream);
      String encrypted = keySdk.encrypt(input, publicKey);
      System.out.println("Encrypted message:");
      System.out.println(encrypted);
    }
  }

  private void decrypt() throws IOException, KeySdkException {
    KeyPair keyPair = KeyPairParser.parsePem(privateKeyPath);

    try (InputStream inputStream = new FileInputStream(filePath)) {
      String encryptedMessage = IOUtils.readInputStreamToString(inputStream);
      KeySdk keySdk = new KeySdk();
      String decryptedMessage = keySdk.decrypt(encryptedMessage, keyPair.getPrivate());
      System.out.println("Decrypted message:");
      System.out.println(decryptedMessage);
    }
  }

  private boolean isEncrypt() {
    return "encrypt".equalsIgnoreCase(mode);
  }

  private boolean isDecrypt() {
    return "decrypt".equalsIgnoreCase(mode);
  }

  private boolean isValid() {
    if (!isEncrypt() && !isDecrypt()) return false;
    if (filePath == null || filePath.isBlank()) return false;
    return true;
  }
}
