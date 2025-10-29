package com.devjulen.coloredportals.common.event;

import com.devjulen.coloredportals.ColoredPortals;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ColoredPortals.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;

        BlockState state = event.getLevel().getBlockState(event.getPos());
        ItemStack heldItem = event.getItemStack();

        if (state.getBlock() instanceof NetherPortalBlock) {
            // Y que el jugador tenga un tinte en la mano
            if (heldItem.getItem() instanceof DyeItem dye) {
                // Obtenemos el color del tinte
                DyeColor color = dye.getDyeColor();

                // Lógica para cambiar el color del portal
                // (en este ejemplo, almacenamos el color en un "bloque alternativo" o propiedad)
                changePortalColor(event, color);

                event.getEntity().sendSystemMessage(Component.literal("¡Has cambiado el color del portal! : " + color.getName()));

                // Gastar el tinte
                if (!event.getEntity().isCreative()) {
                    heldItem.shrink(1);
                }

                // Cancelamos la acción por defecto
                event.setCanceled(true);
            }
        }
    }

    private static void changePortalColor(PlayerInteractEvent event, DyeColor color) {
        // Aquí es donde decides *cómo* cambiar el color.
        // Opción 1: reemplazar el bloque del portal por uno personalizado que tenga textura de ese color.
        // Opción 2: cambiar una propiedad visual (requiere modificar el renderizado del portal).
        // Ejemplo básico (reemplazando el bloque por un "portal personalizado"):
        var world = event.getLevel();
        var pos = event.getPos();

        switch (color) {
            // Change all the blocks of the portal for a new nether block
            case RED -> world.setBlock(pos, Blocks.RED_STAINED_GLASS_PANE.defaultBlockState(), 3);
            case YELLOW -> world.setBlock(pos, Blocks.YELLOW_STAINED_GLASS_PANE.defaultBlockState(), 3);
            case WHITE -> world.setBlock(pos, Blocks.NETHER_PORTAL.defaultBlockState(), 3);
        }
    }
}
