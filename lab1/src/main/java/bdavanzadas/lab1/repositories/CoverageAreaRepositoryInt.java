package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.CoverageAreaEntity;
import java.util.List;

public interface CoverageAreaRepositoryInt {
    List<CoverageAreaEntity> findAll();
    CoverageAreaEntity findById(int id);
    void save(CoverageAreaEntity coverageArea);
    void update(CoverageAreaEntity coverageArea);
    void delete(int id);
}