package com.lendistry.keysdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EncryptionTest {

  private static KeyPair keyPair;
  private static KeySdk keySdk;

  @BeforeAll
  static void setUp() throws NoSuchAlgorithmException {
    keySdk = new KeySdk();
    keyPair = keySdk.getKeyGenerator().generateRSA2048();
  }

  @Test
  public void demo() throws ParseException, JOSEException, KeySdkException {
    String key =
        "{\n"
            + "      \"p\": \"9ksPNrgSA3Dg7VNAMmeLL2MdHS1IXLpAt1XLi14ZpFz-E-q0oJurihtfrrW7ButRTpBeP_4eQVz45sKN8vteuL75wwvMGYiDYer_dzZ-Giyku3y9djndttcYn9i79dmex42BYPI77VxMkPpekZpr_now0fTZMsXGbvwSYzS5XTpi5POZ2DaeVR2LGNhgKC4YYUErxqJuwdWYLSUw7Dy3A_Gewb73QwDJuxhRctXtB4Lk1HtwIKH99tdE8X3hXxcQSTx0ymS1xzZNZkNx-P0zOVXpIZvwIoM3PgUqWtxafiKYaP5dYaKXFe_AaP1zkfjnG3xuV2t0wUcQW3FMHqmCBQ\",\n"
            + "      \"kty\": \"RSA\",\n"
            + "      \"q\": \"w1dma3ALVFBrMd9NcDxFgW-ejz65tXv9mCm20CKtjeQZGbf2z-cNsPKWkg92Thex5Kk4IeWm-lztRV7nGZF1FueG3W1rzgxl3GpU5ABt92LwerlQHyfuY1gj1yCb8VnbShFfBzvM-SFb3frfsItBJn2YCnJLeX6nP4RbWT7yE5ohk4PfieUMaaQ2hw_YHvo6r1n1G1Y9Jgs76TVgP7XtxfW0nyyzKcfb9LzMpUNmmCPU-zVEI1zLbAQ3y3SqTcdnelWTE6-z3KJs-p_EpdUvt7BLq_dAy-lacacuqbLqjJaXv82v89WS4D9qgQhvnDx2uO7RZ3drLJ2tEaRhEIH2BQ\",\n"
            + "      \"d\": \"H8Rz2P-yB105G_BFjSgGRL32m8hwk1cWZ8i1t4txJkto5lXvaw3UKQdtpihF6rzShkSdhKttnZP5YA1ujeurz30pG535dx39QGAxEKdGsHSdoGBCkyVjyUZYr1KRuwYonTbfdd7mT27_FwNHxA90O2fabXKo_ml5shD896uQsMJYkkpJdZpeZpFqMVDLSKIIFpbJhNIayNPcFwaD2ssOxxjINpUtgZiYx0nKxdarrPL3RSePMZSzBs4DLU_43FAZSNePETqOsMknF9BquqJ9cxbk0h_JO9ayAChOXIgvuRemoKVIyIPbRltsWazI8fi6n8iIS9NWS9hGasy5Hk4bXGkuZyTfUcT9aWOLQ45gq0gtP68ZRpcoC3sWpsSg2qgSjORBPSkaXZldNLDxG4Dl1J435NDZJFh8GBFhO3S0IkknkIwPCQTd026XPad7aUiOEsx5oIGNdPqYHq9cuEXLJ0h1reKJi0sMxva9V8W55kkvdGSbL3a38Xaj0iN4Ve6I6u63wdG31IpHMqGRuikNX-LK0EWv86jne8ehLpPDjjzQzMHKdnE6iW_ASIujZIi3WNdSsiisGHzm5nzGYkBnWKMFEWgezKo7bIrSjFudhF_XOYv1ZYHVOcaF4gaR0raILz9dJJ4MFapdT7vjPwIGWCejtv11ZouvNt2rHRRU9GE\",\n"
            + "      \"e\": \"AQAB\",\n"
            + "      \"use\": \"enc\",\n"
            + "      \"kid\": \"ec79ea17-bcad-4313-bf96-5aa788d9b506\",\n"
            + "      \"qi\": \"36CxMA3JiDye6RcrukZhqrT9u1oqE0i-B2u_U8GE0V78Go_UCmg0Eb-OfbGZfZfvnZ41_rzsBwJX20BZvGwuuDSEFiOt7rVXSLyaIWEYY-hgx7zRPN6Wi1O3ytP7vtpw0aZK-xHTxNJJUTBWwh4iWzydo50GY4_AAVGsPfopMLJYEPtmA6g1p1bqu61xd722n_Q7mSNKKHQTGkcUOYmzJVUtTRytJu4cl_fJMGRTWktem18Xox5WyARMcBqmHm5c0TSDdQ5AdwxV44fKA3lly8uWyAmm8EZX7SiOtz9SKeCjcXfksbTlS5DXZ7vYTNrR9yWZ2le3klbeFHarCNUZww\",\n"
            + "      \"dp\": \"HZhJXLbjmzw-GuZtCWF9zRTo06-0SUgsLJuHmNiW1jRCZ5xFViM-iprddX3q2lr-ayVX3fKoKgyE8B2itvbTm_l-w5wwS0UnHGYDioymvHhu1wSXM5Sieq0ADX0aoOEkfjAXwtF4t_3mSWl8YZyfq3V9Vk1IqBwxyvZJM1iLiZNRfNd0vhFaFzo0k6i0bT-UuGL_Yp_0FSCYZ5xXc5ucFkI7ZGaRCaEXnH7Akr73-3YUDyBCCfnNLZMSer44jZeU4mf8VLGjUivJQyeJk3TBaftTxzEYutzvj2lJfJ-L1CpODBe8mtXRt9YWVcdW0syI4Lmj_Kcx4WOrqIFFt02BDQ\",\n"
            + "      \"dq\": \"VC9SgHLlLw0JZZDq-re-7K2CFBHgTwqEnBM-Ki0HxuLKvn6965OUHvItWf036yfjDryu6EzkzXlG-GKSDj7Vy200fjEuCLrnGzroIurceYU2Rm2ezg6TelT9_TScgENNcnpSDfgDukpaISyBQsCA72mpILdpN48R3RnZlHjAMxp15fErYW5JDy4qIcL-TTtiFzgZnhJogADPbQ4H0O1C79GYahEFlbgAX1UOjRqjGGY435pxJxxSvtaChThePO729Y3zuMQ8K5YEiW5ZfW6HBg8ATbhdQIPs6pY2UgQ9eSFdfNfUQK2GgwZRjBJqp1aXg9PIg2K_2ZaKg9Pn0QlxUQ\",\n"
            + "      \"n\": \"u-9Coh8JLPPbDXs0Zc7PoLRTmh03x5tJIvdkWDXQQEFs-bvSur-ZvW_c0Zh5WEr-i9XAhsry2eg4SzcDW-LUlQdFHQy8IaJHajIKdvjp2wJ_ZiN4kX4af4SNMADDia4YBMt3Th3EtdnKP74oYQoJN5oWB5rW7-1UnTPJqcUgf6GPzE1c2U_RbyY9WQnECthod2aPRBHsYQahXdYCJ__Z92SxRsqe3kNwuO4pT2OiWqXVky0pqyRLgiB9mqvLmH6UwBmx6QVn1gfarazGQIxRqDG6a2yM9JUkEc0IqX45_JC_WSbm8gLEqU_JxAkehWxNSbKiI9tCfQg7mXgeNDVIBqMBuMVipB7vPxxNA-XwKLV1C-pgNyOavEewuk96SoW0hLueivTSMCSE1c_DZodfqvi5omqIDkt_H3hfr4NR-AeGMHoCvbPiMa0h_L6DzMdGLJZma2L1JtQwpvwROsBpQL_54yOIhnEBy3SOsqjKBuDKb25ZR5CpX0Zy83lO9YBs1rrOGTz8tQILN6LfkiSrGJpxgsiOil8ZpVc4L5TpTsk7_Jho96XK-TV82TpYR3iMXxnzkbny46kvxjd9o3MKlWXhWad0hHhblpyUcH0gTzESn-q2xDWdyCvOYGxQoMrTNB-nraT66EAb5alXQFauIzmt0Sx6POXwQsjfY1DFWBk\"\n"
            + "    }";

    String message = "merchant lendistry";
    JWK keyPair = JWK.parse(key);
    String encryptedMessage = keySdk.encrypt(message, keyPair.toRSAKey().toPublicKey());

    System.out.println(encryptedMessage);

    String decryptedMessage = keySdk.decrypt(encryptedMessage, keyPair.toRSAKey().toPrivateKey());
    assertEquals(message, decryptedMessage);
  }

  @Test
  public void encryptPlainText_afterDecryption_textMustMatch() throws KeySdkException {
    String message = "merchant lendistry";

    String encryptedMessage = keySdk.encrypt(message, keyPair.getPublic());
    System.out.println(encryptedMessage);

    String decryptedMessage = keySdk.decrypt(encryptedMessage, keyPair.getPrivate());
    assertEquals(message, decryptedMessage);
  }

  @Test
  public void encryptJson_afterDecryption_textMustMatch() throws KeySdkException, ParseException {
    String exp = KeySdk.dateToIsoInUtc(new Date());

    JSONObject json = new JSONObject();
    json.put("message", "merchant lendistry");
    json.put("exp", exp); // UTC exceeds -> expires -> reject it
    json.put("int", 5);

    String encryptedMessage = keySdk.encrypt(json.toString(), keyPair.getPublic());
    String decryptedMessage = keySdk.decrypt(encryptedMessage, keyPair.getPrivate());

    JSONObject decryptedJson = new JSONObject(JSONObjectUtils.parse(decryptedMessage));
    assertEquals("merchant lendistry", decryptedJson.getAsString("message"));
    assertEquals(exp, decryptedJson.getAsString("exp"));
    assertEquals(5L, decryptedJson.getAsNumber("int"));
  }
}
