package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.dtos.CoverageCheckDTO;
import bdavanzadas.lab1.entities.CoverageAreaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Collections;
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
     * Verifica eficientemente si un cliente está en zona de cobertura
     * @param clientId ID del cliente
     * @param companyId ID de la empresa
     * @return true si está en cobertura, false si no
     */
    /**
     * Verifica eficientemente si un cliente está en zona de cobertura
     * @param clientId ID del cliente
     * @param companyId ID de la empresa
     * @return true si está en cobertura, false si no
     */
    public boolean isClientInCoverageArea(int clientId, int companyId) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM clients c
                JOIN coverage_area_company cac ON cac.company_id = ?
                JOIN coverage_area ca ON ca.coverage_id = cac.coverage_id
                WHERE c.id = ?
                AND ST_Within(c.ubication, ca.coverageArea)
                            )"""; // Paréntesis correctamente cerrado

        try {
            return Boolean.TRUE.equals(
                    jdbcTemplate.queryForObject(sql, Boolean.class, companyId, clientId)
            );
        } catch (DataAccessException e) {
            throw new DataAccessException("Error en consulta de cobertura", e) {};
        }
    }

    /**
     * Obtiene información detallada de cobertura para un cliente y empresa
     * @param clientId ID del cliente
     * @param companyId ID de la empresa
     * @return CoverageCheckDTO con la información (null si no existe relación)
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
                CASE 
                    WHEN ca.coverage_id IS NULL THEN false
                    ELSE ST_Within(c.ubication, ca.coverageArea)
                END AS is_covered,
                ST_Distance(
                    c.ubication::geography, 
                    comp.ubication::geography
                ) AS distance_meters
            FROM 
                clients c
            CROSS JOIN 
                companies comp
            LEFT JOIN (
                SELECT cac.company_id, ca.coverage_id, ca.name, ca.coverageArea
                FROM coverage_area_company cac
                JOIN coverage_area ca ON ca.coverage_id = cac.coverage_id
                WHERE cac.company_id = ?
            ) ca ON ST_Intersects(c.ubication, ca.coverageArea)
            WHERE 
                c.id = ? 
                AND comp.id = ?
            ORDER BY
                CASE WHEN ca.coverage_id IS NULL THEN 1 ELSE 0 END,
                ST_Distance(c.ubication, ca.coverageArea)
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new CoverageCheckDTO(
                            rs.getInt("client_id"),
                            rs.getString("client_name"),
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getObject("coverage_id", Integer.class), // Maneja posible null
                            rs.getString("coverage_name"), // Puede ser null
                            rs.getBoolean("is_covered"),
                            rs.getDouble("distance_meters")
                    ), companyId, clientId, companyId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Versión optimizada que incluye radio de distancia máxima
     * @param clientId ID del cliente
     * @param companyId ID de la empresa
     * @param maxDistanceMeters Radio máximo en metros
     * @return true si está dentro del radio y zona de cobertura
     */
    public boolean isClientInCoverageWithRadius(int clientId, int companyId, double maxDistanceMeters) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM clients c
                JOIN companies comp ON comp.id = ?
                LEFT JOIN coverage_area_company cac ON cac.company_id = comp.id
                LEFT JOIN coverage_area ca ON ca.coverage_id = cac.coverage_id
                WHERE c.id = ?
                AND (
                    ST_DWithin(
                        c.ubication::geography, 
                        comp.ubication::geography, 
                        ?
                    )
                    OR 
                    ST_Within(c.ubication, ca.coverageArea)
                )
            )""";

        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class,
                companyId, clientId, maxDistanceMeters);
        return Boolean.TRUE.equals(result);
    }

    public List<CoverageCheckDTO> getClientCoverages(int clientId) {
        String sql = """
        SELECT 
            c.id AS client_id,
            c.name AS client_name,
            comp.id AS company_id,
            comp.name AS company_name,
            ca.coverage_id,
            ca.name AS coverage_name,
            TRUE AS is_covered,
            0 AS distance_meters
        FROM 
            clients c
        JOIN coverage_area_company cac ON 1=1
        JOIN coverage_area ca ON ca.coverage_id = cac.coverage_id
        JOIN companies comp ON comp.id = cac.company_id
        WHERE 
            c.id = ?
            AND ST_Within(c.ubication, ca.coverageArea)
        """;

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    new CoverageCheckDTO(
                            rs.getInt("client_id"),
                            rs.getString("client_name"),
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getObject("coverage_id", Integer.class),
                            rs.getString("coverage_name"),
                            true, // Siempre true porque el WHERE ya filtra por ST_Within
                            0 // distance_meters fijado en 0 como solicitaste
                    ), clientId);
        } catch (DataAccessException e) {
            // Si hay error, devolvemos lista vacía
            return Collections.emptyList();
        }
    }
}
