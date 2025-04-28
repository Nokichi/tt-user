package ru.jabka.ttuser.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabka.ttuser.exception.BadRequestException;
import ru.jabka.ttuser.model.User;
import ru.jabka.ttuser.repository.mapper.UserMapper;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    private static final String INSERT = """
            INSERT INTO tt.user (username, password_hash, role_id)
            VALUES (:username, :password_hash, :role_id)
            RETURNING *
            """;

    private static final String GET_BY_ID = """
            SELECT *
            FROM tt.user
            WHERE id = :id
            AND is_deleted = false
            """;

    private static final String GET_ALL_BY_IDS = """
            SELECT *
            FROM tt.user
            WHERE id IN (:ids)
            AND is_deleted = false
            """;

    private static final String DELETE = """
            UPDATE tt.user
            SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP
            WHERE id = :id
            AND is_deleted = false
            RETURNING *
            """;

    public User insert(final User user) {
        return jdbcTemplate.queryForObject(INSERT, userToSql(user), userMapper);
    }

    public User getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), userMapper);
        } catch (Throwable e) {
            throw new BadRequestException(String.format("Пользователь с id %d не найден", id));
        }
    }

    public List<User> getAllByIds(final Set<Long> ids) {
        return jdbcTemplate.query(GET_ALL_BY_IDS, new MapSqlParameterSource("ids", ids), userMapper);
    }

    public User delete(final Long id) {
        return jdbcTemplate.queryForObject(DELETE, new MapSqlParameterSource("id", id), userMapper);
    }

    private MapSqlParameterSource userToSql(User user) {
        return new MapSqlParameterSource()
                .addValue("id", user.id())
                .addValue("username", user.username())
                .addValue("password_hash", user.passwordHash())
                .addValue("role_id", user.role().getId())
                .addValue("is_deleted", user.isDeleted())
                .addValue("created_at", user.createdAt())
                .addValue("updated_at", user.updatedAt())
                .addValue("deleted_at", user.deletedAt());
    }
}