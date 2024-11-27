package com.iuh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.iuh.dto.LoginRequest;
import com.iuh.dto.LoginResponse;
import com.iuh.util.ConnectionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ViewController {

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@PostMapping("/login")
	public ModelAndView handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session,
			Model model) {
		ModelAndView modelAndView = new ModelAndView();
		// Gửi yêu cầu đến API REST
		RestTemplate restTemplate = new RestTemplate();
//		String url = "http://localhost:8080/bookstore";

		try {
			LoginRequest loginRequest = new LoginRequest(username, password);
			ResponseEntity<LoginResponse> response = restTemplate.postForEntity(ConnectionUtil.URL + "/auth/login", loginRequest, LoginResponse.class);
//			log.info("JWT: {}", response.getBody());

			if (response.getStatusCode().is2xxSuccessful()&& ConnectionUtil.isAdmin(response.getBody().getData().getToken())) {

				// Lưu JWT vào session
				session.setAttribute("jwt", response.getBody().getData().getToken());
				modelAndView.setViewName("redirect:/dashboard");
				return modelAndView;
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Invalid username or password");
			return modelAndView;
		}

		modelAndView.setViewName("login");
		return modelAndView;
	}
// Save to commit
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
		String jwt = (String) session.getAttribute("jwt");
		log.info(jwt);
		if (jwt == null) {
			return "redirect:/login";
		}

//		// Gửi JWT đến API để lấy dữ liệu dashboard
//		RestTemplate restTemplate = new RestTemplate();
//		String url = "http://localhost:8080/api/dashboard";
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Authorization", "Bearer " + jwt);
//		HttpEntity<Void> request = new HttpEntity<>(headers);
		model.addAttribute("activePage", "home");
		try {
//            ResponseEntity<String> response = restTemplate.exchange(
//                    url, HttpMethod.GET, request, String.class);
//            model.addAttribute("data", response.getBody());
		} catch (Exception e) {
			session.invalidate();
			return "redirect:/login";
		}

		return "dashboard";
	}
}
