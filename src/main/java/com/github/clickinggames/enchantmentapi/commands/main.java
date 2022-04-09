package com.rdactive.enchantmentapi.commands;

import com.rdactive.enchantmentapi.CustomEnchant;
import com.rdactive.enchantmentapi.EnchantmentAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class main implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = sender.getServer().getPlayer(sender.getName());
        if(args.length<1){
            player.sendMessage(ChatColor.YELLOW + "EAPI: " +
                    "\n "+ChatColor.AQUA+" list | displays the list of all custom enchants " +
                    "\n reload | reloads settings from config " +
                    "\n ench {ID} {LVL} | applies the custom enchantment with the ID to the item you are holding " +
                    "\n info | returns some info on the plugin " +
                    "\n howtouse | returns with a link of how to use the EAPI" +
                    "\n version | tells you the current version of the plugin" +
                    "\n updatelink | gives you the link for the download file to update");
            return true;
        }
        else if (args[0].equalsIgnoreCase("list")){
            String list = "";
            for(int i = 0; i < EnchantmentAPI.getCustomEnchantList().size(); i++)
                list = list + EnchantmentAPI.getCustomEnchantList().get(i).getName() + " ";
            if(list=="")
                player.sendMessage(ChatColor.GREEN+"No custom enchants have been registered");
            else
                player.sendMessage(ChatColor.GREEN+"Enchantment list: " + ChatColor.AQUA + list);


        }
        else if (args[0].equalsIgnoreCase("reload")){
            player.sendMessage(ChatColor.RED + "This sub-command is still in development >:o");
        }
        else if (args[0].equalsIgnoreCase("ench")){
            if(args.length<3){
                player.sendMessage(ChatColor.YELLOW + "EAPI: " +
                        "\n "+ChatColor.AQUA+" list | displays the list of all custom enchants " +
                        "\n reload | reloads settings from config " +
                        "\n ench {ID} {LVL} | applies the custom enchantment with the ID to the item you are holding " +
                        "\n info | returns some info on the plugin " +
                        "\n howtouse | returns with a link of how to use the EAPI" +
                        "\n version | tells you the current version of the plugin" +
                        "\n updatelink | gives you the link for the download file to update");
                return true;
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item==null){
                player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"You must hold an item to enchant it!");
                return true;
            }
            int level = Integer.parseInt(args[2]);
            CustomEnchant enchant = null;
            for(int i = 0; i < EnchantmentAPI.getCustomEnchantList().size(); i++){
                if(EnchantmentAPI.getCustomEnchantList().get(i).getId().equalsIgnoreCase(args[1])){
                    if(enchant!=null){
                        player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"The enchant you are looking for has been registered TWICE! this is a massive issue and you should check your code for any double registrations \n aldo it can also be caused by reloading the plugin with the /reload command \n to fix this reboot your server using /restart or form your console");
                    }
                    enchant=EnchantmentAPI.getCustomEnchantList().get(i);
                }
            }
            if(enchant==null){
                player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"The enchant with the id of: " + args[1]+ " cannot be found, this could be typo, the registration does not work or an error with the EAPI plugin");
            }
            boolean forced = false;
            if(args.length>3 && args[3].equalsIgnoreCase("-force")){
                forced=true;
            }
            else{
                if(!enchant.canEnchantItem(item)){
                    player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"The item you are holding does not support the enchantment. \n use -force at the end of the command to enchant anyway");
                    return true;
                }
                if(level>enchant.getMaxLevel() || level > 0){
                    player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"The enchantment does not support the level you entered. \n use -force at the end of the command to enchant anyway");
                    return true;
                }
            }
            CustomEnchant.addEnchantment(item,enchant,level,forced);
            if(item.containsEnchantment(enchant)){
                player.sendMessage(ChatColor.GREEN+"The enchant: " + enchant.getName() + " On level: " + level + " Was added to your: " + item.getType().name());
            }
            else{
                player.sendMessage(ChatColor.RED+"The enchant: " + enchant.getName() + " On level: " + level + " on item: " + item.getType().name() + " could not be added, it could be a server error, an enchantment error or a Spigot error");
            }


        }
        else if (args[0].equalsIgnoreCase("info")){
            player.sendMessage(ChatColor.RED + "This sub-command is still in development >:o");
        }
        else if (args[0].equalsIgnoreCase("howtouse")){
            player.sendMessage(ChatColor.RED + "This sub-command is still in development >:o");
        }
        else if (args[0].equalsIgnoreCase("version")){
            player.sendMessage(ChatColor.GREEN + "The version of EAPI is: 1.0.0");
        }
        else if (args[0].equalsIgnoreCase("updatelink")){

        }
        return true;
    }
}