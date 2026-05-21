package com.aws.dynamo.service;

import com.aws.shared.exception.CampoObrigatorioException;
import com.aws.shared.exception.RecursoJaExisteException;
import com.aws.shared.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioDynamoService {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;
    private final String partitionKeyName;

    public UsuarioDynamoService(
            DynamoDbClient dynamoDbClient,
            @Value("${aws.dynamo.table-name:academia-java-aws}") String tableName,
            @Value("${aws.dynamo.partition-key:aula-6}") String partitionKeyName
    ) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
        this.partitionKeyName = partitionKeyName;
    }

    public Map<String, String> criar(String id, String nome, String email) {
        validarCampoObrigatorio(id, "id");
        validarCampoObrigatorio(nome, "nome");
        validarCampoObrigatorio(email, "email");

        Map<String, AttributeValue> item = new HashMap<>();
        item.put(partitionKeyName, AttributeValue.builder().s(id).build());
        item.put("nome", AttributeValue.builder().s(nome).build());
        item.put("email", AttributeValue.builder().s(email).build());

        try {
            dynamoDbClient.putItem(PutItemRequest.builder()
                    .tableName(tableName)
                    .item(item)
                    .conditionExpression("attribute_not_exists(#pk)")
                    .expressionAttributeNames(Map.of("#pk", partitionKeyName))
                    .build());
        } catch (ConditionalCheckFailedException exception) {
            throw new RecursoJaExisteException("Usuario ja existe com id: " + id);
        }
        return Map.of("id", id, "nome", nome, "email", email);
    }

    public Map<String, String> buscarPorId(String id) {
        validarCampoObrigatorio(id, "id");

        Map<String, AttributeValue> key = Map.of(partitionKeyName, AttributeValue.builder().s(id).build());

        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build());

        if (!response.hasItem() || response.item().isEmpty()) {
            throw new RecursoNaoEncontradoException("Usuario nao encontrado com id: " + id);
        }

        return toUsuario(response.item());
    }

    public List<Map<String, String>> listarTodos() {
        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(tableName)
                .build());

        return response.items().stream()
                .map(this::toUsuario)
                .toList();
    }

    public Map<String, String> atualizar(String id, String nome, String email) {
        validarCampoObrigatorio(id, "id");
        validarCampoObrigatorio(nome, "nome");
        validarCampoObrigatorio(email, "email");

        Map<String, AttributeValue> key = Map.of(partitionKeyName, AttributeValue.builder().s(id).build());

        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("nome", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(nome).build())
                .action(AttributeAction.PUT)
                .build());
        updates.put("email", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(email).build())
                .action(AttributeAction.PUT)
                .build());

        try {
            dynamoDbClient.updateItem(UpdateItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .attributeUpdates(updates)
                    .conditionExpression("attribute_exists(#pk)")
                    .expressionAttributeNames(Map.of("#pk", partitionKeyName))
                    .build());
        } catch (ConditionalCheckFailedException exception) {
            throw new RecursoNaoEncontradoException("Usuario nao encontrado com id: " + id);
        }

        return Map.of("id", id, "nome", nome, "email", email);
    }

    public void deletar(String id) {
        validarCampoObrigatorio(id, "id");

        Map<String, AttributeValue> key = Map.of(partitionKeyName, AttributeValue.builder().s(id).build());

        try {
            dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .conditionExpression("attribute_exists(#pk)")
                    .expressionAttributeNames(Map.of("#pk", partitionKeyName))
                    .build());
        } catch (ConditionalCheckFailedException exception) {
            throw new RecursoNaoEncontradoException("Usuario nao encontrado com id: " + id);
        }
    }

    private Map<String, String> toUsuario(Map<String, AttributeValue> item) {
        return Map.of(
                "id", item.getOrDefault(partitionKeyName, AttributeValue.builder().s("").build()).s(),
                "nome", item.getOrDefault("nome", AttributeValue.builder().s("").build()).s(),
                "email", item.getOrDefault("email", AttributeValue.builder().s("").build()).s()
        );
    }

    private void validarCampoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new CampoObrigatorioException("O campo " + campo + " deve ser informado.");
        }
    }
}
