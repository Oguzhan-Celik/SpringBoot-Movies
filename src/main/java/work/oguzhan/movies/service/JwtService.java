package work.oguzhan.movies.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
	@Value("${jwt.secret}")
    private String secret;
	private static final long JWT_EXPIRATION_HOURS = 5;
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
	
	public String generateToken(UserDetails userDetails) {
    	return generateToken(new HashMap<>(), userDetails);
    }
    
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails);
    }
    
    public long getExpirationTime() {
    	return System.currentTimeMillis() + JWT_EXPIRATION_HOURS * 3600000;
    }
	
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    	LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusHours(JWT_EXPIRATION_HOURS);
    	
    	return Jwts.builder()
        		.setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(convertToDate(now))
                .setExpiration(convertToDate(expiryDate))
                .signWith(getSignInKey())
                .compact();
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }
    
    public LocalDateTime extractExpiration(String token) {
        return convertToLocalDateTime(extractClaim(token, Claims::getExpiration));
    }
    
    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    private Claims extractAllClaims(String token) {
    	return Jwts
    			.parserBuilder()
    			.setSigningKey(getSignInKey())
    			.build()
    			.parseClaimsJws(token)
    			.getBody();
    }
    
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        
        return Keys.hmacShaKeyFor(keyBytes);
    }
}