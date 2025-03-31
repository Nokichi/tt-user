package ru.jabka.ttuser.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabka.ttuser.model.User;
import ru.jabka.ttuser.repository.mapper.UserMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    private static final String INSERT = """
            INSERT INTO tt.user (username, password_hash)
            VALUES (:username, :password_hash)
            RETURNING *
            """;

    private static final String GET_BY_ID = """
            SELECT *
            FROM tt.user
            WHERE id = :id
            """;

    private static final String GET_ALL_BY_NAME = """
            SELECT *
            FROM tt.user
            WHERE username LIKE :username
            """;

    private static final String DELETE = """
            UPDATE tt.user
            SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP
            WHERE id = :id
            RETURNING *
            """;

    public User insert(final User user) {
        return jdbcTemplate.queryForObject(INSERT, userToSql(user), userMapper);
    }

    public User getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), userMapper);
        } catch (Throwable e) {
            throw new RuntimeException(String.format("Пользователь с id %d не найден", id));
        }
    }

    public List<User> getAllByName(final String username) {
        return jdbcTemplate.query(GET_ALL_BY_NAME, new MapSqlParameterSource("username", username), userMapper);
    }

    public User delete(final Long id) {
        return jdbcTemplate.queryForObject(DELETE, new MapSqlParameterSource("id", id), userMapper);
    }

    private MapSqlParameterSource userToSql(User user) {
        return new MapSqlParameterSource()
                .addValue("id", user.id())
                .addValue("username", user.username())
                .addValue("password_hash", user.passwordHash())
                .addValue("is_deleted", user.isDeleted())
                .addValue("created_at", user.createdAt())
                .addValue("updated_at", user.updatedAt())
                .addValue("deleted_at", user.deletedAt());
    }
}