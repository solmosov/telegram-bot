package io.github.solmosov.telegrambot.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.solmosov.telegrambot.keyboard.reply.button.RequestUsers;
import io.github.solmosov.telegrambot.keyboard.util.WebAppInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyKeyboardButton implements ReplyKeyboardElement {
    @JsonProperty("text")
    private final String text;

    @JsonProperty("style")
    private final Style style;

    @JsonProperty("request_users")
    private final RequestUsers requestUsers;

//    @JsonProperty("request_chat")
//    @JsonProperty("request_managed_bot")

    @JsonProperty("request_contact")
    private final Boolean requestContact;

    @JsonProperty("request_location")
    private final Boolean requestLocation;

//    @JsonProperty("request_poll")

    @JsonProperty("web_app")
    private final WebAppInfo webApp;


    private ReplyKeyboardButton(Builder builder) {
        this.text = builder.text;
        this.style = builder.style;
        this.requestLocation = builder.requestLocation;
        this.requestContact = builder.requestContact;
        this.requestUsers = builder.requestUsers;
        this.webApp = builder.webApp;
    }

    public enum Style {
        DANGER("danger"),
        SUCCESS("success"),
        PRIMARY("primary");

        private final String value;

        Style(String value) {
            this.value = value;
        }

        @JsonValue
        public String value() {
            return value;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String text;
        private Style style;
        private Boolean requestLocation;
        private Boolean requestContact;
        private RequestUsers requestUsers;
        private WebAppInfo webApp;

        public Builder() {

        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder style(Style style) {
            this.style = style;
            return this;
        }

        public Builder requestLocation(Boolean requestLocation) {
            this.requestLocation = requestLocation;
            return this;
        }

        public Builder requestContact(Boolean requestContact) {
            this.requestContact = requestContact;
            return this;
        }

        public Builder requestUsers(RequestUsers requestUsers) {
            this.requestUsers = requestUsers;
            return this;
        }

        public Builder webApp(String url) {
            this.webApp = new WebAppInfo(url);
            return this;
        }


        public ReplyKeyboardButton build() {
            return new ReplyKeyboardButton(this);
        }
    }
}
