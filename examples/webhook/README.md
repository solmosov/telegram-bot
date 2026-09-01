# Telegram Bot Webhook Example

Minimal Telegram bot example using `telegram-bot-spring-boot` webhook mode.

## Requirements

* Java 21+
* Maven 3.9+
* Telegram account
* Public HTTPS domain

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

Copy the bot token.

## 3. Set Environment Variables

```bash
export MY_BOT_TOKEN="YOUR_BOT_TOKEN"
export MY_BOT_WEBHOOK_URL="https://your-domain.com"
export MY_BOT_WEBHOOK_PATH_SECRET="YOUR_PATH_SECRET"
export MY_BOT_WEBHOOK_SECRET="YOUR_WEBHOOK_SECRET"
```

## 4. Configure Webhook

Set:

* UpdateMode: `UpdatesMode.WEBHOOK`
* Webhook port: `8080`
* Webhook path: `/webhook/telegram`
* Webhook URL: `https://your-domain.com`
* Webhook path secret: `your-path-secret`
* Webhook secret: `your-secret`


## 5. Run

```bash
mvn spring-boot:run
```

Make sure your domain is publicly accessible over HTTPS.

## Telegram Webhook API

[Telegram Bot API — setWebhook](https://core.telegram.org/bots/api?utm_source=chatgpt.com#setwebhook)

Webhook methods:

```text
setWebhook
deleteWebhook
getWebhookInfo
```
