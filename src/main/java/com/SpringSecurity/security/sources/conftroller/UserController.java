package com.SpringSecurity.security.sources.conftroller;

import com.SpringSecurity.security.sources.EnumRoles.EnumRoles;
import com.SpringSecurity.security.sources.model.Role;
import com.SpringSecurity.security.sources.model.User;
import com.SpringSecurity.security.sources.service.RoleService;
import com.SpringSecurity.security.sources.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        return "admin";
    }

    @GetMapping("admin/allUsers")
    public String printAllUsers(Model model) {
        model.addAttribute("users_list", userService.getAllUsers());
        return "/allUsers";
    }

    @RequestMapping("admin/createUser")
    public String createUserForm(Model model) {
        model.addAttribute("roles_from_service", enumRoles.getSetRoles());
        model.addAttribute("user",new User());
        return "/createUser";
    }

    @PostMapping("admin/createUser")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "createUser";
        }
        userService.addUser(user);
        return "redirect:/admin/allUsers";
    }

    @GetMapping("admin/allUsers/{id}/edit")
    public String editUserForm(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("role_from_service", enumRoles.getSetRoles());
        return "/editUser";
    }

    @PatchMapping("/admin/allUsers/{id}")
    public String updateUser(@ModelAttribute("user") User user,@PathVariable("id") long id ) {
        userService.updateUser(user);
        return "redirect:/admin/allUsers";
    }

    @DeleteMapping("admin/allUsers/delete/{id}")
    public String deleteUser( @PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/allUsers";
    }
}
