# Better Home & TP (BHT) 🏠

BHT is a Minecraft plugin designed to enhance the home teleportation experience for players on Minecraft servers. With BHT, players can set multiple homes, teleport to their homes with ease, and send teleport requests to other players for quick and convenient travel.

## Features 🚀

- **Interactive GUIs**: Seamlessly teleport to homes and manage teleport requests with intuitive graphical user interfaces.
- **Visual Feedback**: Enhance immersion with decorative particles during teleportation, providing visual cues for the process.
- **Vault API Integration**: Integrate with the Vault API for economy plugin support, allowing server owners to charge for teleportation and home services.
- **Customizable Home Limits**: Set custom home limits based on player roles, providing flexibility and control over gameplay experiences.

## Commands 🛠️

- `/home <player>`: Opens the graphical user interface (GUI) for managing homes.
- `/home set <home> [public/private]`: Changes a home visibility setting (public or private).
- `/home del <home>`: Deletes a home.
- `/home change <home> [public/private]`: Changes the access level of a home (public or private).
- `/home go <player> <home>`: Teleports to a specified home.

- `/tpr send <player>`: Sends a teleport request to another player.
- `/tpr accept <player>`: Accepts a teleport request from another player.
- `/tpr cancel <player>`: Cancels a teleport request from/to another player.

- `/bht reload`: Reloads plugin configuration.

## Permissions 🔐

- `bht.admin`: Allows the execution of admin-only commands such as reload. (Default: OP)
- `bht.bypass-home-limit`: Grants permission to bypass home limit restrictions. (Default: OP)
- `bht.bypass-access`: Grants permission to bypass home access restrictions. (Default: OP)
- `bht.home`: Grants permission to teleport to a home. (Default: True)
- `bht.tpr`: Grants permission to send teleport requests. (Default: True)

## Installation 📥

1. Download the BHT.jar file from the Releases section.
2. Place the BHT.jar file into the plugins folder of your Bukkit server.
3. Restart or reload the server to enable the plugin.
4. Configure the plugin according to your likings.

## Support 🤝

If you encounter any issues or have suggestions for improvement, you're welcome to open an issue on the [GitHub repository](https://github.com/flafmg/BHT/issues) :D.

## About ℹ️

BHT is developed and maintained by [flafmg](https://github.com/flafmg).

Made with ❤️ in Brazil 🇧🇷
