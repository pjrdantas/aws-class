# aws-class

API Spring Boot para calcular a distancia em metros entre dois pontos geograficos.

## Endpoint

```http
POST /distancia
```

```http
POST /sns/publicar
```

```http
POST /sqs/enviar
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

## Publicar mensagem no SNS

Topico configurado:

```text
arn:aws:sns:us-east-2:606103596924:academia-aws-aula-8
```

Exemplo:

```powershell
curl -X POST http://localhost:8082/sns/publicar `
  -H "Content-Type: application/json" `
  -d "{\"assunto\":\"ana-aula/8\",\"mensagem\":\"Mensagem enviada pela API Java\"}"
```

Resposta de sucesso:

```json
{
  "messageId": "00000000-0000-0000-0000-000000000000",
  "topicArn": "arn:aws:sns:us-east-2:606103596924:academia-aws-aula-8",
  "mensagem": "Mensagem publicada com sucesso"
}
```

## Enviar mensagem para o SQS

Fila configurada:

```text
arn:aws:sqs:us-east-2:606103596924:aula-7
```

URL da fila:

```text
https://sqs.us-east-2.amazonaws.com/606103596924/aula-7
```

Exemplo:

```powershell
curl -X POST http://localhost:8082/sqs/enviar `
  -H "Content-Type: application/json" `
  -d "{\"mensagem\":\"Mensagem enviada para a fila aula-7 pela API Java\"}"
```

Resposta de sucesso:

```json
{
  "messageId": "00000000-0000-0000-0000-000000000000",
  "queueUrl": "https://sqs.us-east-2.amazonaws.com/606103596924/aula-7",
  "queueArn": "arn:aws:sqs:us-east-2:606103596924:aula-7",
  "mensagem": "Mensagem enviada com sucesso"
}
```
