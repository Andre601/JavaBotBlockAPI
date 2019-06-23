/*
 * Copyright 2019 Andre601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.andre601.javabotblockapi;

import com.andre601.javabotblockapi.exceptions.RatelimitedException;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestHandler {
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final OkHttpClient CLIENT = new OkHttpClient();

    private JDA jda = null;
    private ShardManager shardManager = null;
    private String id = null;
    private BotBlockAPI botBlockAPI;

    private JSONObject json = new JSONObject();

    /**
     * Creates an instance of this class and sets the {@link net.dv8tion.jda.core.JDA JDA instance} and
     * {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI instance}.
     * <br>If the JDA instance is part of sharding (Has ShardInfo) then {@code shard_id} and {@code shard_count} are set too.
     *
     * <p>It is recommended to use {@link #RequestHandler(ShardManager, BotBlockAPI) RequestHandler(ShardManager, BotBlockAPI)}
     * when having a sharded bot.
     *
     * @param jda
     *        The {@link net.dv8tion.jda.core.JDA JDA instance} to use. Can't be null.
     * @param botBlockAPI
     *        An instance of {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}. Can't be null.
     */
    public RequestHandler(@NotNull JDA jda, @NotNull BotBlockAPI botBlockAPI){
        this.jda = jda;
        this.botBlockAPI = botBlockAPI;
        this.id = jda.getSelfUser().getId();

        json.put("server_count", jda.getGuildCache().size())
                .put("bot_id", id);

        if(jda.getShardInfo() != null)
            json.put("shard_id", jda.getShardInfo().getShardId())
                    .put("shard_count", jda.getShardInfo().getShardTotal());

        botBlockAPI.getAuthTokens().forEach(json::put);
    }

    /**
     * Creates an instance of this class and sets the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager instance} and
     * {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI instance}.
     *
     * @param shardManager
     *        The {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager instance} to use. Can't be null.
     * @param botBlockAPI
     *        An instance of {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}. Can't be null.
     */
    public RequestHandler(@NotNull ShardManager shardManager, @NotNull BotBlockAPI botBlockAPI){
        this.shardManager = shardManager;
        this.botBlockAPI = botBlockAPI;
        this.id = shardManager.getShardById(0).getSelfUser().getId();

        json.put("server_count", shardManager.getGuildCache().size())
                .put("bot_id", id)
                .put("shard_count", shardManager.getShardCache().size());

        List<Integer> shards = new ArrayList<>();
        for(JDA jda : shardManager.getShards())
            shards.add(jda.getGuilds().size());

        json.put("shards", new JSONArray(Arrays.deepToString(shards.toArray())));

        botBlockAPI.getAuthTokens().forEach(json::put);
    }

    /**
     * Creates an instance of this class and sets the bots ID, the guild count and the
     * {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}.
     * <br>This is essentially a shortcut to {@link #RequestHandler(String, int, BotBlockAPI)}.
     *
     * @param botId
     *        The ID of the bot.
     * @param guilds
     *        The guild count.
     * @param botBlockAPI
     *        An instance of {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}. Can't be null.
     *
     * @see #RequestHandler(String, int, BotBlockAPI) for full method.
     * @see #RequestHandler(ShardManager, BotBlockAPI) for use with ShardManager.
     * @see #RequestHandler(JDA, BotBlockAPI) for use with JDA.
     */
    public RequestHandler(long botId, int guilds, @NotNull BotBlockAPI botBlockAPI){
        new RequestHandler(Long.toString(botId), guilds, botBlockAPI);
    }

    /**
     * Creates an instance of this class and sets the bots ID, the guild count and the
     * {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}.
     *
     * @param botId
     *        The ID of the bot. Can't be null.
     * @param guilds
     *        The guild count.
     * @param botBlockAPI
     *        An instance of {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}. Can't be null.
     *
     * @see #RequestHandler(ShardManager, BotBlockAPI) for use with ShardManager.
     * @see #RequestHandler(JDA, BotBlockAPI) for use with JDA.
     */
    public RequestHandler(@NotNull String botId, int guilds, @NotNull BotBlockAPI botBlockAPI){
        json.put("server_count", guilds)
                .put("bot_id", botId);

        botBlockAPI.getAuthTokens().forEach(json::put);
    }

    /**
     * Performs a request to post the saved informations to the BotBlock API.
     * <br>Informations are set through calling RequestHandler and provide the informations through it.
     *
     * @throws IOException
     *         When the request failed.
     * @throws RatelimitedException
     *         When we got ratelimited by the API.
     * @throws NullPointerException
     *         When {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI} is null.
     */
    public void postGuilds() throws IOException, RatelimitedException{
        Objects.requireNonNull(botBlockAPI, "BotBlockAPI may not be null.");

        performRequest();
    }

    /**
     * Starts a scheduler to auto-post the guild counts to the BotBlock API.
     *
     * @throws NullPointerException
     *         When {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI} is null or both
     *         {@link net.dv8tion.jda.core.JDA JDA} and {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager} are null.
     */
    public void startAutoPosting(){
        Objects.requireNonNull(botBlockAPI, "BotBlockAPI may not be null.");

        if(!ObjectUtils.anyNotNull(jda, shardManager))
            throw new NullPointerException("startAutoPost() requires either JDA or ShardManager!");

        scheduler.scheduleAtFixedRate(() -> {
            try {
                postGuilds();
            }catch(IOException | RatelimitedException ex){
                ex.printStackTrace();
            }
        }, botBlockAPI.getUpdateInterval(), botBlockAPI.getUpdateInterval(), TimeUnit.MINUTES);
    }

    /**
     * Shuts down the scheduler.
     */
    public void stopAutoPosting(){
        scheduler.shutdown();
    }

    private void performRequest() throws IOException, RatelimitedException{
        Objects.requireNonNull(json, "JSON may not be null.");
        Objects.requireNonNull(id, "Id may not be null.");

        RequestBody body = RequestBody.create(null, json.toString());
        Request request = new Request.Builder()
                .url("https://botblock.org/api/count")
                .addHeader("User-Agent", id)
                .addHeader("Content-Type", "application/json") // Some sites require this in the header.
                .post(body)
                .build();

        try(Response response = CLIENT.newCall(request).execute()){
            Objects.requireNonNull(response.body(), "Received empty body from BotBlocks API.");

            if(!response.isSuccessful()){
                if(response.code() == 429)
                    throw new RatelimitedException(response.body().string());

                throw new IOException(String.format(
                        "Couldn't post guild counts to BotBlockAPI! Site responded with %d (%s)",
                        response.code(),
                        response.message()
                ));
            }

            JSONObject json = new JSONObject(response.body());
            if(json.has("failure")){
                JSONObject failure = json.getJSONObject("failure");

                List<String> sites = new ArrayList<>();
                for (String key : failure.keySet()) {
                    try {
                        JSONArray array = failure.getJSONArray(key);
                        sites.add(String.format(
                                "Name: %s, Error code: %d, Error Message: %s",
                                key,
                                array.getInt(0),
                                array.getString(1)
                        ));
                    } catch (JSONException ex) {
                        Map<String, Object> notFound = failure.toMap();
                        sites.add("Errors: " + notFound.toString());
                    }
                }

                throw new IOException(String.format(
                        "One or multiple requests failed! Response(s): %s",
                        String.join(", ", sites)
                ));
            }
        }
    }
}
