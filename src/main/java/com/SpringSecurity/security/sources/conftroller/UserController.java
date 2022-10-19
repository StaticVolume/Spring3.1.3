package com.SpringSecurity.security.sources.conftroller;

import com.SpringSecurity.security.sources.EnumRoles.EnumRoles;
import com.SpringSecurity.security.sources.model.Role;
import com.SpringSecurity.security.sources.model.User;
import com.SpringSecurity.security.sources.service.RoleService;
import com.SpringSecurity.security.sources.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;



@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    private final EnumRoles enumRoles;

    public UserController(UserService userService, RoleService roleService, EnumRoles enumRoles) {
        this.userService = userService;
        this.roleService = roleService;
        this.enumRoles = enumRoles;
    }


    @GetMapping("/")
    public String printStartPage(Principal principal, Model model) {
        User user = userService.getUserByName(principal.getName());
        for (Role role : user.getRoles()) {
            if (role.getName().contains("ADMIN")) {
                return "redirect:/admin";
            }
        }
        return "redirect:/user";
    }
    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {
        User user = userService.getUserByName(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage(Principal principal, Model model) {
        User admin = userService.getUserByName(principal.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("users_list", userService.getAllUsers());
        model.addAttribute("roles_from_service", enumRoles.getSetRoles());
        model.addAttribute("emptyUser",new User());
        return "admin";
    }

    @PostMapping("admin/createUser")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("admin/edit")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("admin/delete")
    public String deleteUser(@ModelAttribute("DeleteUser") User user) {
        userService.deleteUserById(user.getId());
        return "redirect:/admin";
    }
}
