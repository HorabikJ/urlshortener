package pl.jacekhorabik.urlshortener.mainpage;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

@Component("dbHealthIndicator")
@RequiredArgsConstructor
@Slf4j
class CustomDataSourceHealthIndicator extends DataSourceHealthIndicator {

  private static final String VALIDATION_QUERY = "select 1";
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public CustomDataSourceHealthIndicator(
      final DataSource dataSource, final JdbcTemplate jdbcTemplate) {
    super(dataSource);
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  protected void doHealthCheck(final Health.Builder builder) {
    try {
      jdbcTemplate.query(VALIDATION_QUERY, new SingleColumnRowMapper<>());
      builder.up().withDetail("validation query", VALIDATION_QUERY);
    } catch (Exception e) {
      log.error("Error when executing health check query on db.", e);
      builder.down().withDetail("validation query", VALIDATION_QUERY);
    }
  }
}
