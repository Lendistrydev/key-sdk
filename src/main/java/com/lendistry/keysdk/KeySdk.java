package com.lendistry.keysdk;

import com.lendistry.keysdk.enc.ContentAlg;
import com.lendistry.keysdk.enc.EncryptionAlg;
import com.lendistry.keysdk.enc.Encryptor;
import com.lendistry.keysdk.enc.PlainEncryptor;
import com.lendistry.keysdk.sign.PlainSigner;
import com.lendistry.keysdk.sign.Signer;
import com.lendistry.keysdk.sign.SigningAlg;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jca.JCAContext;
import com.nimbusds.jose.jca.JWEJCAContext;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class KeySdk {

  // https://cryptosense.com/blog/which-algorithms-are-fips-140-3-approved
  // RSAES using Optimal Asymmetric Encryption Padding (OAEP) (RFC 3447),
  // with the SHA-256 hash function and the MGF1 with SHA-256 mask generation function.
  // Under the hood:
  // RSAEncrypter ,line 192
  // RSA_OAEP_SHA2.encryptCEK(publicKey, cek, 256, getJCAContext().getKeyEncryptionProvider())
  // CEK is always AES: return new SecretKeySpec(cekMaterial, "AES");
  // Desc:
  // Used for encrypting CEK with public key and choosing encryptor
  private static final EncryptionAlg DEFAULT_ENCRYPTION_ALG = EncryptionAlg.RSA_OAEP_256;
  // AES in Galois/Counter Mode (GCM) (NIST.800-38D) using a 256 bit key (recommended).
  // Under the hood:
  // ContentCryptoProvider , line 196:
  // Container<byte[]> ivContainer = new
  // Container<>(AESGCM.generateIV(jcaProvider.getSecureRandom()));
  // authCipherText = AESGCM.encrypt(cek, ivContainer, plainText, aad,
  // jcaProvider.getContentEncryptionProvider());
  // Desc:
  // Used to generate Content Encryption Key (CEK) and then ecrypt content with it.
  private static final ContentAlg DEFAULT_BLOCK_ALG = ContentAlg.A256GCM;
  // RSASSA-PKCS-v1_5 using SHA-256 hash algorithm (recommended).
  private static final SigningAlg DEFAULT_SIGNING_ALG = SigningAlg.RS256;

  @Getter private final JCAContext jcaContext;
  @Getter private final JWEJCAContext jweContext;
  @Getter private final KeyGenerator keyGenerator;

  private final Signer signer;
  private final Encryptor encryptor;

  public KeySdk() {
    this(new JCAContext(), new JWEJCAContext());
  }

  protected KeySdk(JCAContext jcaContext, JWEJCAContext jweContext) {
    this.jcaContext = jcaContext;
    this.jweContext = jweContext;
    this.keyGenerator = new KeyGenerator(jcaContext);
    this.signer = new PlainSigner(jcaContext);
    this.encryptor = new PlainEncryptor(jweContext);
  }

  public static void installBouncyCastle() {
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) != null) {
      return;
    }

    Security.addProvider(new BouncyCastleProvider());
  }

  public static DateFormat createDateFormat() {
    DateFormat df =
        new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    return df;
  }

  public static String dateToIsoInUtc(Date date) {
    if (date == null) return "";
    return createDateFormat().format(date);
  }

  public static Date isoStringToDate(String dateIso) throws ParseException {
    return createDateFormat().parse(dateIso);
  }

  public static JWK convertToJwk(KeyPair keyPair) {
    return convertToJwk(keyPair, UUID.randomUUID().toString());
  }

  public static JWK convertToJwk(KeyPair keyPair, String kid) {
    return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
        .privateKey((RSAPrivateKey) keyPair.getPrivate())
        .keyID(kid)
        .build();
  }

  public static JWK convertToJwk(PublicKey publicKey) {
    return convertToJwk(publicKey, UUID.randomUUID().toString());
  }

  public static JWK convertToJwk(PublicKey publicKey, String kid) {
    return new RSAKey.Builder((RSAPublicKey) publicKey).keyID(kid).build();
  }

  public static KeyPair convertJWKToKeyPair(String jwkAsString)
      throws ParseException, JOSEException {
    JWK jwk = JWK.parse(jwkAsString);
    return new KeyPair(jwk.toRSAKey().toPublicKey(), jwk.toRSAKey().toPrivateKey());
  }

  public static PublicKey convertJWKToPublicKey(String publicJwkAsString)
      throws ParseException, JOSEException {
    return JWK.parse(publicJwkAsString).toRSAKey().toPublicKey();
  }

  public String sign(String message, Date expiredAt, PrivateKey privateKey) throws KeySdkException {
    Message m = new Message(message, expiredAt);
    m.checkExpired();
    return signer.sign(DEFAULT_SIGNING_ALG, m.getStringToSign(), privateKey);
  }

  public boolean verify(String message, Date expiredAt, String signature, PublicKey publicKey)
      throws KeySdkException {
    Message m = new Message(message, expiredAt);
    m.checkExpired();
    return signer.verify(DEFAULT_SIGNING_ALG, m.getStringToSign(), signature, publicKey);
  }

  public String sign(String message, PrivateKey privateKey) throws KeySdkException {
    return signer.sign(DEFAULT_SIGNING_ALG, Message.hashCanonicalMessage(message), privateKey);
  }

  public boolean verify(String message, String signature, PublicKey publicKey)
      throws KeySdkException {
    return signer.verify(
        DEFAULT_SIGNING_ALG, Message.hashCanonicalMessage(message), signature, publicKey);
  }

  public byte[] sign(byte[] message, PrivateKey privateKey) throws KeySdkException {
    return signer.sign(DEFAULT_SIGNING_ALG, message, privateKey);
  }

  public boolean verify(byte[] message, byte[] signature, PublicKey publicKey)
      throws KeySdkException {
    return signer.verify(DEFAULT_SIGNING_ALG, message, signature, publicKey);
  }

  public String encrypt(String message, PublicKey publicKey) throws KeySdkException {
    return encryptor.encrypt(DEFAULT_ENCRYPTION_ALG, DEFAULT_BLOCK_ALG, message, publicKey);
  }

  public String decrypt(String encryptedMessage, PrivateKey privateKey) throws KeySdkException {
    return encryptor.decrypt(encryptedMessage, privateKey);
  }
}
