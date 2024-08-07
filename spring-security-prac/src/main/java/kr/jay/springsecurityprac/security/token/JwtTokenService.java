package kr.jay.springsecurityprac.security.token;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtTokenService
 *
 * @author jaypark
 * @version 1.0.0
 * @since 2024. 7. 13.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenService {

	private final JwtTokenProvider tokenProvider;

	public TokenInfo generateToken(String username) {
		return tokenProvider.generateToken(username);
	}
}
