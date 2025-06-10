package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.CompanyEntity;
import bdavanzadas.lab1.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 *
 * La clase CompanyService representa el servicio de empresas en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar empresas en la base de datos.
 *
 */
@Service
public class CompanyService {

    /**
     * Repositorio de empresas.
     * Este repositorio se utiliza para interactuar con la base de datos de empresas.
     */
    @Autowired
    private CompanyRepository companyRepository;


    /**
     * Constructor de la clase CompanyService.
     * @param "companyRepository" El repositorio de empresas a utilizar.
     */
    @Transactional(readOnly = true)
    public List<CompanyEntity> getAllCompanies() {
        List<CompanyEntity> companies = companyRepository.findAll();
        return companies;
    }


    /**
     * Metodo para buscar una empresa por su id.
     * @param "id" El id de la empresa a buscar.
     * @return La empresa encontrada.
     *
     */
    @Transactional(readOnly = true)
    public CompanyEntity findbyid(int id) {
        return companyRepository.findbyid(id);
    }


    /**
     * Metodo para guardar una empresa en la base de datos.
     * @param "company" La empresa a guardar.
     * @return void
     */
    @Transactional
    public void saveCompany(CompanyEntity company) {
        companyRepository.save(company);
    }


    /**
     * Metodo para actualizar una empresa en la base de datos.
     * @param "company" La empresa a actualizar.
     * @return void
     *
     */
    @Transactional
    public void updateCompany(CompanyEntity company) {
        companyRepository.update(company);
    }


    /**
     * Metodo para eliminar una empresa de la base de datos.
     * @param "id" El id de la empresa a eliminar.
     * @return void
     *
     */
    @Transactional
    public void deleteCompany(int id) {
        companyRepository.delete(id);
    }

    /**
     * Metodo para buscar las empresas con más entregas fallidas.
     * @return Una lista de empresas con más entregas fallidas.
     *
     */
    @Transactional(readOnly = true)
    public List<CompanyEntity> getCompaniesWithMostFailedDeliveries() {
        return companyRepository.getCompaniesWithMostFailedDeliveries();
    }


    /**
     * Metodo para actualizar las métricas de las empresas.
     * Este método se utiliza para actualizar las métricas de las empresas en la base de datos.
     * @return void
     */

    @Transactional
    public void updateCompanyMetrics() {
        companyRepository.updateCompanyMetrics();
    }


    /**
     * Metodo para buscar las empresas por el volumen de comida entregada.
     * @return Una lista de empresas ordenadas por el volumen de comida entregada.
     *
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCompaniesByDeliveredFoodVolume() {
        return companyRepository.getCompaniesByDeliveredFoodVolume();
    }
}