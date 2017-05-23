/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller.admin;

import by.nc.lomako.dto.user.UserForCreateDto;
import by.nc.lomako.dto.user.UserForUpdateDto;
import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.exceptions.UniqueEmailException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.pojos.RoleType;
import by.nc.lomako.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Lomako
 * @version 1.0
 */
@Controller("Aa")
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    private final UserForCreateDto.DtoValidator userForCreateDtoValidator;

    private final UserForUpdateDto.DtoValidator userForUpdateDtoValidator;

    @Autowired
    public AdminUserController(UserService userService, UserForCreateDto.DtoValidator userForCreateDtoValidator,
                               UserForUpdateDto.DtoValidator userForUpdateDtoValidator) {
        this.userService = userService;
        this.userForCreateDtoValidator = userForCreateDtoValidator;
        this.userForUpdateDtoValidator = userForUpdateDtoValidator;
    }

    @RequestMapping("/index")
    public String index(
            @RequestParam(value = "role", required = false) String roleString,
            @RequestParam(value = "start", required = false) String startString,
            @RequestParam(value = "limit", required = false) String limitString,
            Model model
    ) {

        List<UserInfoDto> userInfoDtos;
        int start = 0;
        int limit = 0;

        if (startString != null && limitString != null) {
            start = Integer.parseInt(startString);
            limit = Integer.parseInt(limitString);

            userInfoDtos = userService.findAll(start, limit);

        } else if (roleString != null) {
            RoleType role = RoleType.valueOf(roleString);
            userInfoDtos = userService.findByRole(role, start, limit);

        } else {
            userInfoDtos = userService.findAll();
        }

        model.addAttribute("userDtos", userInfoDtos);
        return "admin/user/index";
    }

    @RequestMapping("/{id}")
    public String show(
            @PathVariable(value = "id") String idString,
            Model model
    ) throws UserNotFoundException {

        long id = Long.valueOf(idString);
        UserInfoDto userInfoDto = userService.findById(id);

        model.addAttribute("userDto", userInfoDto);
        return "admin/user/show";
    }

    @RequestMapping("edit/{id}")
    public String edit(
            @PathVariable(value = "id") String idString,
            Model model
    ) throws UserNotFoundException {

        long id = Long.valueOf(idString);
        UserInfoDto userInfoDto = userService.findById(id);

        UserForUpdateDto userForUpdateDto = new UserForUpdateDto();
        userForUpdateDto.setEmail(userInfoDto.getEmail());
        userForUpdateDto.setFirstName(userInfoDto.getFirstName());
        userForUpdateDto.setLastName(userInfoDto.getLastName());

        model.addAttribute("userDto", userForUpdateDto);

        return "admin/user/edit";
    }

    @RequestMapping(value = "/new", method = GET)
    public String newUser(Model model) {
        model.addAttribute("userDto", new UserForCreateDto());

        return "admin/user/new";
    }

    @RequestMapping(value = "/update/{id}", method = POST)
    public String update(
            @PathVariable(value = "id") String idString,
            @ModelAttribute("userForm") UserForUpdateDto userForm,
            BindingResult bindingResult
    )
        throws UserNotFoundException {

        long id = Long.parseLong(idString);

        try {
            userForUpdateDtoValidator.validate(userForm, bindingResult);
            if(bindingResult.hasErrors()) {
                return "redirect:/admin/user/edit/" + id + "?error=user_not_valid";
            }

            userService.update(id, userForm);
            return "redirect:/admin/user/" + id;

        } catch (UniqueEmailException e) {
            return "redirect:/admin/user/edit/" + id + "?error=binding_email";
        }
    }

    @RequestMapping(value = "/create", method = POST)
    public String create(
            @ModelAttribute("userForm") UserForCreateDto userForm,
            BindingResult bindingResult
    ) {
        try {
            userForCreateDtoValidator.validate(userForm, bindingResult);

            if (bindingResult.hasErrors()) {
                return "redirect:/admin/user/new?error=user_not_valide";
            }

            long id = userService.create(userForm);
            return "redirect:/admin/user/" + id;

        } catch (UniqueEmailException e) {
            return "redirect:/admin/user/new?error=binding_email";
        }

    }


}
