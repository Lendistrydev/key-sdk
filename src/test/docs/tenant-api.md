## Introduction

Tenant API is using token and encryption for communication. Thus, to interact with API following is needed:

- authorization token
- encrypt request with Lendisty's public key (see [Onboarding](onboard.md))
- decrypt response with merchant's private key

## Obtain token to call API

In order to call lendistry API token has to be obtained. See [auth flow doc](https://dash.readme.com/project/lendistry-sbl/v1.0/docs/auth-flow) for details.
Token should be provided in `Authorization` header with `Bearer` prefix.

Example
```
POST /tenant/prequal HTTP/1.1
Kid: 1234-1234-1234-1234
Lendistry-Kid: 0509f70a-45fd-11ec-81d3-0242ac130003
Authorization: Bearer <token>
```

## How it works

When API call is made to lendistry API then:
- request payload must be encrypted with lendistry's public key
- `Lendistry-Kid` header value should specify lendistry's kid (see [Onboarding](onboard.md))
- `Kid` header value must specify `kid` value of merchant's key (`kid` value provided by Lendistry team via email). It can be used to decrypt response.

```
POST /tenant/prequal HTTP/1.1
Kid: 1234-1234-1234-1234
Lendistry-Kid: 0509f70a-45fd-11ec-81d3-0242ac130003
Authorization: Bearer <token>

eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.p1Y66...
```

Response is encrypted with merchant's public key (and can be decrypted with merchant's private key)
```
HTTP/1.1 200 OK
Kid: 3456-3456-3456-3456

eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.p1Y66
```

