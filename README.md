# Hildabot
Hildabot, built for Hildacord.

This bot was built from the ground up to work with a specific Discord guild - as such, I provide no guarantees that it'll run properly in any guilds outside of the one it was designed for, or any help accomplishing such.

However, if you wish to run it in your own guild:
1. You'll need to swap out all of the IDs in the source for IDs in your server. A projet-wide regex search in your IDE for Discord snowflakes (`[0-9]{17,}`) should identify anything that needs to be swapped out.
2. Set up a Redis database. This is required for things such as XP, submissions, and the store.
3. Fill out [`config.java`](src/main/java/com/cominatyou/Config.java). You'll need to supply a prefix, your Discord bot token, and a lettuce-compatible Redis URI. More details on that [here](https://github.com/lettuce-io/lettuce-core/wiki/Redis-URI-and-connection-details#uri-syntax).
