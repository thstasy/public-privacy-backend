package org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.Stasy.PublicPrivacyAppBackendHeroku.Globe.DTO.GeocodingResponse;
import org.Stasy.PublicPrivacyAppBackendHeroku.Globe.POJO.Coordinates;
import org.Stasy.PublicPrivacyAppBackendHeroku.Globe.POJO.CoordinatesWithInfo;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.Opinion;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomOpinionRepositoryImpl implements CustomOpinionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final RestTemplate restTemplate;

    private final OpinionsRepository opinionsRepository;

    public CustomOpinionRepositoryImpl(RestTemplate restTemplate, OpinionsRepository opinionsRepository) {
        this.restTemplate = restTemplate;
        this.opinionsRepository = opinionsRepository;
    }

    @Override
    public <S extends Opinion> S saveOpinionWithCollaboratorName(S entity, String collaboratorName) {
        entity.setCollaboratorName(collaboratorName);
        S savedEntity = opinionsRepository.save(entity); // Use the save method from JpaRepository
        entityManager.flush();
        return savedEntity;
    }

    /**extra-territorial plan*/

    private Coordinates getCoordinates(String countryCode, String city) {
        String apiKey = "AIzaSyD4dwS8ZBPle6bP_p6pd8QxnkIAP-xAM54";
        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json" +
                "?address=" + city + "," + countryCode +
                "&key=" + apiKey;

        try {
            GeocodingResponse response = restTemplate.getForObject(apiUrl, GeocodingResponse.class);
            if (response != null && response.getResults().size() > 0) {
                GeocodingResponse.Result result = response.getResults().get(0);
                GeocodingResponse.Geometry geometry = result.getGeometry();
                GeocodingResponse.Location location = geometry.getLocation();
                double latitude = location.getLat();
                double longitude = location.getLng();
                return new Coordinates(latitude, longitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void updateOpinionsWithCoordinates(String countryCode, String city, Coordinates coordinates) {
        List<Opinion> opinionsToUpdate = entityManager.createQuery(
                        "SELECT o FROM Opinion o " +
                                "WHERE o.countryCode = :countryCode AND o.city = :city", Opinion.class)
                .setParameter("countryCode", countryCode)
                .setParameter("city", city)
                .getResultList();

        for (Opinion opinion : opinionsToUpdate) {
            opinionsRepository.save(opinion);
        }
    }

    public void fetchAndSaveCoordinatesForOpinions() {
        List<Object[]> locations = entityManager.createQuery(
                        "SELECT DISTINCT o.countryCode, o.city FROM Opinion o " +
                                "WHERE o.countryCode IS NOT NULL AND o.city IS NOT NULL", Object[].class)
                .getResultList();

        for (Object[] location : locations) {
            String countryCode = (String) location[0];
            String city = (String) location[1];

            Coordinates coordinates = getCoordinates(countryCode, city);

            if (coordinates != null) {
                updateOpinionsWithCoordinates(countryCode, city, coordinates);
            }
        }
    }

    public List<Coordinates> fetchCoordinatesForOpinions() {
        List<Object[]> locations = entityManager.createQuery(
                        "SELECT DISTINCT o.countryCode, o.city FROM Opinion o " +
                                "WHERE o.countryCode IS NOT NULL AND o.city IS NOT NULL", Object[].class)
                .getResultList();

        List<Coordinates> coordinatesList = new ArrayList<>();

        for (Object[] location : locations) {
            String countryCode = (String) location[0];
            String city = (String) location[1];

            Coordinates coordinates = getCoordinates(countryCode, city);

            if (coordinates != null) {
                coordinatesList.add(coordinates);
            }
        }
        return coordinatesList;
    }


    public List<CoordinatesWithInfo> fetchCoordinatesWithInfoForOpinions() {
        List<Object[]> locations = entityManager.createQuery(
                        "SELECT DISTINCT o.countryCode, o.city, o.category, o.title FROM Opinion o " +
                                "WHERE o.countryCode IS NOT NULL AND o.city IS NOT NULL", Object[].class)
                .getResultList();

        List<CoordinatesWithInfo> coordinatesList = new ArrayList<>();

        for (Object[] location : locations) {
            String countryCode = (String) location[0];
            String city = (String) location[1];
            String category = (String) location[2];
            String title = (String) location[3];

            Coordinates coordinates = getCoordinates(countryCode, city);

            if (coordinates != null) {
                coordinatesList.add(new CoordinatesWithInfo(coordinates.latitude, coordinates.longitude, category, title));
            }
        }
        return coordinatesList;
    }

}
