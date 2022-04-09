
package com.github.clickinggames.enchantmentapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;

public class MyListener implements Listener {
    Plugin plugin;
    public MyListener(Plugin plugin){
        this.plugin=plugin;
    }

    @EventHandler
    public void EnchantItemEvent(EnchantItemEvent event) throws InstantiationException, IllegalAccessException {
        ItemStack itemStack= event.getItem();
        ItemMeta meta = itemStack.getItemMeta();
        if(meta==null){
            //meta = ItemMeta.class.newInstance();;
        }

        Player player = event.getEnchanter();
        ArrayList<CustomEnchant> customEnchants = EnchantmentAPI.getCustomEnchantList();
        ArrayList<String> Lore = new ArrayList<String>();

        for( int i = 0; i < customEnchants.size(); i++){
            if(!customEnchants.get(i).canEnchantItem(itemStack))
                return;

            int random = EnchantmentAPI.getRandomNumber(0,1000);
            if(customEnchants.get(i).chanceToAppear<random){
                int number = EnchantmentAPI.getRandomNumber(1,customEnchants.get(i).getMaxLevel());
                String numberToString = "";
                if(number>1)
                    numberToString=EnchantmentAPI.integerToRoman(number);
                if(customEnchants.get(i).isCursed())
                    Lore.add(ChatColor.RED +customEnchants.get(i).name+" " + numberToString);
                else
                    Lore.add(ChatColor.GRAY+customEnchants.get(i).name+" " + numberToString);
                meta.addEnchant(customEnchants.get(i),number,false);
            }

        }


        meta.setLore(Lore);

        itemStack.setItemMeta(meta);
    }

    @EventHandler
    public void PlayerCommandPreProcess(PlayerCommandPreprocessEvent event){
        if(event.getMessage()=="/reload"){
            Bukkit.broadcastMessage("EAPI: reloading the server is a bad idea, do not expect support from the plugin after reloading \n \n the plugin could break and enchantments will not register. use the regular /stop function as a replacement, as it does not bug out the plugin");
        }
    }

}
