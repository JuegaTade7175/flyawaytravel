package com.example.flyawaytravel.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightCreateManyRequest {
    @NotNull
    private List<@Valid FlightCreateRequest> inputs;
}