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

/**
 * The {@code JwkManager} class provides functionality for managing JSON Web Keys (JWKs) and handling JWT operations.
 *
 * <p><strong>Properties:</strong></p>
 * <ul>
 *   <li>{@code jwkSetLoader}: The loader for obtaining JWK sets.</li>
 *   <li>{@code eventClassProvider}: The provider for obtaining the event class.</li>
 *   <li>{@code queryGateway}: The Axon Framework Query Gateway for querying events.</li>
 * </ul>
 *
 * <p><strong>Constructors:</strong></p>
 * <ul>
 *   <li>{@code JwkManager(JwkSetLoader jwkSetLoader, EventClassProvider eventClassProvider,
 *       QueryGateway queryGateway)}: Constructor initializing the JWK set loader,
 *       event class provider, and Query Gateway.</li>
 * </ul>
 *
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@code Claims extractClaimsFromToken(String jwtToken)}:
 *       Extracts and returns claims from a given JWT token.</li>
 *
 *   <li>{@code Map<String, String> generateTokenPair(JwtBuilder access, JwtBuilder refresh, String tokenId)}:
 *       Generates a new pair of access and refresh tokens, including the public key.</li>
 *
 *   <li>{@code KeyPair obtainKeyContainer()}:
 *       Obtains a KeyPair for cryptographic operations.</li>
 *
 *   <li>{@code List<SimpleJWK> loadJwkSetFromSource(QueryGateway queryGateway)}:
 *       Loads JWK set from a specified source using the Query Gateway.</li>
 *
 *   <li>{@code String extractKidFromTokenHeader(String jwtToken)}:
 *       Extracts the Key ID (kid) from the token header.</li>
 *
 *   <li>{@code String generateSignedCompactToken(JwtBuilder jwt, String kid, KeyPair keyPair)}:
 *       Generates a signed compact token using the provided JWT builder, kid, and KeyPair.</li>
 *
 *   <li>{@code Class<?> getEventClass()}:
 *       Returns the event class from the event class provider.</li>
 * </ul>
 *
 * <p><strong>Dependencies:</strong></p>
 * <ul>
 *   <li>{@code JwkSetLoader}:
 *       A loader for obtaining JWK sets.</li>
 *   <li>{@code EventClassProvider}:
 *       A provider for obtaining the event class.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <p>The {@code JwkManager} class is used to manage JWKs and handle JWT operations,
 * such as extracting claims, generating token pairs, and obtaining KeyPairs.</p>
 *
 * <p><strong>Important Notes:</strong></p>
 * <ul>
 *   <li>This class relies on the Nimbus JOSE library for JSON Web Token operations.</li>
 *   <li>Key-related operations are delegated to the {@code RSAParser} and {@code SimpleJWK} classes.</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * JwkSetLoader jwkSetLoader = // create a JwkSetLoader instance
 * EventClassProvider eventClassProvider = // create an EventClassProvider instance
 * QueryGateway queryGateway = // create a QueryGateway instance
 * JwkManager jwkManager = new JwkManager(jwkSetLoader, eventClassProvider, queryGateway);
 * Claims extractedClaims = jwkManager.extractClaimsFromToken(jwtToken);
 * // Process the claims or perform other token-related operations
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> Konstantin Kokoshnikov</p>
 *
 * <p><strong>See Also:</strong></p>
 * <ul>
 *   <li>{@link tokenlib.util.jwk.RSAParser}</li>
 *   <li>{@link tokenlib.util.jwk.SimpleJWK}</li>
 *   <li>{@link org.axonframework.queryhandling.QueryGateway}</li>
 *   <li>{@link com.project.core.events.user.JwkTokenInfoEvent}</li>
 *   <li>{@link tokenlib.util.tokenenum.TokenFields}</li>
 *   <li>{@link tokenlib.util.jwk.AppConstants}</li>
 * </ul>
 */
public final class JwkManager implements JwkProvider {
  // The loader for obtaining JWK sets
  private JwkSetLoader jwkSetLoader;
  // The provider for obtaining the event class
  private EventClassProvider eventClassProvider;
  // The Axon Framework Query Gateway for querying events
  private QueryGateway queryGateway;

  /**
   * Constructor initializing the JWK set loader, event class provider, and Query Gateway.
   *
   * @param jwkSetLoader The loader for obtaining JWK sets.
   * @param eventClassProvider The provider for obtaining the event class.
   * @param queryGateway The Axon Framework Query Gateway for querying events.
   */
  public JwkManager(JwkSetLoader jwkSetLoader, EventClassProvider eventClassProvider, QueryGateway queryGateway) {
    this.jwkSetLoader = jwkSetLoader;
    this.eventClassProvider = eventClassProvider;
    this.queryGateway = queryGateway;
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
    String kid = extractKidFromTokenHeader(jwtToken);
    var jwk = loadJwkSetFromSource(queryGateway).stream()
      .filter(x -> x.getKid().equals(kid))
      .findFirst().orElseThrow(() -> new IllegalArgumentException("Public key not found"));
    RSAKey rsaKey = RSAParser.parseRSAKeyFromSimpleJWK(jwk);
    return Jwts.parser()
      .setSigningKey(rsaKey.toRSAPublicKey())
      .parseClaimsJws(jwtToken)
      .getBody().setId(kid);
  }

  /**
   * Generates a new pair of access and refresh tokens, including the public key.
   *
   * @param access The JWT builder for the access token.
   * @param refresh The JWT builder for the refresh token.
   * @param tokenId The identifier for the tokens.
   * @return A map containing the generated refresh and access tokens, along with the public key.
   * @throws NoSuchAlgorithmException If there is an issue with the cryptographic algorithms.
   */
  @Override
  public Map<String, String> generateTokenPair(JwtBuilder access, JwtBuilder refresh, String tokenId)
    throws NoSuchAlgorithmException {
    var keyPair = obtainKeyContainer();
    var rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
      .keyID(tokenId)
      .build();
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateSignedCompactToken(refresh, tokenId, keyPair));
    tokenMap.put("access", generateSignedCompactToken(access, tokenId, keyPair));
    tokenMap.put(TokenFields.PUBLIC_KEY.getValue(), rsaKey.toJSONString());
    return tokenMap;
  }

  /**
   * Obtains a KeyPair for cryptographic operations.
   *
   * @return The obtained KeyPair.
   * @throws NoSuchAlgorithmException If there is an issue with the cryptographic algorithms.
   */
  @Override
  public KeyPair obtainKeyContainer() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.generateKeyPair();
  }

  /**
   * Loads JWK set from a specified source using the Query Gateway.
   *
   * @param queryGateway The Query Gateway for querying events.
   * @return The list of SimpleJWK objects representing the JWK set.
   */
  @Override
  public List<SimpleJWK> loadJwkSetFromSource(QueryGateway queryGateway) {
    return jwkSetLoader.loadJwkSetFromSource(queryGateway);
  }

  /**
   * Extracts the Key ID (kid) from the token header.
   *
   * @param jwtToken The JWT token from which to extract the kid.
   * @return The extracted Key ID (kid).
   * @throws ParseException If there is an issue parsing the token.
   */
  @Override
  public String extractKidFromTokenHeader(String jwtToken) throws ParseException {
    JWSObject jwsObject = JWSObject.parse(jwtToken);
    return jwsObject.getHeader().getKeyID();
  }

  /**
   * Generates a signed compact token using the provided JWT builder, kid, and KeyPair.
   *
   * @param jwt The JWT builder for the token.
   * @param kid The Key ID (kid) for the token.
   * @param keyPair The KeyPair for signing the token.
   * @return The generated signed compact token.
   */
  @Override
  public String generateSignedCompactToken(JwtBuilder jwt, String kid, KeyPair keyPair) {
    return jwt
      .setIssuer("http://localhost:8080")
      .setIssuedAt(new Date())
      .setHeader(Map.of(TokenFields.KID.getValue(), kid))
      .signWith(keyPair.getPrivate()).compact();
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
