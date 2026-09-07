import React, { useState, useEffect } from 'react';
import axios from 'axios';

function App() {
  const [produtos, setProdutos] = useState([]);
  const [nome, setNome] = useState('');
  const [preco, setPreco] = useState('');

  const carregarProdutos = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/produtos');
      setProdutos(res.data);
    } catch (error) {
      console.error('Erro ao buscar produtos:', error);
    }
  };

  useEffect(() => {
    carregarProdutos();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/produtos', {
        nome,
        preco: parseFloat(preco)
      });
      setNome('');
      setPreco('');
      carregarProdutos();
    } catch (error) {
      console.error('Erro ao cadastrar produto:', error);
    }
  };

  return (
    <div style={{ maxWidth: '450px', margin: '40px auto', fontFamily: 'sans-serif' }}>
      <h2>Cadastro de Produtos</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input
          type="text"
          placeholder="Nome do produto"
          value={nome}
          onChange={(e) => setNome(e.target.value)}
          required
          style={{ padding: '8px', fontSize: '14px' }}
        />
        <input
          type="number"
          step="0.01"
          placeholder="Preço (ex: 29.90)"
          value={preco}
          onChange={(e) => setPreco(e.target.value)}
          required
          style={{ padding: '8px', fontSize: '14px' }}
        />
        <button
          type="submit"
          style={{ padding: '10px', background: '#007bff', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
        >
          Cadastrar Produto
        </button>
      </form>

      <h3 style={{ marginTop: '30px' }}>Produtos no Banco:</h3>
      <ul>
        {produtos.map((p) => (
          <li key={p.id} style={{ marginBottom: '6px' }}>
            <strong>{p.nome}</strong> — R$ {Number(p.preco).toFixed(2)}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;