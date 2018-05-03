package red.man10.mandeed;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class Mandeed extends JavaPlugin implements Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    getServer().getPluginManager().disablePlugin(this);
                    getServer().getPluginManager().enablePlugin(this);
                    getLogger().info("設定を再読み込みしました。");
                    return true;
                }
            }
            getLogger().info("mdeed reload");
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage("==========§d§l●§f§l●§a§l●" + prefix + "§a§l●§f§l●§d§l●§r==========");
            p.sendMessage("§e--プレイヤー向けヘルプ--");
            p.sendMessage("§e/mdeed search [id/player/jobname/category] [内容] : 仕事をサーチします。");
            p.sendMessage("§e/mdeed recruit [ジョブ名] [カテゴリー] [報酬] : 仕事を募集します。");
            p.sendMessage("§e/mdeed accept [id/player/jobname] [内容]: 仕事を受諾します。");
            p.sendMessage("§e/mdeed cancel: 募集している仕事をキャンセルします");
            p.sendMessage("");
            p.sendMessage("§e[カテゴリ一覧] 1:釣り代行 2:スロット代行 3:建築 4:採掘・材料集め 5:整地 6:その他");
            if (p.hasPermission("mandeed.adminhelp")) {
                p.sendMessage("§c--admin向けヘルプ--");
                p.sendMessage("§c/mdeed delete [id/player/jobname] [内容] : 特定のjobを削除します");
            }
            p.sendMessage("==========§d§l●§f§l●§a§l●" + prefix + "§a§l●§f§l●§d§l●§r==========");
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("cancel")) {
                MandeedData.JobData getjob = data.recruitgetfromplayer(p);
                if (getjob == null) {
                    p.sendMessage(prefix + "§c§l結果: あなたは募集をしていませんでした");
                    return true;
                }
                data.recruitdelete(p);
                p.sendMessage(prefix + "§a§l結果: キャンセルに成功しました。");
                return true;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("search")) {
                if (args[1].equalsIgnoreCase("id")) {
                    int id = -1;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(prefix + "§4数字を入力してください");
                        return true;
                    }
                    MandeedData.JobData getjob = data.recruitgetfromid(id);
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l検索結果: そのidのjobは見つかりませんでした");
                        return true;
                    }
                    p.sendMessage(prefix + "§a§l検索結果: Hitしました。");
                    String pname = null;
                    if (Bukkit.getPlayer(UUID.fromString(getjob.uuid)) != null) {
                        pname = Bukkit.getPlayer(UUID.fromString(getjob.uuid)).getName();
                    } else {
                        pname = Bukkit.getOfflinePlayer(UUID.fromString(getjob.uuid)).getName();
                    }
                    p.sendMessage(prefix + "§eid:§6" + getjob.id + " §eカテゴリ:§6" + getjob.category + " §ejob名:§6" + getjob.jobname + " §e報酬:§6" + getjob.reward + "§e募集者:§6" + pname);
                    return true;
                } else if (args[1].equalsIgnoreCase("player")) {
                    String pname = null;
                    if (Bukkit.getPlayer(args[1]) != null) {
                        pname = args[1];
                    } else {
                        p.sendMessage(prefix + "§4オンラインのプレイヤー名を入力してください");
                        return true;
                    }
                    MandeedData.JobData getjob = data.recruitgetfromplayer(Bukkit.getPlayer(args[1]));
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l検索結果: そのプレイヤー名のjobは見つかりませんでした");
                        return true;
                    }
                    p.sendMessage(prefix + "§a§l検索結果: Hitしました。");
                    p.sendMessage(prefix + "§eid:§6" + getjob.id + " §eカテゴリ:§6" + getjob.category + " §ejob名:§6" + getjob.jobname + " §e報酬:§6" + getjob.reward + "§e募集者:§6" + pname);
                    return true;
                } else if (args[1].equalsIgnoreCase("jobname")) {
                    MandeedData.JobData getjob = data.recruitgetfromjobname(args[2]);
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l検索結果: そのjob名のjobは見つかりませんでした");
                        return true;
                    }
                    p.sendMessage(prefix + "§a§l検索結果: Hitしました。");
                    String pname = null;
                    if (Bukkit.getPlayer(UUID.fromString(getjob.uuid)) != null) {
                        pname = Bukkit.getPlayer(UUID.fromString(getjob.uuid)).getName();
                    } else {
                        pname = Bukkit.getOfflinePlayer(UUID.fromString(getjob.uuid)).getName();
                    }
                    p.sendMessage(prefix + "§eid:§6" + getjob.id + " §eカテゴリ:§6" + getjob.category + " §ejob名:§6" + getjob.jobname + " §e報酬:§6" + getjob.reward + "§e募集者:§6" + pname);
                    return true;
                } else if (args[1].equalsIgnoreCase("category")) {
                    int category = -1;
                    try {
                        category = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(prefix + "§4数字を入力してください");
                        return true;
                    }
                    ArrayList<MandeedData.JobData> jobs = data.recruitgetfromcategory(category);
                    if (jobs.isEmpty()) {
                        p.sendMessage(prefix + "§c§l検索結果: そのカテゴリーのjobは見つかりませんでした");
                        return true;
                    }
                    p.sendMessage(prefix + "§a§l検索結果: Hitしました。…§e" + jobs.size() + "件");
                    for (MandeedData.JobData getjob : jobs) {
                        String pname = null;
                        if (Bukkit.getPlayer(UUID.fromString(getjob.uuid)) != null) {
                            pname = Bukkit.getPlayer(UUID.fromString(getjob.uuid)).getName();
                        } else {
                            pname = Bukkit.getOfflinePlayer(UUID.fromString(getjob.uuid)).getName();
                        }
                        p.sendMessage(prefix + "§eid:§6" + getjob.id + " §ejob名:§6" + getjob.jobname + " §e報酬:§6" + getjob.reward + " §e募集者:§6" + pname);
                    }
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("accept")) {
                if (args[1].equalsIgnoreCase("id")) {
                    int id = -1;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(prefix + "§4数字を入力してください");
                        return true;
                    }
                    MandeedData.JobData getjob = data.recruitgetfromid(id);
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l結果: そのidのjobは見つかりませんでした");
                        return true;
                    }
                    if (Bukkit.getPlayer(UUID.fromString(getjob.uuid)) != null) {
                        data.recruitdelete(Bukkit.getPlayer(UUID.fromString(getjob.uuid)));
                        p.sendMessage(prefix + "§a§l結果: 受諾に成功しました。");
                        Bukkit.getPlayer(UUID.fromString(getjob.uuid)).sendMessage(prefix + "§e§l" + p.getName() + "§a§lがあなたの募集「§6§l" + getjob.jobname + "§e§l」を受諾しました。");
                    } else {
                        p.sendMessage(prefix + "§c§l結果: 募集者がofflineのため受諾に失敗しました。");
                    }
                    return true;
                } else if (args[1].equalsIgnoreCase("player")) {
                    String pname = null;
                    if (Bukkit.getPlayer(args[1]) != null) {
                        pname = args[1];
                    } else {
                        p.sendMessage(prefix + "§4オンラインのプレイヤー名を入力してください");
                        return true;
                    }
                    MandeedData.JobData getjob = data.recruitgetfromplayer(Bukkit.getPlayer(args[1]));
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l結果: そのプレイヤー名のjobは見つかりませんでした");
                        return true;
                    }
                    if (Bukkit.getPlayer(UUID.fromString(getjob.uuid)) != null) {
                        data.recruitdelete(Bukkit.getPlayer(UUID.fromString(getjob.uuid)));
                        p.sendMessage(prefix + "§a§l結果: 受諾に成功しました。");
                        Bukkit.getPlayer(UUID.fromString(getjob.uuid)).sendMessage(prefix + "§e§l" + p.getName() + "§a§lがあなたの募集「§6§l" + getjob.jobname + "§e§l」を受諾しました。");
                    } else {
                        p.sendMessage(prefix + "§c§l結果: 募集者がofflineのため受諾に失敗しました。");
                    }
                    return true;
                } else if (args[1].equalsIgnoreCase("jobname")) {
                    MandeedData.JobData getjob = data.recruitgetfromjobname(args[2]);
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l結果: そのjob名のjobは見つかりませんでした");
                        return true;
                    }
                    if (Bukkit.getPlayer(UUID.fromString(getjob.uuid)) != null) {
                        data.recruitdelete(Bukkit.getPlayer(UUID.fromString(getjob.uuid)));
                        p.sendMessage(prefix + "§a§l結果: 受諾に成功しました。");
                        Bukkit.getPlayer(UUID.fromString(getjob.uuid)).sendMessage(prefix + "§e§l" + p.getName() + "§a§lがあなたの募集「§6§l" + getjob.jobname + "§e§l」を受諾しました。");
                    } else {
                        p.sendMessage(prefix + "§c§l結果: 募集者がofflineのため受諾に失敗しました。");
                    }
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args[1].equalsIgnoreCase("id")) {
                    int id = -1;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(prefix + "§4数字を入力してください");
                        return true;
                    }
                    MandeedData.JobData getjob = data.recruitgetfromid(id);
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l結果: そのidのjobは見つかりませんでした");
                        return true;
                    }
                    data.recruitdelete(Bukkit.getPlayer(UUID.fromString(getjob.uuid)));
                    return true;
                } else if (args[1].equalsIgnoreCase("player")) {
                    String pname = null;
                    if (Bukkit.getPlayer(args[1]) != null) {
                        pname = args[1];
                    } else {
                        p.sendMessage(prefix + "§4数字を入力してください");
                        return true;
                    }
                    MandeedData.JobData getjob = data.recruitgetfromplayer(Bukkit.getPlayer(args[1]));
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l結果: そのプレイヤー名のjobは見つかりませんでした");
                        return true;
                    }
                    data.recruitdelete(Bukkit.getPlayer(UUID.fromString(getjob.uuid)));
                    return true;
                } else if (args[1].equalsIgnoreCase("jobname")) {
                    MandeedData.JobData getjob = data.recruitgetfromjobname(args[2]);
                    if (getjob == null) {
                        p.sendMessage(prefix + "§c§l結果: そのjob名のjobは見つかりませんでした");
                        return true;
                    }
                    data.recruitdelete(Bukkit.getPlayer(UUID.fromString(getjob.uuid)));
                    return true;
                }
            }
        } else if (args.length == 4) {
           if (args[0].equalsIgnoreCase("recruit")) {
                int category = 0;
                try {
                    category = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    p.sendMessage(prefix + "§4数字を入力してください");
                }
                Double reward = 0.0;
                try {
                    reward = Double.parseDouble(args[3]);
                } catch (NumberFormatException e) {
                    try {
                        reward = Double.parseDouble(args[2] + ".0");
                    } catch (NumberFormatException ee) {
                        p.sendMessage(prefix + "§4数字を入力してください");
                        return true;
                    }
                }
                if (data.playercontain(p)) {
                    p.sendMessage(prefix + "§4すでにあなたは仕事を募集しています!");
                    return true;
                }
                if (data.jobnamecontain(args[1])) {
                    p.sendMessage(prefix + "§4そのjob名は既に存在します!");
                    return true;
                }
                if (!data.categorycontain(category)) {
                    p.sendMessage(prefix + "§4そのカテゴリーは存在しません！");
                    return true;
                }
                if (data.recruitcreate(p, args[1], category, reward)) {
                    p.sendMessage(prefix + "§a募集を開始しました!");
                    return true;
                } else {
                    p.sendMessage(prefix + "§c募集に失敗しました。");
                    return true;
                }
            }
        }
        p.sendMessage("==========§d§l●§f§l●§a§l●" + prefix + "§a§l●§f§l●§d§l●==========");
        p.sendMessage("§e--プレイヤー向けヘルプ--");
        p.sendMessage("§e/mdeed search [id/player/jobname/category] [内容] : 仕事をサーチします。");
        p.sendMessage("§e/mdeed recruit [ジョブ名] [カテゴリー] [報酬] : 仕事を募集します。");
        p.sendMessage("§e/mdeed accept [id/player/jobname] [内容]: 仕事を受諾します。");
        p.sendMessage("§e/mdeed cancel: 募集している仕事をキャンセルします");
        p.sendMessage("");
        p.sendMessage("§e[カテゴリ一覧] 1:釣り代行 2:スロット代行 3:建築 4:採掘・材料集め 5:整地 6:その他");
        if (p.hasPermission("mandeed.adminhelp")) {
            p.sendMessage("§c--admin向けヘルプ--");
            p.sendMessage("§c/mdeed delete [id/player/jobname] [内容] : 特定のjobを削除します");
        }
        p.sendMessage("==========§d§l●§f§l●§a§l●" + prefix + "§a§l●§f§l●§d§l●==========");
        return true;
    }

    FileConfiguration config1;
    MySQLManager mysql;
    MandeedData data;
    String prefix = "§3[§dM§fa§an§fdeed§3]§r";
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents (this,this);
        saveDefaultConfig();
        config1 = getConfig();
        mysql = new MySQLManager(this, "Mdeed");
        data = new MandeedData(this);
        getCommand("mdeed").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
