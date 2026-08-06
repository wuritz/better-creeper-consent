<!------------>
<!-- Start  -->
<!------------>

<h1 align="center">Better Creeper Consent</h1>

<p align="center">Allow or deny every creeper explosion that happens around you!</p>

<div align="center">
    <img src="assets/screen1.png" alt="screen1">
</div>

<!-- Badges  -->

<div align="center">
    <!-- Curse  -->
    <a href="https://www.curseforge.com/minecraft/mc-mods/better-creeper-consent">
        <img alt="curseforge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg">
    </a>
    <!-- Modrinth  -->
    <a href="https://www.curseforge.com/minecraft/mc-mods/better-creeper-consent">
        <img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg">
    </a>
    <!-- Fabric API  -->
    <img alt="fabric-api" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg">
    <!-- Ko-fi  -->
    <a href="https://ko-fi.com/wuritz">
        <img alt="kofi-singular-alt" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/donate/kofi-singular-alt_vector.svg">
    </a>
</div>

---

<div align="center">
    Ever had creepers walk behind your back and destroy your buildings? <br>
    This mod gives you the power to simply click <strong>Deny</strong> when creepers want to explode!
</div>

<!----------->
<!-- Rest  -->
<!----------->

<h2 align="center">❗ Requirements ❗</h2>

> [!IMPORTANT]
> Better Creeper Consent won't launch without these mods installed

- Minecraft - `26.2` (as of now only 1 version is supported)
- [Fabric API](https://modrinth.com/mod/fabric-api) - *(a version that supports MC 26.2)*
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) - *(a version that supports MC 26.2)*

<h2 align="center">Features</h2>

### Core Features

- Before a creeper explodes, a *screen pops up*, giving you 3 buttons to choose from.
- You can either `Allow` or `Deny` the request, or you can `Gamble` to let it be chosen randomly

> [!NOTE]
>
> - If there are multiple creepers around, only **1 gets to ask for consent**, while the other ones are _automatically denied and won't explode_.
> - While playing on multiplayer, note that **one creeper** sends a consent request to **one player** ONLY, and that's *the nearest player to the creeper*.

### Gambling ✨

If you wanna test your luck, you can press the `Gambling` button.
The gambling part is simple: if you roll `Allowed` - you explode, but if you roll `Denied` - you get a *random item*!

### Images

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

<details>
    <summary>Bonus!</summary>
    When the creeper is denied:
    <br>
    <img src="assets/denymsg.png" alt="Deny message">
    <br>
    Or when you allow the explosion:
    <br>
    <img src="assets/allowmsg.png" alt="Allow message">
</details>

### Prizes
There are 3 categories of prizes you can get: Common, Rare and Legendary.
You can win various creeper-related items:

| Rarity       | Items                                         |
| ------------ | --------------------------------------------- |
| 🟩 Common    | Gunpowder, String, Dirt, Cobblestone, Oak Log |
| 🟦 Rare      | Raw Cod, TNT                                  |
| 🟨 Legendary | Creeper Head, Diamond, Golden Apple           |

> [!WARNING]
> There's also a 5% chance you get an **ignited TNT** in the place of the creeper, instead of getting an item drop.

<h2 align="center">Installation</h2>
1. Download the latest `.jar` file from here or from Modrinth
2. Locate your `mods/` folder
3. Put the BCC `.jar` file alongside with your Fabric API and Kotlin `.jar` files
4. Have fun!

<h2 align="center">Credits</h2>
- Thank you to my *lovely girlfriend* for making the illustrations, including the logo and the creepers :33
- [Creeper's Consent](https://github.com/GameMech007-git/Creeper-Consent) by GameMech007, giving the code foundation and the original idea for the mod

## License
MIT © wuritz