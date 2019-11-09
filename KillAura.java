// 
// Decompiled by Procyon v0.5.36
// 

package com.backdoored.hacks.combat;

import com.backdoored.utils.NoStackTraceThrowable;
import com.backdoored.DrmManager;
import net.minecraftforge.fml.common.FMLLog;
import com.backdoored.Backdoored;
import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.util.EnumHand;
import com.backdoored.utils.FriendUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import com.backdoored.gui.CategoriesInit;
import com.backdoored.setting.Setting;
import com.backdoored.hacks.BaseHack;

public class KillAura extends BaseHack
{
    private Setting delay;
    private int hasWaited;
    
    public KillAura() {
        super("Kill Aura", CategoriesInit.COMBAT, "Attack players near you");
        this.hasWaited = 0;
        new Setting("Range", this, 5.0, 1.0, 15.0);
        new Setting("32k Only", this, false);
        new Setting("Players only", this, true);
        this.delay = new Setting("Delay in ticks", this, 0, 0, 50);
    }
    
    public void onUpdate() {
        if (!this.getEnabled() || KillAura.mc.field_71439_g.field_70128_L || KillAura.mc.field_71441_e == null) {
            return;
        }
        if (this.hasWaited < this.delay.getValInt()) {
            ++this.hasWaited;
            return;
        }
        this.hasWaited = 0;
        for (final Entity entity : KillAura.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityLivingBase) {
                if (entity == KillAura.mc.field_71439_g) {
                    continue;
                }
                if (KillAura.mc.field_71439_g.func_70032_d(entity) > this.getSetting("Range").getValDouble() || ((EntityLivingBase)entity).func_110143_aJ() <= 0.0f || (!(entity instanceof EntityPlayer) && this.getSetting("Players only").getValBoolean()) || (entity instanceof EntityPlayer && FriendUtils.isFriend((EntityPlayer)entity)) || (!this.isSuperWeapon(KillAura.mc.field_71439_g.func_184614_ca()) && this.getSetting("32k Only").getValBoolean())) {
                    continue;
                }
                KillAura.mc.field_71442_b.func_78764_a((EntityPlayer)KillAura.mc.field_71439_g, entity);
                KillAura.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
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
    
    public void onDisabled() {
        checkDRM();
    }
    
    private static String getHWID() {
        final String hwid = System.getenv("os") + System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("os.version") + System.getProperty("user.language") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS");
        return Hashing.sha512().hashString((CharSequence)hwid, StandardCharsets.UTF_8).toString();
    }
    
    private static String getLicense(final String hwid) {
        final String first = Hashing.sha512().hashString((CharSequence)hwid, StandardCharsets.UTF_8).toString();
        final String second = Hashing.sha512().hashString((CharSequence)first, StandardCharsets.UTF_8).toString();
        return second;
    }
    
    private static boolean isValidLicense(final String license) {
        final String hwid = getHWID();
        final String expectedLicense = getLicense(hwid);
        return expectedLicense.equalsIgnoreCase(license);
    }
    
    private static void checkDRM() {
        if (!isValidLicense(Backdoored.providedLicense)) {
            FMLLog.log.info("Invalid License detected");
            FMLLog.log.info("Provided License: " + Backdoored.providedLicense);
            FMLLog.log.info("HWID: " + getHWID());
            DrmManager.hasCrashed = true;
            throw new NoStackTraceThrowable("Invalid License");
        }
    }
}
