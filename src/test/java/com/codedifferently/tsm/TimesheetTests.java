package com.codedifferently.tsm;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class TimesheetTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupDatabase() {
        jdbcTemplate.execute("CREATE TABLE users (user_id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255));");
        jdbcTemplate.execute("CREATE TABLE worksites (worksite_id INT AUTO_INCREMENT PRIMARY KEY, location VARCHAR(255));");

        jdbcTemplate.execute("CREATE TABLE timesheets (" +
                "timesheet_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "worksite_id INT NOT NULL, " +
                "created DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "week DATE NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                "FOREIGN KEY (worksite_id) REFERENCES worksites(worksite_id));");

        jdbcTemplate.execute("CREATE TABLE timesheet_entries (" +
                "entry_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "timesheet_id INT NOT NULL, " +
                "date DATE NOT NULL, " +
                "start DATETIME NOT NULL, " +
                "end DATETIME NOT NULL, " +
                "FOREIGN KEY (timesheet_id) REFERENCES timesheets(timesheet_id) ON DELETE CASCADE);");
    }

    @Test
    void testCreateTimesheetWithoutUser_ShouldFail() {
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcTemplate.update("INSERT INTO timesheets (user_id, worksite_id, week) VALUES (?, ?, ?)",
                    999, 1, "2025-02-06"); // User ID 999 does not exist
        });

        System.out.println("Expected error: " + exception.getMessage());
    }

    @Test
    void testCreateTimesheetWithoutWorksite_ShouldFail() {
        jdbcTemplate.update("INSERT INTO users (name) VALUES (?)", "John Doe");
        Integer userId = jdbcTemplate.queryForObject("SELECT user_id FROM users LIMIT 1", Integer.class);

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcTemplate.update("INSERT INTO timesheets (user_id, worksite_id, week) VALUES (?, ?, ?)",
                    userId, 999, "2025-02-06"); // Worksite ID 999 does not exist
        });

        System.out.println("Expected error: " + exception.getMessage());
    }

    @Test
    void testDeleteTimesheet_ShouldCascadeDeleteEntries() {
        jdbcTemplate.update("INSERT INTO users (name) VALUES (?)", "Jane Doe");
        jdbcTemplate.update("INSERT INTO worksites (location) VALUES (?)", "Site A");

        Integer userId = jdbcTemplate.queryForObject("SELECT user_id FROM users LIMIT 1", Integer.class);
        Integer worksiteId = jdbcTemplate.queryForObject("SELECT worksite_id FROM worksites LIMIT 1", Integer.class);

        jdbcTemplate.update("INSERT INTO timesheets (user_id, worksite_id, week) VALUES (?, ?, ?)",
                userId, worksiteId, "2025-02-06");

        Integer timesheetId = jdbcTemplate.queryForObject("SELECT timesheet_id FROM timesheets LIMIT 1", Integer.class);
        jdbcTemplate.update("INSERT INTO timesheet_entries (timesheet_id, date, start, end) VALUES (?, ?, ?, ?)",
                timesheetId, "2025-02-06", "2025-02-06 08:00:00", "2025-02-06 17:00:00");

        jdbcTemplate.update("DELETE FROM timesheets WHERE timesheet_id = ?", timesheetId);

        Integer entryCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM timesheet_entries WHERE timesheet_id = ?",
                new Object[]{timesheetId}, Integer.class);

        assertEquals(0, entryCount, "Entries should be deleted when timesheet is removed");
    }

    @Test
    void testInsertDuplicateTimesheet_ShouldFail() {
        jdbcTemplate.update("INSERT INTO users (name) VALUES (?)", "John Smith");
        jdbcTemplate.update("INSERT INTO worksites (location) VALUES (?)", "Site B");

        Integer userId = jdbcTemplate.queryForObject("SELECT user_id FROM users LIMIT 1", Integer.class);
        Integer worksiteId = jdbcTemplate.queryForObject("SELECT worksite_id FROM worksites LIMIT 1", Integer.class);

        jdbcTemplate.update("INSERT INTO timesheets (user_id, worksite_id, week) VALUES (?, ?, ?)",
                userId, worksiteId, "2025-02-06");

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcTemplate.update("INSERT INTO timesheets (user_id, worksite_id, week) VALUES (?, ?, ?)",
                    userId, worksiteId, "2025-02-06"); // Same user & week should fail if constraint added
        });

        System.out.println("Expected error: " + exception.getMessage());
    }

    @Test
    void testUpdateNonExistentTimesheet_ShouldFail() {
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcTemplate.update("UPDATE timesheets SET week = ? WHERE timesheet_id = ?", "2025-02-13", 999);
        });

        System.out.println("Expected error: " + exception.getMessage());
    }

    @Test
    void testDeleteNonExistentTimesheet_ShouldFail() {
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcTemplate.update("DELETE FROM timesheets WHERE timesheet_id = ?", 999);
        });

        System.out.println("Expected error: " + exception.getMessage());
    }
}
