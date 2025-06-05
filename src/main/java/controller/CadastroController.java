package controller;

import java.awt.event.*;
import javax.swing.*;
import view.*;
import model.entity.Usuario;
import model.service.GerenciadorUsuario;
import model.util.DebugDatabasePrinter;
import util.CriptografarSenha;

public class CadastroController {
    private MainFrame mainFrame;
    private TelaCadastro telaCadastro;
    private GerenciadorUsuario gerenciadorUsuario;

    public CadastroController(MainFrame mainFrame, TelaCadastro telaCadastro, GerenciadorUsuario gerenciadorUsuario) {
        this.mainFrame = mainFrame;
        this.telaCadastro = telaCadastro;
        this.gerenciadorUsuario = gerenciadorUsuario;
        initController();
    }

    private void initController() {
        // Listener para o botão “Voltar”
        telaCadastro.getBotaoVoltar().addActionListener(e -> {
            // Limpa campos ao voltar
            telaCadastro.getUserInput().setText("");
            telaCadastro.getIncomeInput().setText("");
            telaCadastro.getPasswordInput().setText("");
            telaCadastro.getConfirmPasswordInput().setText("");
            mainFrame.mostrarTela("login");
        });

        // Listener para o botão “Registrar”
        telaCadastro.getRegisterButton().addActionListener(e -> validarCadastro());
    }

    private void validarCadastro() {
        String nome = telaCadastro.getUserInput().getText().trim().toLowerCase();
        String salarioString = telaCadastro.getIncomeInput().getText().trim();
        String senha = telaCadastro.getPasswordInput().getText().trim();
        String confirmarSenha = telaCadastro.getConfirmPasswordInput().getText().trim();

        // 1) Verifica se todos os campos foram preenchidos
        if (nome.isEmpty() || salarioString.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "Preencha todos os campos obrigatórios.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 2) Verifica se usuário já existe
        if (gerenciadorUsuario.buscarPorNome(nome) != null) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "Este nome de usuário já está em uso.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 3) Verifica tamanho mínimo da senha (por exemplo, 6 caracteres)
        if (senha.length() < 6) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "A senha deve ter ao menos 6 caracteres.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 4) Verifica se senha e confirmar senha coincidem
        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "A senha e a confirmação não coincidem.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 5) Converte salário para double (tratando possível NumberFormatException)
        double salarioDouble;
        try {
            salarioDouble = Double.parseDouble(salarioString.replace(",", "."));
            if (salarioDouble < 0) {
                JOptionPane.showMessageDialog(
                    telaCadastro,
                    "O salário não pode ser negativo.",
                    "Erro ao cadastrar",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "Informe um valor válido para o salário.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 6) Criptografa a senha
        String senhaCriptografada = CriptografarSenha.criptografarSenha(senha);

        // 7) Tenta persistir o usuário no banco
        try {
            Usuario novoUsuario = gerenciadorUsuario.adicionarUsuario(nome, senhaCriptografada, salarioDouble);

            // 8) Se chegou aqui, foi cadastrado com sucesso.
            // Limpa todos os campos da tela de cadastro
            telaCadastro.getUserInput().setText("");
            telaCadastro.getIncomeInput().setText("");
            telaCadastro.getPasswordInput().setText("");
            telaCadastro.getConfirmPasswordInput().setText("");

            // Retorna para a tela de login
            mainFrame.mostrarTela("login");
            JOptionPane.showMessageDialog(
                mainFrame,
                "Sua conta foi criada com sucesso!",
                "Cadastro realizado",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            DebugDatabasePrinter.imprimirTodasTabelas();
        } catch (Exception ex) {
            // Caso o DAO ou alguma restrição dispare erro, exibimos
            JOptionPane.showMessageDialog(
                telaCadastro,
                "Erro ao cadastrar usuário: " + ex.getMessage(),
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
