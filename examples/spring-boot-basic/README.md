# Spring Boot Basic Example

Minimal Telegram bot example using `telegram-bot-spring-boot`.

## Requirements

* Java 21+
* Maven 3.9+
* Telegram account

## 1. Add Dependency

```xml
<dependency>
    <groupId>io.github.solmosov</groupId>
    <artifactId>telegram-bot-spring-boot</artifactId>
    <version>1.0.0-beta.2</version>
</dependency>
```

## 2. Create a Bot

Open [@BotFather](https://t.me/BotFather) in Telegram.

```text
/start
/newbot
```

Create your bot and copy the token.

## 3. Set Token

Set the token as an environment variable:

```bash
export MY_BOT_TOKEN="YOUR_BOT_TOKEN"
```

The application reads it from:

```java
.token(System.getenv("MY_BOT_TOKEN"))
```

## 4. Register Commands

In [@BotFather](https://t.me/BotFather):

```text
/setcommands
```

Add:

```text
start - Start the bot
help - Show help message
```

## 5. Run

```bash
mvn spring-boot:run
```

Or run the Spring Boot application from your IDE.

## 6. Try the Bot

Open your bot in Telegram and send:

```text
/start
```

```text
/help
```

```text
Hello
```

Each message is handled by `MyTelegramBot`.

## What This Example Shows

```java
@CommandHandler("/start")
```

Handles `/start`.

```java
@CommandHandler("/help")
```

Handles `/help`.

```java
@MessageHandler("Hello")
```

Handles `Hello` messages.
