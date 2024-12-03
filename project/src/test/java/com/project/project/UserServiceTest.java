package com.project.project;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.project.project.domain.model.User;
import com.project.project.domain.model.service.UserService;

public class UserServiceTest {
    private UserService userService = new UserService();

    @Test
    public void deveRetornarUsuario_QuandoCriadoComSucesso() {
        User user = new User("Ailton", "ailton@teste.com", "teste123");

        List<User> users = userService.create(user);

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    public void deveLancarExcecao_QuandoEmailDuplicado() {
        User user1 = new User("Ailton", "ailton@teste.com", "teste123");
        User user2 = new User("Marcos", "marcos@email.com", "marquinhos123");

        userService.create(user1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(user2);
        });

        assertEquals("Email já foi cadastrado!", exception.getMessage());
    }

    @Test
    public void deveLancarExcecao_QuandoCriarSenhaFraca() {
        User user = new User("Ailton", "ailton@teste.com", "short"); // Senha inválida

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(user);
        });

        assertEquals("Senha incorreta! É necessário conter no mínimo 8 caracteres e incluir pelo menos 1 dígito!", exception.getMessage());
    }

    @Test
    public void deveRetornarUsuarioLogado_QuandoFazerLoginCorretamente() {
        // Cria e adiciona um usuário
        User user = new User("Ailton", "ailton@teste.com", "Ailton123");
        userService.create(user);

        // Faz login com as credenciais corretas
        User loggedInUser = userService.login("ailton@teste.com", "Ailton123");

        // Verifica se o usuário retornado é o mesmo
        assertEquals(user, loggedInUser);
    }

    @Test
    public void deveLancarExcecao_QuandoCredenciaisInvalidas() {
        // Cria e adiciona um usuário
        User user = new User("Ailton", "ailton@teste.com", "Ailton123");
        userService.create(user);

        // Tenta login com credenciais inválidas
        try {
            userService.login("ailton@teste.com", "senha666");
        } catch (IllegalArgumentException e) {
            assertEquals("Email ou senha inválidos!", e.getMessage());
        }
    }

    @Test
    public void deveLancarExcecao_QuandoCriarUsuarioComSenhaCurta() {
        User user = new User("Ailton", "ailton@teste.com", "1234"); // Senha curta

        try {
            userService.create(user);
        } catch (IllegalArgumentException e) {
            assertEquals("Senha incorreta! É necessário conter no mínimo 8 caracteres e incluir pelo menos 1 dígito!", e.getMessage());
        }
    }

    @Test
    public void deveLancarExcecao_QuandoLogarComEmailNaoCadastrado() {
        // Tenta login com email que não foi cadastrado
        try {
            userService.login("email_fake@email.com", "Ailton123");
        } catch (IllegalArgumentException e) {
            assertEquals("Email ou senha inválidos!", e.getMessage());
        }
    }

    @Test
    public void deveLancarExcecao_QuandoCriarSenhaSemNumero() {
        User user = new User("Ailton", "ailton@teste.com", "SenhaSemNumero");

        try {
            userService.create(user);
        } catch (IllegalArgumentException e) {
            assertEquals("Senha incorreta! É necessário conter no mínimo 8 caracteres e incluir pelo menos 1 dígito!", e.getMessage());
        }
    }

    @Test
    public void deverRetornarSucesso_QuandoCriadoMultiplosUsuarios() {
        User user1 = new User("Ailton", "ailton@teste.com", "Ailton123");
        User user2 = new User("Marcos", "marcos@teste.com", "Marcos456");
        User user3 = new User("Gabriel", "gabriel@teste.com", "Gabriel789");

        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        List<User> users = userService.create(new User("nova", "nova@gmail.com", "Senha000"));

        assertEquals(4, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
        assertEquals(user3, users.get(2));
    }

    @Test
    public void deveLancarExcecao_QuandoCriadoEmailIgualComMaiuscula() {
        User user1 = new User("Ailton", "ailton@teste.com", "Ailton123");
        userService.create(user1);

        User user2 = new User("Marcos", "marcos@teste.com", "Marcos456");

        try {
            userService.create(user2);
        } catch (IllegalArgumentException e) {
            assertEquals("Email já foi cadastrado!", e.getMessage());
        }
    }
}
