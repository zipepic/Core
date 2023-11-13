package tokenlib.util;

import org.axonframework.queryhandling.QueryGateway;
import tokenlib.util.jwk.AppConstants;
import tokenlib.util.jwk.RSAParser;
import tokenlib.util.jwk.SimpleJWK;
import tokenlib.util.lamdas.EventClassProvider;
import tokenlib.util.lamdas.JwkSetLoader;
import tokenlib.util.tokenenum.TokenFields;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
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

public class JwkManager implements JwkProvider{
  private JwkSetLoader jwkSetLoader;
  private EventClassProvider eventClassProvider;
  private QueryGateway queryGateway;

  public JwkManager(JwkSetLoader jwkSetLoader,EventClassProvider eventClassProvider, QueryGateway queryGateway) {
    this.jwkSetLoader = jwkSetLoader;
    this.eventClassProvider = eventClassProvider;
    this.queryGateway = queryGateway;
  }

  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {

    String kid = extractKidFromTokenHeader(jwtToken);

    var jwk = loadJwkSetFromSource(queryGateway).stream()
      .filter(x->x.getKid().equals(kid))
      .findFirst().orElseThrow(()->new IllegalArgumentException("Public key not found"));

    RSAKey rsaKey = RSAParser.parseRSAKeyFromSimpleJWK(jwk);

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
  public List<SimpleJWK> loadJwkSetFromSource(QueryGateway queryGateway){
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
  public Class<?> getEventClass() {
    return eventClassProvider.getEventClass();
  }
}
