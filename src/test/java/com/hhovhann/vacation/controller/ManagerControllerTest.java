package com.hhovhann.vacation.controller;

import com.hhovhann.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

class ManagerControllerTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;

    private static final String BASE_URL = "v1/api/manager";

    @Autowired
    private TestRestTemplate restTemplate;

}