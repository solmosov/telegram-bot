# Basic Bot Example

Minimal Telegram bot example using `telegram-bot-core`.

## Requirements

* Java 21+
* Maven 3.9+
* Telegram account

## 1. Create a Bot

Open [@BotFather](https://t.me/BotFather) in Telegram.

```text
/start
/newbot
```

Choose a name and username for your bot.

BotFather will give you a **bot token**. Keep it private.

## 2. Configure Token

Add your bot token to the example configuration:

```java
String token = "YOUR_BOT_TOKEN";
```

## 3. Register Commands

In [@BotFather](https://t.me/BotFather):

```text
/setcommands
```

Add:

```text
start - Start the bot
help - Show help message
```

## 4. Run

Build the project:

```bash
mvn clean package
```

Run the application using the provided main class.

## 5. Try the Bot

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

You should receive a response for each message.

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
