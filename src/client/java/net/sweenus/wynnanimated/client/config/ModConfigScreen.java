package net.sweenus.wynnanimated.client.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.sweenus.wynnanimated.client.AnimationRegistry;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModConfigScreen extends Screen {
    private final Screen parent;
    private ConfigListWidget list;

    public ModConfigScreen(Screen parent) {
        super(Text.literal("WynnAnimated Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        list = new ConfigListWidget(client, width, height - 64, 32, 25);

        // General settings
        list.addEntry(new CategoryEntry("General"));
        list.addEntry(new BooleanEntry("Debug Mode", () -> ModConfig.get().debugMode, v -> ModConfig.get().debugMode = v));
        list.addEntry(new BooleanEntry("Show Arms (1st Person)", () -> ModConfig.get().showArms, v -> ModConfig.get().showArms = v));
        list.addEntry(new BooleanEntry("Animate Other Players", () -> ModConfig.get().animateOtherPlayers, v -> ModConfig.get().animateOtherPlayers = v));
        list.addEntry(new IntSliderEntry("Animation Range", 5, 50, () -> ModConfig.get().animationRange, v -> ModConfig.get().animationRange = v));
        list.addEntry(new IntSliderEntry("Rate Limit (Player Cache)", 5, 60, () -> ModConfig.get().rateLimit, v -> ModConfig.get().rateLimit = v));
        list.addEntry(new IntSliderEntry("Stance Timeout (ticks)", 10, 100, () -> ModConfig.get().stanceTimeoutTicks, v -> ModConfig.get().stanceTimeoutTicks = v));

        // Archer spells
        list.addEntry(new CategoryEntry("Archer Spells"));
        list.addEntry(new FloatSliderEntry("Arrow Storm Speed", () -> ModConfig.get().arrowStormSpeed, v -> ModConfig.get().arrowStormSpeed = v));
        list.addEntry(new FloatSliderEntry("Escape Speed", () -> ModConfig.get().escapeSpeed, v -> ModConfig.get().escapeSpeed = v));
        list.addEntry(new FloatSliderEntry("Arrow Bomb Speed", () -> ModConfig.get().bombSpeed, v -> ModConfig.get().bombSpeed = v));
        list.addEntry(new FloatSliderEntry("Arrow Shield Speed", () -> ModConfig.get().arrowShieldSpeed, v -> ModConfig.get().arrowShieldSpeed = v));
        list.addEntry(new FloatSliderEntry("Bow Stance Speed", () -> ModConfig.get().bowStanceReadySpeed, v -> ModConfig.get().bowStanceReadySpeed = v));

        // Assassin spells
        list.addEntry(new CategoryEntry("Assassin Spells"));
        list.addEntry(new FloatSliderEntry("Spin Attack Speed", () -> ModConfig.get().spinAttackSpeed, v -> ModConfig.get().spinAttackSpeed = v));
        list.addEntry(new FloatSliderEntry("Dash Speed", () -> ModConfig.get().dashSpeed, v -> ModConfig.get().dashSpeed = v));
        list.addEntry(new FloatSliderEntry("Multi Hit Speed", () -> ModConfig.get().multiHitSpeed, v -> ModConfig.get().multiHitSpeed = v));
        list.addEntry(new FloatSliderEntry("Smoke Bomb Speed", () -> ModConfig.get().smokeBombSpeed, v -> ModConfig.get().smokeBombSpeed = v));

        // Warrior spells
        list.addEntry(new CategoryEntry("Warrior Spells"));
        list.addEntry(new FloatSliderEntry("Bash Speed", () -> ModConfig.get().bashSpeed, v -> ModConfig.get().bashSpeed = v));
        list.addEntry(new FloatSliderEntry("Charge Speed", () -> ModConfig.get().chargeSpeed, v -> ModConfig.get().chargeSpeed = v));
        list.addEntry(new FloatSliderEntry("War Scream Speed", () -> ModConfig.get().warScreamSpeed, v -> ModConfig.get().warScreamSpeed = v));
        list.addEntry(new FloatSliderEntry("Uppercut Speed", () -> ModConfig.get().uppercutSpeed, v -> ModConfig.get().uppercutSpeed = v));

        // Mage spells
        list.addEntry(new CategoryEntry("Mage Spells"));
        list.addEntry(new FloatSliderEntry("Heal Speed", () -> ModConfig.get().healSpeed, v -> ModConfig.get().healSpeed = v));
        list.addEntry(new FloatSliderEntry("Teleport Speed", () -> ModConfig.get().teleportSpeed, v -> ModConfig.get().teleportSpeed = v));
        list.addEntry(new FloatSliderEntry("Meteor Speed", () -> ModConfig.get().meteorSpeed, v -> ModConfig.get().meteorSpeed = v));
        list.addEntry(new FloatSliderEntry("Ice Snake Speed", () -> ModConfig.get().iceSnakeSpeed, v -> ModConfig.get().iceSnakeSpeed = v));

        // Shaman spells
        list.addEntry(new CategoryEntry("Shaman Spells"));
        list.addEntry(new FloatSliderEntry("Totem Speed", () -> ModConfig.get().totemSpeed, v -> ModConfig.get().totemSpeed = v));
        list.addEntry(new FloatSliderEntry("Haul Speed", () -> ModConfig.get().haulSpeed, v -> ModConfig.get().haulSpeed = v));
        list.addEntry(new FloatSliderEntry("Uproot Speed", () -> ModConfig.get().uprootSpeed, v -> ModConfig.get().uprootSpeed = v));
        list.addEntry(new FloatSliderEntry("Aura Speed", () -> ModConfig.get().auraSpeed, v -> ModConfig.get().auraSpeed = v));

        addDrawableChild(list);

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> {
            ModConfig.save();
            AnimationRegistry.applyConfig();
            client.setScreen(parent);
        }).dimensions(width / 2 - 100, height - 28, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 12, 0xFFFFFF);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    // --- List widget ---

    private static class ConfigListWidget extends ElementListWidget<ConfigEntry> {
        public ConfigListWidget(MinecraftClient client, int width, int height, int y, int itemHeight) {
            super(client, width, height, y, itemHeight);
        }

        @Override
        public int getRowWidth() {
            return 310;
        }

        public int addEntry(ConfigEntry entry) {
            super.addEntry(entry);
            return 0;
        }
    }

    // --- Entry types ---

    private static abstract class ConfigEntry extends ElementListWidget.Entry<ConfigEntry> {
    }

    private static class CategoryEntry extends ConfigEntry {
        private final Text label;

        CategoryEntry(String name) {
            this.label = Text.literal(name);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int x = getContentX();
            int entryWidth = getContentWidth();
            int textY = getContentY() + 6;
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, label, x + entryWidth / 2, textY, 0xFFFF55);
        }

        @Override
        public List<? extends Element> children() {
            return List.of();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }
    }

    private static class BooleanEntry extends ConfigEntry {
        private final ButtonWidget button;

        BooleanEntry(String label, Supplier<Boolean> getter, Consumer<Boolean> setter) {
            button = ButtonWidget.builder(Text.literal(label + ": " + (getter.get() ? "ON" : "OFF")), btn -> {
                boolean newVal = !getter.get();
                setter.accept(newVal);
                btn.setMessage(Text.literal(label + ": " + (newVal ? "ON" : "OFF")));
            }).dimensions(0, 0, 310, 20).build();
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            button.setX(getContentX());
            button.setY(getContentY());
            button.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(button);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(button);
        }
    }

    private static class FloatSliderEntry extends ConfigEntry {
        private final ConfigSlider slider;

        FloatSliderEntry(String label, Supplier<Float> getter, Consumer<Float> setter) {
            float min = 0.1f;
            float max = 6.0f;
            float current = getter.get();
            double ratio = (current - min) / (max - min);
            slider = new ConfigSlider(0, 0, 310, 20, label, ratio, min, max, getter, setter);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            slider.setX(getContentX());
            slider.setY(getContentY());
            slider.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(slider);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(slider);
        }
    }

    private static class IntSliderEntry extends ConfigEntry {
        private final IntConfigSlider slider;

        IntSliderEntry(String label, int min, int max, Supplier<Integer> getter, Consumer<Integer> setter) {
            int current = getter.get();
            double ratio = (double) (current - min) / (max - min);
            slider = new IntConfigSlider(0, 0, 310, 20, label, ratio, min, max, getter, setter);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            slider.setX(getContentX());
            slider.setY(getContentY());
            slider.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(slider);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(slider);
        }
    }

    // --- Slider widgets ---

    private static class ConfigSlider extends SliderWidget {
        private final String label;
        private final float min;
        private final float max;
        private final Consumer<Float> setter;

        ConfigSlider(int x, int y, int width, int height, String label, double value, float min, float max, Supplier<Float> getter, Consumer<Float> setter) {
            super(x, y, width, height, Text.empty(), value);
            this.label = label;
            this.min = min;
            this.max = max;
            this.setter = setter;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            float val = min + (float) value * (max - min);
            setMessage(Text.literal(label + ": " + String.format("%.1f", val)));
        }

        @Override
        protected void applyValue() {
            float val = min + (float) value * (max - min);
            val = Math.round(val * 10f) / 10f;
            setter.accept(val);
        }
    }

    private static class IntConfigSlider extends SliderWidget {
        private final String label;
        private final int min;
        private final int max;
        private final Consumer<Integer> setter;

        IntConfigSlider(int x, int y, int width, int height, String label, double value, int min, int max, Supplier<Integer> getter, Consumer<Integer> setter) {
            super(x, y, width, height, Text.empty(), value);
            this.label = label;
            this.min = min;
            this.max = max;
            this.setter = setter;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int val = min + (int) Math.round(value * (max - min));
            setMessage(Text.literal(label + ": " + val));
        }

        @Override
        protected void applyValue() {
            int val = min + (int) Math.round(value * (max - min));
            setter.accept(val);
        }
    }
}
