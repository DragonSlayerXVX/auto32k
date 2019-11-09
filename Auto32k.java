// 
// Decompiled by Procyon v0.5.36
// 

package com.backdoored.hacks.combat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import com.backdoored.utils.Utils;
import com.backdoored.utils.WorldUtils;
import net.minecraft.item.Item;
import com.backdoored.setting.Setting;
import com.backdoored.gui.CategoriesInit;
import com.backdoored.hacks.BaseHack;

public class Auto32k extends BaseHack
{
    private boolean executeEquip32k;
    
    public Auto32k() {
        super("Auto32k", CategoriesInit.COMBAT, "Instantly places shulker and hopper and grabs a 32k sword");
        this.executeEquip32k = false;
        new Setting("SecretClose", this, false);
    }
    
    public void onEnabled() {
        if (Auto32k.mc.field_71476_x == null || Auto32k.mc.field_71476_x.field_178784_b == null) {
            return;
        }
        if (!this.run()) {
            this.setEnabled(false);
        }
    }
    
    private boolean run() {
        BlockPos basePos = null;
        if (Auto32k.mc.field_71476_x == null || Auto32k.mc.field_71476_x.field_178784_b == null) {
            return false;
        }
        basePos = Auto32k.mc.field_71476_x.func_178782_a().func_177972_a(Auto32k.mc.field_71476_x.field_178784_b);
        if (WorldUtils.findItem(Item.func_150899_d(154)) == -1) {
            Utils.printMessage("A hopper was not found in your hotbar!", "red");
            this.setEnabled(false);
            return false;
        }
        for (int i = 219; i <= 234 && WorldUtils.findItem(Item.func_150899_d(i)) == -1; ++i) {
            if (i == 234) {
                Utils.printMessage("A shulker was not found in your hotbar!", "red");
                this.setEnabled(false);
                return false;
            }
        }
        Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = WorldUtils.findItem(Item.func_150899_d(154));
        WorldUtils.placeBlockMainHand(basePos);
        for (int i = 219; i <= 234; ++i) {
            if (WorldUtils.findItem(Item.func_150899_d(i)) != -1) {
                Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = WorldUtils.findItem(Item.func_150899_d(i));
                break;
            }
        }
        WorldUtils.placeBlockMainHand(new BlockPos(basePos.func_177958_n(), basePos.func_177956_o() + 1, basePos.func_177952_p()));
        if (this.getSetting("SecretClose").getValBoolean()) {
            BaseHack.setEnabled("Secret Close", false);
            BaseHack.setEnabled("Secret Close", true);
        }
        Auto32k.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(basePos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
        return this.executeEquip32k = true;
    }
    
    public void onUpdate() {
        if (!this.executeEquip32k || !this.getEnabled() || this.isSuperWeapon(Auto32k.mc.field_71439_g.field_71071_by.func_70448_g())) {
            return;
        }
        for (int x = 0; x < Auto32k.mc.field_71439_g.field_71070_bA.field_75151_b.size(); ++x) {
            if (this.isSuperWeapon(Auto32k.mc.field_71439_g.field_71070_bA.field_75151_b.get(x).func_75211_c())) {
                Auto32k.mc.field_71442_b.func_187098_a(Auto32k.mc.field_71439_g.field_71070_bA.field_75152_c, x, 0, ClickType.QUICK_MOVE, (EntityPlayer)Auto32k.mc.field_71439_g);
                for (int i = 0; i < 9; ++i) {
                    if (this.isSuperWeapon(Auto32k.mc.field_71439_g.field_71071_by.func_70301_a(i))) {
                        Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = i;
                        break;
                    }
                }
                if (this.getSetting("SecretClose").getValBoolean()) {
                    Auto32k.mc.field_71439_g.func_71053_j();
                }
                this.setEnabled(this.executeEquip32k = false);
                return;
            }
        }
    }
    
    private boolean isSuperWeapon(final ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.func_77978_p() == null) {
            return false;
        }
        if (item.func_77986_q().func_150303_d() == 0) {
            return false;
        }
        final NBTTagList enchants = (NBTTagList)item.func_77978_p().func_74781_a("ench");
        int i = 0;
        while (i < enchants.func_74745_c()) {
            final NBTTagCompound enchant = enchants.func_150305_b(i);
            if (enchant.func_74762_e("id") == 16) {
                final int lvl = enchant.func_74762_e("lvl");
                if (lvl >= 16) {
                    return true;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return false;
    }
}
