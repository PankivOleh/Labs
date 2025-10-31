package Droids;
import java.util.Random;

public class AttackDroid extends Droids.Droid{
    double extraDamage;
    public AttackDroid(String name, double maxhp, double damage) {
        super(name, maxhp, damage);
        this.maxhp*=0.5;
        this.damage*=2.5;
        this.hp = this.maxhp;
        this.extraDamage= 2.;
    }

    @Override
    public boolean attack(Droid droid) {
        Random rand = new Random();
        int i = rand.nextInt(10);
        if (i == 0) {
            if (droid.takeDamage(this.damage * this.extraDamage)) {
                return false;
            } else {
                this.takeXp(droid);
                return true;
            }
        }else{
            return super.attack(droid);
        }
    }

    @Override
    public void levelUp() {
        super.levelUp();
        this.extraDamage+=0.1;
    }

    @Override
    public String toString() {
        String healthBar = createHealthBar();
        String xpBar = createXpBar();
        
        return String.format("""
                ╔════════════════════════════════════════
                ║ ⚔️  АТАКУЮЧИЙ ДРОЇД: %s
                ║ ⚡ Рівень: %d
                ║ ❤️  HP: %s [%.1f/%.1f]
                ║ 💫 XP: %s [%.1f/%.1f]
                ║ 🗡️  Урон: %.1f
                ║ 💥 Критичний урон: %.0f%%
                ╚════════════════════════════════════════
                """,
                name,
                level,
                healthBar, hp, maxhp,
                xpBar, xp, level * 100.0,
                damage,
                extraDamage * 100
        );
    }
    
    protected String createHealthBar() {
       return super.createHealthBar();
    }
    
    protected String createXpBar() {
     return super.createXpBar();
    }
}
