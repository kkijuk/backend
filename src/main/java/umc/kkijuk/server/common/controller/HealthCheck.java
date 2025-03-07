package umc.kkijuk.server.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Slf4j
@Controller
public class HealthCheck {

    @Value("${commit.hash}")
    public String commitHash;

    @ResponseBody
    @GetMapping("/health-check")
    public String healthCheck() {
        return commitHash;
    }

    @ResponseBody
    @GetMapping("/health-check/exception")
    public String exception() {
        log.info("Exception Occured");
        throw new RuntimeException("Exception for test");
    }
}
