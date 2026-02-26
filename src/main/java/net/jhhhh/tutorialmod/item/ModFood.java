package net.jhhhh.tutorialmod.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFood {
    public static final FoodProperties STRAWBERRY = new FoodProperties.Builder().fast()
            .nutrition(2).saturationMod(0.3f)
            .effect( () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,200),1.0f).build();
    public static final FoodProperties PINE_CONE = new FoodProperties.Builder().fast()
            .nutrition(1).saturationMod(0.5f).build();
}
