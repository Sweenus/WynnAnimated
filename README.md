# WynnAnimated

## Limitations

### Basic Attack Animation Speed
The first basic attack with a weapon plays the animation at the wrong speed.
This is intended as WynnAnimated calculates the cooldown on the first swing, caches this information, and then uses it to determine the correct speed for subsequent attack animations.

### Player Animation Constraints
- Other players are only animated with basic attack animations because there's no way (to my knowledge) to fetch information about other players' ability use.Instead, we intercept player hand swings locally and convert them into basic attack animations based on their class (fetched from the API). Archer is an exception as it doesn't perform a hand swing on attack.
- Other players that are Archers will only be animated if you're on the same channel (server) because we use a hacky method of listening for bow sounds to apply animations.

### API Rate Limiting
Only some other players are animated, not all. This is due to API rate limiting for fetching character class information.
In high-density areas, the rate limiter may activate and prevent animations for some players. Additionally, if a player has certain privacy settings enabled in their Wynncraft account, we won't be able to fetch their character class at all.

### Character Swap Animation Issues
Other players' animations may be incorrect after they swap to a different character. This is because we only refresh character information every 5 minutes (if within range).

### Dependency on Wynntils
Wynntils is currently a required dependency for providing ability usage and player class information. Future updates might remove this dependency.

### Animation Limitations in First Person View
Attack animations can look strange when the character is looking up or down in first-person view due to limitations in the animation engine.

## Known Issues

### Iris Shader Incompatibility
When shaders are enabled, the player model will fully render in first-person mode during attack animations, obscuring the screen. 
This incompatibility between Player Animator and Iris can only be resolved when development moves to Minecraft 1.21.11+.