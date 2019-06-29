[BotBlock]: https://botblock.org
[API]: https://botblock.org/api/docs

[BotBlock4J]: https://github.com/Nathan-webb/BotBlock4J

[Wiki]: https://github.com/Andre601/JavaBotBlockAPI/wiki
[Javadocs]: https://Andre601.github.io/jbba-doc

[BadgeDownload]: https://api.bintray.com/packages/andre601/maven/JavaBotBlockAPI/images/download.svg
[Download]: https://bintray.com/andre601/maven/JavaBotBlockAPI/_latestVersion

# JavaBotBlockAPI
JavaBotBlockAPI is a continued and updated Java Wrapper for [BotBlock], a website that makes it possible to update guild counts on multiple lists with one API.  
This wrapper is a fork of [BotBlock4J] and was updated and improved to make it as userfriendly as possible.

## Installation
[![BadgeDownload]][Download]

You can install JavaBotBlockAPI through the following methods.  
Make sure to replace `{version}` with the above shown version.

### Gradle
Put this code into your `build.gradle`:  
```gradle
repositories{
    jcenter()
}

dependencies{
    compile group: 'com.andre601', name: 'JavaBotBlockAPI', version: '{version}'
}
```

### Maven
For maven use this code snipped:  
```xml
<dependencies>
  <dependency>
    <groupId>com.andre601</groupId>
    <artifactId>JavaBotBlockAPI</artifactId>
    <version>{version}</version>
  </dependency>
</dependencies>
```

## Usage
To use the Wrapper you have to follow these steps.

### Notes
In the below examples do I use a JDA instance called `jda`.  
This will also work with ShardManager.

### Creating a BotBlockAPI instance
You first need to create an instance of the BotBlockAPI class.  
This class is the center of everything, including on what sites you want to post your guild counts.

You can use the internal Builder class of BotBlockAPI to create an instance. It would look something like this:  
```java
// Creating an instance of BotBlockAPI using BotBlockAPI.Builder
BotBlockAPI api = new BotBlockAPI.Builder()
    .addAuthToken("lbots.org", "MySecretToken123") // Adds a site with the corresponding API token.
    .addAuthToken("botlist.space", "MySecretToken456") // The builder allows chaining of the methods.
    .build();
```

#### Notes
There are a lot of other methods that you can use. Head over to the [Wiki] for more information.

### Posting
You can post the guilds either automatically or manually depending on your own preferences.

#### Auto-posting
JavaBotBlockAPI comes with an inbuilt scheduler to post your guilds automatically.
To use it simply use the `startAutoPosting` method and provide either a JDA instance, ShardManager instance or the bot id and guild count.

**Example**:  
```java
// We need to get an instance of RequestHandler to use the methods.
RequestHandler handler = new RequestHandler();

// jda is a JDA instance and api a BotBlockAPI instance.
handler.startAutoPosting(jda, api);
```

But what if you want to stop it?  
For that just call the `stopAutoPosting` method:  
```
handler.stopAutoPosting();
```

#### Manual posting
If you want to post the guild counts manually you can use the `postGuilds` method.  
```java
// We need to get an instance of RequestHandler to use the methods.
RequestHandler handler = new RequestHandler();

// jda is a JDA instance and api a BotBlockAPI instance.
handler.postGuilds(jda, api);
```

### Getting botinfo
JavaBotBlockAPI allows you to receive different information of a certain bot.  
What the sites returns can be completely different. There are only methods to receive general informations.  

#### Receive full JSON
You can use the `getAll(...)` method to receive the full JSON of the BotBlock API.  
```java
// We need to get an instance of RequestHandler to use the methods.
RequestHandler handler = new RequestHandler();

// Like with all other methods can you use JDA, ShardManager or ID.
JSONObject json = handler.getAll(jda);
```

#### Receive Owners
You can use `getOwners(...)` to receive a List of all owners of the bot.  
```java
// We need to get an instance of RequestHandler to use the methods.
RequestHandler handler = new RequestHandler();

// Like with all other methods can you use JDA, ShardManager or ID.
List<String> owners = handler.getOwners(jda);
```

#### Receive all botlists.
If you only want the Botlists and their information, use `getBotlists(...)`.  
It is returned as a JSONObject.  
```java
// We need to get an instance of RequestHandler to use the methods.
RequestHandler handler = new RequestHandler();

// Like with all other methods can you use JDA, ShardManager or ID.
JSONObject json = handler.getBotlists(jda);
```

#### Receive certain Botlist info
You can use `getBotlist(..., String)` to get the information of a specific botlist.  
The information you receive is given as JSONArray and depends on what the botlist returns.  
```java
// We need to get an instance of RequestHandler to use the methods.
RequestHandler handler = new RequestHandler();

// Like with all other methods can you use JDA, ShardManager or ID.
JSONArray array = handler.getBotlist(jda, "lbots.org");
```

### Exceptions
When you post the guild counts you could encounter certain Exceptions.  
You can receive the following exceptions:
- `IOException`  
The Request couldn't be performed properly. This can be f.e. the case when BotBlock.org denies access (403).
- `RatelimitedException`  
When we exceed the ratelimit of BotBlock.org  
This shouldn't be the case with auto-posting since it has a minimum delay of 1 minute.
- `NullPointerException`  
Thrown when BotBlock.org sends an empty response, meaning something got messed up on their side.

## Links
Here are some useful links:
- [BotBlock.org][BotBlock] Site for which this wrapper was made.
  - [API] API documentation.
- [Wiki] Contains additional information on how you can use JavaBotBlockAPI.
- [Javadocs] Java documentation of the Wrapper.
- [BotBlock4J] Original Wrapper from which this one originates.
