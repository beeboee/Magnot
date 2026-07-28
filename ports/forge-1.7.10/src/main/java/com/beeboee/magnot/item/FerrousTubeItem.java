package com.beeboee.magnot.item;

import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.MagnotEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public final class FerrousTubeItem extends Item {
    private static final int LIMIT = 24;
    private static final String HAS_FIRST = "MagnotHasFirstCorner";
    private static final String FIRST_X = "MagnotFirstX";
    private static final String FIRST_Y = "MagnotFirstY";
    private static final String FIRST_Z = "MagnotFirstZ";
    private static final String FIRST_DIMENSION = "MagnotFirstDimension";

    public FerrousTubeItem() {
        setMaxStackSize(1);
        setMaxDamage(99);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
                             int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote || !(world instanceof WorldServer)) {
            return true;
        }

        WorldServer serverWorld = (WorldServer) world;
        if (player.isSneaking()) {
            if (hasFirst(stack)) {
                clear(stack);
                player.addChatMessage(new ChatComponentTranslation("message.magnot.selection_cleared"));
            } else if (FerrousRegionSavedData.get(serverWorld).removeContaining(x, y, z)) {
                player.addChatMessage(new ChatComponentTranslation("message.magnot.region_removed"));
            }
            return true;
        }

        int dimension = world.provider.dimensionId;
        if (!hasFirst(stack) || dimension != getDimension(stack)) {
            setFirst(stack, x, y, z, dimension);
            player.addChatMessage(new ChatComponentTranslation("message.magnot.first_corner"));
            return true;
        }

        int firstX = getTag(stack).getInteger(FIRST_X);
        int firstY = getTag(stack).getInteger(FIRST_Y);
        int firstZ = getTag(stack).getInteger(FIRST_Z);
        int secondX = firstX + MathHelper.clamp_int(x - firstX, -LIMIT, LIMIT);
        int secondY = firstY + MathHelper.clamp_int(y - firstY, -LIMIT, LIMIT);
        int secondZ = firstZ + MathHelper.clamp_int(z - firstZ, -LIMIT, LIMIT);

        FerrousRegion region = FerrousRegionSavedData.get(serverWorld)
                .addRegion(firstX, firstY, firstZ, secondX, secondY, secondZ);
        clear(stack);
        player.addChatMessage(new ChatComponentTranslation("message.magnot.region_created"));
        MagnotEvents.showRegion(serverWorld, region);
        if (!player.capabilities.isCreativeMode) {
            stack.damageItem(2, player);
        }
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isRemote && !selected && hasFirst(stack)) {
            clear(stack);
        }
    }

    private static boolean hasFirst(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean(HAS_FIRST);
    }

    private static int getDimension(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag == null ? Integer.MIN_VALUE : tag.getInteger(FIRST_DIMENSION);
    }

    private static NBTTagCompound getTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    private static void setFirst(ItemStack stack, int x, int y, int z, int dimension) {
        NBTTagCompound tag = getTag(stack);
        tag.setBoolean(HAS_FIRST, true);
        tag.setInteger(FIRST_X, x);
        tag.setInteger(FIRST_Y, y);
        tag.setInteger(FIRST_Z, z);
        tag.setInteger(FIRST_DIMENSION, dimension);
    }

    private static void clear(ItemStack stack) {
        NBTTagCompound tag = getTag(stack);
        tag.removeTag(HAS_FIRST);
        tag.removeTag(FIRST_X);
        tag.removeTag(FIRST_Y);
        tag.removeTag(FIRST_Z);
        tag.removeTag(FIRST_DIMENSION);
    }
}
