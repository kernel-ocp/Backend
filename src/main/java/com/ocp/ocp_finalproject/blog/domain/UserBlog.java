package com.ocp.ocp_finalproject.blog.domain;

import com.ocp.ocp_finalproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_blog")
public class UserBlog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_type_id")
    private BlogType blogType;

    private String accountId;

    private String accountPassword;

    private String blogUrl;

    @Builder
    private UserBlog(Long id, BlogType blogType, String accountId, String accountPassword, String blogUrl) {
        this.id = id;
        this.blogType = blogType;
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.blogUrl = blogUrl;
    }

    public void updateCredentials(String accountId, String accountPassword) {
        this.accountId = accountId;
        this.accountPassword = accountPassword;
    }

    public void updateBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }
}
