## Onboard merchant

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