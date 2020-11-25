package model.sources;

public class TableRowData {

    private String key;
    private String nickname;
    private String first_name;
    private String last_name;
    private String place;
    private String level;
    private String exp;
    private String HP;
    private String MP;
    private String SP;
    private String STR;
    private String GES;
    private String INT;
    private String AUS;
    private String KON;
    private String gamelevel;
    private String isJumping;
    private String skillpoints;
    private String resourcepoints;
    private String highscore;
    private String lastlogin;


    public TableRowData(String key, String nickname, String first_name, String last_name, String place, String level, String exp, String hp, String mp, String sp, String str, String ges, String inT, String aus, String kon, String gamelevel, String isJumping, String skillpoints, String resourcepoints, String highscore, String lastlogin) {
        this.key = key;
        this.nickname = nickname;
        this.first_name = first_name;
        this.last_name = last_name;
        this.place = place;
        this.level = level;
        this.exp = exp;
        this.HP = hp;
        this.MP = mp;
        this.SP = sp;
        this.STR = str;
        this.GES = ges;
        this.INT = inT;
        this.AUS = aus;
        this.KON = kon;
        this.gamelevel = gamelevel;
        this.isJumping = isJumping;
        this.skillpoints = skillpoints;
        this.resourcepoints = resourcepoints;
        this.highscore = highscore;
        this.lastlogin = lastlogin;
    }

    public String getName() {
        return key;
    }

    public String getPlayer_id() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getHP() {
        return HP;
    }

    public void setHP(String HP) {
        this.HP = HP;
    }

    public String getMP() {
        return MP;
    }

    public void setMP(String MP) {
        this.MP = MP;
    }

    public String getSP() {
        return SP;
    }

    public void setSP(String SP) {
        this.SP = SP;
    }

    public String getSTR() {
        return STR;
    }

    public void setSTR(String STR) {
        this.STR = STR;
    }

    public String getGES() {
        return GES;
    }

    public void setGES(String GES) {
        this.GES = GES;
    }

    public String getINT() {
        return INT;
    }

    public void setINT(String INT) {
        this.INT = INT;
    }

    public String getAUS() {
        return AUS;
    }

    public void setAUS(String AUS) {
        this.AUS = AUS;
    }

    public String getKON() {
        return KON;
    }

    public void setKON(String KON) {
        this.KON = KON;
    }

    public String getGamelevel() {
        return gamelevel;
    }

    public void setGamelevel(String gamelevel) {
        this.gamelevel = gamelevel;
    }

    public String getIsJumping() {
        return isJumping;
    }

    public void setIsJumping(String isJumping) {
        this.isJumping = isJumping;
    }

    public String getSkillpoints() {
        return skillpoints;
    }

    public void setSkillpoints(String skillpoints) {
        this.skillpoints = skillpoints;
    }

    public String getResourcepoints() {
        return resourcepoints;
    }

    public void setResourcepoints(String resourcepoints) {
        this.resourcepoints = resourcepoints;
    }

    public String getHighscore() {
        return highscore;
    }

    public void setHighscore(String highscore) {
        this.highscore = highscore;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFirst_Name() {
        return first_name;
    }

    public void setFirst_Name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_Name() {
        return last_name;
    }

    public void setLast_Name(String last_name) {
        this.last_name = last_name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}