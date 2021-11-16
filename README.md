# Lendistry Key SDK

## Installation

**Adding key-sdk to your project using Maven.** 

Include within the <dependencies> section of your project's pom.xml file.

```xml
<dependency>
    <groupId>com.lendistry</groupId>
    <artifactId>key-sdk</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

You can find releases here https://github.com/Lendistrydev/key-sdk/releases/

## Overview

Key SDK provides following functionality :
- message signing and verification of signature
- encryption and decryption of data

### Key Management

Lendistry Public Key can be found here [lendistry-public-key.pem](https://github.com/Lendistrydev/key-sdk/blob/main/src/main/resources/lendistry-public-key.pem)
Lendistry Public Key ID (kid) is `0509f70a-45fd-11ec-81d3-0242ac130003`.

To get key pair merchant can :

**Generate merchant's key pair locally and send public key to lendistry via email**

Generate RSA keys in command line (write keys in `private-key.pem`)
```
~$ openssl genrsa -out private-key.pem 4096
```

If you have keys in DER format, please convert it with `openssl` to PEM and then load keys as shown below

```
~$ openssl rsa -inform der -in private.der -outform pem -out private.pem
```

Extract public key from `private-key.pem`

```
~$ openssl rsa -in private-key.pem -pubout -out public-key.pem
```

Send `public-key.pem` to lendistry via email. (Don't send private key, e.g.`private-key.pem`.)

In response lendistry sends `kid` value which has to be sent in all further calls to lendistry API via `Kid` header (upper-cased first letter).

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

PublicKey lendistryPublicKey = KeyPairParser.parsePublicKeyPem("lendistry-public-key.pem");

// encrypt on merchant side
KeySdk keySdk = new KeySdk();
String encryptedMessage = keySdk.encrypt(message, lendistryPublicKey);

// merchant encrypts payload of request before sending it to lendistry API
// From API merchant gets encrypted by lendistry public key response. It can be decrypted as following 
String encryptedResponse = ... ;// call do lendistry API

String decryptedResponse = keySdk.decrypt(encryptedResponse, <merchant's private key>).getMessage();
```

### Signing and Signature Verification

**Create signature and verification**
```java 
KeyPair merchantKeyPair = ...; // obtained outside

// signed by merchant
KeySdk keySdk = new KeySdk();
String signature = keySdk.sign(message, merchantKeyPair.getPrivate());

// send signature to lendistry API, so lendistry can verify it
// As reply lendistry API will send response back with signature which can be verified with lendistry public key. 

boolean ok = keySdk.verify(response, signature, <lendistry public key>);
```

**Create signature and verification with expiration date**

Notes:
- If expiration date is set it includes date as signing input together with message.
- If expiration date is "older" or less than current time in jvm that perform singing/verification then operation will be failed with `KeySdkException`

```java
String originalMessage = "some message";
Date dateInFuture = new Date(new Date().getTime() + 10000);
String stringToSign = new Message(originalMessage, dateInFuture).getStringToSign();

// sign at merchant
KeySdk keySdk = new KeySdk();
String signature = keySdk.sign(message, expiresAt, merchantKeyPair.getPrivate());

// verify at lendistry
boolean ok = keySdk.verify(message, expiresAt, signature, <lendistry public key>);
```

## FAQ

**How to generate jar with dependencies**
Run `mvn clean compile assembly:single`