package com.spring.reserve;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

  @Id
  @Column(name = "TEAM_ID")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @OneToMany(
          mappedBy = "team",
          cascade = {CascadeType.REMOVE, CascadeType.PERSIST}
  )
  private List<Member> members = new ArrayList<>();

  // custructor

}
