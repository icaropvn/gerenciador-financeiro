package model.util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.entity.Categoria;
import model.entity.Transacao;
import model.entity.Usuario;

public class DebugDatabasePrinter {

    public static void imprimirTodasTabelas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // ===================== TABELA USUARIO =====================
            System.out.println("===== INÍCIO TABELA USUARIO =====");
            List<Usuario> usuarios = session
                .createQuery("from Usuario", Usuario.class)
                .list();
            if (usuarios.isEmpty()) {
                System.out.println("  (nenhum registro encontrado)");
            } else {
                for (Usuario u : usuarios) {
                    System.out.printf(
                        "  ID: %d | Nome: %s | Saldo: R$ %.2f | Senha (hash): %s%n",
                        u.getId(),
                        u.getNome(),
                        u.getSaldo(),
                        u.getSenha()
                    );
                }
            }
            System.out.println("===== FIM TABELA USUARIO =====\n");

            // ===================== TABELA CATEGORIA =====================
            System.out.println("===== INÍCIO TABELA CATEGORIA =====");
            List<Categoria> categorias = session
                .createQuery("from Categoria", Categoria.class)
                .list();
            if (categorias.isEmpty()) {
                System.out.println("  (nenhum registro encontrado)");
            } else {
                for (Categoria c : categorias) {
                    System.out.printf(
                        "  Código: %d | Descrição: %s%n",
                        c.getId(),
                        c.getDescricao()
                    );
                }
            }
            System.out.println("===== FIM TABELA CATEGORIA =====\n");

            // ===================== TABELA TRANSACAO =====================
            System.out.println("===== INÍCIO TABELA TRANSACAO =====");
            List<Transacao> transacoes = session
                .createQuery(
                    "select t from Transacao t "
                  + "join fetch t.usuario u "
                  + "join fetch t.categoria c",
                    Transacao.class
                )
                .list();
            if (transacoes.isEmpty()) {
                System.out.println("  (nenhum registro encontrado)");
            } else {
                for (Transacao t : transacoes) {
                    System.out.printf(
                        "  ID: %d | Usuário: %s | Categoria: %s | Classificação: %s | Valor: R$ %.2f | Data: %s%n",
                        t.getId(),
                        t.getUsuario().getNome(),
                        t.getCategoria().getDescricao(),
                        t.getClassificacao(),
                        t.getValor(),
                        t.getData()
                    );
                }
            }
            System.out.println("===== FIM TABELA TRANSACAO =====");

            tx.commit();
        }
    }
}
