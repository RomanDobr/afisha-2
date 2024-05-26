package org.javaacademy.afisha.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Profile(value = "first")
@Slf4j
public class InitDatabaseService {
  private static final String SCRIPT_URL = "src/main/resources/init.sql";
  private final JdbcTemplate jdbcTemplate;

  @PostConstruct
  public void init() throws SQLException {
    try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
      PathResource resource = new PathResource(SCRIPT_URL);
      ScriptUtils.executeSqlScript(connection, resource);
    }
    log.info("Соединение установлено");
  }
}
