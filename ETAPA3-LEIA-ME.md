# Etapa 3 — Automatização de testes

**Pergunta da etapa:** os testes que a IA escreve são confiáveis?

## 1. Métodos escolhidos
| Método | O que faz | Por que é importante |
|---|---|---|
| `Ball.calculateImpact` | Detecta colisão da bola com as raquetes e inverte a direção vertical | Regra central do jogo |
| `Ball.calculateDefeat` | Detecta quando a bola cruza a linha de fundo e soma o ponto | Define o placar salvo no banco |
| `Player.notifyKeyEvent` | Traduz teclas pressionadas/soltas em movimento da raquete | Única entrada do jogador |

> Obs.: o enunciado sugere usar métodos refatorados na Etapa 2. Os testes foram feitos sobre o código original; quando a Etapa 2 for entregue, reaplicar os testes (ver seção 6).

## 2. Testes gerados pela IA (framework: JUnit 5.11 + Maven Surefire)
- `src/test/java/components/BallImpactTest.java` — 4 testes de `calculateImpact`
- `src/test/java/components/BallScoreTest.java` — 3 testes de `calculateDefeat`
- `src/test/java/components/PlayerInputTest.java` — 3 testes de `notifyKeyEvent`

Os testes não precisam do PostgreSQL: a `GameSession` é criada em memória.

## 3. Resultado da primeira execução (`mvn test`)
| Classe | Testes | Passaram de primeira | Falharam |
|---|---|---|---|
| BallImpactTest | 4 | 3 | 1 |
| BallScoreTest | 3 | 3 | 0 |
| PlayerInputTest | 3 | 3 | 0 |
| **Total** | **10** | **9** | **1** |

- **Erros de compilação:** nenhum.
- **Falha:** `casoDeBordaBolaAcimaDaRaqueteNaoDeveriaColidir`. A falha é **do código, não do teste** (ver seção 5, defeito D1). O teste **não** foi alterado para ficar verde.

## 4. Testes que precisaram de ajuste
Nenhum teste precisou de ajuste para compilar ou rodar. Mas, na revisão manual, **1 classe tinha verificações que passavam sem testar nada**:

- Em `BallScoreTest`, o `assertFalse(ball.isMoving())` passava **sempre**, porque o construtor de `Ball` já cria a bola parada. Ou seja, o teste não provava que a bola é reiniciada depois do ponto.
  **Ajuste:** chamar `ball.setMovementToTheBall()` antes, para a bola começar em movimento.
- Em `bolaDentroDoCampoNaoAlteraPontuacao`, só o placar dos jogadores era conferido, não o da `GameSession`.
  **Ajuste:** incluídas as verificações da sessão.

Depois dos ajustes, os 3 testes continuaram passando. Agora, porém, eles testam o que dizem testar.

## 5. Casos de borda (vazio, negativo, inválido)
A IA gerou mais 12 testes de borda em `BallEdgeCaseTest.java` (7) e `PlayerEdgeCaseTest.java` (5).

| Teste | Tipo | Resultado |
|---|---|---|
| Bola encostando exatamente na base da raquete | limite | passou |
| Bola na quina direita da raquete (x = 240) | limite | passou |
| Bola 1 px além da quina (x = 241) | limite | passou |
| Bola exatamente no limite da linha superior (y = -15) | negativo / limite | passou |
| Bola 1 px antes da linha superior (y = -14) | negativo / limite | passou |
| Evento `KEY_TYPED` não move o jogador | inválido | passou |
| Soltar tecla sem ter pressionado | inválido | passou |
| Bola abaixo da raquete inferior não deve colidir | negativo | **falhou → D1** |
| Sessão nula não deve alterar o placar | vazio (null) | **falhou → D2** |
| Jogador sem teclas configuradas | vazio | **falhou → D3** |
| Tecla maiúscula (Caps Lock ligado) | inválido | **falhou → D4** |
| Raquete não deve sair da tela pela esquerda | negativo | **falhou → D5** |

**Execução final:** 22 testes, 16 passaram e 6 falharam. Todas as 6 falhas são defeitos do código.

### Defeitos encontrados
| # | Defeito | Onde | Afeta o jogo real? |
|---|---|---|---|
| D1 | A colisão só compara um lado da raquete: uma bola que **já passou** da raquete ainda "rebate" se a raquete estiver na mesma faixa horizontal. Vale para as duas raquetes. | `Ball.java`, linhas 75 e 82 | **Sim** |
| D5 | A raquete não tem limite de posição: segurando a tecla, ela sai da tela (x = -50 após 5 quadros). | `Player.draw` | **Sim** |
| D4 | Com Caps Lock ligado, as teclas viram `'A'`/`'D'` e o jogador não se move. | `Player.notifyKeyEvent` | Sim (usabilidade) |
| D2 | Se a sessão for nula, o placar do jogador é somado **antes** do erro, e o estado fica inconsistente. | `Ball.calculateDefeat` | Não hoje (o `GamePanel` sempre passa uma sessão), mas é uma fragilidade |
| D3 | Um `Player` criado sem teclas lança `ArrayIndexOutOfBoundsException` no primeiro evento. | `Player.notifyKeyEvent` | Não hoje (os jogadores sempre recebem 2 teclas), falta validação |

**Decisão do grupo:** os testes que falham ficaram **vermelhos de propósito**, como registro dos defeitos. O grupo decide se corrige o código (por exemplo, na Etapa 2) ou se marca os testes com `@Disabled("defeito conhecido")`.

## 6. Conclusão: os testes da IA são confiáveis?
**Em parte.**
- **A favor:** os 22 testes compilaram e rodaram de primeira, e as expectativas estavam corretas. Os casos de borda pedidos revelaram **5 defeitos reais**, 2 deles visíveis no jogo (D1 e D5).
- **Contra:** um teste "verde" pode não testar nada. O `assertFalse(ball.isMoving())` passava sempre, e isso só foi percebido lendo o código do teste. A primeira leva também quase não tinha casos de borda: eles só apareceram quando foram pedidos explicitamente.
- **Lição:** teste passando ≠ teste bom. É preciso revisar cada verificação e se perguntar "esse teste falharia se o código estivesse errado?".

## 7. Como executar
1. JDK 24 e Maven. O Maven que vem com o IntelliJ serve. O `JAVA_HOME` precisa apontar para o JDK 24.
2. Na pasta do `pom.xml`: `mvn test`. No IntelliJ: botão direito em `src/test/java` → *Run 'All Tests'*.
3. Os relatórios ficam em `target/surefire-reports/`.

## 8. Diário de IA
**Rodada 1: geração dos testes**
- Prompt: "Analise três métodos críticos do Pong Java e crie testes JUnit 5 para colisão, pontuação e teclado, incluindo casos de borda."
- Resposta: 10 testes em 3 classes, só 1 deles marcado explicitamente como caso de borda.
- Validação: `mvn test` → 9 passaram e 1 falhou (defeito real D1).

**Rodada 2: revisão e casos de borda (Claude Code)**
- Prompt: pedido para rodar os testes, revisar se são confiáveis e criar casos de borda com valores vazios, negativos e inválidos.
- Resposta: apontou 2 verificações fracas em `BallScoreTest` e gerou 12 testes de borda.
- Validação: `mvn test` → 22 testes, 16 passaram e 6 falharam (5 defeitos reais distintos).
- Ajustes aceitos: reforço das verificações de `BallScoreTest`.
- Sugestões rejeitadas: alterar o teste D1 para ele passar (isso esconderia o defeito).

## 9. Integração com a refatoração (Etapa 2)
Quando a Etapa 2 estiver pronta, rodar estes 22 testes no código refatorado. Adaptar só as chamadas que mudaram de assinatura e registrar:
- testes que passavam e passaram a falhar (regressão);
- defeitos D1 a D5 que foram corrigidos (testes que ficaram verdes).
