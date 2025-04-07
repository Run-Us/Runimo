package org.runimo.runimo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CleanUpUtil {

  private static final String[] USER_TABLES = {
      "user_item",
      "oauth_account",
      "users",
      "running_record",
      "user_love_point",
      "incubating_egg",
      "runimo_definition",
      "runimo",
  };

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void cleanUpUserInfos() {
    for (String table : USER_TABLES) {
      jdbcTemplate.execute("DELETE FROM " + table);
      jdbcTemplate.update("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1");
    }
  }
}
