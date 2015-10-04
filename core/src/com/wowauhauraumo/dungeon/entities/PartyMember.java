package com.wowauhauraumo.dungeon.entities;

public class PartyMember {
	
	private String name;
	private int maxHP;
	private int currentHP;
	private Job job;
	
	public PartyMember(String name, int maxHP, Job job) {
		this.name = name;
		this.maxHP = maxHP;
		currentHP = maxHP;
		this.setJob(job);
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public int getMaxHP() { return maxHP; }
	public void setMaxHP(int maxHP) { this.maxHP = maxHP; }
	public Job getJob() { return job; }
	public void setJob(Job job) { this.job = job; }
	public int getCurrentHP() { return currentHP; }
	public void setCurrentHP(int hp) { this.currentHP = hp; }

	public enum Job {
		WARRIOR(Sprite.WARRIOR), WMAGE(Sprite.WMAGE), BMAGE(Sprite.BMAGE), 
		MONK(Sprite.MONK), THIEF(Sprite.THIEF), RMAGE(Sprite.RMAGE);
		
		public final Sprite spriteType;
		
		private Job(Sprite s) {
			spriteType = s;
		}
	}
	
}
