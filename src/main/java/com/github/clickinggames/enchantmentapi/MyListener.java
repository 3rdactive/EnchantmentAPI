
package com.github.clickinggames.enchantmentapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

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
            //roll a random number
            int random = EnchantmentAPI.getRandomNumber(0,1000);
            //does the enchantment appear?
            if(customEnchants.get(i).chanceToAppear<random){
                int number = EnchantmentAPI.getRandomNumber(1,customEnchants.get(i).getMaxLevel());
                String numberToString = "";
                //convert the number to roman if its bigger then 1
                if(number>1)
                    numberToString=EnchantmentAPI.integerToRoman(number);
                //add the custom text
                if(customEnchants.get(i).isCursed())
                    Lore.add(ChatColor.RED +customEnchants.get(i).name+" " + numberToString);
                else
                    Lore.add(ChatColor.GRAY+customEnchants.get(i).name+" " + numberToString);
                meta.addEnchant(customEnchants.get(i),number,false);
            }

        }

        //set the custom text!
        meta.setLore(Lore);

        itemStack.setItemMeta(meta);
    }

    @EventHandler
    public void PlayerCommandPreProcess(PlayerCommandPreprocessEvent event){
        if(event.getMessage()=="/reload"){
            Bukkit.broadcastMessage("EAPI: reloading the server is a bad idea, do not expect support from the plugin after reloading \n \n the plugin could break and enchantments will not register. use the regular /stop function as a replacement, as it does not bug out the plugin");
        }
    }

    @EventHandler
    public void PlayerInteractItem(InventoryClickEvent event){
        //check if player clicked on nothing
        if(event.getInventory()==null)
            return;
        //check if player clicked a non-result slot
        if(event.getSlotType()!= InventoryType.SlotType.RESULT)
            return;
        //check if a player clicked an empty slot
        if(event.getInventory().getItem(event.getSlot())==null)
            return;
        ItemStack resultItem = event.getInventory().getItem(event.getSlot());
        //check if item has a Meta
        if(!resultItem.hasItemMeta())
            return;
        List<CustomEnchant> enchants = EnchantmentAPI.getCustomEnchantList();
        List<String> lore = resultItem.getItemMeta().getLore();
        ItemMeta meta = resultItem.getItemMeta();
        List<String> WillRemove = new ArrayList<String>();
        //code for anvil
        if(event.getInventory().getType()== InventoryType.ANVIL){
            ItemStack slot0 = event.getInventory().getItem(0);
            ItemStack slot1 = event.getInventory().getItem(1);
            List<String> lore0 = slot0.getItemMeta().getLore();
            List<String> lore1 = slot1.getItemMeta().getLore();
            if(!slot0.getItemMeta().hasLore())
                lore0=new ArrayList<>();
            if(!slot1.getItemMeta().hasLore())
                lore1=new ArrayList<>();
            List<CustomEnchant> found0 = new ArrayList<CustomEnchant>();
            List<CustomEnchant> found1 = new ArrayList<CustomEnchant>();
            Map<CustomEnchant, Integer> toAdd = new HashMap<>();
            //scan enchantments on first item
            for(int i = 0; i < lore0.size(); i++){
                for(int l = 0; l < enchants.size(); l++){
                    if(lore0.get(i).toLowerCase().contains(enchants.get(l).getName().toLowerCase())){
                        found0.add(enchants.get(i));
                    }
                }
            }
            //scan enchantments on second item
            for(int i = 0; i < lore1.size(); i++){
                for(int l = 0; l < enchants.size(); l++){
                    if(lore1.get(i).toLowerCase().contains(enchants.get(l).getName().toLowerCase())){
                        found1.add(enchants.get(i));
                    }
                }
            }
            Bukkit.broadcastMessage(lore0.toString()+ " | + | " + lore1.toString());
            Bukkit.broadcastMessage(found0.toString()+ " | + | " + found1.toString());
            //combine enchantments

            for(int i = 0; i < found0.size(); i++){
                if(!toAdd.containsKey(found0.get(i))){
                    toAdd.put(found0.get(i),slot0.getEnchantmentLevel(found0.get(i)));
                }
                else if(toAdd.get(found0.get(i)).intValue()==slot0.getEnchantmentLevel(found0.get(i)))
                    toAdd.put(found0.get(i),slot0.getEnchantmentLevel(found0.get(i))+1);
                else if(toAdd.get(found0.get(i)).intValue()>slot0.getEnchantmentLevel(found0.get(i)))
                    continue;
                else
                    toAdd.put(found0.get(i),slot0.getEnchantmentLevel(found0.get(i))+1);

            }
            for(int  i= 0; i < found1.size(); i++){
                if(!toAdd.containsKey(found1.get(i))){
                    toAdd.put(found1.get(i),slot1.getEnchantmentLevel(found1.get(i)));
                }
                else if(toAdd.get(found1.get(i)).intValue()==slot1.getEnchantmentLevel(found1.get(i)))
                    toAdd.put(found1.get(i),slot1.getEnchantmentLevel(found1.get(i))+1);
                else if(toAdd.get(found1.get(i)).intValue()>slot1.getEnchantmentLevel(found1.get(i)))
                    continue;
                else
                    toAdd.put(found1.get(i),slot1.getEnchantmentLevel(found1.get(i))+1);
            }
            Bukkit.broadcastMessage(toAdd.keySet().toString());
            ItemMeta slot2meta = resultItem.getItemMeta();
            lore = slot2meta.getLore();
            if(!slot2meta.hasLore())
                lore=new ArrayList<>();
            for(int i = 0; i < lore.size(); i++){
                for(int l = 0; l < enchants.size(); l++){
                    if(lore.get(i).toLowerCase().contains(enchants.get(l).getName().toLowerCase())){
                        WillRemove.add(lore.get(i));
                    }
                }
            }
            //remove the enchantments
            lore.removeAll(WillRemove);
            slot2meta.setLore(lore);
            resultItem.setItemMeta(slot2meta);
            //Bukkit.broadcastMessage(toAdd.keySet().toArray().toString());
            for(int i = 0; i < toAdd.keySet().size(); i++){
                resultItem = CustomEnchant.addEnchantment(resultItem, (CustomEnchant) toAdd.keySet().toArray()[i],toAdd.get(toAdd.keySet().toArray()[i]).intValue(),false);

            }
            event.getInventory().setItem(event.getSlot(),resultItem);


        }
        //code for grindstone
        else if(event.getInventory().getType()== InventoryType.GRINDSTONE){
            //get custom enchantments on item
             for(int i = 0; i < lore.size(); i++){
                 for(int l = 0; l < enchants.size(); l++){
                     if(lore.get(i).toLowerCase().contains(enchants.get(l).getName().toLowerCase())){
                         WillRemove.add(lore.get(i));
                     }
                 }
             }
             //remove the enchantments
            lore.removeAll(WillRemove);
            meta.setLore(lore);



        }
        resultItem.setItemMeta(meta);
    }

}
