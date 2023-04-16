package kr.api.lenders.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.api.lenders.domain.Location;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.error.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class LocationUtil {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    public Location getLocationFromCoordinates(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        URI uri = UriComponentsBuilder
                .fromUriString(KAKAO_API_URL)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .build()
                .toUri();

        LocationApiResponse response;
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, LocationApiResponse.class).getBody();
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("Failed to retrieve location information from Kakao API.");
        }

        if (response == null || response.getDocuments() == null || response.getDocuments().length == 0) {
            throw new NotFoundException("Location not found for given coordinates.");
        }

        LocationApiDocument selectedDocument = getSelectedDocument(response);
        if (selectedDocument == null ||
                ObjectUtils.isEmpty(selectedDocument.getRegionType()) ||
                ObjectUtils.isEmpty(selectedDocument.getRegion1depthName()) ||
                ObjectUtils.isEmpty(selectedDocument.getRegion2depthName())) {
            throw new NotFoundException("Location not found for given coordinates.");
        }

        return Location.builder()
                .province(selectedDocument.getRegion1depthName())
                .district(selectedDocument.getRegion2depthName())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    private LocationApiDocument getSelectedDocument(LocationApiResponse response) {
        LocationApiDocument selectedDocument = null;
        for (LocationApiDocument document : response.getDocuments()) {
            if ("H".equals(document.getRegionType())) {
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
        @JsonProperty("region_type") // field mapping for region_type
        private String regionType;

        @JsonProperty("region_1depth_name")
        private String region1depthName;

        @JsonProperty("region_2depth_name")
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
