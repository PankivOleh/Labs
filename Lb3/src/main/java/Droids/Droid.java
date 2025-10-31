package Droids;

public class Droid {
    double hp , maxhp;
    String name;
    double damage;
    int level;
    double xp;
    boolean dead;
    double defhp = 100 , defdamage = 15;

    public Droid(String name, double maxhp, double damage) {
        this.maxhp = maxhp;
        this.hp = maxhp;
        this.name = name;
        this.damage = damage;
        this.level = 1;
        this.xp = 0;
        this.dead = false;
    }

    public Droid() {}

    public double getHp() {
        return hp;
    }

    public double getMaxHp() {return maxhp;};

    public void setHp(double hp) {
        this.hp = hp;
        if(this.hp > maxhp){
            this.hp = maxhp;  // ✅ Виправлено
        }
        if(this.hp < 0){
            this.hp = 0;
        }
    }
    public double getDefHp() {
        return defhp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDamage() {
        return damage;
    }

    public double getDefDamage() {return defdamage;}

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        String healthBar = createHealthBar();
        String xpBar = createXpBar();
        
        return String.format("""
                ╔════════════════════════════════════════
                ║ 🤖 ДРОЇД: %s
                ║ ⚡ Рівень: %d
                ║ ❤️  HP: %s [%.1f/%.1f]
                ║ 💫 XP: %s [%.1f/%.1f]
                ║ ⚔️  Урон: %.1f
                ╚════════════════════════════════════════
                """,
                name,
                level,
                healthBar, hp, maxhp,
                xpBar, xp, level * 100.0,
                damage
        );
    }
    
    protected String createHealthBar() {
        int barLength = 20;
        double percentage = (hp / maxhp) * 100;
        int filled = (int) ((hp / maxhp) * barLength);
        
        String emoji;
        if (percentage > 66) {
            emoji = "💚";
        } else if (percentage > 33) {
            emoji = "💛";
        } else {
            emoji = "❤️";
        }
        
        StringBuilder bar = new StringBuilder();
        bar.append(emoji).append("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }
    
    protected String createXpBar() {
        int barLength = 20;
        double maxXp = level * 100.0;
        int filled = (int) ((xp / maxXp) * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("✨[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }

    public void destroyDroid() {
        this.dead = true;
    }


    public boolean takeDamage(double damage) {
        if ((this.hp - damage) <= 0) {
            hp = 0;
            this.destroyDroid();
            return false;
        } else {
            this.hp -= damage;
        }
        return true;
    }

    public void levelUp(){
        this.level++;
        this.damage *= 1.15;
        this.maxhp *= 1.15;
        this.hp = this.maxhp;
    }

    public boolean takeXp(Droid droid) {
        this.xp += 50 * (droid.getLevel() * 0.75);
        if (this.xp >= this.getLevel() * 100) {
            this.levelUp();
            System.out.println(this.name+" LEVEL UP!");
            return true;
        }
        return false;
    }

    public boolean attack(Droid droid) {
        if (droid.takeDamage(this.damage)) {
            return false;
        }
        else {
            this.takeXp(droid);
            return true;
        }
    }

    public boolean isDead() {
        return this.dead;
    }
    
    public boolean isAlive() {
        return !this.dead;
    }
    
    public void heal(double amount) {
        if(!this.dead) {
            this.hp += amount;
            if(this.hp > this.maxhp) {
                this.hp = this.maxhp;
            }
        }
    }

    public void revive() {
        this.hp = this.maxhp;
        this.dead = false;
    }
}
