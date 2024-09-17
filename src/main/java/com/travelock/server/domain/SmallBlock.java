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
public class SmallBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smallBlockId;

    @OneToMany(mappedBy = "smallBlock", fetch = FetchType.LAZY)
    private List<SmallBlockReview> smallBlockReviews;
}
