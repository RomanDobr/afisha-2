package org.javaacademy.afisha.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.entity.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketRepository {
  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;

  public void createTicket(int id, String email, BigDecimal price) {
    String sql =
      """
        INSERT INTO application.ticket (id, event_id, client_email, price, is_selled) values (
        ?,
        (SELECT id FROM application.event WHERE id = ? ORDER BY ID DESC LIMIT 1),
        ?,
        ?,
        ?)
      """;
    transactionTemplate.executeWithoutResult(transactionStatus -> {
      int result = jdbcTemplate.update(sql, preparedStatement -> {
        preparedStatement.setInt(1, id);
        preparedStatement.setInt(2, id);
        preparedStatement.setString(3, email);
        preparedStatement.setBigDecimal(4, price);
        preparedStatement.setBoolean(5, true);
      });
    });
  }

  public Ticket getTicketId(Integer id) {
    String sql = "SELECT * FROM application.ticket WHERE id = ?";
    Ticket ticketResult = jdbcTemplate.query(
        sql,
        preparedStatement -> preparedStatement.setInt(1, id),
        this::ticketRowMapper)
        .stream()
        .findFirst()
        .orElseThrow();
    return ticketResult;
  }

  public List<Ticket> fundAllTicket() {
    String sql = "SELECT * FROM application.ticket";
    List<Ticket> tickets = jdbcTemplate.query(
        sql,
        this::ticketRowMapper);
    return tickets;
  }

  private Ticket ticketRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
    Ticket ticket = new Ticket();
    ticket.setId(resultSet.getInt("id"));
    ticket.setEventId(resultSet.getInt("event_id"));
    ticket.setClientEmail(resultSet.getString("client_email"));
    ticket.setIsSelled(resultSet.getBoolean("is_selled"));
    return ticket;
  }
}
