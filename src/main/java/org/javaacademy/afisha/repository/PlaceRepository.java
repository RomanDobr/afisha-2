package org.javaacademy.afisha.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.afisha.entity.Place;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceRepository {
  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;

  public void createPlace(int id, String name, String address, String city) {
    String sql =
      """
        INSERT INTO application.place (id, name, address, city) values (
        ?,
        ?,
        ?,
        ?)
      """;
    transactionTemplate.executeWithoutResult(transactionStatus -> {
      int update = jdbcTemplate.update(sql, preparedStatement -> {
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, address);
        preparedStatement.setString(4, city);
      });
    });
  }

  public Place getPlaceId(Integer id) {
    String sql = "SELECT * FROM application.place WHERE id = ?";
    Place placeResult = jdbcTemplate.query(
        sql,
        preparedStatement -> preparedStatement.setInt(1, id),
        this::placeRowMapper)
        .stream()
        .findFirst()
        .orElseThrow();
    return placeResult;
  }

  public List<Place> fundAllPlace() {
    String sql = "SELECT * FROM application.place";
    List<Place> places = jdbcTemplate.query(
        sql,
        this::placeRowMapper);
    return places;
  }

  private Place placeRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
    Place place = new Place();
    place.setId(resultSet.getInt("id"));
    place.setName(resultSet.getString("name"));
    place.setName(resultSet.getString("address"));
    place.setCity(resultSet.getString("city"));
    return place;
  }

}
