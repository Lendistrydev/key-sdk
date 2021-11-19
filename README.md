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
- encryption and decryption of data
- message signing and verification of signature

### Key Management

* Lendistry Public Key can be found here [lendistry-public-key.pem](https://github.com/Lendistrydev/key-sdk/blob/main/src/main/resources/lendistry-public-key.pem)
* Lendistry Public Key ID (kid) is `0509f70a-45fd-11ec-81d3-0242ac130003`.

To get keypair a tenant can :

**Generate tenant's key pair locally and send public key to Lendistry via email**

Generate RSA keys in command line (write keys in `private-key.pem`)
```
openssl genrsa -out private-key.pem 4096
```

Extract public key from `private-key.pem`

```
openssl rsa -in private-key.pem -pubout -out public-key.pem
```

Send `public-key.pem` to Lendistry via email. (Don't send private key, e.g.`private-key.pem`.)

In response, Lendistry sends `kid` value assigned to your public key which has to be sent in all further calls to Lendistry API via `Kid` header (upper-cased first letter).

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

// encrypt on the tenant side
KeySdk keySdk = new KeySdk();
String encryptedMessage = keySdk.encrypt(message, lendistryPublicKey);

// tenant encrypts payload of request before sending it to lendistry API
// From API tenant gets encrypted by lendistry public key response. It can be decrypted as following 
String encryptedResponse = ... ;// call do lendistry API

String decryptedResponse = keySdk.decrypt(encryptedResponse, <tenant's private key>).getMessage();
```

### Signing and Signature Verification

**Create signature and verification**
```java 
KeyPair tenantKeyPair = ...; // obtained outside

// signed by tenant
KeySdk keySdk = new KeySdk();
String signature = keySdk.sign(message, tenantKeyPair.getPrivate());

// send signature to lendistry API, so lendistry can verify it
// As reply lendistry API will send response back with signature which can be verified with lendistry public key. 

boolean ok = keySdk.verify(response, signature, <lendistry public key>);
```

**Create signature and verification with expiration date**

Notes:
- If expiration date is set, it includes date as signing input along with the message.
- If expiration date is "older" or less than current time, then the operation fails with `KeySdkException`

```java
String originalMessage = "some message";
Date dateInFuture = new Date(new Date().getTime() + 10000);
String stringToSign = new Message(originalMessage, dateInFuture).getStringToSign();

// signed by tenant at tenant side
KeySdk keySdk = new KeySdk();
String signature = keySdk.sign(message, expiresAt, tenantKeyPair.getPrivate());

// verify at lendistry
boolean ok = keySdk.verify(message, expiresAt, signature, <lendistry public key>);
```

## FAQ

**How to generate jar with dependencies**
Run `mvn clean compile assembly:single`
