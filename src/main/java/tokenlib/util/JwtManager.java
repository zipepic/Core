package tokenlib.util;

import tokenlib.util.jwk.AppConstants;
import com.nimbusds.jose.JOSEException;
import com.project.core.events.user.JwtTokenInfoEvent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import tokenlib.util.lamdas.EventClassProvider;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtManager implements JwtProvider{
  private final SecretKeySpec secretKey;
  private EventClassProvider eventClassProvider;
  public JwtManager(SecretKeySpec secretKey,EventClassProvider eventClassProvider) {
    this.secretKey = secretKey;
    this.eventClassProvider = eventClassProvider;
  }
  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {
    return Jwts.parser()
      .setSigningKey(secretKey)
      .parseClaimsJws(jwtToken)
      .getBody();
  }
  @Override
  public String generateSignedCompactToken(JwtBuilder jwt, String tokenId) {
    return jwt
      .setIssuer(AppConstants.ISSUER.toString())
      .setIssuedAt(new Date())
      .signWith(secretKey).compact();
  }

  @Override
  public Map<String, String> generateTokenPair(JwtBuilder access, JwtBuilder refresh, String tokenId) throws NoSuchAlgorithmException {
    access.setId(tokenId);
    refresh.setId(tokenId);
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateSignedCompactToken(refresh,tokenId));
    tokenMap.put("access", generateSignedCompactToken(access,tokenId));
    return tokenMap;
  }
  @Override
  public Class<?> getEventClass() {
    return eventClassProvider.getEventClass();
  }
}
