package org.Stasy.PublicPrivacyAppBackendHeroku.JWT;

import org.Stasy.PublicPrivacyAppBackendHeroku.User.User;

import java.io.UnsupportedEncodingException;

public interface JwtGeneratorInterface {
    String generateLoginToken(User user) throws UnsupportedEncodingException;
}
