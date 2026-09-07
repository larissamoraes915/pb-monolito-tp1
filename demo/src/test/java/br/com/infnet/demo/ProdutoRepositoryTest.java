package br.com.infnet.demo;

import br.com.infnet.demo.domain.Produto;
import br.com.infnet.demo.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.history.Revisions;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    @DisplayName("Deve salvar e recuperar um produto com sucesso")
    void deveSalvarERecuperarProduto() {
        Produto produto = new Produto("Teclado Mecânico", 250.0);
        Produto salvo = produtoRepository.save(produto);

        assertNotNull(salvo.getId());
        assertEquals("Teclado Mecânico", salvo.getNome());
        assertNotNull(salvo.getDataCriacao());
    }

    @Test
    @DisplayName("Deve buscar produtos pelo nome ignorando maiúsculas/minúsculas")
    void deveBuscarProdutosPorNome() {
        produtoRepository.save(new Produto("Monitor Gamer", 1200.0));

        List<Produto> resultado = produtoRepository.findByNomeContainingIgnoreCase("gamer");

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.stream().anyMatch(p -> p.getNome().contains("Monitor Gamer")));
    }

    @Test
    @DisplayName("Deve buscar produtos filtrando por preço máximo com JPQL")
    void deveBuscarPorPrecoMaximo() {
        produtoRepository.save(new Produto("Mousepad", 40.0));
        produtoRepository.save(new Produto("Cadeira Ergonômica", 900.0));

        List<Produto> acessiveis = produtoRepository.buscarProdutosAtePreco(100.0);

        assertFalse(acessiveis.isEmpty());
        assertTrue(acessiveis.stream().allMatch(p -> p.getPreco() <= 100.0));
    }

    @Test
    @DisplayName("Deve registrar revisões de histórico de alterações (Envers)")
    void deveRegistrarHistoricoDeAlteracoes() {
        Produto produto = produtoRepository.save(new Produto("Headset", 180.0));
        Long id = produto.getId();

        // Altera o preço para gerar uma nova revisão no histórico
        produto.setPreco(210.0);
        produtoRepository.save(produto);

        Revisions<Integer, Produto> revisoes = produtoRepository.findRevisions(id);

        // Deve conter ao menos 2 estados registrados no histórico
        assertTrue(revisoes.getContent().size() >= 2);
    }
}