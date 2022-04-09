package com.github.clickinggames.enchantmentapi;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomEnchant extends Enchantment {
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setTarget(EnchantmentTarget target) {
        this.target = target;
    }

    public EnchantmentTarget getTarget() {
        return target;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Enchantment> getConflicted() {
        return conflicted;
    }

    public void setKey(NamespacedKey key) {
        this.key = key;
    }

    public void setConflicted(ArrayList<Enchantment> conflicted) {
        this.conflicted = conflicted;
    }

    public void setCurse(boolean curse) {
        isCurse = curse;
    }

    public static NamespacedKey generateKey(Plugin plugin, String id){
        return new NamespacedKey(plugin,id);
    }

    public int getChanceToAppear() {
        return chanceToAppear;
    }

    public void setChanceToAppear(int chanceToAppear) {
        this.chanceToAppear = chanceToAppear;
    }

    public static ItemStack addEnchantment(ItemStack itemStack,CustomEnchant enchant,int level,boolean ignoreRestriction){
        if(ignoreRestriction){
            String numberToString="";
            ItemMeta meta = itemStack.getItemMeta();
            itemStack.addEnchantment(enchant,level);
            List<String> Lore = meta.getLore();
            if(enchant.getMaxLevel()>1)
                numberToString=EnchantmentAPI.integerToRoman(level);
            if(enchant.isCursed())
                Lore.add(ChatColor.RED +enchant.name+" " + numberToString);
            else
                Lore.add(ChatColor.GRAY+enchant.name+" " + numberToString);
            meta.setLore(Lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }
        else if(enchant.canEnchantItem(itemStack)){
            String numberToString="";
            ItemMeta meta = itemStack.getItemMeta();
            itemStack.addEnchantment(enchant,level);
            List<String> Lore = meta.getLore();
            if(Lore==null)
                Lore = new ArrayList<String>();
            if(enchant.getMaxLevel()>1)
                numberToString=EnchantmentAPI.integerToRoman(level);
            if(enchant.isCursed())
                Lore.add(ChatColor.RED +enchant.name+" " + numberToString);
            else
                Lore.add(ChatColor.GRAY+enchant.name+" " + numberToString);
            meta.setLore(Lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }
        return null;
    }

    NamespacedKey key;
    EnchantmentTarget target;
    int maxLevel;
    String id;
    String name;
    boolean isCurse;
    int chanceToAppear;
    ArrayList<Enchantment> conflicted = new ArrayList<Enchantment>();

    public CustomEnchant(NamespacedKey key, EnchantmentTarget target, int maxLevel, String name, boolean isCurse,int chanceToAppear, ArrayList<Enchantment> conflicted) {
        super(key);
        this.key=key;
        this.target=target;
        this.maxLevel=maxLevel;
        this.name=name;
        this.conflicted=conflicted;
        this.id=key.getKey();
        this.isCurse=isCurse;
        this.chanceToAppear=chanceToAppear;
        EnchantmentAPI.getCustomEnchantList().add(this);
        boolean registered = false;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null,true);
            Enchantment.registerEnchantment(this);

        }catch (Exception e){
            registered=false;
            e.printStackTrace();
        }
        if(registered){
            EnchantmentAPI.getCustomEnchantList().add(this);
        }
    }
    public CustomEnchant(NamespacedKey key,EnchantmentTarget target,int maxLevel,String name,boolean isCurse,int chanceToAppear) {
        super(key);
        this.key=key;
        this.target=target;
        this.maxLevel=maxLevel;
        this.name=name;
        this.id=key.getKey();
        this.isCurse=isCurse;
        this.chanceToAppear=chanceToAppear;
        EnchantmentAPI.getCustomEnchantList().add(this);
        boolean registered = false;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null,true);
            Enchantment.registerEnchantment(this);

        }catch (Exception e){
            registered=false;
            e.printStackTrace();
        }
        if(registered){
            EnchantmentAPI.getCustomEnchantList().add(this);
        }
    }

    @Override
    public boolean
    canEnchantItem(ItemStack item) {
        return target.includes(item);
    }

    @Override
    public boolean
    conflictsWith(Enchantment other){
        return conflicted.contains(other);
    }

    @Override
    public EnchantmentTarget
    getItemTarget() {
        return target;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return isCurse;
    }

    @Override
    public NamespacedKey getKey(){
        return key;
    }

    @Override
    public int
    getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String
    getName() {
        return name;
    }

    @Override
    public int
    getStartLevel() {
        return 0;
    }
}
