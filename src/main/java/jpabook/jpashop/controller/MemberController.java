package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result){
        //@Valid 를 붙이면 @NotEmpty붙여놨던 필드를 검증함

        if(result.hasErrors()){
            return "members/createMemberForm"; //Valid 후에 에러발생시 처리
        }
        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        //엔티티와 폼데이터가 다르다면 변환해야 하지만 여기서는 엔티티를 전달해도 괜찮아서 그대로 등록
        //API를 만들때는 절대로 엔티티를 반환하지 말 것. 데이터 노출이 되고 API의 스펙이 변경되기 때문이다.
        //하지만 템플릿엔진으로 넘기는건.. 크게상관 없을듯
        model.addAttribute("members",members);

        return "members/memberList";
    }
}
