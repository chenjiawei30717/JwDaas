package com.ricoh.jwdaas.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {

	private Map<String, Date> blacklist = new HashMap<>();
	private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hour
    private static final long REFRESH_TIME = 1000 * 60 * 60 * 5; // 5 hour
    private final String SECRET_KEY = "WXCLYDBrtbwkjhrjhyzjwdlzHDKSJHBCJDKcndmncslkcnldcnxzncdmnlfn"; // 秘钥，根据实际情况更改

    // 创建JWT
    public String generateToken(UserDetails userDetails, String trustedDomain) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), trustedDomain);
    }

    private String createToken(Map<String, Object> claims, String subject, String trustedDomain) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        String jwtId = UUID.randomUUID().toString();
        claims.put("jti", jwtId);
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME); // token过期时间
        Date refreshDate = new Date(now.getTime() + REFRESH_TIME);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
//                .setId(jwtId)
                .setExpiration(expirationDate)
                .claim("refreshDate", refreshDate)
                .claim("trustedDomain", trustedDomain) // 添加可信域的声明
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

 // 清理过期的JWT
    public void clearExpiredTokens() {
//        final Date now = new Date();
//        jwtBlacklist.removeIf(jwtId -> {
//        	Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(jwtId);
//        	Claims claims = jws.getBody();
////            final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(jwtId).getBody();
//            final Date expiration = claims.getExpiration();
//            return expiration.before(now);
//        });
    	long nowMillis = System.currentTimeMillis();
        Iterator<Map.Entry<String, Date>> iterator = blacklist.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Date> entry = iterator.next();
            Date expirationDate = entry.getValue();
//            String jti = entry.getKey();
            if (expirationDate.getTime() < nowMillis) {
                iterator.remove();
                // 可选：将被删除的 JWT 加入到过期 JWT 黑名单中
//                expiredTokens.put(jti, expirationDate);
            }
        }
    }
    // 验证JWT
    public Boolean validateToken(String token, UserDetails userDetails, String trustedDomain) {
    	clearExpiredTokens();
        final String username = extractUsername(token);
        
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && extractTrustedDomain(token).equals(trustedDomain) && !isTokenBlacklisted(token));
    }

    // 从JWT中获取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 从JWT中获取可信域
    public String extractTrustedDomain(String token) {
        return (String) Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody().get("trustedDomain");
    }

    // 判断JWT是否过期
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 从JWT中获取过期时间
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    // 将JWT加入黑名单
    public void blacklistToken(String token) {
        final String jwtId = extractClaim(token, Claims::getId);
        final Date expirationDate = extractClaim(token, Claims::getExpiration);
        blacklist.put(jwtId, expirationDate);
//        jwtBlacklist.add(jwtId);
    }
    
    // 判断JWT是否在黑名单中
    public boolean isTokenBlacklisted(String token) {
        final String jwtId = extractClaim(token, Claims::getId);
//        return jwtBlacklist.contains(jwtId);
        return blacklist.containsKey(jwtId);
    }
    
    public boolean isTokenRefreshable(String token) {
    	Claims claims = extractAllClaims(token);
    	if(new Date((long)claims.get("refreshDate")).before(new Date()) && !isTokenExpired(token)) {
    		return true;
    	}else {
    		return false;
    	}
//    	new Date((long)Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody().get("trustedDomain"));
//    	return new Date((long)Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody().get("refreshDate")).before(new Date());
    }
    public String refreshToken(String token) {
    	final Claims claims = extractAllClaims(token);
    	blacklistToken(token);
    	claims.setIssuedAt(new Date());
    	String jwtId = UUID.randomUUID().toString();
        claims.put("jti", jwtId);
//        Claims claims = Jwts.parser()
//                .setSigningKey(SECRET_KEY.getBytes())
//                .parseClaimsJws(token)
//                .getBody();

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
        Date refreshDate = new Date(now.getTime() + REFRESH_TIME);

        claims.setExpiration(expirationDate);
        claims.put("refreshDate", refreshDate);
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(extractUsername(token))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 通用方法，从JWT中获取指定的值
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}
