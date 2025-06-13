package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.dtos.CoverageCheckDTO;
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

        /**
         * Verifica si un cliente está dentro de la zona de cobertura de una empresa
         * @param clientId ID del cliente
         * @param companyId ID de la empresa
         * @return true si el cliente está en la zona de cobertura
         */
        public boolean isClientInCoverageArea(int clientId, int companyId) {
            String sql = """
            SELECT COUNT(*) > 0
            FROM clients c
            JOIN coverage_area_company cac ON cac.company_id = ?
            JOIN coverage_area ca ON ca.coverage_id = cac.coverage_id
            WHERE c.id = ? 
            AND ST_Within(c.ubication, ca.coverageArea)
            """;

            return jdbcTemplate.queryForObject(sql, Boolean.class, companyId, clientId);
        }

        /**
         * Versión detallada que devuelve información sobre la cobertura
         * @param clientId ID del cliente
         * @param companyId ID de la empresa
         * @return DTO con información detallada
         */
        public CoverageCheckDTO getClientCoverageDetails(int clientId, int companyId) {
            String sql = """
            SELECT 
                c.id AS client_id,
                c.name AS client_name,
                comp.id AS company_id,
                comp.name AS company_name,
                ca.coverage_id,
                ca.name AS coverage_name,
                ST_Within(c.ubication, ca.coverageArea) AS is_covered,
                ST_Distance(c.ubication, comp.ubication) AS distance_meters
            FROM 
                clients c
            CROSS JOIN 
                companies comp
            LEFT JOIN 
                coverage_area_company cac ON cac.company_id = comp.id
            LEFT JOIN 
                coverage_area ca ON ca.coverage_id = cac.coverage_id
            WHERE 
                c.id = ? AND comp.id = ?
            ORDER BY 
                ST_Distance(c.ubication, ca.coverageArea)
            LIMIT 1
            """;

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new CoverageCheckDTO(
                            rs.getInt("client_id"),
                            rs.getString("client_name"),
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getInt("coverage_id"),
                            rs.getString("coverage_name"),
                            rs.getBoolean("is_covered"),
                            rs.getDouble("distance_meters")
                    ), clientId, companyId);
        }
    }
