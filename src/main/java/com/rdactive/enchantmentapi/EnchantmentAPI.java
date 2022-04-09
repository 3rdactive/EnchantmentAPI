package com.rdactive.enchantmentapi;


import com.rdactive.enchantmentapi.commands.main;
import com.sun.tools.javac.util.List;
import jdk.tools.jmod.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class EnchantmentAPI extends JavaPlugin {
    static ArrayList<CustomEnchant> customEnchantList = new ArrayList<CustomEnchant>();

    public static ArrayList<CustomEnchant> getCustomEnchantList() {
        return customEnchantList;
    }

    public static void setCustomEnchantList(ArrayList<CustomEnchant> customEnchantList) {
        EnchantmentAPI.customEnchantList = customEnchantList;
    }

    @Override
    public void onLoad() {
        Bukkit.broadcastMessage("EAPI: Eapi finished loading! thank you for picking EAPI!");
    }

    @Override
    public void onEnable() {
        Bukkit.broadcastMessage("EAPI: Eapi is now enabled! thank you for picking EAPI!");
        this.getCommand("eapi").setExecutor(new main());

    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage("EAPI: Eapi is now shutting down! goodbye!");
    }

    static public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static String integerToRoman(int num) {

        System.out.println("Integer: " + num);
        int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romanLiterals = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};

        StringBuilder roman = new StringBuilder();

        for(int i=0;i<values.length;i++) {
            while(num >= values[i]) {
                num -= values[i];
                roman.append(romanLiterals[i]);
            }
        }
        return roman.toString();
    }

}
