package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor //final 멤버만으로 구성된 생성자를 만들어줌
@Service
@Transactional() //스프링 제공 transactional을 쓰는것이 옵션이 많음
public class MemberService {


    private final MemberRepository memberRepository; //final로 설정하면 초기화되지않았을 때 오류 발생
//
//    @Autowired //생성자가 하나만 있으면 autowired 어노테이션 부착하지 않아도 자동으로 주입해줌
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository=memberRepository;
//    }


    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty())
            throw new IllegalStateException("이미 존재하는 회원입니다.");
    }

    //회원 전체 조회
    @Transactional(readOnly = true) //읽기전용 트랜잭션으로 설정하면 최적화됨
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //한 건의 회원 조회
    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
