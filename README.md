# aws-class

API Spring Boot para calcular a distancia em metros entre dois pontos geograficos.

## Endpoint

```http
POST /distancia
```

## JSON de entrada

```json
{
  "pontoA": {
    "latitude": -23.561684,
    "longitude": -46.655981
  },
  "pontoB": {
    "latitude": -22.951916,
    "longitude": -43.210487
  }
}
```

## Resposta de sucesso

HTTP 200 OK

```json
{
  "distanciaMetros": 358450.9088115591,
  "mensagem": "Distancia calculada com sucesso"
}
```

## Respostas de erro

As respostas de erro seguem o mesmo padrao:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Mensagem clara explicando o erro"
}
```

### Latitude ausente, nula ou em branco

HTTP 400 Bad Request

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "A latitude deve ser informada."
}
```

### Longitude ausente, nula ou em branco

HTTP 400 Bad Request

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "A longitude deve ser informada."
}
```

### Coordenadas ausentes

HTTP 400 Bad Request

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "As coordenadas devem ser informadas."
}
```

### Recurso nao encontrado

HTTP 404 Not Found

```json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Recurso nao encontrado."
}
```

## Executar os testes

```powershell
.\mvnw.cmd test
```
