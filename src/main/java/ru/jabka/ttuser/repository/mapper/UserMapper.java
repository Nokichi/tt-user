package ru.jabka.ttuser.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.jabka.ttuser.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static java.util.Optional.ofNullable;

@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User.UserBuilder userBuilder = User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .passwordHash(rs.getString("password_hash"))
                .isDeleted(rs.getBoolean("is_deleted"))
                .createdAt(rs.getObject("created_at", Timestamp.class).toLocalDateTime())
                .updatedAt(rs.getObject("updated_at", Timestamp.class).toLocalDateTime());
        ofNullable(rs.getObject("deleted_at", Timestamp.class))
                .ifPresent(timestamp -> userBuilder.deletedAt(timestamp.toLocalDateTime()));
        return userBuilder.build();
    }
}