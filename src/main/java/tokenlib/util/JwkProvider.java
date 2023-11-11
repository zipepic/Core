package tokenlib.util;

import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.JwtBuilder;
import org.axonframework.queryhandling.QueryGateway;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

interface JwkProvider extends TokenProcessingStrategy, JwkSetLoader{
  KeyPair obtainKeyContainer() throws NoSuchAlgorithmException;
    String extractKidFromTokenHeader(String jwtToken) throws ParseException;
  String generateSignedCompactToken(JwtBuilder jwt, String kid, KeyPair KeyPair);
}
