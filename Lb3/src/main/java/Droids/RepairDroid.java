package Droids;

public class RepairDroid extends Droid{
    double repair;
    double defrepair = 10;

    public RepairDroid(String name, double maxhp, double damage, double repair) {
        super(name, maxhp, damage);
        this.repair = repair;
    }
    public RepairDroid(String name, double maxhp, double damage) {
        super(name, maxhp, damage);
        this.repair = this.defrepair;
    }
    public void repair(Droid droid) {
        if(droid != null && droid.isAlive()) {
            droid.heal(this.repair);
            System.out.println("🔧 " + this.name + " відремонтував " + droid.getName() + " на " + this.repair + " HP!");
        }
    }

    public double getDefRepair() {return defrepair;}

    public double getRepair() {return repair;}

    @Override
    public void levelUp() {
        super.levelUp();
        this.repair*=1.15;
    }

    @Override
    public String toString() {
        String healthBar = createHealthBar();
        String xpBar = createXpBar();
        
        return String.format("""
                ╔════════════════════════════════════════
                ║ 🔧 РЕМОНТНИЙ ДРОЇД: %s
                ║ ⚡ Рівень: %d
                ║ ❤️  HP: %s [%.1f/%.1f]
                ║ 💫 XP: %s [%.1f/%.1f]
                ║ ⚔️  Урон: %.1f
                ║ 💚 Лікування: %.1f
                ╚════════════════════════════════════════
                """,
                name,
                level,
                healthBar, hp, maxhp,
                xpBar, xp, level * 100.0,
                damage,
                repair
        );
    }
    
    protected String createHealthBar() {
        return super.createHealthBar();
    }
    
    protected String createXpBar() {
       return super.createXpBar();
    }
}
