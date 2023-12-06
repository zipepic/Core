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

/**
 * The {@code JwtManager} class provides functionality for managing JSON Web Tokens (JWTs) and handling JWT operations.
 *
 * <p><strong>Properties:</strong></p>
 * <ul>
 *   <li>{@code secretKey}: The secret key for signing JWTs.</li>
 *   <li>{@code eventClassProvider}: The provider for obtaining the event class.</li>
 * </ul>
 *
 * <p><strong>Constructors:</strong></p>
 * <ul>
 *   <li>{@code JwtManager(SecretKeySpec secretKey, EventClassProvider eventClassProvider)}:
 *       Constructor initializing the secret key and event class provider.</li>
 * </ul>
 *
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@code Claims extractClaimsFromToken(String jwtToken)}:
 *       Extracts and returns claims from a given JWT token.</li>
 *
 *   <li>{@code String generateSignedCompactToken(JwtBuilder jwt, String tokenId)}:
 *       Generates a signed compact token using the provided JWT builder and token ID.</li>
 *
 *   <li>{@code Map<String, String> generateTokenPair(JwtBuilder access, JwtBuilder refresh, String tokenId)}:
 *       Generates a new pair of access and refresh tokens.</li>
 *
 *   <li>{@code Class<?> getEventClass()}:
 *       Returns the event class from the event class provider.</li>
 * </ul>
 *
 * <p><strong>Dependencies:</strong></p>
 * <ul>
 *   <li>{@code EventClassProvider}:
 *       A provider for obtaining the event class.</li>
 *   <li>{@code AppConstants}:
 *       Constants for JWT issuer.</li>
 *   <li>{@code JOSEException}:
 *       Exception for JOSE-related errors.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <p>The {@code JwtManager} class is used to manage JWTs and handle JWT operations,
 * such as extracting claims and generating token pairs.</p>
 *
 * <p><strong>Important Notes:</strong></p>
 * <ul>
 *   <li>This class relies on the Nimbus JOSE library for JSON Web Token operations.</li>
 *   <li>Key-related operations are delegated to the {@code SecretKeySpec} class.</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * SecretKeySpec secretKey = // create a SecretKeySpec instance
 * EventClassProvider eventClassProvider = // create an EventClassProvider instance
 * JwtManager jwtManager = new JwtManager(secretKey, eventClassProvider);
 * Claims extractedClaims = jwtManager.extractClaimsFromToken(jwtToken);
 * // Process the claims or perform other token-related operations
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> Konstantin Kokoshnikov</p>
 *
 * <p><strong>See Also:</strong></p>
 * <ul>
 *   <li>{@link tokenlib.util.jwk.AppConstants}</li>
 *   <li>{@link com.project.core.events.user.JwtTokenInfoEvent}</li>
 *   <li>{@link javax.crypto.spec.SecretKeySpec}</li>
 *   <li>{@link tokenlib.util.lamdas.EventClassProvider}</li>
 * </ul>
 */
public final class JwtManager implements JwtProvider {
  // The secret key for signing JWTs
  private final SecretKeySpec secretKey;
  // The provider for obtaining the event class
  private EventClassProvider eventClassProvider;

  /**
   * Constructor initializing the secret key and event class provider.
   *
   * @param secretKey The secret key for signing JWTs.
   * @param eventClassProvider The provider for obtaining the event class.
   */
  public JwtManager(SecretKeySpec secretKey, EventClassProvider eventClassProvider) {
    this.secretKey = secretKey;
    this.eventClassProvider = eventClassProvider;
  }

  /**
   * Extracts and returns claims from a given JWT token.
   *
   * @param jwtToken The JWT token from which to extract claims.
   * @return The extracted claims.
   * @throws ParseException If there is an issue parsing the token.
   * @throws IOException If there is an issue reading the token.
   * @throws JOSEException If there is an issue with the JOSE library.
   */
  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {
    return Jwts.parser()
      .setSigningKey(secretKey)
      .parseClaimsJws(jwtToken)
      .getBody();
  }

  /**
   * Generates a signed compact token using the provided JWT builder and token ID.
   *
   * @param jwt The JWT builder for the token.
   * @param tokenId The identifier for the token.
   * @return The generated signed compact token.
   */
  @Override
  public String generateSignedCompactToken(JwtBuilder jwt, String tokenId) {
    return jwt
      .setIssuer("http://localhost:8080")
      .setIssuedAt(new Date())
      .signWith(secretKey).compact();
  }

  /**
   * Generates a new pair of access and refresh tokens.
   *
   * @param access The JWT builder for the access token.
   * @param refresh The JWT builder for the refresh token.
   * @param tokenId The identifier for the tokens.
   * @return A map containing the generated refresh and access tokens.
   * @throws NoSuchAlgorithmException If there is an issue with the cryptographic algorithms.
   */
  @Override
  public Map<String, String> generateTokenPair(JwtBuilder access, JwtBuilder refresh, String tokenId)
    throws NoSuchAlgorithmException {
    access.setId(tokenId);
    refresh.setId(tokenId);
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateSignedCompactToken(refresh, tokenId));
    tokenMap.put("access", generateSignedCompactToken(access, tokenId));
    return tokenMap;
  }

  /**
   * Returns the event class from the event class provider.
   *
   * @return The event class.
   */
  @Override
  public Class<?> getEventClass() {
    return eventClassProvider.getEventClass();
  }
}
