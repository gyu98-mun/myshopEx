package com.example.myShop.service;


import com.example.myShop.entity.Member;
import com.example.myShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional // 로직 처리하다 에러가 발생하면, 변경된 게이터 로직 수행 전 상태로 콜백
@RequiredArgsConstructor // final이나 @NonNull 이 붙은 필드에 생성자 생성.
// 빈에 생성자가 1개이고, 생성자의 파라미터 타입이 빈으로 등록가능하다면 @Autowired 없이 의존성 주입 가능.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return  memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email);
        if(member == null){
            throw  new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
