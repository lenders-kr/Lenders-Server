package kr.api.lenders.util;

import kr.api.lenders.domain.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class LocationUtil {

    @Value("${kakao.api.key}") // Add your Kakao API key as a property in your Spring Boot application.properties file
    private String kakaoApiKey;

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    public Location getLocationFromCoordinates(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String apiUrl = KAKAO_API_URL + "?x=" + longitude + "&y=" + latitude;
        Location location;

        // [TODO] change exceptions to custom exceptions
        LocationApiResponse response;
        try {
            response = restTemplate.getForObject(apiUrl, LocationApiResponse.class, headers);
        } catch (RestClientException ex) {
            throw new RuntimeException("Failed to retrieve location information from Kakao API.", ex);
        }

        if (response == null || response.getDocuments() == null || response.getDocuments().length == 0) {
            throw new RuntimeException("Location not found for given coordinates.");
        }

        LocationApiDocument selectedDocument = getSelectedDocument(response);
        if (selectedDocument == null) {
            throw new RuntimeException("Location not found for given coordinates.");
        }

        location = Location.builder()
                .province(selectedDocument.getRegion1depthName())
                .district(selectedDocument.getRegion2depthName())
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return location;
    }

    private LocationApiDocument getSelectedDocument(LocationApiResponse response) {
        LocationApiDocument selectedDocument = null;
        for (LocationApiDocument document : response.getDocuments()) {
            if (document.getRegionType().equals("H")) {
                selectedDocument = document;
                break;
            } else if (selectedDocument == null) {
                selectedDocument = document;
            }
        }

        return selectedDocument;
    }

    // Nested classes for mapping the response from Kakao API
    private static class LocationApiResponse {
        private LocationApiDocument[] documents;

        public LocationApiDocument[] getDocuments() {
            return documents;
        }

        public void setDocuments(LocationApiDocument[] documents) {
            this.documents = documents;
        }
    }

    private static class LocationApiDocument {
        private String regionType;
        private String region1depthName;
        private String region2depthName;

        public String getRegionType() {
            return regionType;
        }

        public void setRegionType(String regionType) {
            this.regionType = regionType;
        }

        public String getRegion1depthName() {
            return region1depthName;
        }

        public void setRegion1depthName(String region1depthName) {
            this.region1depthName = region1depthName;
        }

        public String getRegion2depthName() {
            return region2depthName;
        }

        public void setRegion2depthName(String region2depthName) {
            this.region2depthName = region2depthName;
        }
    }
}
