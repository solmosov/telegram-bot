package io.shahbozolmosov.telegrambot.springboot.example.user;

import java.util.Set;

public record MyUser(
        Long telegramUserId,
        String username,
        Set<String> roles
) {
}
