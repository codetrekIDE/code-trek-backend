package webide.codeeditor.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webide.codeeditor.member.domain.UserRole;
import webide.codeeditor.member.domain.dto.JoinRequest;
import webide.codeeditor.member.domain.dto.LoginRequest;
import webide.codeeditor.member.domain.entity.User;
import webide.codeeditor.member.service.UserService;


@RestController  // @Controller -> @RestController로 변경
@RequiredArgsConstructor
@RequestMapping("/auth/")  // JSON 방식의 API 경로로 변경
public class CookieLoginRestController {

    private final UserService userService;

    // 홈 화면 (유저 상태 확인)
    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> home(@CookieValue(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);
        if (loginUser == null) {
            return ResponseEntity.status(401).body("Unauthorized: User not logged in.");
        }
        // 로그인한 사용자의 정보를 JSON으로 반환
        return ResponseEntity.ok("Welcome, " + loginUser.getLoginId() + "!");
    }

    // 회원 가입 처리
    @PostMapping("/signup")
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequest joinRequest) {
        // 중복 체크
        if (userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            return ResponseEntity.badRequest().body("Error: Login ID already in use.");
        }
        // 비밀번호 일치 확인
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return ResponseEntity.badRequest().body("Error: Passwords do not match.");
        }
        // 회원 가입 처리
        userService.join(joinRequest);
        return ResponseEntity.ok("User successfully registered.");
    }

    // 로그인 처리
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User user = userService.login(loginRequest);
        if (user == null) {
            return ResponseEntity.status(401).body("Error: Invalid login credentials.");
        }
//        // 로그인 성공 시 쿠키 설정
//        Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
//        cookie.setMaxAge(60 * 60);  // 쿠키 유효 시간 1시간
//        response.addCookie(cookie);

        // 로그인 성공 시 쿠키 설정
        Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
        cookie.setMaxAge(60 * 60);  // 쿠키 유효 시간 1시간
        cookie.setHttpOnly(true);   // JavaScript로 접근 불가
        cookie.setSecure(false);    // HTTPS 사용 시 true로 설정
        cookie.setPath("/");        // 모든 경로에서 쿠키 접근 가능
//        cookie.setSameSite("Lax");  // SameSite 설정 (필요에 따라 'Strict' 또는 'None'으로 변경)
        response.addCookie(cookie);
        return ResponseEntity.ok("Login successful.");
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 쿠키 무효화
        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully.");
    }

    // 유저 정보 확인
    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@CookieValue(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);
        if (loginUser == null) {
            return ResponseEntity.status(401).body("Error: Unauthorized access.");
        }
        return ResponseEntity.ok(loginUser);
    }

    // 관리자 페이지 접근
    @GetMapping("/admin")
    public ResponseEntity<?> adminPage(@CookieValue(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);
//        if (loginUser == null || !loginUser.getRole().equals(UserRole.ADMIN)) {
//            return ResponseEntity.status(403).body("Error: Access denied.");
//        }
        return ResponseEntity.ok("Welcome to the admin page.");
    }

    // 새로운 삭제 API 추가
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        // 유효한 사용자 ID인지 확인
        User user = userService.getLoginUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // 회원 정보 삭제
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    }

