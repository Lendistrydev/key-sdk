package com.lendistry.keysdk;

import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.Test;

public class PureTest {

  @Test
  public void pureTest()
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
          IllegalBlockSizeException, BadPaddingException, SignatureException {

    // First generate a public/private key pair
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048, new SecureRandom());
    KeyPair pair = generator.generateKeyPair();

    // The private key can be used to sign (not encrypt!) a message. The public key holder can then
    // verify the message.

    String message = "To Be or not To Be";

    signing(pair, message);
    encryption(pair, message);
  }

  private void encryption(KeyPair pair, String message)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
          IllegalBlockSizeException, BadPaddingException {
    // The public key can be used to encrypt a message, the private key can be used to decrypt it.
    // Encrypt the message
    Cipher encryptCipher = Cipher.getInstance("RSA");
    encryptCipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());

    byte[] cipherText = encryptCipher.doFinal(message.getBytes());

    // Now decrypt it
    Cipher decriptCipher = Cipher.getInstance("RSA");
    decriptCipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());

    String decipheredMessage =
        new String(decriptCipher.doFinal(cipherText), StandardCharsets.UTF_8);

    System.out.println(decipheredMessage);
  }

  private void signing(KeyPair pair, String message)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    // Let's sign our message
    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(pair.getPrivate());
    privateSignature.update(message.getBytes(StandardCharsets.UTF_8));

    byte[] signature = privateSignature.sign();

    // Let's check the signature
    Signature publicSignature = Signature.getInstance("SHA256withRSA");
    publicSignature.initVerify(pair.getPublic());
    publicSignature.update(message.getBytes(StandardCharsets.UTF_8));
    boolean isCorrect = publicSignature.verify(signature);

    System.out.println("Signature correct: " + isCorrect);
  }
}
