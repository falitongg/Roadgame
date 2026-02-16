# Post-apocalyptic 2D Game (Rogue-like side-scroller)
[![Watch the video](https://img.youtube.com/vi/1CA494PFkos/maxresdefault.jpg)](https://www.youtube.com/watch?v=1CA494PFkos)

## Concept

This is a 2D post-apocalyptic game in which the player advances along a road while facing various obstacles and enemies.

The goal of the game is to survive as long as possible. During their adventure, the player will find randomly generated loot. The player will also encounter various threats, such as bandits and other creatures (zombies, dogs).

### Detailed Description:

#### Graphics

* **Linear level** (player moves left/right)
* The game scene is a standard 2D scene bounded by the edges of the screen
* **Parallax scrolling** will be used in the background.

#### Game World

* **Obstacles** and **blockades** appear on the road, which must be bypassed or destroyed.
* The player encounters **enemies** that can be defeated using weapons.
* Obstacles and blockades will be randomly generated throughout the game.

#### Game Mechanics

1. **Survival**.
2. **Interaction with the world**:
* **Combat** – direct confrontation with enemies, use of weapons.
* **Stealth** – the ability to bypass enemies using the environment.
* **Trading** – exchanging found resources with traders.


3. **Loot** – randomly generated supplies, the appearance of which depends on the location and the danger level of the area:
* **Water** – clean water (+50 STAMINA)
* **Medicine and medical supplies** – bandages (+50 HP)
* **Melee combat:**
1. **Knuckle duster** – 50 DMG, allows breaking boxes.
2. **Fists** – 15 DMG


* **Clothing and armor** – military vest (+100 ARMOR)



#### Enemies and Threats:

* **Dogs** – 50 HP.
* **Zombies** – 100 HP.

### Functionalities

#### Main Menu and UI

After launching the game, the player will see the **main menu**, which contains:

* **New Game** – starts a new game with a generated world.
* **Load Game** – continue from the last saved state.
* **Settings** – option to change controls and sound.
* **Exit Game** – return to desktop.

The game also includes a **pause menu**, which opens with the `Esc` key and offers options to **continue, settings, save game, exit game**.

#### Game Controls

* **Player Movement**: `WASD`.
* **Sprint**: `Shift`.
* **Stealth**: `Ctrl`.
* **Jump**: `Space`.
* **Interaction**: `E` (collecting loot, communicating with traders).
* **Attack**: Left Mouse Button.
* **Block Attacks**: Right Mouse Button.
* **Craft**: `C`
* **Use**: `1-5`
* **SAVE GAME**: `F5`
* **LOAD GAME**: `F9`

#### Saving and Loading the Game

* **Autosave** upon interaction with a trader.
* **Manual save** anytime in the settings.
* **Loading the game** will occur from the last save point.
* **What is saved?** Player position, inventory, current situation on the map.

## User manual

### **Interface**

* **New Game** - Starts a new game from the beginning
* **Load Game** - Loads a saved game after selecting a file from the list
* **Disable Logging** - Toggles logging mode
* **Quit** - Exits the application

**Blue** - Armor

**Red** - HP

**Light blue** - Stamina

### Game Controls

* **Player Movement**: `WASD`
* **Sprint**: `Shift`
* **Sneaking**: `Ctrl`
* **Jump**: `Space`
* **Interaction**: `E` (collecting loot, communicating with traders)
* **Attack**: Left Mouse Button
* **Blocking**: Right Mouse Button
* **Craft** - `C`
* **Use** - `1-5`
* **SAVE GAME -** `F5`
* **LOAD GAME -** `F9`
* **PAUSE** - `ESC`


## Technical Documentation

### 1. Project Overview

* **Name**: RoughLikeGame / The Road
* **Goal**: Survive in a linear 2D level with a parallax background, collect loot, face enemies (zombies, dogs), trade with NPCs (Trader), and progress as long as possible.
* **Engine**: Pure Java on JVM, GUI in JavaFX.

---

### 2. Requirements and Environment

* **JDK**: Java 23 and above
* **Dependency Management**: Apache Maven (`pom.xml` contains plugins and dependencies)
* **Version Control**: GitLab
* **Testing**: JUnit 5.10.2, Mockito Core + Mockito JUnit Jupiter
* **Logging**: `java.util.logging`

---

### 3. Project Structure

**<--- *java* --->**

**Main** – Application entry point

`controller` – Key and input controllers

`data` – Serialization (**GameStateSaver**, **GameStateLoader**, **GameSnapshot**, **DTOs**)

`model` – Game logic (**Game**, **Player**, **Enemy**, **Obstacle**, **Trader**...)

`util` – Helper classes and enums (**Direction**, **ItemType**, **GameState**, **LoggingUtil**...)

`view` – Rendering (**GameView**, **BackgroundLayer**)

**<--- *resources* --->**

**designtest.fxml** – FXML for the menu

`images/` – Assets (parallax backgrounds, player sprites, enemies, UI)

**<--- *test* --->**

**GameTest** – Unit tests for the Game class

**PlayerTest** – Unit tests for the Player class

**<--- *others* --->**

**pom.xml** – Maven configuration (JavaFX dependencies, GSON, JUnit, Mockito)

---

### 4. Technologies and Libraries Used

| Component | Technology / Version | Description |
| --- | --- | --- |
| JVM | Java 23 | Modern features: `record`, `sealed` |
| GUI | JavaFX 23 (controls, fxml) | Scene, Timeline, FXML menu |
| Serialization | Gson (com.google.code.gson) | Saving/loading state to JSON |
| Testing | JUnit 5.10.2, Mockito | Unit tests and mocking |
| Logging | `java.util.logging` | File log `log.txt`, level configurable via parameter |
| Build & Dependency | Maven | Dependency management, plugins |

---

### 5. Main Components and Feature Descriptions

#### 5.1 Model (`model`)

* **Game**: Management of the game cycle, enemy spawning, obstacle generation, and transitions between states (`GameState`).
* **Entity** + derived classes: `Player`, `Enemy`, `DogEnemy`, `ZombieEnemy`, `Obstacle`, `Trader`.
* **Camera**: Tracking the player's position and scene scrolling.

#### 5.2 Data (`data`)

* **GameStateSaver / GameStateLoader**: Saving and loading the entire game state to/from JSON files.
* ***GameSnapshot*** *+ **Data***: DTO classes for entity serialization (PlayerData, EnemyData, ObstacleData, TraderData, InventoryItemData).

#### 5.3 Control (`controller`)

* **MainMenuController**: FXML menu – New Game, Load Game, Logger, Quit.
* **InputHandler**: Processing keys and mouse during gameplay (movement, interaction, attack, saving at the trader).

#### 5.4 View (`view`)

* **GameView**: Main game window – rendering parallax layers (`BackgroundLayer`), entities, UI elements, and Notifications.
* **Timeline**: Main game loop using `KeyFrame` for regular updates and rendering.

#### 5.5 Utilities (`util`)

* **Enums**: `Direction`, `EnemyType`, `ObstacleType`, `ItemType`, `GameState`, `VisualState`.
* **LoggingUtil**: Initialization of logging to `log.txt` based on the `LOGGING` system parameter.

---

### 6. Multithreading

* **Stamina Regeneration** (`Player`): Its own `Thread` with periodic `sleep` and `Platform.runLater` to push changes back to the UI thread.

---

### 7. Testing

* **GameTest.java** and **PlayerTest.java**: Verification of spawning logic, collisions, and player state changes.
* **Mockito** is prepared for mocking (currently unused in existing tests).

---

### 8. Logging

* Configuration in `LoggingUtil.setup()`: Checks `System.getProperty("LOGGING", "true")`.
* Writes to the `log.txt` file in the current directory.
* Level set to `Level.ALL`.
