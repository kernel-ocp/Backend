package com.ocp.ocp_finalproject.trend.domain;

import com.ocp.ocp_finalproject.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "set_trend_category")
public class SetTrendCategory extends BaseEntity {

    @Id
    private Long trendKeywordId;

    private Integer depth1Category;

    private Integer depth2Category;

    private Integer depth3Category;

    @Builder
    private SetTrendCategory(Long trendKeywordId, Integer depth1Category, Integer depth2Category, Integer depth3Category) {
        this.trendKeywordId = trendKeywordId;
        this.depth1Category = depth1Category;
        this.depth2Category = depth2Category;
        this.depth3Category = depth3Category;
    }

    public void updateDepths(Integer depth1Category, Integer depth2Category, Integer depth3Category) {
        this.depth1Category = depth1Category;
        this.depth2Category = depth2Category;
        this.depth3Category = depth3Category;
    }
}
