package com.geneostr.ibm.codech.passwordvalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@RestController
public class PasswordValidator {

    private static int MIN_LETTERS = 4;
    private static int MIN_DIGITS = 4;


    public static void main(String[] args) {
        SpringApplication.run(PasswordValidator.class, args);
    }

    @RequestMapping("/validate/pwd")
    public String validatePassword(@RequestParam String password) {
        int letters = 0;
        int digits = 0;
        for(char ch: password.toCharArray()){
            if(Character.isLetter(ch)) {
                letters++;
            }else if(Character.isDigit(ch)){
                digits++;
            }
        }
        return (letters>=4 && digits >=2)?"VALID": "INVALID";
    }
}
