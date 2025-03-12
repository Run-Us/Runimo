package org.runimo.runimo.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  private String publicId;
  private String nickname;
  private String imgUrl;

  @Builder
  public User(String nickname, String imgUrl) {
    this.nickname = nickname;
    this.imgUrl = imgUrl;
  }

  @PrePersist
  public void prePersist() {
    this.publicId = UUID.randomUUID().toString();
  }
}