package com.modiejun.cloudfiles.Security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class SecurityController {
    @GetMapping("/login")
    public String loginPage(Principal principal, RedirectAttributes redirectAttributes) {
//        redirect
        if (principal!=null ){
            redirectAttributes.addFlashAttribute("message","You cannot go to login");
            return "redirect:/";
        }
        return "login";
    }
}
