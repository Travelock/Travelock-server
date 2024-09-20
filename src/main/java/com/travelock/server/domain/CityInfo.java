package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CityInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityInfoId;

    @OneToMany(mappedBy = "cityInfo")
    List<BigBlock> bigBlockList;

}
