package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.CoverageAreaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CoverageAreaRepository implements CoverageAreaRepositoryInt {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CoverageAreaEntity> findAll() {
        String sql = "SELECT coverage_id AS id, name, ST_AsText(coverageArea) AS coverageArea FROM coverage_area";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CoverageAreaEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("coverageArea")
                ));
    }

    @Override
    public CoverageAreaEntity findById(int id) {
        String sql = "SELECT coverage_id AS id, name, ST_AsText(coverageArea) AS coverageArea FROM coverage_area WHERE coverage_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new CoverageAreaEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("coverageArea")
                ));
    }

    @Override
    public void save(CoverageAreaEntity coverageArea) {
        String sql = "INSERT INTO coverage_area (name, coverageArea) VALUES (?, ST_GeomFromText(?, 4326))";
        jdbcTemplate.update(sql,
                coverageArea.getName(),
                coverageArea.getCoverageArea());
    }

    @Override
    public void update(CoverageAreaEntity coverageArea) {
        String sql = "UPDATE coverage_area SET name = ?, coverageArea = ST_GeomFromText(?, 4326) WHERE coverage_id = ?";
        jdbcTemplate.update(sql,
                coverageArea.getName(),
                coverageArea.getCoverageArea(),
                coverageArea.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM coverage_area WHERE coverage_id = ?";
        jdbcTemplate.update(sql, id);
    }
}