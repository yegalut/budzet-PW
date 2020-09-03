package com.projekt_pai.budzet.controllers;

import com.projekt_pai.budzet.Additional.LoginCredentials;
import com.projekt_pai.budzet.entities.User;
import com.projekt_pai.budzet.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Controller
public class LoginController {

    private final UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String email) {
        User user = userRepository.findByEmail(email);
        if(user!=null)
            return user;
        else
            return new User();
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("login_credentials", new LoginCredentials());
        model.addAttribute("bad_credentials", error);
        return "login";
    }

    @PostMapping("/login_user")
    public String submitLogin(@ModelAttribute LoginCredentials user,
                              Model model,
                              RedirectAttributes re,
                              HttpServletResponse response) {

        if((user.getLogin() !=null)&&(!user.getLogin().isEmpty()) && (user.getPassword() !=null) && (!user.getPassword().isEmpty()) ) {
            User validateAgainst = getUser(user.getLogin());

            if (user.getLogin().equals(validateAgainst.getEmail())) {
                if (user.getPassword().equals(validateAgainst.getPassword())) {
                    System.out.println(user.getLogin() + " logged in with password " + user.getPassword());

                    Cookie cookie = new Cookie("username", user.getLogin());
                    cookie.setPath("/");//global
                    response.addCookie(cookie);
                    re.addAttribute("action","finances");
                    return "redirect:/user_page";
                }

            }
        }
        re.addAttribute("error", "yes");
        return "redirect:/login";


    }
}
