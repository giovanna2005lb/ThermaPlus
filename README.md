# ThermaPlus
API REST para gerenciamento de usuários e registro de localizações com consulta de clima e recomendações personalizadas.

## Integrantes
RM555679 - Lavinia Soo Hyun Park
RM556242 - Giovanna Laturague Bueno

## Descrição
A Thermaplus é uma API REST desenvolvida em Java com Spring Boot que permite:

- Cadastro e autenticação de usuários (com JWT)
- Registro de localizações geográficas associadas ao usuário
- Consulta de dados meteorológicos através da integração com uma API externa
- Geração de recomendações baseadas nas condições climáticas atuais
- Paginação e filtros para listagem de localizações

## Endpoints
- Cadastro de novo usuário
```POST /users```
- Autenticação e geração de token JWT
  ```POST /login```
- Registrar nova localização
  ```POST /localizacoes```
- Listar localizações do usuário
  ```GET /localizacoes```
- Atualizar localização existente
  ```PUT /localizacoes/{id}```
- Deletar localização
  ```DELETE /localizacoes/{id}```
- Obter recomendações climáticas:
  ```GET /localizacoes/recomendacoes```
- Interface Swagger (documentação):
  ```GET /swagger-ui/**```

## Exemplos:
Usuario
```json
{
  "nome": "Usuario1",
  "email": "usuario1@example.com",
  "senha": "senhaSegura123"
}
```
Login
```json
{
  "email": "usuario1@example.com",
  "password": "senhaSegura123"
}
```
Localização
```json
{
  "latitude": -23.55052,
  "longitude": -46.633308
}
```

