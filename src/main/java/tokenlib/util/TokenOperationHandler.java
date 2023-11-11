package tokenlib.util;

interface TokenOperationHandler extends TokenRefresher, TokenClaimsExtractor, UserTokenIssuer, EventClassProvider {
}
