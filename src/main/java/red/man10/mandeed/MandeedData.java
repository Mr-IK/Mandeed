package red.man10.mandeed;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MandeedData {
    Mandeed plugin;
    MySQLManager mysql;
    public MandeedData(Mandeed plugin) {
        this.plugin = plugin;
        this.mysql = plugin.mysql;
    }

    class JobData{
        int id;
        int category;
        String uuid;
        double reward;
        String jobname;
    }

    public boolean playercontain(Player p) {
        String sql = "SELECT * FROM "+mysql.DB+".recruit WHERE uuid = '"+p.getUniqueId().toString()+"';";
        ResultSet rs = mysql.query(sql);
        if(rs == null){
            return false;
        }
        try {
            if(rs.next()) {
                // UUIDが一致するユーザが見つかった
                return true;
            }
            return false;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
    }
    public boolean playercontain(OfflinePlayer p) {
        String sql = "SELECT * FROM "+mysql.DB+".recruit WHERE uuid = '"+p.getUniqueId().toString()+"';";
        ResultSet rs = mysql.query(sql);
        if(rs == null){
            return false;
        }
        try {
            if(rs.next()) {
                // UUIDが一致するJobが見つかった
                return true;
            }
            return false;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
    }
    public boolean idcontain(int id) {
        String sql = "SELECT * FROM "+mysql.DB+".recruit WHERE id = "+id+";";
        ResultSet rs = mysql.query(sql);
        if(rs == null){
            return false;
        }
        try {
            if(rs.next()) {
                // IDが一致するJobが見つかった
                return true;
            }
            return false;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
    }
    public boolean jobnamecontain(String jobname) {
        String sql = "SELECT * FROM "+mysql.DB+".recruit WHERE jobname = "+jobname+";";
        ResultSet rs = mysql.query(sql);
        if(rs == null){
            return false;
        }
        try {
            if(rs.next()) {
                // jobnameが一致するJobが見つかった
                return true;
            }
            return false;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
    }
    public boolean categorycontain(int category) {
        if(category >= 1&&category <= 6){
            return true;
        }
        return false;
    }

    public boolean recruitcreate(Player p,String jobname,int category,Double reward) {
        String sql = "INSERT INTO "+mysql.DB+".recruit (jobname , name , uuid , category ,reward) VALUES ('"+jobname+"' , '"+p.getName()+"' , '"+p.getUniqueId().toString()+"' , "+category+" , "+reward+");";
        boolean done = mysql.execute(sql);
        if(done){
            Bukkit.broadcastMessage(plugin.prefix+"§a§l仕事速報: §e§l"+p.getName()+"さんの\"§6§l"+jobname+"§e§l\"が募集開始されました！§6§lカテゴリ: §e§l"+category+" §6§l報酬: §e§l$"+reward);
        }
        return done;
    }

    public boolean recruitdelete(Player p) {
        if(!playercontain(p)){
            return false;
        }
        String sql = "DELETE FROM "+mysql.DB+".recruit WHERE uuid = '"+p.getUniqueId().toString()+"';";
        boolean done = mysql.execute(sql);
        return done;
    }
    public boolean recruitdelete(OfflinePlayer p) {
        if(!playercontain(p)){
            return false;
        }
        String sql = "DELETE FROM "+mysql.DB+".recruit WHERE uuid = '"+p.getUniqueId().toString()+"';";
        boolean done = mysql.execute(sql);
        return done;
    }

    public JobData recruitgetfromplayer(Player p) {
        if (!playercontain(p)) {
            return null;
        }
        String sql = "select * from "+mysql.DB+".recruit where uuid = '"+p.getUniqueId().toString()+"';";
        ResultSet rs = mysql.query(sql);
        if(rs == null){
            return null;
        }
        try {
            if(rs.next()) {
                // UUIDが一致するJobが見つかった
                JobData job = new JobData();
                job.id = rs.getInt("id");
                job.category = rs.getInt("category");
                job.uuid = rs.getString("uuid");
                job.reward = rs.getDouble("reward");
                job.jobname = rs.getString("jobname");
                return job;
            }
            return null;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }
    public JobData recruitgetfromid(int id) {
        if (!idcontain(id)) {
            return null;
        }
        String sql = "select * from "+mysql.DB+".recruit where id = "+id+";";
        ResultSet rs = mysql.query(sql);
        if(rs == null){
            return null;
        }
        try {
            if(rs.next()) {
                // IDが一致するJobが見つかった
                JobData job = new JobData();
                job.id = rs.getInt("id");
                job.category = rs.getInt("category");
                job.uuid = rs.getString("uuid");
                job.reward = rs.getDouble("reward");
                job.jobname = rs.getString("jobname");
                return job;
            }
            return null;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }
    public JobData recruitgetfromjobname(String jobname) {
        if (!jobnamecontain(jobname)) {
            return null;
        }
        String sql = "select * from "+mysql.DB+".recruit where jobname = "+jobname+";";
        ResultSet rs = mysql.query(sql);
        if(rs == null){
            return null;
        }
        try {
            if(rs.next()) {
                // IDが一致するJobが見つかった
                JobData job = new JobData();
                job.id = rs.getInt("id");
                job.category = rs.getInt("category");
                job.uuid = rs.getString("uuid");
                job.reward = rs.getDouble("reward");
                job.jobname = rs.getString("jobname");
                return job;
            }
            return null;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }
    public ArrayList<JobData> recruitgetfromcategory(int category) {
        ArrayList<JobData> jobs = new ArrayList<JobData>();
        if (!categorycontain(category)) {
            return null;
        }
        String sql = "select * from "+mysql.DB+".recruit where category = "+category+";";
        ResultSet rs = mysql.query(sql);
        if(rs == null){
            return null;
        }
        try {
            while(rs.next()){
                // IDが一致するJobが見つかった
                JobData job = new JobData();
                job.id = rs.getInt("id");
                job.category = rs.getInt("category");
                job.uuid = rs.getString("uuid");
                job.reward = rs.getDouble("reward");
                job.jobname = rs.getString("jobname");
                jobs.add(job);
            }
            return jobs;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

}
