//package spring_client.semestral.ui;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.HttpClientErrorException;
//import spring_client.semestral.data_format.UserDto;
//import spring_client.semestral.service.UserService;
//
//@Controller
//@RequestMapping("/users")
//public class UsersController_client {
//    private UserService userService;
//
//    public UsersController_client(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping
//    public String list(Model model) {
//        var allUsers = userService.readAll();
//        model.addAttribute("listOfUsers", allUsers);
//        return "users";
//    }
//
//    @GetMapping("/edit")
//    public String showForm(@RequestParam String id, Model model) {
//        userService.setCurrentUser(id);
//        var user = userService.readOne();
//        model.addAttribute("user", user);
//        return "editUser";
//    }
//
//    @PostMapping("/edit")
//    public String submitForm(Model model, @ModelAttribute UserDto userDto) {
//        try {
//            userService.updateCurrentUser(userDto);
//        } catch (HttpClientErrorException.MethodNotAllowed e) {
//            model.addAttribute("error_snh", true);
//        }
//        return list(model);
//    }
//}
