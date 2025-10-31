package Droids;

public class  DefensiveDroid extends Droid {
    int taunt = 0;
    public DefensiveDroid(String name, double maxhp, double damage) {
        super(name, maxhp, damage);
        this.maxhp *= 1.5;
        this.hp =this.maxhp;
        this.damage *= 0.5;
    }

    @Override
    public String toString() {
        String healthBar = createHealthBar();
        String xpBar = createXpBar();
        
        return String.format("""
                ╔════════════════════════════════════════╗
                ║ 🛡️  ЗАХИСНИЙ ДРОЇД: %s
                ║ ⚡ Рівень: %d
                ║ ❤️  HP: %s [%.1f/%.1f]
                ║ 💫 XP: %s [%.1f/%.1f]
                ║ ⚔️  Урон: %.1f
                ║ 🎭 Насмішка: %d
                ╚════════════════════════════════════════╝
                        """,
                name,
                level,
                healthBar, hp, maxhp,
                xpBar, xp, level * 100.0,
                damage,
                taunt
        );
    }
    public void taunt(){
        this.taunt = 2;
    }
    public void stopTaunt(){
        this.taunt = 0;
    }
    public int getTaunt() {
        return taunt;
    }
    public void useTaunt(int taunt) {
        this.taunt--;
    }
    
    protected String createHealthBar() {
     return super.createHealthBar();
    }

    protected String createXpBar() {
        return super.createXpBar();
    }
}
