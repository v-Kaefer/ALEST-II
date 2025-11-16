# ALEST-II
 Trabalhos e Tarefas de ALEST II

## Atualizado para o 2° Semestre de 2025

## LabirintoGraph - Resolvedor de Labirintos

A classe `LabirintoGraph` resolve labirintos usando a classe existente `CaminhamentoLargura` (BFS - Busca em Largura).

### Funcionalidades

- Lê labirintos de arquivos de texto
- Converte o labirinto em um grafo onde cada célula navegável é um vértice
- Cria arestas apenas para movimentos **horizontais e verticais** (sem diagonais)
- Usa `CaminhamentoLargura` para encontrar o caminho mais curto entre dois pontos
- Exibe o labirinto com o caminho marcado

### Formato do Arquivo de Labirinto

- `#` = paredes
- `.` = espaços navegáveis
- `A` = ponto final
- `B` = ponto inicial

### Como Usar

```bash
# Compilar
cd src
java LabirintoGraph.java

# Executar com arquivo específico

> Altere o String filename = "../Casos-T2/caso1.txt" na linha 214, para o teste desejado.

```

### Exemplo de Saída

```
=== Resolvedor de Labirinto usando CaminhamentoLargura ===
Arquivo: Casos-T2/caso1.txt
Movimentos permitidos: apenas horizontal e vertical

Caminho encontrado!
Comprimento do caminho: 149 passos

Labirinto com caminho marcado (* representa o caminho):
[Labirinto com o caminho de B até A marcado com *]
```