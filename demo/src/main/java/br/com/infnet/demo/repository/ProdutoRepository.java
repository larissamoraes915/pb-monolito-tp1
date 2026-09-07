package br.com.infnet.demo.repository;

import br.com.infnet.demo.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}