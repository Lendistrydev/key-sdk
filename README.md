# Lendistry Key SDK

## Installation

**Adding key-sdk to your project using Maven.** 

Include within the <dependencies> section of your project's pom.xml file.

```xml
<dependency>
    <groupId>com.lendistry</groupId>
    <artifactId>key-sdk</artifactId>
    <version>0.0.5-SNAPSHOT</version>
</dependency>
```

## Overview

Key SDK provides following functionality :

- generate key pair and service key pair
- register public key and get service key pair
- signing and verification of signature
- encryption and decryption

### Key pairs

There are 4 keys pairs involved in flows:

- private key 1 - merchant's private key (not saved on service side)
- public key 1 - merchant's public key (shared with service)
- private key 2 - service's private key (not shared with merchant, exists only on service)
- public key 2 - service's public key (shared with merchant)


To get key pair merchant can :

1. **Generate key pair on lendistry service**
   
   Note: Metchant's private key is not saved on lendistry service.
   ```java 
   KeyHttpClient httpClient = new KeyHttpClient(<url>);
   KeyPairDTO keyPairDTO = httpClient.generateKeyPair(2048);
   
   KeyPair merchantPair = keyPairDTO.toKeyPair();
   String merchantKid = keyPairDTO.getKid(); 
   
   PublicKey servicePublicKey = keyPairDTO.toServicePublicKey();
   String serviceKid = keyPairDTO.getServiceKid();
   ```
   
2. **Generate merchant's key pair locally and register public key on service**

   Generate RSA keys in command line (write keys in `private-key.pem` or use wh)
   ```
    ~$ openssl genrsa -out private-key.pem 4096
   ```

   If you have keys in DER format, please convert it with `openssl` to PEM and then load keys as shown above

   ```
   ~$ openssl rsa -inform der -in private.der -outform pem -out private.pem
   ```


   Load keys from PEM file
   ```
   String pathToPemFile = "<path to pem file here>";
   KeyPair keyPair = KeyPairParser.parsePem(pathToPemFile);
   ```
   
   Register public key on service
   ```java 
    KeyHttpClient httpClient = new KeyHttpClient(<url>);
    PublicKeyDTO response = httpClient.registerPublicKey(keyPair.getPublic());
   
    String merchantKid = response.getKid();
   
    PublicKey servicePublicKey = keyPairDTO.toServicePublicKey();
    String serviceKid = keyPairDTO.getServiceKid();   
   ```


### Signing and Signature Verification

**Create signature and verification**
```java 
KeyHttpClient httpClient = createHttpClient();

// Generate Key Pair at lendistry
KeyPairDTO keyPairDTO = httpClient.generateKeyPair(2048);
KeyPair keyPair = keyPairDTO.toKeyPair();

// sign at merchant
KeySdk keySdk = new KeySdk();
String signature = keySdk.sign(message, keyPair.getPrivate());

// verify at lendistry
boolean ok = httpClient.verify(message, signature, keyPairDTO.getKid());
```

**Create signature and verification with expiration date**

Notes:
- If expiration date is set it includes date as signing input together with message.
- If expiration date is "older" or less then current time in jvm that perform singing/verification then operation will be failed with `KeySdkException`

```java
String originalMessage = "some message";
Date dateInFuture = new Date(new Date().getTime() + 10000);
String stringToSign = new Message(originalMessage, dateInFuture).getStringToSign();

// sign at merchant
KeySdk keySdk = new KeySdk();
String signature = keySdk.sign(message, expiresAt, keyPair.getPrivate());

// verify at lendistry
KeyHttpClient httpClient = createHttpClient();
boolean ok = httpClient.verify(message, expiresAt, signature, keyPair.getPublic());
```

### Encryption and Decryption 

**Encryption and decryption**
```java 
String message = "some message";

KeyHttpClient httpClient = createHttpClient();

// Generate Key Pair at lendistry
KeyPairDTO keyPairDTO = httpClient.generateKeyPair(2048);
PublicKey servicePublicKey = keyPairDTO.toServicePublicKey();

// encrypt at merchant
KeySdk keySdk = new KeySdk();
String encryptedMessage = keySdk.encrypt(message, servicePublicKey);

// verify at lendistry
String decryptedMessage = httpClient.decrypt(encryptedMessage, keyPairDTO.getServiceKid()).getMessage();
```

[API Reference](https://lendistry-sbl.readme.io/reference/generateusingget)