/*Copyright (c) [2015], [Sycholic]
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of Arrows4Players nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package com.sycholic.tridents4players;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author sycholic
 *
 */
public class PoachTridents extends JavaPlugin implements Listener {

    private boolean hardmodeEnabled;
    private boolean breakableEnabled;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.loadPluginConfig();
        getLogger().info("Plugin enabled and configuration loaded successfully!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTridentLaunch(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Trident x) {
            if (x.getShooter() instanceof Player player) {
                return;
            }
            if (this.breakableEnabled) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    x.remove();
                    return;
                }
            }
            if (!this.hardmodeEnabled) {
                x.setShooter(null);
            }
            x.setPickupStatus(PickupStatus.ALLOWED);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTridentHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Trident x) {
            if (x.getShooter() instanceof Player player) {
                ItemStack tridentItem = x.getItem();
                int loyaltyLevel = tridentItem.getEnchantmentLevel(Enchantment.LOYALTY);
                if (loyaltyLevel > 0) {
                    return;
                }
            }
            if (this.breakableEnabled) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    x.remove();
                }
            }
        }
    }

    public void loadPluginConfig() {
        this.reloadConfig();
        FileConfiguration config = this.getConfig();
        this.hardmodeEnabled = config.getBoolean("hardmode-Enabled", false);
        this.breakableEnabled = config.getBoolean("breakable-Enabled", false);
    }

    public boolean isHardmodeEnabled() {
        return hardmodeEnabled;
    }

    public boolean isBreakableEnabled() {
        return breakableEnabled;
    }
}
