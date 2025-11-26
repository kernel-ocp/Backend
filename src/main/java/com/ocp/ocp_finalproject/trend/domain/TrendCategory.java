package com.ocp.ocp_finalproject.trend.domain;

import com.ocp.ocp_finalproject.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "trend_category")
public class TrendCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private TrendCategory parentCategory;

    private String name;

    private Integer depth;

    @Builder
    private TrendCategory(TrendCategory parentCategory, String name, Integer depth) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.depth = depth;
    }

    public void updateInfo(String name, Integer depth, TrendCategory parentCategory) {
        this.name = name;
        this.depth = depth;
        this.parentCategory = parentCategory;
    }
}
