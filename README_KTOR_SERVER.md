# Ktor Server — README

Documentação técnica do servidor desenvolvido com **Ktor**.  
Este documento descreve como configurar, executar e manter o serviço.

---

# Sumário

1. Visão Geral
2. Requisitos
3. Estrutura do Projeto
4. Build do Projeto
5. Configuração
6. Executando o Servidor
7. Endpoints
8. Autenticação
9. Tratamento de Erros
10. Logs
11. Testes
12. Docker
13. Troubleshooting
14. Licença

---

# 1. Visão Geral

Este projeto utiliza **Ktor Server** para fornecer uma API HTTP.

Principais tecnologias:

- Kotlin
- Ktor
- kotlinx.serialization
- Gradle
- Logback
- Docker (opcional)

---

# 2. Requisitos

Antes de executar o projeto, certifique-se de possuir:

- **JDK 17+**
- **Gradle** ou usar o wrapper `./gradlew`
- **Docker** (opcional)
- Banco de dados configurado (caso aplicável)

---

# 3. Estrutura do Projeto

```
src
 ├── main
 │   ├── kotlin
 │   │   └── com.example.server
 │   │        ├── Application.kt
 │   │        ├── routes
 │   │        ├── controllers
 │   │        ├── services
 │   │        └── repositories
 │   │
 │   └── resources
 │        ├── application.conf
 │        └── logback.xml
 │
 └── test
```

---

# 4. Build do Projeto

Para compilar o projeto:

```bash
./gradlew build
```

Para limpar o projeto:

```bash
./gradlew clean
```

---

# 5. Configuração

A configuração principal está em:

```
resources/application.conf
```

Exemplo:

```hocon
ktor {
  deployment {
    port = 8080
  }

  application {
    modules = [ com.example.server.ApplicationKt.module ]
  }
}
```

---

# 6. Executando o Servidor

Executar com Gradle:

```bash
./gradlew run
```

Executar o `.jar`:

```bash
java -jar build/libs/app.jar
```

---

# 7. Endpoints

### Health Check

```
GET /health
```

Resposta:

```json
{
  "status": "ok"
}
```

### Exemplo de requisição

```bash
curl http://localhost:8080/health
```

---

# 8. Autenticação

O servidor pode utilizar **JWT Authentication**.

Fluxo típico:

1. Usuário faz login
2. Servidor gera um token JWT
3. Cliente envia o token no header

```
Authorization: Bearer <token>
```

---

# 9. Tratamento de Erros

Erros são tratados utilizando `StatusPages`.

Exemplo de resposta de erro:

```json
{
  "status": 404,
  "message": "Resource not found"
}
```

---

# 10. Logs

Logs são configurados através do **Logback**.

Arquivo:

```
resources/logback.xml
```

---

# 11. Testes

Executar testes:

```bash
./gradlew test
```

---

# 12. Docker

Exemplo de Dockerfile:

```dockerfile
FROM eclipse-temurin:17-jdk

COPY build/libs/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
```

Build da imagem:

```bash
docker build -t ktor-server .
```

Executar container:

```bash
docker run -p 8080:8080 ktor-server
```

---

# 13. Troubleshooting

### Porta em uso

Erro:

```
Address already in use
```

Solução: alterar a porta em `application.conf`.

---

### Falha de conexão com banco

Verificar:

- URL do banco
- Usuário e senha
- Rede / firewall

---

# 14. Licença

Defina aqui a licença do projeto.

Exemplo:

```
MIT License
```