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
        telaCadastro.getBotaoVoltar().addActionListener(e -> {
            telaCadastro.getUserInput().setText("");
            telaCadastro.getIncomeInput().setText("");
            telaCadastro.getPasswordInput().setText("");
            telaCadastro.getConfirmPasswordInput().setText("");
            mainFrame.mostrarTela("login");
        });

        telaCadastro.getRegisterButton().addActionListener(e -> validarCadastro());
    }

    private void validarCadastro() {
        String nome = telaCadastro.getUserInput().getText().trim().toLowerCase();
        String salarioString = telaCadastro.getIncomeInput().getText().trim();
        String senha = telaCadastro.getPasswordInput().getText().trim();
        String confirmarSenha = telaCadastro.getConfirmPasswordInput().getText().trim();

        if (nome.isEmpty() || salarioString.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "Preencha todos os campos obrigatórios.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (gerenciadorUsuario.buscarPorNome(nome) != null) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "Este nome de usuário já está em uso.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (senha.length() < 6) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "A senha deve ter ao menos 6 caracteres.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "A senha e a confirmação não coincidem.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

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

        String senhaCriptografada = CriptografarSenha.criptografarSenha(senha);

        try {
            Usuario novoUsuario = gerenciadorUsuario.adicionarUsuario(nome, senhaCriptografada, salarioDouble);

            telaCadastro.getUserInput().setText("");
            telaCadastro.getIncomeInput().setText("");
            telaCadastro.getPasswordInput().setText("");
            telaCadastro.getConfirmPasswordInput().setText("");

            mainFrame.mostrarTela("login");
            JOptionPane.showMessageDialog(
                mainFrame,
                "Sua conta foi criada com sucesso!",
                "Cadastro realizado",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            DebugDatabasePrinter.imprimirTodasTabelas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                telaCadastro,
                "Erro ao cadastrar usuário: " + ex.getMessage(),
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
