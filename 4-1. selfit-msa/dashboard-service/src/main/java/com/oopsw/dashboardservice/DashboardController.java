package com.oopsw.dashboardservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/dashboard-service")
public class DashboardController {

	@GetMapping("/api-test")
	public String test() {
		return "test";
	}
}
