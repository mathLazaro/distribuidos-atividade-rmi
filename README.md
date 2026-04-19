# Atividade RMI (Java)

Projeto cliente/servidor usando **Java RMI** com uma arquitetura **MVC** simples no cliente e uma biblioteca **shared** para compartilhar contratos (interfaces remotas + modelos) entre client e server.

## Requisitos

- **Linux** (ou ambiente compatível)
- **Java 25** (JDK 25) instalado e disponível como `java`/`javac`
  - Verifique com: `java --version` e `javac --version`
- **GNU Make** instalado (`make`)

> Observação: o build/execução via `make` já configura o classpath incluindo a lib `shared/target/shared.jar`.

## Como rodar

### 1) Build (compila tudo)

Na raiz do repositório:

```bash
make build
```

Isso irá:

- compilar a lib `shared` e gerar `shared/target/shared.jar`
- compilar `client` e `server` usando o `shared.jar` no classpath

### 2) Subir o servidor

Na raiz:

```bash
make run-server
```

ou diretamente:

```bash
make -C server run
```

### 3) Rodar o client

Em outro terminal, na raiz:

```bash
make run-client
```

ou diretamente:

```bash
make -C client run
```

## Configuração (opcional): host/porta via `.env`

O projeto lê as configurações a partir de arquivos `.env` em cada módulo.

### Server

- Arquivo: `server/.env`
- Exemplo: `server/.env.example`

Chaves suportadas:

- `HOST` (padrão: `127.0.0.1`)
- `PORT` (padrão: `1099`)

Para alterar, edite `server/.env` de acordo com o exemplo.

### Client

- Arquivo: `client/.env`
- Exemplo: `client/.env.example`

Chaves suportadas:

- `RMI_HOST` (padrão: `127.0.0.1`)
- `RMI_PORT` (padrão: `1099`)

Para alterar, edite `client/.env` de acordo com o exemplo.

## Estrutura do código

Visão geral (principais pastas):

- `shared/`
  - `src/distribuidos/api/` — contratos compartilhados (interface RMI + modelos)
  - `src/distribuidos/util/` — utilitários compartilhados (ex.: `EnvLoader`)
  - `target/shared.jar` — biblioteca gerada e usada por `client` e `server`

- `server/`
  - `src/distribuidos/rmi/server/` — entrada do servidor e camada de serviço
  - `src/.../service/` — implementação da interface remota (RMI)
  - `src/.../view/` — view/IO no terminal para o servidor

- `client/`
  - `src/distribuidos/rmi/client/` — entrada do cliente
  - `src/.../controller/` — controllers (ex.: `FlowerController`)
  - `src/.../view/` — view no terminal
  - `src/.../model/` — modelos locais do cliente

## Arquitetura (MVC simples)

No cliente:

- **View**: classes em `client/src/.../view/` (interação via terminal)
- **Controller**: classes em `client/src/.../controller/` (orquestram a interação e chamadas)
- **Model**: classes em `client/src/.../model/` (dados do domínio do cliente)

No servidor:

- Implementa a interface remota do `shared` e expõe via RMI (camada `service`).

## Por que existe o módulo `shared`?

No Java RMI, client e server precisam **compartilhar**:

- a **interface remota** (ex.: `distribuidos.api.service.DistanceCalculatorService`)
- os **tipos/DTOs** trafegados (ex.: `FlowerFeature`, `Dimension`)

E esses tipos precisam ter o **mesmo nome totalmente qualificado** (package + classe) em ambos os lados.

Para evitar duplicação e divergência de pacotes, o projeto gera uma lib `shared/target/shared.jar` e adiciona esse JAR ao classpath do build/run de `client` e `server` (via Makefiles).

## Dicas (VS Code)

- Se você abrir o workspace multi-root (`atividade_rmi.code-workspace`), os lançamentos/debug podem depender do `cwd` correto para encontrar `client/.env` e `server/.env`.
- Se aparecer erro de `.env` não encontrado no debug, rode via `make run-server`/`make run-client` (ou ajuste o `cwd` no `launch.json`).
