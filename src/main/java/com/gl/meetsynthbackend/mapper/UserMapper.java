package com.gl.meetsynthbackend.mapper;

import com.gl.meetsynthbackend.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user (id, username, email, password_hash, avatar_url) " +
            "VALUES (#{id}, #{username}, #{email}, #{passwordHash}, #{avatarUrl})")
    int insert(User user);

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    boolean existsByUsername(String username);

    @Select("SELECT COUNT(*) FROM user WHERE email = #{email}")
    boolean existsByEmail(String email);

    @Select("SELECT username FROM user WHERE id = #{userId}")
    String selectUsernameByUerId(String userId);

    @Select("SELECT id, username, avatar_url FROM user WHERE email = #{email} AND password = #{password}")
    User selectByEmailAndPassword(String email, String password);
}