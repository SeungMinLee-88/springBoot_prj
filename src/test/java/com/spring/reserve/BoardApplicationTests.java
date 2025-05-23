package com.spring.reserve;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

/*@SpringBootTest*/
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BoardApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	public void initTest() {
		Team team = new Team();
		team.setId(0L);
		team.setName("팀1");
		entityManager.persist(team);

		Member member1 = new Member();
		member1.setId(0L);
		member1.setUsername("회원1");
		Member member2 = new Member();
		member2.setId(1L);
		member2.setUsername("회원2");

		// 연관관계의 주인에 값 설정
		member1.setTeam(team);
		member2.setTeam(team);

		// CascadeType.PERSIST 로 인하여 영속성 전이
//        entityManager.persist(member1);
//        entityManager.persist(member2);

		// 영속성 컨텍스트의 변경 내용을 DB에 반영
		entityManager.flush();
	}
	@DisplayName("부모 엔티티(Team)을 삭제하는 경우")
	@Test
	public void contextLoads() {
		System.out.println("call contextLoads");
		// when
		Team team = entityManager.find(Team.class, 0L);
		entityManager.remove(team); // 부모 엔티티 삭제

		entityManager.flush();

		// then
		List<Team> teamList = entityManager.createQuery("select t from Team t", Team.class).getResultList();
		Assertions.assertEquals(0, teamList.size());

		List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
		Assertions.assertEquals(0, memberList.size());
	}

	@DisplayName("고아객체 - 부모 엔티티(Team)에서 자식 엔티티(Member)와 연관관계를 끊는 경우")
	@Test
	public void cascadeType_REMOVE_Persistence_Remove() {
		System.out.println("cascadeType_REMOVE_Persistence_Remove");
		// when
		Team team = entityManager.find(Team.class, 0L);
		team.getMembers().get(0).setTeam(null);

		entityManager.flush();

		// then
		List<Team> teamList = entityManager.createQuery("select t from Team t", Team.class).getResultList();
		Assertions.assertEquals(1, teamList.size());

		List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
		Assertions.assertEquals(2, memberList.size());
	}

	@DisplayName("자식 엔티티의 연관관계 변경 시")
	@Test
	public void change_persistence_child() {
		System.out.println("change_persistence_child");
		// given
		Team team = new Team();
		team.setId(0L);
		team.setName("팀1");
		entityManager.persist(team);

		// when
		Member member1 = entityManager.find(Member.class, 0L);
		member1.setTeam(team); // UPDATE 쿼리 수행
		entityManager.flush();

		// then
		Team team1 = entityManager.createQuery("select t from Team t where t.id = 0", Team.class).getSingleResult();
		Assertions.assertEquals(1L, team1.getMembers().get(0).getId());

		Team team2 = entityManager.createQuery("select t from Team t where t.id = 1", Team.class).getSingleResult();
		Assertions.assertEquals(0L, team2.getMembers().get(0).getId());

		List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
		Assertions.assertEquals(2, memberList.size());
	}

}
