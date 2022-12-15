package edu.sjsu.cmpe275.nfttradingmarket.controller;

import edu.sjsu.cmpe275.nfttradingmarket.dto.request.UserDetailsUpdateDTO;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.MessageResponse;
import edu.sjsu.cmpe275.nfttradingmarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/updateNickname")
    public ResponseEntity<MessageResponse> updateNickname(@RequestBody UserDetailsUpdateDTO userDetailsUpdateDTO) {
        return userService.updateNickname(userDetailsUpdateDTO);
    }

    @PostMapping(path = "/updatePassword")
    public ResponseEntity<MessageResponse> updatePassword(@RequestBody UserDetailsUpdateDTO userDetailsUpdateDTO) {
        return userService.updatePassword(userDetailsUpdateDTO);
    }

}
