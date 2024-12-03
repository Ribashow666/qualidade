package com.project.project.domain.model.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.project.domain.model.User;

@Service
public class UserService {
    List<User> users = new ArrayList<>();

    public List<User> create(User user) {
        // verifica a existencia do usuario
        if (users.contains(user))
            throw new RuntimeException();

        // verifica o e-mail se tá cadastrado ou não
        if (users.stream().anyMatch(u -> u.email().equals(user.email()))) {
            throw new IllegalArgumentException("Email já foi cadastrado!");
        }

        // valida a senha do usuario
        if (!isPasswordValid(user.password())) {
            throw new IllegalArgumentException("Senha incorreta! É necessário conter no mínimo 8 caracteres e incluir pelo menos um dígito.");
        }

        users.add(user);
        return users;
    }

    // validacao da senha do usuario
    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*"); // Verifica se tem ao menos um número
    }

    public User login(String email, String password) {
        return users.stream()
                .filter(u -> u.email().equals(email) && u.password().equals(password))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha incorretas!"));
    }
    
}
