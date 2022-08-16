package com.example.produtos.api.users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Api(value = "Produtos")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping()
    @ApiOperation(value = "Retorna todos usuários cadastrados.")
    public ResponseEntity<List<UserDTO>> get() {
        List<UserDTO> list = service.getUsers();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/info")
    @ApiOperation(value = "Retorna os detalhes do usuário logado.")
    public UserDTO userInfo(@AuthenticationPrincipal User user) { //a anotação retorna o user logado

        //User userLoged = (User) JwtUtil.getUserDetails(); //outra forma de retornar o user logado (nesse projeto)

        return UserDTO.create(user);
    }
}