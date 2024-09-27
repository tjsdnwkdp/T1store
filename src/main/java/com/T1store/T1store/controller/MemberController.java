package com.T1store.T1store.controller;

import com.T1store.T1store.dto.MemberFormDto;
import com.T1store.T1store.entity.Member;
import com.T1store.T1store.service.MailService;
import com.T1store.T1store.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.T1store.T1store.entity.QMember.member;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    String confirm = "";

    boolean confirmCheck = false;

    @GetMapping(value = "/new") // GetMapping은 화면 보여주는 용도
    public String memberForm(Model model){
        model.addAttribute("memberFormDto",new MemberFormDto());
        model.addAttribute("hideSearchBar", true);
        return "member/memberForm";
    }
    @PostMapping(value = "/new") // 유효성 검사는 Post 에서
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                             Model model) {
        //@Valid 붙은 객체 검사 후 결과에 에러가 있으면 실행
        if(bindingResult.hasErrors()){
            return "member/memberForm"; // 다시 회원가입으로 돌려보냄
        }
        if (!confirmCheck){
            model.addAttribute("errorMessage","이메일 인증을 하세요.");
            return "member/memberForm";
        }
        try {
            // Member 객체 생성
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            // 데이터베이스에 저장
            memberService.saveMember(member);
        }
        catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(Model model){
        model.addAttribute("hideSearchBar", true);
        return "/member/memberLoginForm";
    }
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        model.addAttribute("hideSearchBar", true);
        return "/member/memberLoginForm";
    }

    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity emailConfrim(@PathVariable("email") String email)
        throws Exception{
        confirm = mailService.sendSimpleMessage(email);
        return new ResponseEntity<String>("인증 메일을 보냈습니다.", HttpStatus.OK);
    }

    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codeConfirm(@PathVariable("code")String code)
            throws Exception{
        if(confirm.equals(code)){
            confirmCheck = true;
            return new ResponseEntity<String>("인증 성공하였습니다.",HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증 코드를 올바르게 입력해주세요.",HttpStatus.BAD_REQUEST);
    }
}
