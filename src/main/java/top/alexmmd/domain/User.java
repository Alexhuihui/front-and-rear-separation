package top.alexmmd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    //邮箱
    private String email;
    @Column(name = "password")
    //密码
    private String password;
    @Column(name = "tcoin")
    //t币
    private int tcoin;
    @Column(name = "nickname")
    //昵称
    private String nickname;
    @Column(name = "sex")
    //性别
    private int sex;
    @Column(name = "address")
    //地址
    private String address;
    @Column(name = "profiles")
    //简介
    private String profiles;
    @Column(name = "head_url")
    //头像
    private String headUrl;

    public User(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickname = nickName;
    }
}
