package top.alexmmd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.alexmmd.domain.User;

import java.util.Optional;

/**
 * @author 汪永晖
 */
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
}
