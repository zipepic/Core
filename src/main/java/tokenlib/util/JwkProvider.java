package tokenlib.util;

import io.jsonwebtoken.JwtBuilder;
import tokenlib.util.lamdas.JwkSetLoader;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

interface JwkProvider extends TokenProcessingStrategy, JwkSetLoader {
  KeyPair obtainKeyContainer() throws NoSuchAlgorithmException;
    String extractKidFromTokenHeader(String jwtToken) throws ParseException;
  String generateSignedCompactToken(JwtBuilder jwt, String kid, KeyPair KeyPair);
}
