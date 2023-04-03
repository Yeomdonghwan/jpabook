package jpabook.jpashop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberRepoistoryTest {

    @Autowired MemberRepoistory memberRepoistory;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("member1");
        //when
        Long saveId = memberRepoistory.save(member);
        Member findmember = memberRepoistory.find(saveId);

        //then
        Assertions.assertEquals(findmember.getId(),member.getId());
        Assertions.assertEquals(findmember.getUsername(),member.getUsername());
        Assertions.assertEquals(findmember,member);//같은 영속성 안에서는 id가 같으면 같은 엔티티로 인식함
    }

}