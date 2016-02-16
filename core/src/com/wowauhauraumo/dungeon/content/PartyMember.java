package com.wowauhauraumo.dungeon.content;

import java.util.Random;

public class PartyMember {

    private Random statRandom;

    // characteristics
    private String name; // display name
    private Job job;
    private Sprite sprite; // sprite prefix

    /**
     * stats; almost exactly the same as final fantasy 1
     * <p/>
     * initial stats are a number like 5 with the lvlup process done on it:
     * <p/>
     * when levelling up, all of the stats below increase based on a multiplier contained in the Job and a random factor
     * based on the multiplier. e.g. if the multiplier is 1.25, the randomly generated actual multiplier would be 1.25 +
     * (random.nextDouble() - 0.5) <-- this is crap
     */
    private int level; // whenever this is increased, all stats increase (depending on job and some random)
    private int xp; // running total of xp
    private int maxHP; // maximum health increases on lvlup
    private int currentHP; // current health value (kept as a percentage of maxHP on lvlup)
    private int xpForLvl; // a certain number of experience points cause level to increase
    private int strength; // included in final physical damage calculation
    private int agility; // included in final evade calculation. also used in run calculation (alone)
    private int intelligence; // (should be) included in final spell and item damage calculation
    private int vitality; // included in calculation for amount of maxHP gained on lvlup
    private int luck; // affects chance of item drops
    private int evadePc; // chance to avoid an attack (combined with agility)
    private int armor; // reduces enemy physical attack damage
    private int magicDefence; // reduces enemy magic[/item] damage

    // item stats - not changed by lvlup at all
    private int damage; // affected by weapon damage; included in physical damage calculation
    private int accuracyPc; // chance of hitting, and of how many hits

    public PartyMember(String name, Job job, Random statRandom) {
        this.statRandom = statRandom;
        setName(name);
        setJob(job);
        setSprite(job.spritePrefix);

        setLevel(1);
        setXp(0);
        setMaxHP(levelStat(5, job.maxHPMultiplier));
        setCurrentHP(maxHP);
        setXpForLvl(levelStat(5, job.xpForLvlMultiplier));
        setStrength(levelStat(5, job.strengthMultiplier));
        setAgility(levelStat(5, job.agilityMultiplier));
        setIntelligence(levelStat(5, job.intelligenceMultiplier));
        setVitality(levelStat(5, job.vitalityMultiplier));
        setLuck(levelStat(5, job.luckMultiplier));
        setEvadePc(levelStat(5, job.evadePcMultiplier));
        setArmor(levelStat(5, job.armorMultiplier));
        setMagicDefence(levelStat(5, job.magicDefenceMultiplier));

        setDamage(0);
        setAccuracyPc(0);
    }

    /**
     * Level up all of the character's stats. Called when xp reaches xpForLvl.
     */
    public void levelUp() {
        level++;
    }

    /**
     * Calculate the new value for a stat
     *
     * @param stat         the current value of the stat
     * @param statModifier the default multiplier of the stat (Job._Multiplier)
     * @return the new value for this level
     */
    public int levelStat(int stat, double statModifier) {
        // please change this algorithm
        double multiplier = statModifier + (statRandom.nextDouble() * 0.1 - 0.5);
        return (int) (stat * multiplier);
    }

    public enum Job {
        WARRIOR(Sprite.WARRIOR, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        WMAGE(Sprite.WMAGE, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        BMAGE(Sprite.BMAGE, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        MONK(Sprite.MONK, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        THIEF(Sprite.THIEF, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        RMAGE(Sprite.RMAGE, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

        // what is my life
        public final Sprite spritePrefix; // prefix for sprite names
        public final int levelMultiplier;
        public final int maxHPMultiplier;
        public final int currentHPMultiplier;
        public final int xpForLvlMultiplier;
        public final int strengthMultiplier;
        public final int agilityMultiplier;
        public final int intelligenceMultiplier;
        public final int vitalityMultiplier;
        public final int luckMultiplier;
        public final int evadePcMultiplier;
        public final int armorMultiplier;
        public final int magicDefenceMultiplier;

        Job(
                Sprite s, int levelMultiplier, int maxHPMultiplier, int currentHPMultiplier,
                int xpForLvlMultiplier, int strengthMultiplier, int agilityMultiplier, int intelligenceMultiplier,
                int vitalityMultiplier, int luckMultiplier, int evadePcMultiplier, int armorMultiplier,
                int magicDefenceMultiplier
        ) {
            spritePrefix = s;
            this.levelMultiplier = levelMultiplier;
            this.maxHPMultiplier = maxHPMultiplier;
            this.currentHPMultiplier = currentHPMultiplier;
            this.xpForLvlMultiplier = xpForLvlMultiplier;
            this.strengthMultiplier = strengthMultiplier;
            this.agilityMultiplier = agilityMultiplier;
            this.intelligenceMultiplier = intelligenceMultiplier;
            this.vitalityMultiplier = vitalityMultiplier;
            this.luckMultiplier = luckMultiplier;
            this.evadePcMultiplier = evadePcMultiplier;
            this.armorMultiplier = armorMultiplier;
            this.magicDefenceMultiplier = magicDefenceMultiplier;
        }
    }

    // getters and setters very nicely formatted (using spaces)
    // because I thought I should make something look nice about this code
    // yes this could be even better but meh I'm not that ocd
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getMaxHP() { return maxHP; }

    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }

    public Job getJob() { return job; }

    public void setJob(Job job) { this.job = job; }

    public Sprite getSprite() { return sprite; }

    public void setSprite(Sprite sprite) { this.sprite = sprite; }

    public int getCurrentHP() { return currentHP; }

    public void setCurrentHP(int hp) { this.currentHP = hp; }

    public int getLevel() { return level; }

    public void setLevel(int level) { this.level = level; }

    public int getXp() { return xp; }

    public void setXp(int xp) { this.xp = xp; }

    public int getXpForLvl() { return xpForLvl; }

    public void setXpForLvl(int xpForLvl) { this.xpForLvl = xpForLvl; }

    public int getStrength() { return strength; }

    public void setStrength(int strength) { this.strength = strength; }

    public int getAgility() { return agility; }

    public void setAgility(int agility) { this.agility = agility; }

    public int getIntelligence() { return intelligence; }

    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }

    public int getVitality() { return vitality; }

    public void setVitality(int vitality) { this.vitality = vitality; }

    public int getLuck() { return luck; }

    public void setLuck(int luck) { this.luck = luck; }

    public int getEvadePc() { return evadePc; }

    public void setEvadePc(int evadePc) { this.evadePc = evadePc; }

    public int getArmor() { return armor; }

    public void setArmor(int armor) { this.armor = armor; }

    public int getMagicDefence() { return magicDefence; }

    public void setMagicDefence(int magicDefence) { this.magicDefence = magicDefence; }

    public int getDamage() { return damage; }

    public void setDamage(int damage) { this.damage = damage; }

    public int getAccuracyPc() { return accuracyPc; }

    public void setAccuracyPc(int accuracyPc) { this.accuracyPc = accuracyPc; }
    // java rulez

}
