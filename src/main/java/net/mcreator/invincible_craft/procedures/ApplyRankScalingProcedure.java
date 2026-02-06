package net.mcreator.invincible_craft.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ApplyRankScalingProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world.dayTime() % 20 == 0) {
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 1000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 10000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 2, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, -1, 1, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 0, true, false));
				if (entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(Attributes.ARMOR))
					_livingEntity4.getAttribute(Attributes.ARMOR).setBaseValue(5);
				if (entity instanceof LivingEntity _livingEntity5 && _livingEntity5.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS))
					_livingEntity5.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(1);
				if (entity instanceof LivingEntity _livingEntity6 && _livingEntity6.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE))
					_livingEntity6.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.3);
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 10000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 100000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 6, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, -1, 3, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 1, true, false));
				if (entity instanceof LivingEntity _livingEntity10 && _livingEntity10.getAttributes().hasAttribute(Attributes.ARMOR))
					_livingEntity10.getAttribute(Attributes.ARMOR).setBaseValue(10);
				if (entity instanceof LivingEntity _livingEntity11 && _livingEntity11.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS))
					_livingEntity11.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(3);
				if (entity instanceof LivingEntity _livingEntity12 && _livingEntity12.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE))
					_livingEntity12.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.3);
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 100000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 500000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 12, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, -1, 5, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 2, true, false));
				if (entity instanceof LivingEntity _livingEntity16 && _livingEntity16.getAttributes().hasAttribute(Attributes.ARMOR))
					_livingEntity16.getAttribute(Attributes.ARMOR).setBaseValue(17);
				if (entity instanceof LivingEntity _livingEntity17 && _livingEntity17.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS))
					_livingEntity17.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(6);
				if (entity instanceof LivingEntity _livingEntity18 && _livingEntity18.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE))
					_livingEntity18.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.6);
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 500000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 1500000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 20, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, -1, 7, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 2, true, false));
				if (entity instanceof LivingEntity _livingEntity22 && _livingEntity22.getAttributes().hasAttribute(Attributes.ARMOR))
					_livingEntity22.getAttribute(Attributes.ARMOR).setBaseValue(23);
				if (entity instanceof LivingEntity _livingEntity23 && _livingEntity23.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS))
					_livingEntity23.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(12);
				if (entity instanceof LivingEntity _livingEntity24 && _livingEntity24.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE))
					_livingEntity24.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(2.5);
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 1500000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 3000000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 28, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, -1, 8, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, true, false));
				if (entity instanceof LivingEntity _livingEntity28 && _livingEntity28.getAttributes().hasAttribute(Attributes.ARMOR))
					_livingEntity28.getAttribute(Attributes.ARMOR).setBaseValue(27);
				if (entity instanceof LivingEntity _livingEntity29 && _livingEntity29.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS))
					_livingEntity29.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(18);
				if (entity instanceof LivingEntity _livingEntity30 && _livingEntity30.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE))
					_livingEntity30.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(2.9);
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 3000000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame <= 5000000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 34, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, -1, 9, true, false));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, true, false));
				if (entity instanceof LivingEntity _livingEntity34 && _livingEntity34.getAttributes().hasAttribute(Attributes.ARMOR))
					_livingEntity34.getAttribute(Attributes.ARMOR).setBaseValue(30);
				if (entity instanceof LivingEntity _livingEntity35 && _livingEntity35.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS))
					_livingEntity35.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(25);
				if (entity instanceof LivingEntity _livingEntity36 && _livingEntity36.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE))
					_livingEntity36.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(2.9);
			}
		}
	}
}