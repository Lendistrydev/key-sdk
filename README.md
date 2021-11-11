# Lendistry Key SDK

## Installation

**Adding key-sdk to your project using Maven.** 

Include within the <dependencies> section of your project's pom.xml file.

```xml
<dependency>
    <groupId>com.lendistry</groupId>
    <artifactId>key-sdk</artifactId>
    <version>1.0.1</version>
</dependency>
```

You can find releases here https://github.com/Lendistrydev/key-sdk/releases/

## Overview

Key SDK provides following functionality :
- message signing and verification of signature
- encryption and decryption

### Key Management

Services Public Key can be found here [lendistry-public-key.pem](https://github.com/Lendistrydev/key-sdk/blob/main/src/main/resources/lendistry-public-key.pem)

To get key pair merchant can :

**Generate merchant's key pair locally and send public key to lendistry via email**

Generate RSA keys in command line (write keys in `private-key.pem` or use wh)
```
~$ openssl genrsa -out private-key.pem 4096
```

If you have keys in DER format, please convert it with `openssl` to PEM and then load keys as shown above

```
~$ openssl rsa -inform der -in private.der -outform pem -out private.pem
```

Extract public key from `private-key.pem`

```
~$ openssl rsa -in private-key.pem -pubout -out public-key.pem
```

Send `public-key.pem` to lendistry via email. (Don't send private key, e.g.`private-key.pem`.)

In response lendistry will send `kid` value which has to be sent in all further calls to lendistry API via `kid` header.

Example
```
Kid: 1234-1234-1234-1234
POST /tenant/prequal
```


Load key pair from PEM file
```
String pathToPemFile = "<path to private-key.pem file here>";
KeyPair keyPair = KeyPairParser.parsePem(pathToPemFile);
```

Load public key from PEM file
```
String pathToPemFile = "<path to private-key.pem file here>";
PublicKey publicKey = KeyPairParser.parsePublicKeyPem(pathToPemFile);
```

### Encryption and decryption

**Encryption and decryption**
```java 
String message = "some message";

// Merchant's Key Pair (created outside) 
PublicKey lendistryPublicKey = KeyPairParser.parsePublicKeyPem("lendistry-public-key.pem");

// encrypt at merchant
KeySdk keySdk = new KeySdk();
String encryptedMessage = keySdk.encrypt(message, lendistryPublicKey);

// merchant encrypts payload of request before sending it to lendistry API
// From API merchant gets encrypted by lendistry public key response. It can be decrypted as following 
String encryptedResponse = ... ;// call do lendistry API

String decryptedMessage = keySdk.decrypt(encryptedResponse, <merchant's private key>).getMessage();
```

When API call is made to lendistry API then: 
- request payload must be encrypted with merchant's public key
- `kid` value of the public key must be sent in `Kid` header.
E.g.
```
Kid: 1234-1234-1234-1234
POST /tenant/prequal

eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.p1Y66
```

Response is encrypted with merchant's public key (and can be decrypted with merchant's private key)
```
HTTP/1.1 200 OK
Kid: 3456-3456-3456-3456

eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.p1Y66
```


### Signing and Signature Verification

**Create signature and verification**
```java 
KeyPair merchantKeyPair = ...; // obtained outside

// sign at merchant
KeySdk keySdk = new KeySdk();
String signature = keySdk.sign(message, merchantKeyPair.getPrivate());

// send signature to lendistry API key, so lendistry can verify it
// As reply lendistry API will send response back with signature which can be verified with lendistry public key. 

boolean ok = keySdk.verify(message, signature, <lendistry public key>);
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
boolean ok = keySdk.verify(message, expiresAt, signature, keyPair.getPublic());
```
