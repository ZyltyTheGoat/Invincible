package net.mcreator.invincible_craft.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class CommandFameAddProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments) {
		{
			InvincibleCraftModVariables.PlayerVariables _vars = (commandParameterEntity(arguments, "name")).getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
			_vars.fame = (commandParameterEntity(arguments, "name")).getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame + DoubleArgumentType.getDouble(arguments, "amount");
			_vars.markSyncDirty();
		}
		if ((commandParameterEntity(arguments, "name")).getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame > 5000000) {
			{
				InvincibleCraftModVariables.PlayerVariables _vars = (commandParameterEntity(arguments, "name")).getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
				_vars.fame = 5000000;
				_vars.markSyncDirty();
			}
		}
	}

	private static Entity commandParameterEntity(CommandContext<CommandSourceStack> arguments, String parameter) {
		try {
			return EntityArgument.getEntity(arguments, parameter);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}