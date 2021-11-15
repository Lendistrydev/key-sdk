package com.lendistry.keysdk;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.JSONValue;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.Map;

import static com.lendistry.keysdk.KeySdk.dateToIsoInUtc;

public class Message {

  private String canonicalMessage;
  private Date expiresAt;
  private String stringToSign;
  private String stringToEncrypt;

  public Message(String canonicalMessage, Date expiresAt) {
    this.canonicalMessage = canonicalMessage;
    this.expiresAt = expiresAt;
    this.stringToSign = DigestUtils.sha256Hex(canonicalMessage + dateToIsoInUtc(expiresAt));
    this.stringToEncrypt = prepareStringToEncrypt();
  }

  private String prepareStringToEncrypt() {
    JSONObject json = new JSONObject();
    json.put("canonicalMessage", canonicalMessage);
    json.put("exp", dateToIsoInUtc(expiresAt));
    return json.toString();
  }

  public static String getCanonicalMessageFromDecryptedString(String decryptedString) {
    Map<String, String> map = (Map<String, String>) JSONValue.parse(decryptedString);
    if (map == null || !map.containsKey("canonicalMessage")) {
      return decryptedString;
    }
    return map.get("canonicalMessage");
  }

  public boolean isExpired() {
    if (expiresAt == null) {
      return false;
    }
    return isExpired(expiresAt);
  }

  public static boolean isExpired(Date date) {
    return date.getTime() < System.currentTimeMillis();
  }

  public static String hashCanonicalMessage(String canonicalMessage) {
    return DigestUtils.sha256Hex(canonicalMessage);
  }

  public void checkExpired() throws KeySdkException {
    checkExpired(expiresAt);
  }

  public static void checkExpired(Date expiresAt) throws KeySdkException {
    if (isExpired(expiresAt)) {
      throw new KeySdkException(String.format("Message is expired. Expired at %s.", expiresAt));
    }
  }

  public String getCanonicalMessage() {
    return this.canonicalMessage;
  }

  public Date getExpiresAt() {
    return this.expiresAt;
  }

  public String getStringToSign() {
    return this.stringToSign;
  }

  public String getStringToEncrypt() {
    return this.stringToEncrypt;
  }
}
