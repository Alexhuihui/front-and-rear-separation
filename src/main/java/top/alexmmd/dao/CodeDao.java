package top.alexmmd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.alexmmd.domain.VerificationCode;

import java.util.Optional;

/**
 * @author 汪永晖
 */
public interface CodeDao extends JpaRepository<VerificationCode, Long> {
    /**
     * 校验验证码
     *
     */
    Optional<VerificationCode> findByEmail(String email);
}
