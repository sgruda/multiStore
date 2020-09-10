package pl.lodz.p.it.inz.sgruda.multiStore;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class RestApi {

    private final JdbcTemplate jdbcTemplate;

    public RestApi(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world!";
    }

    @GetMapping("/db")
    public List<String> getTuples() {
        return this.jdbcTemplate.queryForList("SELECT * FROM employees").stream()
                .map((m) -> m.values().toString())
                .collect(Collectors.toList());
    }
    @GetMapping("/simple")
    public String simple() {
        return new String("test");
    }
}