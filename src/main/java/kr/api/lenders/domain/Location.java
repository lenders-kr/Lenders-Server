package kr.api.lenders.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "locations",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"latitude", "longitude"})}
)
@EntityListeners(AuditingEntityListener.class)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String province;

    @NotNull
    private String district;

    @NotNull
    private double latitude; // y coordinate

    @NotNull
    private double longitude; // x coordinate

    @Builder
    public Location(String province, String district, double latitude, double longitude) {
        this.province = province;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
