package com.example.config.email;

import com.example.application.backend.entity.User;
import com.example.application.backend.repository.UserRepository;
import com.example.application.backend.service.RegalitosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfirmationController {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  RegalitosService regalitosService;

    @GetMapping("/confirm")
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        User user = userRepository.findByConfirmationToken(confirmationToken);

        if (user != null) {
            user.setEnabled(true);
            user.setConfirmationToken(null);
            userRepository.save(user);
            return "Cuenta confirmada con éxito";
        } else {
            return "El enlace de confirmación es inválido o ha expirado.";
        }
    }
}
