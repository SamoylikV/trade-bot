# Telegram Currency Bot

This is a Telegram bot that provides real-time currency exchange rates. It uses the Frankfurter API to fetch the latest currency rates and sends updates to the user when there are changes in the rates.

## Features

- Fetches real-time currency exchange rates.
- Sends updates to the user when there are changes in the rates.
- Provides charts of currency rate changes.

## Technologies Used

- Java
- Maven
- Telegram Bots API
- Frankfurter API
- JFreeChart for chart generation

## Setup

1. Clone the repository.
2. Create a `credentials.properties` file in the `src/main/resources` directory with your bot's username and token:

```properties
bot.username=YOUR_BOT_USERNAME
bot.token=YOUR_BOT_TOKEN
```

3. Build the project using Maven:

```bash
mvn clean install
```

4. Run the `Main` class to start the bot.

## Usage

Once the bot is running, you can interact with it on Telegram. Send `/start` to start the bot and `/check` to check the current currency rates.
