package top.alexmmd.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author 汪永晖
 */
public class TokenUtil {

    /**
     * 签名秘钥(唯一秘钥，可以用密码做为秘钥)
     */
    public static final String SECRET = "admin";

    /**
     * 生成token
     *
     * @param username
     * @return
     */
    public static String createJwtToken(String username) {
        String issuer = "alex";
        String subject = "liao";
        long ttlMillis = 36000000;//10个小时后过期
        return createJwtToken(username, issuer, subject, ttlMillis);
    }

    /**
     * 生成token
     *
     * @param username  用户名
     * @param issuer    JWT的签发者，是否使用可以选
     * @param subject   JWT所面向的用户，是否使用可选
     * @param ttlMillis 签发时间（有效时间，过期会报错）
     * @return token string
     */
    public static String createJwtToken(String username, String issuer, String subject, long ttlMillis) {
        //签名算法，将token进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成签发时间
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        //通过秘钥签名JWT
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //创建token
        JwtBuilder builder = Jwts.builder().setId(username)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signingKey);
        //添加过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMills + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    //验证和读取JWT的示例方法
    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
