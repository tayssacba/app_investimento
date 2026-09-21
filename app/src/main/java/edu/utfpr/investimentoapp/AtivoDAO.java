package edu.utfpr.investimentoapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AtivoDAO {

    @Insert
    long inserir(Ativo ativo);

    @Update
    void atualizar(Ativo ativo);

    @Delete
    void excluir(Ativo ativo);

    @Delete
    void excluirVarios(List<Ativo> ativos);

    //0 = todos e 1 = favoritos
    @Query("SELECT * FROM ativos WHERE (:apenasFavoritos = 0 OR favorito = 1) "
            + "ORDER BY nomeProduto COLLATE NOCASE ASC")
    List<Ativo> listarPorNome(int apenasFavoritos);

    @Query("SELECT * FROM ativos WHERE (:apenasFavoritos = 0 OR favorito = 1) "
            + "ORDER BY valorInicial DESC, nomeProduto COLLATE NOCASE ASC")
    List<Ativo> listarPorValor(int apenasFavoritos);

    @Query("SELECT * FROM ativos WHERE (:apenasFavoritos = 0 OR favorito = 1) "
            + "ORDER BY categoria ASC, nomeProduto COLLATE NOCASE ASC")
    List<Ativo> listarPorCategoria(int apenasFavoritos);

    @Query("SELECT COUNT(*) FROM ativos")
    int contar();
}
