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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for handling the sites to post to and the delay for the auto-post option in the
 * {@link com.andre601.javabotblockapi.RequestHandler RequestHandler}.
 */
public class BotBlockAPI{
    private static final int DEFAULT_DELAY = 30;

    private Map<String, String> authTokens;
    private int updateInterval;

    /**
     * Constructor to set the Map with the sites and tokens.
     * <br>This will also set the update interval to 30 minutes.
     *
     * @param authTokens
     *        A Map of sites and their tokens. May not be null.
     *        <br>You may receive the API-token from your botlist.
     */
    public BotBlockAPI(@NotNull Map<String, String> authTokens){
        this.authTokens = authTokens;
        this.updateInterval = DEFAULT_DELAY;
    }

    /**
     * Constructor to set the Map with the sites and tokens and also the update delay..
     *
     * @param authTokens
     *        A Map of sites and their tokens. May not be null.
     *        <br>You may receive the API-token from your botlist.
     * @param updateInterval
     *        The update interval to set.
     */
    public BotBlockAPI(@NotNull Map<String, String> authTokens, int updateInterval){
        if(updateInterval < 2)
            throw new IllegalArgumentException("Update interval may not be less than 2.");

        this.authTokens = authTokens;
        this.updateInterval = updateInterval;
    }

    Map<String, String> getAuthTokens(){
        return authTokens;
    }

    int getUpdateInterval(){
        return updateInterval;
    }

    /**
     * Builder class to create an instance of {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}
     */
    public static class Builder{
        private Map<String, String> authTokens = new HashMap<>();
        private int updateInterval = DEFAULT_DELAY;

        /**
         * Empty constructor to get the class.
         */
        public Builder(){}

        /**
         * Adds the provided Site name and token to the Map.
         * <br>Entries with the same key will be overwritten.
         *
         * @param  site
         *         The name of the site. May not be null.
         *         <br>A list of supported sites can be found <a href="https://botblock.org/api/docs#count" target="_blank">here</a>.
         * @param  token
         *         The API token you get from the corresponding botlist. May not be null.
         *         <br>You may receive the API-token from your botlist.
         *
         * @throws NullPointerException
         *         When either the site or token are empty ({@code ""}).
         *
         * @return The Builder after the site and token were set. Useful for chaining.
         */
        public Builder addAuthToken(@NotNull String site, @NotNull String token){
            Check.notEmpty(site, "Site may not be empty.");
            Check.notEmpty(token, "Token may not be empty.");

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
            Check.notEmpty(authTokens, "AuthTokens may not be null.");

            this.authTokens = authTokens;

            return this;
        }

        /**
         * Sets the update interval in minutes for the auto-posting.
         * <br>You don't need to set this when not using the auto-post option. Default is 30.
         *
         * @param  updateInterval
         *         The update interval in minutes that should be used. This can't be less than 2.
         *
         * @throws IllegalArgumentException
         *         When the updateInterval is less than 2.
         *
         * @return The Builder after the updateInterval was set. Useful for chaining.
         */
        public Builder setUpdateInteval(int updateInterval){
            if(updateInterval < 2)
                throw new IllegalArgumentException("Update interval may not be less than 2.");

            this.updateInterval = updateInterval;

            return this;
        }

        /**
         * Builds the instance of {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}.
         *
         * @return The built, usable {@link com.andre601.javabotblockapi.BotBlockAPI BotBlockAPI}.
         */
        public BotBlockAPI build(){
            return new BotBlockAPI(authTokens, updateInterval);
        }
    }
}
