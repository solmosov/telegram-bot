# Telegram Bot Framework

A framework for building Telegram bots with Java and Spring Boot.

## Feature

- Fast and Simple way to build Telegram bots
- Telegram Bot API integration
- Annotation-based handlers
- Type-safe update handling
- Multiple bot support
- Configurable bot settings
- Graceful error handling
- Graceful shutdown
- Request validation
- Configurable logging
- File Upload support
- Long polling and webhook support for receiving updates
- Single-thread and Multi virtual-thread execution
- Spring Boot auto-configuration
- Custom handler registration
- Pluggable Authorization
- Update handling
- Message handling
- Command handling
- Contact handling
- Location handling
- Photo handling
- Users shared handling
- Secure update retrieval with `getUpdates` validation and parsing
- Automatic webhook configuration
- Webhook security 
- Webhook retry/idempotency protection
- Webhook path and header security

## Requirements
- Java 21 or higher
- Maven 3.9 or higher
- ### Spring Boot

- Spring Boot 4.1.x

## Installation
- ### Core

    Add the following dependency to your `pom.xml`: 

    ```xml
    <dependency>
        <groupId>io.github.solmosov</groupId>
        <artifactId>telegram-bot-core</artifactId>
        <version>1.0.0-beta.1</version>
    </dependency>
    ``` 
- ### Spring Boot
    
    For Spring Boot application, add:
    ```xml
  <dependency>
        <groupId>io.github.solmosov</groupId>
        <artifactId>telegram-bot-spring-boot</artifactId>
        <version>1.0.0-beta.1</version>
  </dependency>
  ```

## Quick Start
- ### Single Bot
    Create a `TelegramBot` instance and start it:
    ```java
    TelegramBot bot = new TelegramBot("myBot", "bot-secret-token");
    bot.start(); 
   ```
    Create a handler for your bot:
    ```java

    @BotHandler("myBot")
    public class MyTelegramBot {
    
        @CommandHandler("/start")
        public void start(BotContext context) {
    
            context.reply("Welcome " + context.message().from().firstName());
        }
    
        @MessageHandler("hello")
        public void hello(BotContext context) {
            context.reply("Hello " + context.message().from().firstName());
        }
    } 
    ```
    Now your bot responds
    - `/start` -> `Welcome [first name]` 
    - `hello` -> `Hello [first name]` 

- ### Multiple Bots
    To run multiple bots with a single application, use `TelegramBotApplication`
    ```java
    TelegramBotApplication application = new TelegramBotApplication();

    application
            .register("myBot", "bot-secret-token")
            .register("myBot2", "bot-secret-token2");

    application.start();
    ```
    The same `@BotHandler` approach is used for each registered bot:
    ```java

    @BotHandler("myBot")
    public class MyTelegramBotFirst {
    
        @CommandHandler("/start")
        public void start(BotContext context) {
    
            context.reply("Welcome " + context.message().from().firstName());
        }
    
        @MessageHandler("hello")
        public void hello(BotContext context) {
            context.reply("Hello " + context.message().from().firstName());
        }
    }
  
    @BotHandler("myBot2")
    public class MyTelegramBotSecond {
    
        @CommandHandler("/start")
        public void start(BotContext context) {
    
            context.reply("Welcome " + context.message().from().firstName());
        }
    
        @MessageHandler("hello")
        public void hello(BotContext context) {
            context.reply("Hello " + context.message().from().firstName());
        }
    } 
    ```
  Replace the bot names and tokens with your own values
  > **Security**: Never commit bot tokens to source control. Store them in enviroment variables or another security configuration  machanism.
---

## Spring Boot
The framework provides Spring Boot auto-configuration and supports multiple bots.
  
  - ### Configuration
  
    Register your bots using `TelegramBotRegistration`:
    ```java 
    @Configuration
    public class TelegramBotConfiguration {
    
      @Bean
      public TelegramBotRegistration myBotRegistration() {
        return TelegramBotRegistration.builder()
                .botName("myBot")
                .token("bot-secret-token")
                .build();
      }
    
      @Bean
      public TelegramBotRegistration myBot2Registration() {
        return TelegramBotRegistration.builder()
                .botName("myBot2")
                .token("bot-secret-token2")
                .build();
      }
    }
    ```
  - Bot Handlers
  
    Create a Spring component for each bot:
    ```java 
    @Component
    @BotHandler("myBot")
    public class MyTelegramBot {
    
        @CommandHandler("/start")
        public void start(BotContext context) {
    
            context.reply("Welcome " + context.message().from().firstName());
        }
    
        @MessageHandler("hello")
        public void hello(BotContext context){
                context.reply("Hello " + context.message().from().firstName());
        }
    ```
    For the second bot:
    ```java
    @Component
    @BotHandler("myBot2")
    public class MyTelegramBotSecond {
    
        @CommandHandler("/start")
        public void start(BotContext context) {
    
            context.reply("Welcome " + context.message().from().firstName());
        }
    
        @MessageHandler("hello")
        public void hello(BotContext context){
                context.reply("Hello " + context.message().from().firstName());
        }
    ```
  
    The same handler API is used for both single-bot and multi-bot applications.
    > **Security**: Never commit bot tokens to source control. Store them in environment variables or another secure configuration mechanism.
---
## License

This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for the full license text.

## Security

If you discover a security vulnerability, please do not report it through a public GitHub issue.

Instead, report it privately to the project maintainers so the issue can be investigated and addressed before public disclosure.

See [SECURITY.md](SECURITY.md) for information about how to report security vulnerabilities.

Please include:
- A description of the vulnerability
- Steps to reproduce the issue
- The affected module and version
- Any relevant logs or proof of concept

## Disclaimer

This software is provided "as is", without warranty of any kind.

The authors and contributors are not responsible for any damages, data loss, security incidents, service interruptions, or other consequences resulting from the use of this software.