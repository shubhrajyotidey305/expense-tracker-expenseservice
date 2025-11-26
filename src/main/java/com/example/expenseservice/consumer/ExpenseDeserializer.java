package com.example.expenseservice.consumer;

import com.example.expenseservice.dto.ExpenseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class ExpenseDeserializer implements Deserializer<ExpenseDto> {
    @Override
    public ExpenseDto deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseDto expenseDto = null;
        try {
            expenseDto = objectMapper.readValue(data, ExpenseDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expenseDto;
    }
}
