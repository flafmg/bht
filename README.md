# Better Home & TPA (BHT)

BHT is a minecraft plugin designed to enhance the home teleportation experience for players on Minecraft servers. With BHT, players can set multiple homes, teleport to their homes with ease, and send teleport requests to other players for quick and convenient travel.

## Features

- **Multiple Homes**: Players can set multiple homes at different locations.
- **Teleport Requests**: Send teleport requests to other players for quick travel.
- **Cooldown System**: Prevents spamming of teleport commands with a cooldown timer.
- **Customizable Messages**: Customize plugin messages to suit your server's style.
- **Permission System**: Granular permission settings for controlling access to plugin features.
- **Compatibility**: Works with various Bukkit server versions.

## Commands

- `/sethome <name> [public/private]`: Set a home at the player's current location.
- `/delhome <name>`: Delete a player's home.
- `/listhomes [player]`: List a player's homes.
- `/home [player] <name>`: Teleport to a home.
- `/tpa <player>`: Send a teleport request to another player.
- `/tpaccept [player]`: Accept a teleport request from another player.
- `/tpdeny [player]`: Deny a teleport request from another player.

## Permissions

- `bht.sethome`: Allows setting a home.
- `bht.delhome`: Allows deleting a home.
- `bht.listhomes`: Allows listing homes.
- `bht.home`: Allows teleporting to a home.
- `bht.tpa`: Allows sending teleport requests.
- `bht.tpaccept`: Allows accepting teleport requests.
- `bht.tpdeny`: Allows denying teleport requests.

## Installation

1. Download the BHT.jar file from the Releases section.
2. Place the BHT.jar file into the plugins folder of your Bukkit server.
3. Restart or reload the server to enable the plugin.

## Configuration

BHT comes with a configuration file (config.yml) where you can customize various settings such as cooldown times, message formats, and more.

## Vault Integration

BHT supports the use of the Vault API, allowing server owners to integrate economy plugins and charge players for their teleportation and homes.

## Support

If you encounter any issues or have suggestions for improvement, please open an issue on the GitHub repository.

## About

BHT is developed and maintained by [flafmg](https://github.com/flafmg).

Made with ❤️ in Brazil 🇧🇷
