package tokenlib.util;

import tokenlib.util.lamdas.EventClassProvider;

interface TokenOperationHandler extends TokenRefresher, TokenClaimsExtractor, UserTokenIssuer, EventClassProvider {
}
