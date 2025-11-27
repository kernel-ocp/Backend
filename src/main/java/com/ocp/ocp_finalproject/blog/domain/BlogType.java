package com.ocp.ocp_finalproject.blog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "blog_type")
public class BlogType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_type_id")
    private Long id;

    @Column(name = "blog_type_name")
    private String name;

    @Column(name = "blog_base_url")
    private String baseUrl;

    @Builder
    private BlogType(Long id, String name, String baseUrl) {
        this.id = id;
        this.name = name;
        this.baseUrl = baseUrl;
    }
}
