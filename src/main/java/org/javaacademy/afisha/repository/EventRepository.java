package org.javaacademy.afisha.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.entity.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventRepository {
  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;

  public void createEvent(int id, String nameEvent) {
    String sql =
      """
        INSERT INTO application.event (id, name, event_type_id, event_date, place_id) values (
        ?,
        ?,
        (SELECT id FROM application.event_type WHERE name = ?),
        ?,
        (SELECT id FROM application.place WHERE name = ?))
      """;
    transactionTemplate.executeWithoutResult(transactionStatus -> {
      int update = jdbcTemplate.update(sql, preparedStatement -> {
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, nameEvent);
        preparedStatement.setString(3, nameEvent);
        preparedStatement.setString(4, LocalDateTime.now().toString());
        preparedStatement.setString(5, nameEvent);
      });
      System.out.println(update);
      System.out.println(getEventId(1));
    });
  }

  public Event getEventId(Integer id) {
    String sql = "SELECT * FROM application.event WHERE id = ?";
    Event eventResult = jdbcTemplate.query(
        sql,
        preparedStatement -> preparedStatement.setInt(1, id),
        this::eventRowMapper)
        .stream()
        .findFirst()
        .orElseThrow();
    return eventResult;
  }

  public List<Event> fundAllEvents() {
    String sql = "SELECT * FROM application.event";
    List<Event> events = jdbcTemplate.query(
        sql,
        this::eventRowMapper);
    return events;
  }

  private Event eventRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
    Event event = new Event();
    event.setId(resultSet.getInt("id"));
    event.setName(resultSet.getString("name"));
    event.setEventTypeId(resultSet.getInt("event_type_id"));
    event.setEventDate(LocalDateTime.parse(resultSet.getString("event_date")));
    event.setPlaceId(resultSet.getInt("place_id"));
    return event;
  }
}
