package br.com.infnet.demo.repository;

import br.com.infnet.demo.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long>, RevisionRepository<Produto, Long, Integer> {

  
    List<Produto> findByNomeContainingIgnoreCase(String nome);

   
    @Query("SELECT p FROM Produto p WHERE p.preco <= :precoMaximo ORDER BY p.preco ASC")
    List<Produto> buscarProdutosAtePreco(@Param("precoMaximo") Double precoMaximo);
}