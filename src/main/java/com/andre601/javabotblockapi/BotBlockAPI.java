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

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BotBlockAPI{
    private Map<String, String> authTokens = new HashMap<>();

    private int updateInterval;
    private boolean jdaDisabled;
    private JDA jda;
    private ShardManager shardManager;

    /**
     * Creates an instance of BotBlockAPI with the provided api tokens (as Map) and update interval.
     *
     * @param authTokens
     *        A Map of sites and their tokens. May not be null.
     * @param updateInterval
     *        The update interval to set.
     */
    public BotBlockAPI(@NotNull Map<String, String> authTokens, int updateInterval){
        this.authTokens = authTokens;
        this.updateInterval = updateInterval;
        this.jdaDisabled = true;
        this.jda = null;
        this.shardManager = null;
    }

    /**
     * Creates an instance of BotBlockAPI with the provided api tokens (as Map), update interval and
     * {@link net.dv8tion.jda.core.JDA JDA instance}.
     *
     * @param authTokens
     *        A Map of sites and their tokens. May not be null.
     * @param updateInterval
     *        The update interval to set.
     * @param jda
     *        An instance of {@link net.dv8tion.jda.core.JDA JDA}. May not be null.
     */
    public BotBlockAPI(@NotNull Map<String, String> authTokens, int updateInterval, @NotNull JDA jda){
        this.authTokens = authTokens;
        this.updateInterval = updateInterval;
        this.jdaDisabled = false;
        this.jda = jda;
        this.shardManager = null;
    }

    /**
     * Creates an instance of BotBlockAPI with the provided api tokens (as Map), update interval and
     * {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager instance}.
     *
     * @param authTokens
     *        A Map of sites and their tokens. May not be null.
     * @param updateInterval
     *        The update interval to set.
     * @param shardManager
     *        An instance of {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
     */
    public BotBlockAPI(@NotNull Map<String, String> authTokens, int updateInterval, @NotNull ShardManager shardManager){
        this.authTokens = authTokens;
        this.updateInterval = updateInterval;
        this.jdaDisabled = false;
        this.jda = null;
        this.shardManager = shardManager;
    }

    Map<String, String> getAuthTokens(){
        return authTokens;
    }

    boolean isJdaDisabled(){
        return jdaDisabled;
    }

    JDA getJDA(){
        return jda;
    }

    ShardManager getShardManager(){
        return shardManager;
    }

    int getUpdateInterval(){
        return updateInterval;
    }

    /**
     * Builder class to create an instance of {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}
     */
    public class Builder{
        private Map<String, String> authTokens = new HashMap<>();

        private int updateInterval = 30;
        private boolean jdaDisabled = false;
        private JDA jda = null;
        private ShardManager shardManager = null;

        /**
         * Empty Builder class
         */
        public Builder(){}

        /**
         * Constructor that also sets the {@link net.dv8tion.jda.core.JDA JDA instance}.
         *
         * @param jda
         *        The instance of {@link net.dv8tion.jda.core.JDA JDA}.
         */
        public Builder(JDA jda){
            this.jda = jda;
        }

        /**
         * Constructor that also sets the {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager instance}.
         *
         * @param shardManager
         *        The instance of {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
         */
        public Builder(ShardManager shardManager){
            this.shardManager = shardManager;
        }

        /**
         * Adds the provided Site name and token to the Map.
         * <br>If there is already an entry with the same key, it will be overwritten.
         *
         * @param  site
         *         The name of the site. May not be null.
         *         <br>A list of supported sites can be found <a href="https://botblock.org/api/docs#count" target="_blank">here</a>.
         * @param  token
         *         The API token you get from the corresponding botlist. May not be null.
         *
         * @throws NullPointerException
         *         When either the site or token are empty ({@code ""}).
         *
         * @return The Builder after the site and token were set. Useful for chaining.
         */
        public Builder addAuthToken(@NotNull String site, @NotNull String token){
            if(ObjectUtils.isEmpty(site) || ObjectUtils.isEmpty(token))
                throw new NullPointerException("Empty site and/or token is not allowed!");

            authTokens.put(site, token);

            return this;
        }

        /**
         * Sets the provided Map as the new Map.
         * <br><b>This will overwrite every previously set entry!</b>
         *
         * @param  authTokens
         *         The Map that should be used. May not be null.
         *
         * @throws NullPointerException
         *         When the provided Map is empty.
         *
         * @return The Builder after the Map was set. Useful for chaining.
         */
        public Builder setAuthTokens(@NotNull Map<String, String> authTokens){
            if(ObjectUtils.isEmpty(authTokens))
                throw new NullPointerException("Empty Map for authTokens is not allowed!");

            this.authTokens = authTokens;

            return this;
        }

        /**
         * Sets the update interval in minutes for the auto-posting.
         * <br>You don't need to set this when not using the auto-post option. Default is 30.
         *
         * @param  updateInterval
         *         The update interval in minutes that should be used. This can't be less than 1.
         *
         * @throws IllegalArgumentException
         *         When the updateInterval is less than 1.
         *
         * @return The Builder after the updateInterval was set. Useful for chaining.
         */
        public Builder setUpdateInteval(int updateInterval){
            if(updateInterval < 1)
                throw new IllegalArgumentException("updateInterval can't be less than 1!");

            this.updateInterval = updateInterval;

            return this;
        }

        /**
         * Sets if an instance of {@link net.dv8tion.jda.core.JDA JDA} or {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}
         * need to be set.
         * <br>{@code true} means that it is *NOT* required to set. Default is false.
         *
         * <p><b>This will be ignored when either {@link #setJDA(JDA)} or {@link #setShardManager(ShardManager)} are set!</b>
         *
         * @param  disable
         *         The boolean to set if JDA/ShardManager is required. True means it's *NOT* required.
         *
         * @return The Builder after the boolean was set. Useful for chaining.
         */
        public Builder disableJDA(boolean disable){
            this.jdaDisabled = disable;

            return this;
        }

        /**
         * Sets the instance of {@link net.dv8tion.jda.core.JDA JDA}. This will be ignored when {@link #setShardManager(ShardManager)}
         * is used!
         * <br>It will also disable {@link #disableJDA(boolean)} (set it to false) when it was set.
         *
         * <p>You can as an alternative define JDA directly through the constructor.
         *
         * <p><b>Example:</b>
         * <pre><code>
         * JDA jda = // Getting the JDA from somewhere
         *
         * BotBlockAPI api = new BotBlockAPI.Builder(jda) // Setting the JDA
         *     // Adding sites through addAuthToken(String, String) and the build it with build()
         * </code></pre>
         *
         * @param  jda
         *         The instance of {@link net.dv8tion.jda.core.JDA JDA} to use. May not be null.
         *
         * @return The Builder after JDA was set. Useful for chaining.
         */
        public Builder setJDA(@NotNull JDA jda){
            if(jdaDisabled)
                jdaDisabled = false;

            this.jda = jda;

            return this;
        }

        /**
         * Sets the instance of {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager}.
         * <br>This will disable {@link #disableJDA(boolean)} (set it to false) when it was set.
         *
         * <p>You can as an alternative define ShardManager directly through the constructor.
         *
         * <p><b>Example:</b>
         * <pre><code>
         * ShardManager shardManager = // Getting the ShardManager from somewhere
         *
         * BotBlockAPI api = new BotBlockAPI.Builder(shardManager) // Setting the ShardManager.
         *     // Adding sites through addAuthToken(String, String) and the build it with build()
         * </code></pre>
         *
         * @param  shardManager
         *         The instance of {@link net.dv8tion.jda.bot.sharding.ShardManager ShardManager} to use. May not be null.
         *
         * @return The Builder after ShardManager was set. Useful for chaining.
         */
        public Builder setShardManager(@NotNull ShardManager shardManager){
            if(jdaDisabled)
                jdaDisabled = false;

            this.shardManager = shardManager;

            return this;
        }

        /**
         * Builds the instance of {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}.
         *
         * @throws NullPointerException
         *         When JDA nor ShardManager are set (null) and {@link #disableJDA(boolean)} is false.
         *
         * @return The built, usable {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}.
         */
        public BotBlockAPI build(){
            if(shardManager != null)
                return new BotBlockAPI(authTokens, updateInterval, shardManager);
            else
            if(jda != null)
                return new BotBlockAPI(authTokens, updateInterval, jda);
            else
            if(jdaDisabled)
                return new BotBlockAPI(authTokens, updateInterval);
            else
                throw new NullPointerException("disableJDA(Boolean) is false and JDA as-well as ShardManager are null!");
        }
    }
}
