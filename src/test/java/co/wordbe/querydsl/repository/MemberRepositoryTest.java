package co.wordbe.querydsl.repository;

import co.wordbe.querydsl.dto.MemberSearchCondition;
import co.wordbe.querydsl.dto.MemberTeamDto;
import co.wordbe.querydsl.entity.Member;
import co.wordbe.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired MemberRepository memberRepository;

    @BeforeEach
    public void before() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void basicTest() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1); // 스프링 데이터 JPA 기본 제공

        Member findMember = memberRepository.findById(member1.getId()).get();
        assertThat(findMember).isEqualTo(member1); // 스프링 데이터 JPA 기본 제공

        List<Member> result1 = memberRepository.findAll();
        assertThat(result1).containsExactly(member1); // 스프링 데이터 JPA 기본 제공

        List<Member> result2 = memberRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member1); // 스프링 데이터 JPA 쿼리메소드로 생성
    }

    @Test
    public void searchTest() {
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberRepository.search(condition);

        assertThat(result).extracting("username")
                .containsExactly("member4");
    }

    @Test
    public void searchPageSimpleTest() {
        MemberSearchCondition condition = new MemberSearchCondition();
        PageRequest pageRequest = PageRequest.of(0, 3);

        Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition, pageRequest);

        assertThat(result).extracting("username")
                .containsExactly("member1", "member2", "member3");
    }
}
