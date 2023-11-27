package com.example.models.response;

import java.util.List;

public record CustomerResponse(
        Integer id,
        String name,
        String lastName,
        String email,
        String mobile
) {}
