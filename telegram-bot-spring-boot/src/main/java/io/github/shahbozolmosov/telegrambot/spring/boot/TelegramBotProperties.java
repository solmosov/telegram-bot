package io.github.shahbozolmosov.telegrambot.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperties {

    private Map<String, Bot> bots = new LinkedHashMap<>();

    public Map<String, Bot> getBots() {
        return bots;
    }

    public void setBots(Map<String, Bot> bots) {
        this.bots = bots;
    }

    public static class Bot {
        private String token;
        private boolean enabled = true;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
