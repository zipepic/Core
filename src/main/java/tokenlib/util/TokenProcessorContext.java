package tokenlib.util;

import tokenlib.util.jwk.AppConstants;
import tokenlib.util.tokenenum.TokenExpiration;
import tokenlib.util.tokenenum.TokenFields;
import tokenlib.util.tokenenum.TokenTypes;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * The {@code TokenProcessorContext} class represents the context for processing and handling JWT operations
 * within the token library. It utilizes the Nimbus JOSE library and JSON Web Tokens (JWT).
 *
 * <p><strong>Properties:</strong></p>
 * <ul>
 *   <li>{@code tokenProcessingStrategy}: The strategy used for token processing.</li>
 * </ul>
 *
 * <p><strong>Constructors:</strong></p>
 * <ul>
 *   <li>{@code TokenProcessorContext(TokenProcessingStrategy tokenProcessingStrategy)}:
 *       Constructor initializing the token processing strategy.</li>
 * </ul>
 *
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@code Claims extractClaimsFromToken(String jwtToken)}:
 *       Extracts and returns claims from a given JWT token.</li>
 *
 *   <li>{@code Map<String, String> refreshTokens(Claims claims, String tokenId)}:
 *       Refreshes the provided JWT token's claims, generating a new pair of access and refresh tokens.</li>
 *
 *   <li>{@code Map<String, String> issueUserTokens(String userId, String tokenId)}:
 *       Issues a new pair of access and refresh tokens for a given user ID.</li>
 *
 *   <li>{@code Class<?> getEventClass()}:
 *       Returns the event class from the token processing strategy.</li>
 * </ul>
 *
 * <p><strong>Dependencies:</strong></p>
 * <ul>
 *   <li>{@code TokenProcessingStrategy}:
 *       An interface defining the strategy for token processing.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <p>The {@code TokenProcessorContext} is used to process and handle JWT operations within the token library.
 * It relies on a specific token processing strategy provided during instantiation.</p>
 *
 * <p><strong>Important Notes:</strong></p>
 * <ul>
 *   <li>This class relies on the Nimbus JOSE library for JSON Web Token operations.</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * TokenProcessingStrategy strategy = // create a specific strategy
 * TokenProcessorContext tokenProcessor = new TokenProcessorContext(strategy);
 * Claims extractedClaims = tokenProcessor.extractClaimsFromToken(jwtToken);
 * // Process the claims or perform other token-related operations
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> [Your Name]</p>
 *
 * <p><strong>See Also:</strong></p>
 * <ul>
 *   <li>{@link tokenlib.util.TokenProcessingStrategy}</li>
 *   <li>{@link tokenlib.util.jwk.AppConstants}</li>
 *   <li>{@link tokenlib.util.tokenenum.TokenExpiration}</li>
 *   <li>{@link tokenlib.util.tokenenum.TokenFields}</li>
 *   <li>{@link tokenlib.util.tokenenum.TokenTypes}</li>
 * </ul>
 */
public class TokenProcessorContext implements TokenOperationHandler, TokenFacade {
  // The strategy used for token processing
  private TokenProcessingStrategy tokenProcessingStrategy;

  /**
   * Constructor initializing the token processing strategy.
   *
   * @param tokenProcessingStrategy The strategy for token processing.
   */
  public TokenProcessorContext(TokenProcessingStrategy tokenProcessingStrategy) {
    this.tokenProcessingStrategy = tokenProcessingStrategy;
  }

  /**
   * Extracts and returns claims from a given JWT token.
   *
   * @param jwtToken The JWT token from which claims need to be extracted.
   * @return An instance of {@code Claims} containing the extracted claims.
   * @throws ParseException If an error occurs during parsing the token.
   * @throws IOException If an I/O error occurs.
   * @throws JOSEException If an error occurs during JSON Object Signing and Encryption (JOSE) operations.
   */
  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {
    return tokenProcessingStrategy.extractClaimsFromToken(jwtToken);
  }

  /**
   * Refreshes the provided JWT token's claims, generating a new pair of access and refresh tokens.
   *
   * @param claims The existing claims from the original token.
   * @param tokenId The identifier for the token.
   * @return A {@code Map} containing the new access and refresh tokens.
   * @throws NoSuchAlgorithmException If the required cryptographic algorithm is not available.
   */
  @Override
  public Map<String, String> refreshTokens(Claims claims, String tokenId) throws NoSuchAlgorithmException {
    // Check if the token is a refresh token
    if (!isRefreshToken(claims))
      throw new IllegalArgumentException("Token is not a refresh token");

    // Create new claims for refresh token
    Claims refreshTokenClaims = Jwts.claims();
    refreshTokenClaims.putAll(claims);
    refreshTokenClaims.setExpiration(new Date(System.currentTimeMillis() + TokenExpiration.ONE_HOUR.getMilliseconds()));

    // Create new claims for access token
    Claims accessTokenClaims = Jwts.claims();
    accessTokenClaims.putAll(claims);
    accessTokenClaims.setExpiration(new Date(System.currentTimeMillis() + TokenExpiration.TEN_MINUTES.getMilliseconds()));
    accessTokenClaims.put(TokenFields.TOKEN_TYPE.getValue(), TokenTypes.ACCESS_TOKEN.getValue());

    // Generate a new token pair using the strategy
    return tokenProcessingStrategy.generateTokenPair(
      claimsToJwtBuilder(accessTokenClaims),
      claimsToJwtBuilder(refreshTokenClaims),
      tokenId);
  }

  /**
   * Issues a new pair of access and refresh tokens for a given user ID.
   *
   * @param userId The user ID for whom tokens are issued.
   * @param tokenId The identifier for the token.
   * @return A {@code Map} containing the new access and refresh tokens.
   * @throws NoSuchAlgorithmException If the required cryptographic algorithm is not available.
   */
  @Override
  public Map<String, String> issueUserTokens(String userId, String tokenId) throws NoSuchAlgorithmException {
    var refresh = Jwts.builder()
      .setSubject(userId)
      .setIssuer(AppConstants.ISSUER.toString())
      .setExpiration(new Date(System.currentTimeMillis() + TokenExpiration.ONE_HOUR.getMilliseconds()))
      .setIssuedAt(new Date())
      .addClaims(Map.of(TokenFields.TOKEN_TYPE.getValue(), TokenTypes.REFRESH_TOKEN.getValue()));

    var access = Jwts.builder()
      .setSubject(userId)
      .setExpiration(new Date(System.currentTimeMillis() + TokenExpiration.TEN_MINUTES.getMilliseconds()))
      .addClaims(Map.of(TokenFields.TOKEN_TYPE.getValue(), TokenTypes.ACCESS_TOKEN.getValue()));

    // Generate a new token pair using the strategy
    return tokenProcessingStrategy.generateTokenPair(access, refresh, tokenId);
  }

  /**
   * Returns the event class from the token processing strategy.
   *
   * @return The event class.
   */
  @Override
  public Class<?> getEventClass() {
    return tokenProcessingStrategy.getEventClass();
  }

  // Helper method to check if the token is a refresh token
  private boolean isRefreshToken(Claims claims) {
    String tokenType = (String) claims.get(TokenFields.TOKEN_TYPE.getValue());
    return TokenTypes.REFRESH_TOKEN.getValue().equals(tokenType);
  }

  // Helper method to convert Claims to JwtBuilder
  private JwtBuilder claimsToJwtBuilder(Claims claims) {
    return Jwts.builder().setClaims(claims);
  }
}
