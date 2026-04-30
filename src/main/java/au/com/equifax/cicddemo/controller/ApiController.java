package au.com.equifax.cicddemo.controller;

import au.com.equifax.cicddemo.domain.EnvDetail;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class ApiController {

    @GetMapping("/")
    public String home() {
        return "Hola desde CI/CD Demo v2 - Pipeline funcionando!";
    }
}