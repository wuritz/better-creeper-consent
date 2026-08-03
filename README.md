<h1 align="center">Better Creeper Consent</h1>

<p align="center">Allow or deny every creeper explosion that happens around you!</p>

<div align="center">
    <img src="assets/screen1.png" alt="screen1">
</div>

## ❗ Requirements ❗

> [!IMPORTANT]
> Better Creeper Consent won't launch without these mods installed

- Minecraft - `26.2` or newer
- Fabric **API** - *(a version that supports MC 26.2)*
- Fabric **Kotlin** - *(a version that supports MC 26.2)*

## Features
- Before a creeper explodes, a *screen pops up*.
- You can either `Allow` or `Deny` the request, or you can `Gamble` to let it be chosen randomly

<details>
    <summary>Plus feature</summary>
    When the creeper is denied:
    <br>
    <img src="assets/denymsg.png" alt="Deny message">
    <br>
    Or when you allow the explosion:
    <br>
    <img src="assets/allowmsg.png" alt="Allow message">
</details>

> [!NOTE]
> - If there are multiple creepers around, only **1 gets to ask for consent**, while the other ones are _automatically denied and won't explode_. 
> - While playing on multiplayer, note that **one creeper** sends a consent request to **one player** ONLY, and that's *the nearest player to the creeper*.

## Gambling ✨
If you wanna test your luck, you can press the `Gambling` button.
The gambling part is simple: if you roll `Allowed` - you explode, but if you roll `Denied` - you get a *random item*!

<details>
    <summary>Images of Gambling</summary>
    <h3>Rolling</h3>
    Upon pressing the `Gambling` button, this screen pops up and immediately starts rolling:
    <img src="assets/rolling.png" alt="rolling">
    <br>
    <h3>Result</h3>
    After 5 seconds, you'll get a result:
    <img src="assets/result.png" alt="result">
</details>

### Prices  
There are 3 categories of prizes you can get: Common, Rare and Legendary. 
You can win various creeper-related items:

| Rarity       | Items                                         |
|--------------|-----------------------------------------------|
| 🟩 Common    | Gunpowder, String, Dirt, Cobblestone, Oak Log |
| 🟦 Rare      | Raw Cod, TNT                                  |
| 🟨 Legendary | Creeper Head, Diamond, Golden Apple           |

> [!WARNING]
> There's also a 5% chance you get an **ignited TNT** in the place of the creeper, instead of getting an item drop.

## Installation
- Download the latest `.jar` file from here or from Modrinth
- Locate your `mods/` folder
- Put the BCC `.jar` file alongside with your Fabric API and Kotlin `.jar` files
- Have fun!

## Credits
- Thank you to my *lovely girlfriend* for making the illustrations, including the logo and the creepers :33
- [Creeper's Consent](https://github.com/GameMech007-git/Creeper-Consent) by GameMech007, for giving the foundation and the idea for the mod

## License
MIT © wuritz