package tokenlib.util;

import com.nimbusds.jose.jwk.JWK;
import org.axonframework.queryhandling.QueryGateway;
import tokenlib.util.jwk.AppConstants;
import tokenlib.util.jwk.Jwk;
import tokenlib.util.jwk.RSAParser;
import tokenlib.util.tokenenum.TokenFields;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.project.core.events.user.JwkTokenInfoEvent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwkManager implements JwkProvider{
  private static final String JWK_FILE_PATH = "/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json";
  private JwkSetLoader jwkSetLoader;
  private QueryGateway queryGateway;

  public JwkManager(JwkSetLoader jwkSetLoader, QueryGateway queryGateway) {
    this.jwkSetLoader = jwkSetLoader;
    this.queryGateway = queryGateway;
  }

  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {
    //TODO optimize this
    String kid = extractKidFromTokenHeader(jwtToken);
    var jwks = loadJwkSetFromSource(queryGateway).stream().map(x->{
      try {
        return JWK.parse(x);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }).collect(Collectors.toList());
    var jwk =jwks.stream().filter(x->x.getKeyID().equals(kid)).findFirst().orElseThrow(()->new IllegalArgumentException("Invalid kid"));
    RSAKey rsaKey = RSAParser.parseRSAKeyFromJson(jwk.toJSONString());

    return Jwts.parser()
      .setSigningKey(rsaKey.toRSAPublicKey())
      .parseClaimsJws(jwtToken)
      .getBody().setId(kid);
  }
  @Override
  public Map<String, String> generateTokenPair(JwtBuilder access, JwtBuilder refresh, String tokenId) throws NoSuchAlgorithmException {
    var keyPair = obtainKeyContainer();
    var rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
      .keyID(tokenId)
      .build();
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateSignedCompactToken(refresh,tokenId,keyPair));
    tokenMap.put("access", generateSignedCompactToken(access,tokenId, keyPair));
    tokenMap.put(TokenFields.PUBLIC_KEY.getValue(), rsaKey.toJSONString());
    return tokenMap;
  }

  @Override
  public KeyPair obtainKeyContainer() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);

    return keyPairGenerator.generateKeyPair();
  }

  @Override
  public List<String> loadJwkSetFromSource(QueryGateway queryGateway){
    return jwkSetLoader.loadJwkSetFromSource(queryGateway);
  }

  @Override
  public String extractKidFromTokenHeader(String jwtToken) throws ParseException {
    JWSObject jwsObject = JWSObject.parse(jwtToken);
    return jwsObject.getHeader().getKeyID();
  }

  @Override
  public String generateSignedCompactToken(JwtBuilder jwt, String kid, KeyPair KeyPair) {
    return  jwt
        .setIssuer(AppConstants.ISSUER.toString())
        .setIssuedAt(new Date())
        .setHeader(Map.of(TokenFields.KID.getValue(),kid))
        .signWith(KeyPair.getPrivate()).compact();
  }
  @Override
  public Class<JwkTokenInfoEvent> getEventClass() {
    return JwkTokenInfoEvent.class;
  }
}
