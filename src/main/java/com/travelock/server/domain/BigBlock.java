package com.travelock.server.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Entity
@EnableJpaAuditing
public class BigBlock extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // cityName과 areacode 저장
    private String blockName;

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String cityName) {
        this.blockName = cityName;
    }


    private String areacode;

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    @ManyToOne
    @JoinColumn(name = "city_info_id")
    private CityInfo cityInfo;



}
