[BotBlock]: https://botblock.org
[API]: https://botblock.org/api/docs

[BotBlock4J]: https://github.com/Nathan-webb/BotBlock4J

[Wiki]: https://github.com/Andre601/JavaBotBlockAPI/wiki

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
    maven { url = 'https://dl.bintray.com/andre601/maven' }
}

dependencies{
    compile group: 'com.andre601', name: 'JavaBotBlockAPI', version: '{version}'
}
```

### Maven
For maven use this code snipped:  
```xml
<repositories>
  <repository>
    <id>jcenter</id>
    <url>https://dl.bintray.com/andre601/maven</url>
  </repository>
</repositories>

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
BotBlockAPI api = new BotBlockAPI.Builder(jda) // We can provide either a instance of JDA or ShardManager
    .addAuthToken("lbots.org", "MySecretToken123") // Adds a site with the corresponding API token.
    .addAuthToken("botlist.space", "MySecretToken456") // The builder allows chaining of the methods.
    .build();
```

#### Notes
`new BotBlockAPI.Builder()` allows you to provide either a JDA instance, a ShardManager instance or none at all.  
You can define a JDA or ShardManager instance at a later point with `.setJDA(JDA)` or `.setShardManager(ShardManager)` respectively.

Also note that when you don't provide any instance, that you have to use `disableJDA(true)` or else you'll receive an NullPointerException.  
There are also a lot of other methods that you can use. Head over to the [Wiki] for more information.

### Creating a RequestHandler instance
The next step after creating an instance of the BotBlockAPI is to create an instance of the Request Handler.

This example shows the easiest one to use, but there are much more ways you can set it. Check the [Wiki] for examples.  
```java
// We previously created a BotBlockAPI instance called api which we now use here.
RequestHandler handler = new RequestHandler(api);
```

### Posting the guild counts
This step depends on what You previously set for the BotBlockAPI and the RequestHandler since some methods are only available for certain cases.

**All methods require BotBlockAPI to be setup, meaning you have AT LEAST added one site and token!**

#### Auto-posting
The Wrapper comes with an inbuilt auto-post that allows you to easily post the guild counts without worrying too much.  
However, this method is limited to JDA and ShardManager only so you have to define them for using this.

To auto-post guild counts you just need to call `RequestHandler#startAutoPosting();`.  
This would look like this:  
```java
// We previously defined a RequestHandler called handler
handler.startAutoPosting();
```

Easy right? But what if you want to stop the auto-posting?  
For that simply use `Request#stopAutoPosting();`. Here is another example:  
```java
// We previously defined a RequestHandler called handler
handler.stopAutoPosting();
```

Note that the delay in which you post to the API is defined through the BotBlockAPI.
Use `BotBlockAPI.Builder#setUpdateInterval(Integer)` to define a delay. It is counted in minutes and default is 30.

#### Manual posting
If you want to post the guild counts manually you can use the following method:  
```java
// We previously defined a RequestHandler called handler
handler.postGuilds();
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
- [BotBlock.org] Site for which this wrapper was made.
  - [API] API documentation.
- [Wiki] Contains additional information on how you can use JavaBotBlockAPI.
- [BotBlock4J] Original Wrapper from which this one originates.
