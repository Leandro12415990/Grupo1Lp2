package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditarClienteBLL {
    public boolean EditarCliente(Utilizador utilizador, String nome, LocalDate nascimento, String morada, String password) {
        ImportDal importDal = new ImportDal();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int soma = 0, index = 0;

        Tools.utilizadores = importDal.carregarUtilizador();

        for (Utilizador u : importDal.carregarUtilizador())
        {
            if (u.getEmail().equals(utilizador.getEmail()))
            {
                index = soma;
                if (!nome.isEmpty()) utilizador.setNomeUtilizador(nome);
                if (nascimento != null) utilizador.setDataNascimento(nascimento);
                if (!morada.isBlank()) utilizador.setMorada(morada);
                if (!password.isBlank()) utilizador.setPassword(password);
            }
            soma++;
        }

        Tools.utilizadores.set(index, utilizador);

        importDal.gravarUtilizador(Tools.utilizadores);

        return true;
    }
}
