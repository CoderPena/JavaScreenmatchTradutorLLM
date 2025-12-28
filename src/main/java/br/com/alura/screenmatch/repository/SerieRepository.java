package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional <Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List <Serie> findByAtoresContainingIgnoreCase(String nomeAtor);

    List <Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer quantidadeMinima, Double avaliacaoMinima);

//    @Query(value = "select * from series where series.total_temporadas <= 3 AND series.avaliacao >= 0", nativeQuery = true)
    @Query(value = "select s from Serie s where s.totalTemporadas <= :quantidadeMinima AND s.avaliacao >= :avaliacaoMinima", nativeQuery = false)
    List<Serie> seriesPorTemporadaEAvaliacao(Integer quantidadeMinima, Double avaliacaoMinima);
}
