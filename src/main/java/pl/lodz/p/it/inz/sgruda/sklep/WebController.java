package pl.lodz.p.it.inz.sgruda.sklep;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WebController {

    private final JdbcTemplate jdbcTemplate;

    public WebController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/test")
    public List<String> getTuples() {
        return this.jdbcTemplate.queryForList("SELECT * FROM users").stream()
                .map((m) -> m.values().toString())
                .collect(Collectors.toList());
    }
    @GetMapping("/test2")
    public String getSth() {
        return new String("test");
    }
}
