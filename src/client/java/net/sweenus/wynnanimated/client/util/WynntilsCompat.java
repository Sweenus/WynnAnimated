package net.sweenus.wynnanimated.client.util;

import net.sweenus.wynnanimated.client.AnimationRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class WynntilsCompat {

    private static Method getClassTypeMethod;
    private static Object characterModel;

    public static @Nullable String getPlayerClass() {
        if (!AnimationRegistry.isWynntilsLoaded()) return null;

        try {
            // Load Models class
            Class<?> modelsClass =
                    Class.forName("com.wynntils.core.components.Models");

            // Models.Character
            Field characterField = modelsClass.getField("Character");
            Object character = characterField.get(null);

            // Cache method lookup
            if (getClassTypeMethod == null) {
                getClassTypeMethod =
                        character.getClass().getMethod("getClassType");
                characterModel = character;
            }

            Object classTypeEnum = getClassTypeMethod.invoke(characterModel);

            return classTypeEnum != null ? classTypeEnum.toString() : null;
        } catch (Throwable t) {
            return null;
        }
    }
}
